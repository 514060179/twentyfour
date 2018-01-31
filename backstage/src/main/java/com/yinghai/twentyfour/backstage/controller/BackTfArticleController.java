package com.yinghai.twentyfour.backstage.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfProduct;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.service.TfProductService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.ImageUploadUtil;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;

/**
 * 后台管理——评论管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/article")
public class BackTfArticleController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TfUserService tfUserService;

	@Autowired
	private TfArticleService tfArticleService;
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
		TfArticle tfArticle = new TfArticle();
		String masterId = request.getParameter("masterId");
		if(StringUtil.notEmpty(masterId)){
			tfArticle.setaMasterId(TransformUtils.toInt(masterId));
		}
		String aType = request.getParameter("aType");
		if (StringUtil.notEmpty(aType)) {
			tfArticle.setaType(aType);
		}
		Page<TfArticle> page = tfArticleService.getTfArticleRecord(pageNum, pageSize, tfArticle);
		model.addAttribute("article", tfArticle);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("pageCount", page.getPages());
		model.addAttribute("recordCount",page.getTotal());
		return "tfArticle/list";
	}
	
	/**
	 * 删除文章
	 * @param request
	 * @param response
	 */
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response){
		logger.info("BackTfArticleController/delete======后台管理删除评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数：评论id
		String articleId = request.getParameter("articleId");
		if(StringUtil.empty(articleId)){
			logger.error("======BackTfArticleController/delete======masterId为空");
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		Integer masterId = TransformUtils.toInteger(articleId);
		if(masterId<0){
			logger.error("======BackTfArticleController/delete======masterId格式错误");
			ResponseVo.send104Code(response, "masterId格式错误");
			return;
		}
		TfArticle tfArticle = tfArticleService.selectByPrimaryKey(TransformUtils.toInt(articleId));
		if (tfArticle == null) {
			//log.error("TfArticleController/delete=============tfArticle为空,该文章不存在");
			ResponseVo.send102Code(response, "tfArticle该文章不存在");
			return;
		}
		TfArticle updateTfArticle = new TfArticle();
		updateTfArticle.setArticleId(tfArticle.getArticleId());
		updateTfArticle.setaUpdateTime(new Date());
		updateTfArticle.setaDelete(true);
		int i = tfArticleService.updateByPrimaryKeySelective(updateTfArticle);
		if (i < 1) {
			ResponseVo.send106Code(response, "拉黑文章失败，数据库操作失败");
			return;
		}
//		int i = tfArticleService.deleteByPrimaryKey(masterId);
//		if(i!=1){
//			logger.error("======BackTfArticleController/delete======数据出错，masterId删除失败");
//			ResponseVo.send106Code(response, "数据出错，masterId删除失败");
//			return;
//		}
		ResponseVo.send1Code(response, "success", new JSONObject());
	}
	/**
	 * 跳轉编辑或新增文章
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edit")
	public String edittfArticle(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("BackTfArticleController/edit======后台管理进入新增或编辑评论======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取参数
		Integer articleId= TransformUtils.toInteger(request.getParameter("articleId"));
		System.out.println("*********"+articleId);
		if(articleId!=null&&articleId>0){
			TfArticle tfArticle = tfArticleService.selectByPrimaryKey(articleId);
			System.out.println("*********"+tfArticle);
			model.addAttribute("article", tfArticle);
		}
		return "tfArticle/edit";
	}
	/**
	 * 保存或更新文章
	 * @param request
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping("/save")
	public void saveOrUpdate(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		logger.info("BackTfArticleController/edit======后台管理进入新增修改文章======");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" ); 
		String articleId = request.getParameter("articleId");//文章ID
		String title = request.getParameter("title");//標題
		String content = request.getParameter("content");//內容
		String publishDate = request.getParameter("publishDate");//發佈日期
		String abortDate = request.getParameter("abortDate");//截止日期
		String act = request.getParameter("act");//更新or新增
		TfArticle tfArticle = new TfArticle();
		if("udp".equals(act)){
			tfArticle.setArticleId(TransformUtils.toInt(articleId));
		}
		tfArticle.setaTitle(title);
		tfArticle.setaContent(content);
		tfArticle.setaPublishDate(sdf.parse(publishDate));
		tfArticle.setaAbortDate(sdf.parse(abortDate));
		if("udp".equals(act)){//更新操作
			tfArticle.setaUpdateTime(new Date());
			int i = tfArticleService.updateByPrimaryKeySelective(tfArticle);
			if(i<1){
				ResponseVo.send106Code(response, "更新失敗，數據庫更新失敗");
				return;
			}else{
				ResponseVo.common("2", "更新成功", new JSONObject(), response);
			}
		}else if("add".equals(act)){
			tfArticle.setaMasterId(0);
			tfArticle.setaType("");
			tfArticle.setaCreateTime(new Date());
			int i = tfArticleService.insertSelective(tfArticle);
			if(i<1){
				ResponseVo.send106Code(response, "新增失敗，數據庫更新失敗");
				return;
			}else{
				ResponseVo.common("1", "新增成功", new JSONObject(), response);
			}
		}
	}
	
	@RequestMapping("imageUpload")
	public void imageUpload(HttpServletRequest request, HttpServletResponse response) {
        String DirectoryName = "upload";
        try {
            ImageUploadUtil.ckeditor(request, response, DirectoryName);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
