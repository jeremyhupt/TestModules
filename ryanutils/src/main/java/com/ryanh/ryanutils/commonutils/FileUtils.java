package com.ryanh.ryanutils.commonutils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String, String)} read file</li>
 * <li>{@link #readFileToList(String, String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file from String</li>
 * <li>{@link #writeFile(String, String)} write file from String</li>
 * <li>{@link #writeFile(String, List, boolean)} write file from String List</li>
 * <li>{@link #writeFile(String, List)} write file from String List</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #moveFile(File, File)} or {@link #moveFile(String, String)}</li>
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class FileUtils {

    public static final long GB = 1073741824; // 1024 * 1024 * 1024
    public static final long MB = 1048576; // 1024 * 1024
    public static final long KB = 1024;

    public static final int ICON_TYPE_ROOT = 1;
    public static final int ICON_TYPE_FOLDER = 2;
    public static final int ICON_TYPE_MP3 = 3;
    public static final int ICON_TYPE_MTV = 4;
    public static final int ICON_TYPE_JPG = 5;
    public static final int ICON_TYPE_FILE = 6;

    public static final String MTV_REG = "^.*\\.(mp4|3gp)$";
    public static final String MP3_REG = "^.*\\.(mp3|wav)$";
    public static final String JPG_REG = "^.*\\.(gif|jpg|png)$";

    private static final String FILENAME_REGIX = "^[^\\/?\"*:<>\\]{1,255}$";
    public final static String FILE_EXTENSION_SEPARATOR = ".";

    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * read file
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(reader);
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(fileWriter);
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param contentList
     * @param append      is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (ListUtils.isEmpty(contentList)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(fileWriter);
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(o);
            IOUtils.close(stream);
        }
    }

    /**
     * move file
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * move file
     *
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * 输入流转byte[]
     *
     * @param inStream InputStream
     * @return Byte数组
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null)
            return null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return swapStream.toByteArray();
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(reader);
        }
    }

    /**
     * get file name from path, not include suffix
     * <p>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param filePath 文件路径
     * @return 文件的MD5校验码
     */
    public static String getFileMD5ToString(String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return getFileMD5ToString(file);
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param filePath 文件路径
     * @return 文件的MD5校验码
     */
    public static byte[] getFileMD5(String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return getFileMD5(file);
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static String getFileMD5ToString(File file) {
        return ConvertUtils.bytes2HexString(getFileMD5(file));
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static byte[] getFileMD5(File file) {
        if (file == null) return null;
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (dis.read(buffer) > 0) ;
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(dis);
        }
        return null;
    }

    /**
     * 获取全路径中的最长目录
     *
     * @param file 文件
     * @return filePath最长目录
     */
    public static String getDirName(File file) {
        if (file == null) return null;
        return getDirName(file.getPath());
    }

    /**
     * 获取全路径中的最长目录
     *
     * @param filePath 文件路径
     * @return filePath最长目录
     */
    public static String getDirName(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }

    /**
     * get file name from path, include suffix
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 获取全路径中的文件名
     *
     * @param file 文件
     * @return 文件名
     */
    public static String getFileName(File file) {
        if (file == null) return null;
        return getFileName(file.getPath());
    }

    /**
     * get folder name from path
     * <p>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param file 文件
     * @return 不带拓展名的文件名
     */
    public static String getFileNameNoExtension(File file) {
        if (file == null) return null;
        return getFileNameNoExtension(file.getPath());
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param filePath 文件路径
     * @return 不带拓展名的文件名
     */
    public static String getFileNameNoExtension(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    /**
     * get suffix of file from path
     * <p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param file 文件
     * @return 文件拓展名
     */
    public static String getFileExtension(File file) {
        if (file == null) return null;
        return getFileExtension(file.getPath());
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     * <ul>
     * <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     * <li>if target directory already exists, return true</li>
     * <li>return </li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist2(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 重命名文件和文件夹
     *
     * @param file        File对象
     * @param newFileName 新的文件名
     * @return 执行结果
     */
    public static boolean renameFile(File file, String newFileName) {
        if (newFileName.matches(FILENAME_REGIX)) {
            File newFile = null;
            if (file.isDirectory()) {
                newFile = new File(file.getParentFile(), newFileName);
            } else {
                String temp = newFileName
                        + file.getName().substring(
                        file.getName().lastIndexOf('.'));
                newFile = new File(file.getParentFile(), temp);
            }
            if (file.renameTo(newFile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件大小获取
     *
     * @param file File对象
     * @return 文件大小字符串
     */
    public static String getFileSize(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            if (length >= GB) {
                return String.format("%.2f GB", length * 1.0 / GB);
            } else if (length >= MB) {
                return String.format("%.2f MB", length * 1.0 / MB);
            } else {
                return String.format("%.2f KB", length * 1.0 / KB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "未知";
    }

    /**
     * 使用系统程序打开文件
     *
     * @param activity Activity
     * @param file     File
     * @throws Exception
     */
    public static void openFile(Activity activity, File file) throws Exception {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), getMimeType(file, activity));
        activity.startActivity(intent);
    }

    /**
     * 获取以后缀名为ID的值
     *
     * @param file     File
     * @param activity Activity
     * @return MimeType字符串
     * @throws Exception
     */
    public static String getMimeType(File file, Activity activity)
            throws Exception {

        String name = file.getName()
                .substring(file.getName().lastIndexOf('.') + 1).toLowerCase();
        int id = activity.getResources().getIdentifier(
                activity.getPackageName() + ":string/" + name, null, null);

        // 特殊处理
        if ("class".equals(name)) {
            return "application/octet-stream";
        }
        if ("3gp".equals(name)) {
            return "video/3gpp";
        }
        if ("nokia-op-logo".equals(name)) {
            return "image/vnd.nok-oplogo-color";
        }
        if (id == 0) {
            throw new Exception("未找到分享该格式的应用");
        }
        return activity.getString(id);
    }

    /**
     * 用于递归查找文件夹下面的符合条件的文件
     *
     * @param folder 文件夹
     * @param filter 文件过滤器
     * @return 符合条件的文件List
     */
    public static List<HashMap<String, Object>> recursionFolder(File folder,
                                                                FileFilter filter) {

        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        // 获得文件夹下的所有目录和文件集合
        File[] files = folder.listFiles();
        /** 如果文件夹下没内容,会返回一个null **/
        // 判断适配器是否为空
        if (filter != null) {
            files = folder.listFiles(filter);
        }
        // 找到合适的文件返回
        if (files != null) {
            for (int m = 0; m < files.length; m++) {
                File file = files[m];
                if (file.isDirectory()) {
                    // 是否递归调用
                    list.addAll(recursionFolder(file, filter));

                } else {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("file", file);
                    // 设置图标种类
                    if (file.getAbsolutePath().toLowerCase().matches(MP3_REG)) {
                        map.put("iconType", 3);
                    } else if (file.getAbsolutePath().toLowerCase()
                            .matches(MTV_REG)) {
                        map.put("iconType", 4);
                    } else if (file.getAbsolutePath().toLowerCase()
                            .matches(JPG_REG)) {
                        map.put("iconType", 5);
                    } else {
                        map.put("iconType", 6);
                    }
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 资源管理器,查找该文件夹下的文件和目录
     *
     * @param folder 文件夹
     * @param filter 文件过滤器
     * @return 符合条件的List
     */
    public static List<HashMap<String, Object>> unrecursionFolder(File folder,
                                                                  FileFilter filter) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        // 如果是SD卡路径,不添加父路径
        if (!folder.getAbsolutePath().equals(
                Environment.getExternalStorageDirectory().getAbsolutePath())) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("file", folder.getParentFile());
            map.put("iconType", ICON_TYPE_ROOT);
            list.add(map);
        }
        // 获得文件夹下的所有目录和文件集合
        File[] files = folder.listFiles();
        /** 如果文件夹下没内容,会返回一个null **/
        // 判断适配器是否为空
        if (filter != null) {
            files = folder.listFiles(filter);
        }
        if (files != null && files.length > 0) {
            for (File p : files) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("file", p);
                // 设置图标种类
                if (p.isDirectory()) {
                    map.put("iconType", ICON_TYPE_FOLDER);
                } else {
                    if (p.getAbsolutePath().toLowerCase().matches(MP3_REG)) {
                        map.put("iconType", ICON_TYPE_MP3);
                    } else if (p.getAbsolutePath().toLowerCase()
                            .matches(MTV_REG)) {
                        map.put("iconType", ICON_TYPE_MTV);
                    } else if (p.getAbsolutePath().toLowerCase()
                            .matches(JPG_REG)) {
                        map.put("iconType", ICON_TYPE_JPG);
                    } else {
                        map.put("iconType", ICON_TYPE_FILE);
                    }
                }
                // 添加
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 示例:"^.*\\.(mp3|mp4|3gp)$"
     *
     * @param reg 目前允许取值 REG_MTV, REG_MP3, REG_JPG三种
     * @return 文件过滤器
     */
    public static FileFilter getFileFilter(final String reg, boolean isdir) {
        if (isdir) {
            return new FileFilter() {
                @Override
                public boolean accept(File pathname) {

                    return pathname.getAbsolutePath().toLowerCase()
                            .matches(reg)
                            || pathname.isDirectory();
                }
            };
        } else {
            return new FileFilter() {
                @Override
                public boolean accept(File pathname) {

                    return pathname.getAbsolutePath().toLowerCase()
                            .matches(reg)
                            && pathname.isFile();
                }
            };
        }
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtils.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 创建新文件,如果存在就删除文件,如果不存在就创建文件
     *
     * @param filepath
     * @return
     */
    public static boolean makeFileOver(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            boolean newFile = file.delete();
        } else {
            File fileParent = new File(file.getParent());
            try {
                boolean newFile = fileParent.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param filepath
     * @return
     */
    public static boolean isFileExist(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     * @throws java.net.MalformedURLException
     * @throws IOException
     */

    public static InputStream getInputStreamFromUrl(String urlStr)
            throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        return urlConn.getInputStream();
    }

    /**
     * 按照给出的路径读取文件内容
     *
     * @param str_filepath
     * @return
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static synchronized String convertCodeAndGetText(String str_filepath) {
        File file = new File(str_filepath);
        BufferedReader reader;
        String text = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fis);
            in.mark(4);
            byte[] first3bytes = new byte[3];
            in.read(first3bytes);
            in.reset();
            if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                    && first3bytes[2] == (byte) 0xBF) {
                reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFE) {

                reader = new BufferedReader(
                        new InputStreamReader(in, "unicode"));
            } else if (first3bytes[0] == (byte) 0xFE
                    && first3bytes[1] == (byte) 0xFF) {

                reader = new BufferedReader(new InputStreamReader(in,
                        "utf-16be"));
            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFF) {

                reader = new BufferedReader(new InputStreamReader(in,
                        "utf-16le"));
            } else {

                reader = new BufferedReader(new InputStreamReader(in, "GBK"));
            }
            String str = reader.readLine();

            while (str != null) {
                text = text + str;
                str = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 写文件
     *
     * @param context
     * @param dir
     * @param string
     */
    public static synchronized void writerFile(Context context, String dir,
                                               String string) {
        File file = new File(dir);
        File path1 = file.getParentFile();

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        if (!path1.exists()) {
            // 路径不存在? Just 创建
            path1.mkdirs();
        }
        if (!file.exists()) {
            // 文件不存在、 Just创建
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        OutputStreamWriter osw;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osw.write("\n " + string);
            osw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 保存图片
     *
     * @param strPath
     * @param patamBitmap
     * @return
     */
    public static boolean savaQualityBitmap(String strPath, Bitmap patamBitmap,
                                            int quality) {

        makeFileDirs(strPath);
        File localFile = new File(strPath);
        try {
            FileOutputStream localFileOutputStream = new FileOutputStream(
                    localFile);
            patamBitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                    localFileOutputStream);
            localFileOutputStream.flush();
            localFileOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 创建新文件,如果存在就不创建
     *
     * @param filepath
     * @return
     */
    public static boolean makeFileDirs(String filepath) {
        File file = new File(filepath);
        File fileparent = new File(file.getParent());
        if (!fileparent.exists()) {
            fileparent.mkdirs();
        }

        return fileparent.exists();
    }

    public static void saveQualityBitmap(String strPath, Bitmap bitmap, int quality) {
        File localFile = new File(strPath);
        FileOutputStream localFileOutputStream;
        try {
            localFileOutputStream = new FileOutputStream(
                    localFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                    localFileOutputStream);
            localFileOutputStream.flush();
            localFileOutputStream.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把uri转为File对象
     *
     * @param activity Activity
     * @param uri      文件Uri
     * @return File对象
     */
    public static File uri2File(Activity activity, Uri uri) {
        if (Build.VERSION.SDK_INT < 11) {
            // 在API11以下可以使用：managedQuery
            String[] proj = {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null,
                    null);
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                    .getString(actual_image_column_index);
            return new File(img_path);
        } else {
            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(activity, uri, projection, null,
                    null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }
}
