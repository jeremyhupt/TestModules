package com.ryanh.ryanutils.service.impl;


import com.ryanh.ryanutils.commonutils.ObjectUtils;
import com.ryanh.ryanutils.entity.CacheObject;
import com.ryanh.ryanutils.service.CacheFullRemoveType;

/**
 * Remove type when cache is full.<br/>
 * when cache is full, compare data of object in cache, if data is smaller remove it first.<br/>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-12-26
 */
public class RemoveTypeDataSmall<T> implements CacheFullRemoveType<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(CacheObject<T> obj1, CacheObject<T> obj2) {
        return ObjectUtils.compare(obj1.getData(), obj2.getData());
    }
}
