package com.yinghai.twentyfour.init;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.model.TfBusinessType;
import com.yinghai.twentyfour.common.service.TfBusinessTypeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/16.
 * 服务开启就执行
 */
public class CacheInitializingBean implements InitializingBean {

//    @Autowired
//    private LabelService labelService;
//    @Autowired
//    private EhCacheManager cacheManager;
//    @Autowired
//    private ClassificationService classificationService;
//    @Autowired
//    private MavinService mavinService;
	  @Autowired
      private EhCacheManager cacheManager;
	  @Autowired
	  private TfBusinessTypeService tfBusinessTypeService;
    @Override
    public void afterPropertiesSet() throws Exception {
//        Cache<String,Object> cache = cacheManager.getCache("selectList"); 
//        List<Classification> classificationList = classificationService.selectAllClassification();
//        List<Mavin> mavinList = mavinService.findAllMavin();
//        List<Label> labelList = labelService.findAllLabel();
//        cache.put("classificationList",classificationList);
//        cache.put("mavinList",mavinList);
//        cache.put("labelList",labelList);
    	  Cache<String,Object> typeCache = cacheManager.getCache("typeNameMap");
    	  List<TfBusinessType> typeList = tfBusinessTypeService.findAll();
    	  Map<String,String> typeMap = new HashMap<String,String>(); 
    	  for(TfBusinessType b:typeList){
    		  typeMap.put(b.getBusinessTypeId()+"", b.getTypeName());
    	  }
    	  typeCache.put("typeMap", typeMap); 
    }
}
