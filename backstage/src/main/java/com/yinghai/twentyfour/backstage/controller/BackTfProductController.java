package com.yinghai.twentyfour.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfImgTmp;
import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.service.TfImgTmpService;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.EnumUtil;
import com.yinghai.twentyfour.common.util.ImageUploadUtil;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.RandomUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;

/**
 * 后台管理——产品管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/product")
public class BackTfProductController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TfProductService tfProductService;
	
	@Autowired
	private TfImgTmpService tfImgTmpService;
//
//	@Autowired
//	private TfArticleService tfArticleService;
	/**
	 * 文章管理列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfArticleController/list======后台管理文章列表======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：查询条件(评论ID、评论人ID、商品ID、文章ID)，页数、条数
		String num = request.getParameter("page");
		String size = request.getParameter("pageSize");
		Integer pageNum = (num!=null&&TransformUtils.toInteger(num)>0)?TransformUtils.toInteger(num):1;
		Integer pageSize = (size!=null&&TransformUtils.toInteger(size)>0)?TransformUtils.toInteger(size):10;
		TfProduct tfProduct = new TfProduct();
		String masterId = request.getParameter("masterId");
		if(StringUtil.notEmpty(masterId)){
			tfProduct.setpMasterId(TransformUtils.toInt(masterId));
		}
		String name = request.getParameter("pName");
		if (StringUtil.notEmpty(name)) {
			tfProduct.setpName("%"+name.trim()+"%");
		}
		Page<TfProduct> page = tfProductService.getTfProductRecord(pageNum, pageSize, tfProduct);
		  if(tfProduct.getpName()!=null&&tfProduct.getpName().startsWith("%")){
			  tfProduct.setpName(name.trim());
	        }
		model.addAttribute("product", tfProduct);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("pageCount", page.getPages());
		model.addAttribute("recordCount",page.getTotal());
		return "tfProduct/list";
	}
//	
//	/**
//	 * 删除文章
//	 * @param request
//	 * @param response
//	 */
//	@RequestMapping("/delete")
//	public void delete(HttpServletRequest request,HttpServletResponse response){
//		logger.info("BackTfArticleController/delete======后台管理删除评论======");
//		try {
//			request.setCharacterEncoding("utf-8");
//			response.setCharacterEncoding("utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		//获取参数：评论id
//		String articleId = request.getParameter("articleId");
//		if(StringUtil.empty(articleId)){
//			logger.error("======BackTfArticleController/delete======masterId为空");
//			ResponseVo.send101Code(response, "masterId为空");
//			return;
//		}
//		Integer masterId = TransformUtils.toInteger(articleId);
//		if(masterId<0){
//			logger.error("======BackTfArticleController/delete======masterId格式错误");
//			ResponseVo.send104Code(response, "masterId格式错误");
//			return;
//		}
//		int i = tfArticleService.deleteByPrimaryKey(masterId);
//		if(i!=1){
//			logger.error("======BackTfArticleController/delete======数据出错，masterId删除失败");
//			ResponseVo.send106Code(response, "数据出错，masterId删除失败");
//			return;
//		}
//		ResponseVo.send1Code(response, "success", new JSONObject());
//	}
	/**
	 * 跳轉编辑或新增文章
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfArticleController/edit======后台管理进入新增或编辑评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数
		Integer productId= TransformUtils.toInteger(request.getParameter("productId"));
		System.out.println("*********"+productId);
		if(productId!=null&&productId>0){
			TfProduct tfProduct = tfProductService.selectByPrimaryKey(productId);
			System.out.println("*********"+tfProduct);
			model.addAttribute("product", tfProduct);
		}
		return "tfProduct/edit";
	}
	/**
	 * 保存或更新产品
	 * @param request
	 * @param response
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/save")
	public void saveOrUpdate(HttpServletRequest request,HttpServletResponse response) throws ParseException, IllegalStateException, IOException{
		logger.info("BackTfArticleController/edit======后台管理进入新增修改文章======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String productId = request.getParameter("productId");//产品ID
		String pName = request.getParameter("pName");//名称
		String pIntroduction = request.getParameter("pIntroduction");//简介
		String pPrice = request.getParameter("pPrice");//价格
		String pTotal = request.getParameter("pTotal");//数量
		String pSize = request.getParameter("pSize");//尺寸
		String pAttribution = request.getParameter("pAttribution");
		String pOffline = request.getParameter("pOffline");//上下架
		String masterId = request.getParameter("pMasterId");//大师ID
		String pFreeShipping = request.getParameter("pFreeShipping");//包邮
		String act = request.getParameter("act");//更新or新增
		TfProduct tfProduct = new TfProduct();
		tfProduct.setpOffline(TransformUtils.toBoolean(pOffline));
		tfProduct.setpFreeShipping(TransformUtils.toBoolean(pFreeShipping));
		if(StringUtil.notEmpty(pName)){
			tfProduct.setpName(pName);
		}
		if(StringUtil.notEmpty(pIntroduction)){
			tfProduct.setpIntroduction(pIntroduction);
		}
		if(StringUtil.notEmpty(pPrice)){
			tfProduct.setpPrice(TransformUtils.toInt(pPrice)*100);
		}
		if(StringUtil.notEmpty(pTotal)){
			tfProduct.setpTotal(TransformUtils.toInt(pTotal));
		}
		if(StringUtil.notEmpty(pSize)){
			tfProduct.setpSize(pSize);
		}
		if(StringUtil.notEmpty(pAttribution)){
			tfProduct.setpAttribution(pAttribution);
		}
		String imgIds = "";
		String boochange = request.getParameter("boochange");
		if (StringUtil.notEmpty(boochange) && "true".equals(boochange)) {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {
							System.out.println(myFileName);
							// 重命名上传后的文件名
							String fileName =RandomUtil.getrandomString(5, EnumUtil.both)+"_"+myFileName;;
							// 定义上传路径
							String realPath = Constant.filepath + Constant.productimage+masterId+"/";
							File fileIo = new File(realPath);
							// 创建文件夹
							if (!fileIo.exists()) {
								fileIo.mkdirs();
							}
							String path = realPath + fileName;
							File localFile = new File(path);
							file.transferTo(localFile);
							TfImgTmp tfImgTmp = new TfImgTmp();
							tfImgTmp.setItMasterId(TransformUtils.toInt(masterId));
							tfImgTmp.setItKeyId(0);
							tfImgTmp.setItCreateTime(new Date());
							tfImgTmp.setItIsUser(false);
							tfImgTmp.setItType(TfImgTmp.itType_protect);
							tfImgTmp.setItUrl(Constant.imageUrl+"/"+Constant.productimage+"/"+masterId+"/"+fileName);
							tfImgTmp.setItAbsolute(Constant.filepath+"/"+Constant.productimage+"/"+masterId+"/"+fileName);
							int i = tfImgTmpService.insertSelective(tfImgTmp);
							if(i<1){
								ResponseVo.send106Code(response, "圖片數據庫操作失敗");
								return;
							}
							imgIds = imgIds+tfImgTmp.getImgTmpId()+";";
						}
					}
				}
			}
		}
		if("upd".equals(act)){//更新操作
			tfProduct.setProductId(TransformUtils.toInt(productId));
			tfProduct.setpUpdateTime(new Date());
			int i = tfProductService.updateByPrimaryKeySelective(tfProduct);
			if(i<1){
				ResponseVo.send106Code(response, "更新失敗，數據庫更新失敗");
				return;
			}else{
				//更新成功后绑定图片信息
				//新增成功數據庫，判斷是否有附件圖片
				if(StringUtil.notEmpty(imgIds)){
					String[] ids = imgIds.split(";");
					if(ids!=null&&ids.length!=0){
						 i = tfImgTmpService.updateKeyIdById(ids, tfProduct.getProductId());
					}
				}
				String ids = request.getParameter("ids");//需要删除的图片ID
				if (StringUtil.notEmpty(ids)) {
					String[] idss = ids.split(";");
					if(ids!=null&&idss.length!=0){
						for(int j=0;j<idss.length;j++){//循环删除图片信息与图片
							TfImgTmp tfImgTmp = tfImgTmpService.selectByPrimaryKey(TransformUtils.toInt(idss[j]));
							if(tfImgTmp!=null){
								int k = tfImgTmpService.deleteByPrimaryKey(tfImgTmp.getImgTmpId());
								if(k<1){
									logger.error("删除文件失败=================该文件ID为"+tfImgTmp.getImgTmpId()+",进度为"+j+"/"+idss.length);
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
				ResponseVo.common("2", "更新成功", new JSONObject(), response);
			}
		}else if("add".equals(act)){
			tfProduct.setpMasterId(0);
			tfProduct.setpCreateTime(new Date());
			int i = tfProductService.insertSelective(tfProduct);
			if(i<1){
				ResponseVo.send106Code(response, "新增失敗，數據庫更新失敗");
				return;
			}else{
				//新增成功后绑定图片信息
				//新增成功數據庫，判斷是否有附件圖片
				if(StringUtil.notEmpty(imgIds)){
					String[] ids = imgIds.split(";");
					if(ids!=null&&ids.length!=0){
						 i = tfImgTmpService.updateKeyIdById(ids, tfProduct.getProductId());
					}
				}
				ResponseVo.common("1", "新增成功", new JSONObject(), response);
			}
		}
	}
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfArticleController/delete======后台管理进入新增或编辑评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数
		Integer productId= TransformUtils.toInteger(request.getParameter("productId"));
		System.out.println("*********"+productId);
		TfProduct tfProduct = tfProductService.selectByPrimaryKey(TransformUtils.toInt(productId));
		if(tfProduct==null){
			ResponseVo.send102Code(response, "找不到该产品");
			return;
		}
		//更新产品为禁用
		TfProduct upProduct = new TfProduct();
		upProduct.setProductId(TransformUtils.toInt(productId));
		upProduct.setpUpdateTime(new Date());
		upProduct.setpDelete(true);
		int i = tfProductService.updateByPrimaryKeySelective(upProduct);
		if(i<1){
			ResponseVo.send106Code(response, "删除失败，数据库操作失败");
			return;
		}
		ResponseVo.common("1", "删除成功", new JSONObject(), response);
		return;
//		if(productId!=null&&productId>0){
//		int i = tfProductService.deleteByPrimaryKey(productId);
//		if(i>0){
//			ResponseVo.common("1", "删除成功", new JSONObject(), response);
//			return;
//		}else{
//			ResponseVo.send106Code(response, "删除失敗，數據庫更新失敗");
//			return;
//		}
//		}
	}
}
