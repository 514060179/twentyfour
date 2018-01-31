package com.yinghai.twentyfour.freemarker;

import com.yinghai.twentyfour.common.util.SpringContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RichFreeMarkerView extends FreeMarkerView{
	/**
	 * 部署路径属性名称
	 */
	public static final String CONTEXT_PATH = "base";
	public static final String LABLE = "lable";
	/**
	 * 在model中增加部署路径base，方便处理部署路径问题。
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void exposeHelpers(Map model, HttpServletRequest request)
			throws Exception {
        super.exposeHelpers(model, request);
        String requestPath = request.getRequestURI();
        String[] lable =  requestPath.split("\\/");
		Cache<String,Object> cache = ((CacheManager) SpringContextUtils.getBeanById("cacheManager")).getCache("selectList");
		model.put("classificationList",cache.get("classificationList"));
		model.put("mavinList",cache.get("mavinList"));
		model.put("labelList",cache.get("labelList"));
		int lableIndex = lable.length-2;
        model.put(LABLE, lable[lableIndex]);
        model.put(CONTEXT_PATH, request.getContextPath());
    }
}

