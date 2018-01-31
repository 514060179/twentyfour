package com.yinghai.twentyfour.test;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/20.
 */
@Service
public class EhCacheTestServiceImpl implements EhCacheTestService {

    @Cacheable(value="cacheTest",key="#param")
    public String getTimestamp(String param) {
        Long timestamp = System.currentTimeMillis();
        return timestamp.toString();
    }
}
