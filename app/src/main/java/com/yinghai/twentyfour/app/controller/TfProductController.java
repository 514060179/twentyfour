package com.yinghai.twentyfour.app.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.yinghai.twentyfour.app.service.CollectionService;
import com.yinghai.twentyfour.app.service.HistoryService;
import com.yinghai.twentyfour.app.service.TfCarService;
import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfCollection;
import com.yinghai.twentyfour.common.model.TfHistory;
import com.yinghai.twentyfour.common.model.TfImgTmp;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfImgTmpService;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.EncryptUtil;
import com.yinghai.twentyfour.common.util.EnumUtil;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.RandomUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.SmsSenderUtil;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * 用户端管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/product")
public class TfProductController {
	@Autowired
	private TfMasterService tfMasterService;
	@Autowired
	private TfProductService tfProductService;
	@Autowired
	private TfImgTmpService tfImgTmpService;
	@Autowired
	private CollectionService collectionService;
	@Autowired
	private HistoryService tfHistoryService;
	
	private Logger log = Logger.getLogger(this.getClass());
	/**
	 * 商品产品列表
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws IOException
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("======获取产品列表");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer pageNumber = TransformUtils.toInt(request.getParameter("page"));
		if (pageNumber == null || pageNumber <= 0) {
			pageNumber = 1;
		}
		Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));
		if (pageSize == null || pageSize <= 0) {
			pageSize = 10;
		}
		TfProduct tfProduct = new TfProduct();
		String masterId = request.getParameter("masterId");//大师ID
		if (StringUtil.notEmpty(masterId)) {
			tfProduct.setpMasterId(TransformUtils.toInt(masterId));
		}
		String name = request.getParameter("name");//产品名称
		if (StringUtil.notEmpty(name)) {
			tfProduct.setpName("%"+name.trim()+"%");
		}
		String type = request.getParameter("type");//业务类型
		if (StringUtil.notEmpty(type)) {
			//tfProduct.setpType(type);
			String[] array = type.split(",");
			tfProduct.setpTypeArray(array);
		}
		String offline = request.getParameter("offline");//是否上架
		if (StringUtil.notEmpty(offline)) {
			tfProduct.setpOffline(TransformUtils.toBoolean(offline));
		}
		String freeShipping = request.getParameter("freeShipping");//是否包邮
		if (StringUtil.notEmpty(freeShipping)) {
			tfProduct.setpFreeShipping(TransformUtils.toBoolean(freeShipping));
		}
		Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
		if(userId!=null&&userId>0){
			tfProduct.setUserId(userId);
		}
		String realm = request.getHeader("Realm");
		if(!"master".equals(realm)){
			tfProduct.setpOffline(false);
		}
		int statrNumber = 0;
		statrNumber = (pageNumber-1)*pageSize;
		List<TfProduct> page = tfProductService.getTfProductAndImgRecord(statrNumber, pageSize, tfProduct); 
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray data = JSONArray.fromObject(page, config);
		JSONObject modelObject = new JSONObject();
		modelObject.put("productList", data);
		ResponseVo.common("1", "操作成功", modelObject, response);
	}

		/**
		 * 新增产品
		 * @param request
		 * @param response
		 * @throws Exception
		 * @throws IOException
		 */
		@RequestMapping(value = "save", method = RequestMethod.POST)
		public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.info("======新增商品");
			try {
				request.setCharacterEncoding("utf-8");
				response.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			TfProduct tfProduct = new TfProduct();
			String imgIds = request.getParameter("imgIds");
			if(StringUtil.empty(imgIds)){
				ResponseVo.send101Code(response, "图片ID为空");
				return;
			}
			String masterId = request.getParameter("masterId");//大师ID
			if(StringUtil.empty(masterId)){
				ResponseVo.send101Code(response, "大师ID为空");
				return;
			}
			TfMaster tfMaster = tfMasterService.selectByPrimaryKey(TransformUtils.toInt(masterId));
			if(tfMaster==null){
				ResponseVo.send102Code(response, "该大师不存在");
				return;
			}
			if(tfMaster.getmDeleted()){
				ResponseVo.send121Code(response, "该大师呗禁用");
				return;	
			}
			tfProduct.setpMasterId(TransformUtils.toInt(masterId));
			String name = request.getParameter("name");//产品名称
			if (StringUtil.empty(name)) {
				ResponseVo.send101Code(response, "产品名称为空");
				return;
			}
			tfProduct.setpName(name.trim());
			String pPrice = request.getParameter("price");//产品单价
			if (StringUtil.empty(pPrice)) {
				ResponseVo.send101Code(response, "产品价格为空");
				return;
			}
			tfProduct.setpPrice(TransformUtils.toInt(pPrice));
			
			String pTotal = request.getParameter("total");//产品数量
			if (StringUtil.empty(pTotal)) {
				ResponseVo.send101Code(response, "产品数量为空");
				return;
			}
			tfProduct.setpTotal(TransformUtils.toInt(pTotal));
			
			String pType = request.getParameter("type");//产品类型
			if (StringUtil.empty(pType)) {
				ResponseVo.send101Code(response, "产品类型为空"); 
				return;
			}
			tfProduct.setpType(pType);
			String pIntroduction = request.getParameter("introduction");//产品介绍
			if (StringUtil.empty(pIntroduction)) {
				ResponseVo.send101Code(response, "产品介绍为空");
				return;
			}
			tfProduct.setpIntroduction(pIntroduction);
			
			//tfProduct.setpImg(imgUrls);
			String pAttribution = request.getParameter("attribution");//产品归属地
			if (StringUtil.notEmpty(pAttribution)) {
				tfProduct.setpAttribution(pAttribution);
			}
			String pOffline = request.getParameter("offline");//商品是否下架
			if (StringUtil.notEmpty(pOffline)) {
				tfProduct.setpOffline(TransformUtils.toBoolean(pOffline));
			}else{
				tfProduct.setpOffline(false);
			}
			tfProduct.setpCreateTime(new Date());
			String deleteImgIds = request.getParameter("deleteImgIds");//所需要删除的图片ID
			if (StringUtil.notEmpty(deleteImgIds)) {
				String[] ids = deleteImgIds.split(";");
				if(ids!=null&&ids.length!=0){
					for(int i=0;i<ids.length;i++){//循环删除图片信息与图片
						TfImgTmp tfImgTmp = tfImgTmpService.selectByPrimaryKey(TransformUtils.toInt(ids[i]));
						if(tfImgTmp!=null){
							int k = tfImgTmpService.deleteByPrimaryKey(tfImgTmp.getImgTmpId());
							if(k<1){
								log.error("删除文件失败=================该文件ID为"+tfImgTmp.getImgTmpId()+",进度为"+i+"/"+ids.length);
							}else{//删除文件
								 File f = new File(tfImgTmp.getItAbsolute());  
								 if(f.exists() && f.isDirectory()){  
								         f.delete();  
								    }  
							}
						}
					}
				}
			}
			int i = tfProductService.insertSelective(tfProduct);
			if(i<1){
				ResponseVo.send106Code(response, "新增数据失败");
				return;
			}
			//新增成功后绑定图片信息
			//新增成功數據庫，判斷是否有附件圖片
			if(StringUtil.notEmpty(imgIds)){
				String[] ids = imgIds.split(";");
				if(ids!=null&&ids.length!=0){
					 i = tfImgTmpService.updateKeyIdById(ids, tfProduct.getProductId());
				}
			}
			tfProduct = tfProductService.selectByPrimaryKey(tfProduct.getProductId());
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject dataJson = new JSONObject();
			JSONObject userJson = JSONObject.fromObject(tfProduct, config);
			dataJson.put("tfProduct", userJson);
			ResponseVo.common("1", "新增成功", dataJson, response);
		}
		/**
		 * 编辑产品
		 * @param request
		 * @param response
		 * @throws Exception
		 * @throws IOException
		 */
		@RequestMapping(value = "edit", method = RequestMethod.POST)
		public void edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.info("======编辑商品");
			try {
				request.setCharacterEncoding("utf-8");
				response.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String productId = request.getParameter("productId");//大师产品ID
			if(StringUtil.empty(productId)){
				ResponseVo.send101Code(response, "产品为空");
				return;
			}
			TfProduct tfProduct = tfProductService.selectByPrimaryKey(TransformUtils.toInt(productId));
			String masterId = request.getParameter("masterId");//大师ID
			if(StringUtil.empty(masterId)){
				ResponseVo.send101Code(response, "大师ID为空");
				return;
			}
			TfMaster tfMaster = tfMasterService.selectByPrimaryKey(TransformUtils.toInt(masterId));
			if(tfMaster==null){
				ResponseVo.send102Code(response, "该大师不存在");
				return;
			}
			if(tfMaster.getmDeleted()){
				ResponseVo.send121Code(response, "该大师呗禁用");
				return;	
			}
			tfProduct = new TfProduct();
			tfProduct.setProductId(TransformUtils.toInt(productId));
			String name = request.getParameter("name");//产品名称
			if (StringUtil.notEmpty(name)) {
				tfProduct.setpName(name.trim());
			}
			
			String pPrice = request.getParameter("price");//产品单价
			if (StringUtil.notEmpty(pPrice)) {
				tfProduct.setpPrice(TransformUtils.toInt(pPrice));
			}
			
			String pTotal = request.getParameter("total");//产品数量
			if (StringUtil.notEmpty(pTotal)) {
				tfProduct.setpTotal(TransformUtils.toInt(pTotal));
			}
			
			
			String pType = request.getParameter("type");//产品类型
			if (StringUtil.notEmpty(pType)) {
				tfProduct.setpType(pType);
			}
			
			
			String pIntroduction = request.getParameter("introduction");//产品介绍
			if (StringUtil.notEmpty(pIntroduction)) {
				tfProduct.setpIntroduction(pIntroduction);
			}
			
			
			String pImg = request.getParameter("img");//产品图片
			if (StringUtil.notEmpty(pImg)) {
				tfProduct.setpImg(pImg);
			}
			
			
			String pAttribution = request.getParameter("attribution");//产品归属地
			if (StringUtil.notEmpty(pAttribution)) {
				tfProduct.setpAttribution(pAttribution);
			}
			
			String pOffline = request.getParameter("offline");//商品是否下架
			if (StringUtil.notEmpty(pOffline)) {
				tfProduct.setpOffline(TransformUtils.toBoolean(pOffline));
			}
			tfProduct.setpUpdateTime(new Date());
			String imgIds = request.getParameter("imgIds");//产品归属地
			if (StringUtil.notEmpty(imgIds)) {
				String[] ids = imgIds.split(";");
				if(ids!=null&&ids.length!=0){
					int i = tfImgTmpService.updateKeyIdById(ids, tfProduct.getProductId());
				}
			}
			String deleteImgIds = request.getParameter("deleteImgIds");//所需要删除的图片
			if (StringUtil.notEmpty(deleteImgIds)) {
				String[] ids = deleteImgIds.split(";");
				if(ids!=null&&ids.length!=0){
					for(int i=0;i<ids.length;i++){//循环删除图片信息与图片
						TfImgTmp tfImgTmp = tfImgTmpService.selectByPrimaryKey(TransformUtils.toInt(ids[i]));
						if(tfImgTmp!=null){
							int k = tfImgTmpService.deleteByPrimaryKey(tfImgTmp.getImgTmpId());
							if(k<1){
								log.error("删除文件失败=================该文件ID为"+tfImgTmp.getImgTmpId()+",进度为"+i+"/"+ids.length);
							}else{//删除文件
								 File f = new File(tfImgTmp.getItAbsolute());  
								 if(f.exists() && f.isDirectory()){  
								         f.delete();  
								    }  
							}
						}
					}
				}
			}
			
			int i = tfProductService.updateByPrimaryKeySelective(tfProduct);
			if(i<1){
				ResponseVo.send106Code(response, "编辑数据失败");
				return;
			}
			tfProduct = tfProductService.selectByPrimaryKey(tfProduct.getProductId());
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject dataJson = new JSONObject();
			JSONObject userJson = JSONObject.fromObject(tfProduct, config);
			dataJson.put("tfProduct", userJson);
			ResponseVo.common("1", "编辑成功", dataJson, response);
		}
		
		/**
		 * 商品产品列表
		 * @param request
		 * @param response
		 * @throws Exception
		 * @throws IOException
		 */
		@RequestMapping(value = "detail", method = RequestMethod.POST)
		public void detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.info("======获取产品详情");
			try {
				request.setCharacterEncoding("utf-8");
				response.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String productId = request.getParameter("productId");//产品ID
			if (StringUtil.empty(productId)) {
				ResponseVo.send101Code(response, "产品ID为空");
				return;
			}
			TfProduct tfProduct = tfProductService.selectByPrimaryKey(TransformUtils.toInt(productId));
			if(tfProduct==null){
				ResponseVo.send102Code(response, "找不到该产品");
			}
			String userId = request.getParameter("userId");//用户ID
			if(StringUtil.notEmpty(userId)){//如果userId不为空，查询我的收藏中是否有该条记录
				TfCollection tfCollection=collectionService.selectCountByUserIdAndKeyIdAndType(TransformUtils.toInt(userId), TransformUtils.toInt(productId), TfCollection.cn_type_product);
				if(tfCollection!=null){//说明该条件是收藏
					tfProduct.setIsCollection(true);
				}
			}
			//添加查看历史记录：1、如果有最近的记录则覆盖 2、直接添加
			//根据商品类型、商品id查询历史记录,类型为3
			if(TransformUtils.toInteger(userId)!=null&&TransformUtils.toInteger(userId)>0){
				if(TransformUtils.toInteger(productId)==null||TransformUtils.toInteger(productId)<1){
					log.error("TfArticleController/detail======添加历史记录失败,商品Id为空或格式错误");
				}else{
					List<TfHistory>	h  = tfHistoryService.findHistoryByUserAndType(TransformUtils.toInteger(userId), 3, TransformUtils.toInteger(productId));
					if(h==null||h.size()==0){//新增历史记录
						TfHistory h1 = new TfHistory();
						h1.sethProductId(TransformUtils.toInteger(productId));
						h1.sethCreateTime(new Date());
						h1.sethType(3);
						h1.sethUserId(TransformUtils.toInteger(userId));
						int i = tfHistoryService.addHistory(h1);
						if(i!=1){
							log.error("TfArticleController/articleDetail======添加历史记录失败，新增数据失败");
						}
					}else{//更新历史记录
						TfHistory h2 = new TfHistory();
						h2.setHistoryId(h.get(0).getHistoryId());
						h2.sethUpdateTime(new Date());
						int j = tfHistoryService.updateHistory(h2);
						if(j!=1){
							log.error("TfArticleController/articleDetail======更新历史记录失败，更新数据失败");
						}
						if(h.size()>1){
							for(int i=1;i<h.size();i++){
								int k = tfHistoryService.deleteHistoryById(h.get(i).getHistoryId());
								if(k!=1){
									log.error("TfArticleController/articleDetail======删除重复的历史记录失败");
									break;
								}
							}
						}
					}
				}
			}
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONObject dataJson = new JSONObject();
			JSONObject userJson = JSONObject.fromObject(tfProduct, config);
			dataJson.put("tfProduct", userJson);
			ResponseVo.common("1", "操作成功", dataJson, response);
		}
		/**
		 * 商品产品删除(将产品拉黑禁用)
		 * @param request
		 * @param response
		 * @throws Exception
		 * @throws IOException
		 */
		@RequestMapping(value = "delete", method = RequestMethod.POST)
		public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.info("======获取产品删除");
			try {
				request.setCharacterEncoding("utf-8");
				response.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String productId = request.getParameter("productId");//大师ID
			
			String masterId = request.getParameter("masterId");
			String realm = request.getHeader("Realm");
			if(StringUtil.notEmpty(realm)&&"master".equals(realm)){//判断是否为大师端的操作
				if(StringUtil.empty(masterId)){
					ResponseVo.send101Code(response, "masterId为空");
					return;
				}
				TfMaster tfMaster = tfMasterService.selectByPrimaryKey(TransformUtils.toInt(masterId));
				if(tfMaster==null){
					ResponseVo.send102Code(response, "该大师不存在为空");
					return;
				}
				if(tfMaster.getmDeleted()){
					ResponseVo.send121Code(response, "该大师被禁用拉黑");;
					return;
				}
			}else{
				log.error("TfProductController/delete=============数据不正确，Realm为空或者Realm不为大师端"+realm);
				ResponseVo.send103Code(response, "数据不正确，Realm为空或者Realm不为大师端"+realm);
				return;
			}
			if (StringUtil.empty(productId)) {
				ResponseVo.send101Code(response, "产品ID为空");
				return;
			}
			TfProduct tfProduct = tfProductService.selectByPrimaryKey(TransformUtils.toInt(productId));
			if(tfProduct==null){
				ResponseVo.send102Code(response, "找不到该产品");
				return;
			}
			//判断是否属于该大师
			if(tfProduct.getpMasterId()!=TransformUtils.toInt(masterId)){
				log.error("TfProductController/delete=============该文章不属于该大师，不允许删除操作");
				ResponseVo.send103Code(response, "该文章不属于该大师，不允许删除操作");
				return;
			}
			//修改产品
			TfProduct updateTfProduct = new TfProduct();
			updateTfProduct.setProductId(tfProduct.getProductId());
			updateTfProduct.setpDelete(true);
			updateTfProduct.setpUpdateTime(new Date());
			int i = tfProductService.updateByPrimaryKeySelective(updateTfProduct);
			if(i<1){
				ResponseVo.send106Code(response, "删除失败，数据库操作失败");
				return;
			}
//			int i = tfProductService.deleteByPrimaryKey(TransformUtils.toInt(productId));
//			if(i<1){
//				ResponseVo.send106Code(response, "删除失败，数据库操作失败");
//				return;
//			}
//			//删除完记录后删除文件信息并删除附件
//			List<TfImgTmp> list = tfImgTmpService.getListByArticle(TransformUtils.toInt(productId), TfArticle.type_product);
//			TfImgTmp tfImgTmp =  null;
//			if(list!=null&&list.size()!=0){//循环清除数据并删除文件
//				for(int j=0;j<list.size();j++){
//					tfImgTmp = list.get(j);
//					//删除数据
//					int k = tfImgTmpService.deleteByPrimaryKey(tfImgTmp.getImgTmpId());
//					if(k<1){
//						log.error("删除文件失败=================改文件ID为"+tfImgTmp.getImgTmpId()+",进度为"+j+"/"+list.size());
//					}else{//删除文件
//						 File f = new File(tfImgTmp.getItAbsolute());  
//						 if(f.exists() && f.isDirectory()){  
//						         f.delete();  
//						    }  
//					}
//				}
//			}
			ResponseVo.common("1", "操作成功", new JSONObject(), response);
		}
}
