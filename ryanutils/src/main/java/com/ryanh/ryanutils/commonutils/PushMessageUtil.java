//package com.ryanh.ryanutils.commonutils;
//
//import android.content.Context;
//import android.os.Handler;
//
//
///**
// * Author:胡俊杰
// * Date: 2015/9/2
// * FIXME
// * Todo
// */
//public class PushMessageUtil {
//    private static final int NotificationType_Promotion = 0;//活动
//    private static final int NotificationType_OrderDetail = 1;//订单详情
//
//    public PushMessageUtil() {
//    }
//
//    public void preparePushMessage(final Context context, boolean debug) {
//
//        PushAgent mPushAgent = PushAgent.getInstance(context);
//        mPushAgent.enable(new IUmengRegisterCallback() {
//            @Override
//            public void onRegistered(String deviceToken) {
//                DebugLog.i("Tag", "deviceToken  " + deviceToken);
//                getDeviceToken(context,deviceToken);
//            }
//        });
//        mPushAgent.setDebugMode(debug);
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
//        mPushAgent.setMessageHandler(messageHandler);
//    }
//
//    /**
//     * 获取友盟推送DeviceToken
//     */
//    public void getDeviceToken(Context context, String deviceToken) {
//        BaseSharedPreferencesHelper sharedPreferencesHelper
//                = new BaseSharedPreferencesHelper(context.getApplicationContext());
//        sharedPreferencesHelper.saveUMengDeviceToken(deviceToken);
//    }
//
//    /**
//     * 该Handler是在BroadcastReceiver中被调用，故
//     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//     */
//    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//
//        @Override
//        public void launchApp(Context context, UMessage uMessage) {
//            super.launchApp(context, uMessage);
////            Toast.makeText(context, uMessage.custom, Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void dismissNotification(Context context, UMessage uMessage) {
//            super.dismissNotification(context, uMessage);
//        }
//
//        @Override
//        public void autoUpdate(Context context, UMessage uMessage) {
//            super.autoUpdate(context, uMessage);
//        }
//
//        @Override
//        public void openUrl(Context context, UMessage uMessage) {
//            super.openUrl(context, uMessage);
//        }
//
//        @Override
//        public void openActivity(Context context, UMessage uMessage) {
//            super.openActivity(context, uMessage);
//        }
//
//        @Override
//        public void dealWithCustomAction(Context context, UMessage uMessage) {
//            super.dealWithCustomAction(context, uMessage);
//            int notificationType = Integer.valueOf(uMessage.extra.get("notificationType"));
//            switch (notificationType) {
//                case NotificationType_Promotion://活动
//                    String url = uMessage.extra.get("url");
////                    ActivitySwitchBase.toEventDetail(context, url);
//                    break;
//                case NotificationType_OrderDetail://订单详情
//                    String orderID = uMessage.extra.get("orderID");
////                    ActivitySwitchBase.toOrderDetail(context, orderID);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    /**
//     * 该Handler是在IntentService中被调用，故
//     * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//     * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
//     * 或者可以直接启动Service
//     */
//    UmengMessageHandler messageHandler = new UmengMessageHandler() {
//        @Override
//        public void dealWithCustomMessage(final Context context, final UMessage msg) {
//            new Handler(context.getMainLooper()).post(new Runnable() {
//
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
////                    UTrack.getInstance(context).trackMsgClick(msg, false);
////                    Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                }
//            });
//
//
//        }
//    };
//}
//
