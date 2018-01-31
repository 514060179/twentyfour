package com.yinghai.twentyfour.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yinghai.twentyfour.app.service.CollectionService;
import com.yinghai.twentyfour.app.service.HistoryService;
import com.yinghai.twentyfour.common.model.TfArticle;
import com.yinghai.twentyfour.common.model.TfCollection;
import com.yinghai.twentyfour.common.model.TfHistory;
import com.yinghai.twentyfour.common.model.TfImgTmp;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.service.TfArticleService;
import com.yinghai.twentyfour.common.service.TfImgTmpService;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 文章管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/tfarticle")
public class TfArticleController {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private TfMasterService tfMasterService;

	@Autowired
	private TfArticleService tfArticleService;

	@Autowired
	private TfImgTmpService tfImgTmpService;

	@Autowired
	private HistoryService tfHistoryService;

	@Autowired
	private CollectionService collectionService;

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public void saveArticle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		log.info("======保存文章");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String pathAll = receiveFiles(request, response);
		// if(StringUtil.empty(pathAll)){
		// log.info("================该文章无图片提交");
		// }
		String imageIds = request.getParameter("imageIds");
		String masterId = request.getParameter("masterId");
		if (StringUtil.empty(masterId)) {
			log.error("TfArticleController/saveArticle=============masterId为空");
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		String imageUrl = request.getParameter("imageUrl");// 图片URL,多个

		TfMaster tfMaster = tfMasterService.selectByPrimaryKey(TransformUtils.toInt(masterId));
		if (tfMaster == null) {
			log.error("TfArticleController/saveArticle=============tfMaster为空，该大师未登记");
			ResponseVo.send101Code(response, "tfMaster为空");
			return;
		}

		String abortDate = request.getParameter("abortDate");// 截止日期，可为空

		String content = request.getParameter("content");// 发布内容
		if (StringUtil.empty(content)) {
			log.error("TfArticleController/saveArticle=============content为空");
			ResponseVo.send101Code(response, "content为空");
			return;
		}
		String aTitle = request.getParameter("title");// 标题
		if (StringUtil.empty(aTitle)) {
			log.error("TfArticleController/saveArticle=============aTitle为空");
			ResponseVo.send101Code(response, "title标题为空");
			return;
		}
		String url = request.getParameter("url");// 链接地址
		TfArticle tfArticle = new TfArticle();
		if (StringUtil.notEmpty(imageUrl)) {
			tfArticle.setaImg(imageUrl);
		}
		tfArticle.setaMasterId(TransformUtils.toInt(masterId));
		String publishDate = request.getParameter("publishDate");// 发布时间，不可为空
		if (StringUtil.empty(publishDate)) {
			tfArticle.setaPublishDate(new Date());
		} else {
			tfArticle.setaPublishDate(format.parse(publishDate));
		}

		tfArticle.setaContent(content);
		if (StringUtil.notEmpty(abortDate)) {
			tfArticle.setaAbortDate(format.parse(abortDate));
		}
		if (StringUtil.notEmpty(url)) {
			tfArticle.setaUrl(url);
		}
		String aType = request.getParameter("type");
		if (StringUtil.empty(aTitle)) {
			log.error("TfArticleController/saveArticle=============type为空");
			ResponseVo.send101Code(response, "type标题为空");
			return;
		}
		tfArticle.setaType(aType);
		tfArticle.setaTitle(aTitle);
		tfArticle.setaCreateTime(new Date());
		int i = tfArticleService.insertSelective(tfArticle);
		if (i < 1) {
			log.error("TfArticleController/saveArticle=============新增文章数据失败");
			ResponseVo.send106Code(response, "新增文章数据失败");
			return;
		}
		// 新增成功數據庫，判斷是否有附件圖片
		if (StringUtil.notEmpty(imageIds)) {
			String[] ids = imageIds.split(";");
			if (ids != null && ids.length != 0) {
				i = tfImgTmpService.updateKeyIdById(ids, tfArticle.getArticleId());
			}
		}
		String deleteImgIds = request.getParameter("deleteImgIds");// 所需要删除的图片ID
		if (StringUtil.notEmpty(deleteImgIds)) {
			String[] ids = deleteImgIds.split(";");
			if (ids != null && ids.length != 0) {
				for (int j = 0; j < ids.length; j++) {// 循环删除图片信息与图片
					TfImgTmp tfImgTmp = tfImgTmpService.selectByPrimaryKey(TransformUtils.toInt(ids[i]));
					if (tfImgTmp != null) {
						int k = tfImgTmpService.deleteByPrimaryKey(tfImgTmp.getImgTmpId());
						if (k < 1) {
							log.error("删除文件失败=================该文件ID为" + tfImgTmp.getImgTmpId() + ",进度为" + j + "/"
									+ ids.length);
						} else {// 删除文件
							File f = new File(tfImgTmp.getItAbsolute());
							if (f.exists() && f.isDirectory()) {
								f.delete();
							}
						}
					}
				}
			}
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfArticle, config);
		dataJson.put("article", userJson);
		ResponseVo.common("1", "成功", dataJson, response);
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public void edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		log.info("======修改文章");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String pathAll = receiveFiles(request, response);
		// if(StringUtil.empty(pathAll)){
		// log.info("================该文章无图片提交");
		// }
		String imageIds = request.getParameter("imageIds");// 该文章现存的所有图片
		String masterId = request.getParameter("masterId");
		if (StringUtil.empty(masterId)) {
			log.error("TfArticleController/edit=============masterId为空");
			ResponseVo.send101Code(response, "masterId为空");
			return;
		}
		TfMaster tfMaster = tfMasterService.selectByPrimaryKey(TransformUtils.toInt(masterId));
		if (tfMaster == null) {
			ResponseVo.send102Code(response, "该大师不存在");
			return;
		}
		if (tfMaster.getmDeleted()) {
			ResponseVo.send121Code(response, "该大师呗禁用");
			return;
		}
		// String imageUrl = request.getParameter("imageUrl");// 图片URL,多个
		String articleId = request.getParameter("articleId");
		if (StringUtil.empty(articleId)) {
			log.error("TfArticleController/edit=============articleId为空");
			ResponseVo.send101Code(response, "articleId为空");
			return;
		}
		TfArticle tfArticle = tfArticleService.selectByPrimaryKey(TransformUtils.toInt(articleId));
		if (tfArticle == null) {
			log.error("TfArticleController/edit=============该文章不属于该大师，不允许编辑操作");
			ResponseVo.send103Code(response, "该文章不属于该大师，不允许编辑操作");
			return;
		}
		if(tfMaster.getMasterId()!=tfArticle.getaMasterId()){
			
		}
		tfArticle = new TfArticle();
		tfArticle.setArticleId(TransformUtils.toInt(articleId));
		String abortDate = request.getParameter("abortDate");// 截止日期，可为空

		String content = request.getParameter("content");// 发布内容
		if (StringUtil.notEmpty(content)) {
			tfArticle.setaContent(content);
		}
		String aTitle = request.getParameter("title");// 标题
		if (StringUtil.notEmpty(aTitle)) {
			tfArticle.setaTitle(aTitle);
		}
		// String url = request.getParameter("url");// 链接地址
		// if (StringUtil.notEmpty(imageUrl)) {
		// tfArticle.setaImg(imageUrl);
		// }
		tfArticle.setaMasterId(TransformUtils.toInt(masterId));
		String publishDate = request.getParameter("publishDate");// 发布时间
		if (StringUtil.notEmpty(publishDate)) {
			tfArticle.setaPublishDate(format.parse(publishDate));
		}
		if (StringUtil.notEmpty(abortDate)) {
			tfArticle.setaAbortDate(format.parse(abortDate));
		}
		// if (StringUtil.notEmpty(url)) {
		// tfArticle.setaUrl(url);
		// }
		String aType = request.getParameter("type");
		if (StringUtil.notEmpty(aType)) {
			tfArticle.setaType(aType);
		}

		tfArticle.setaUpdateTime(new Date());
		int i = tfArticleService.updateByPrimaryKeySelective(tfArticle);
		if (i < 1) {
			log.error("TfArticleController/edit=============现存图片为" + imageIds);
			log.error("TfArticleController/edit=============新增文章数据失败");
			ResponseVo.send106Code(response, "新增文章数据失败");
			return;
		}
		String[] ids = {};
		// 新增成功數據庫，判斷是否有附件圖片
		if (StringUtil.notEmpty(imageIds)) {
			 ids = imageIds.split(";");
			if (ids != null && ids.length != 0) {
				i = tfImgTmpService.updateKeyIdById(ids, tfArticle.getArticleId());
			}
		}

			// 更新完成后，从数据库查图片数据，然后差集，用查出来的减去APP传过来的，得出来的就是需要删除的图片

			List<TfImgTmp> list = tfImgTmpService.getListByArticle(TransformUtils.toInt(articleId),
					TfImgTmp.itType_article);
			if (list != null && list.size() != 0) {
				ArrayList allIds = new ArrayList();
				for (int j = 0; j < list.size(); j++) {
					allIds.add(list.get(j).getImgTmpId());
				}
				ArrayList list2 = new ArrayList(Arrays.asList(ids));// 现存的所有ids
				// 差集
				allIds.removeAll(list2);
				if (allIds != null && allIds.size() != 0) {// 准备处理需要删除的图片
					for (int j = 0; j < allIds.size(); j++) {// 循环删除图片信息与图片
						TfImgTmp tfImgTmp = tfImgTmpService.selectByPrimaryKey(TransformUtils.toInt(allIds.get(j)));
						if (tfImgTmp != null) {
							int k = tfImgTmpService.deleteByPrimaryKey(tfImgTmp.getImgTmpId());
							if (k < 1) {
								log.error("删除文件失败=================该文件ID为" + tfImgTmp.getImgTmpId() + ",进度为" + j + "/"
										+ allIds.size());
							} else {// 删除文件
								File f = new File(tfImgTmp.getItAbsolute());
								if (f.exists() && f.isDirectory()) {
									f.delete();
								}
							}
						}
					}
				}
			} else {
				log.error("TfArticleController/edit=============文章图片数据查找失败");
				ResponseVo.send102Code(response, "文章图片数据查找失败");
				return;
			}
		tfArticle = tfArticleService.selectByPrimaryKey(tfArticle.getArticleId());
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfArticle, config);
		dataJson.put("article", userJson);
		ResponseVo.common("1", "成功", dataJson, response);
	}

	/**
	 * 文章详情
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	public void articleDetail(HttpServletRequest request, HttpServletResponse response) {
		log.info("======文章详情");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String articleId = request.getParameter("articleId");
		if (StringUtil.empty(articleId)) {
			log.error("TfArticleController/articleDetail=============articleId为空");
			ResponseVo.send101Code(response, "articleId为空");
			return;
		}
		TfArticle tfArticle = tfArticleService.selectByPrimaryKey(TransformUtils.toInt(articleId));
		if (tfArticle == null) {
			log.error("TfArticleController/articleDetail=============tfArticle为空,该文章不存在");
			ResponseVo.send102Code(response, "tfArticle为空");
			return;
		}

		Integer userId = TransformUtils.toInteger(request.getParameter("userId"));
		if (userId == null || (userId < 1&&userId!=-1)) {
			log.error("TfArticleController/articleDetail=============userId为空" + request.getParameter("userId"));
			ResponseVo.send101Code(response, "userId为空");
			return;
		}
		// 添加查看历史记录：1、如果有最近的记录则覆盖 2、直接添加
		// 根据文章类型、文章id查询历史记录,类型为2
		if (userId != null && userId > 0) {
			if (TransformUtils.toInteger(articleId) == null || TransformUtils.toInteger(articleId) < 1) {
				log.error("TfArticleController/articleDetail======添加历史记录失败,文章Id为空或格式错误");
			} else {
				List<TfHistory> h = tfHistoryService.findHistoryByUserAndType(userId, 2,
						TransformUtils.toInteger(articleId));
				if (h == null || h.size() == 0) {// 新增历史记录
					TfHistory h1 = new TfHistory();
					h1.sethArticleId(TransformUtils.toInteger(articleId));
					h1.sethCreateTime(new Date());
					h1.sethType(2);
					h1.sethUserId(userId);
					int i = tfHistoryService.addHistory(h1);
					if (i != 1) {
						log.error("TfArticleController/articleDetail======添加历史记录失败，新增数据失败");
					}
				} else {// 更新历史记录
					TfHistory h2 = new TfHistory();
					h2.setHistoryId(h.get(0).getHistoryId());
					h2.sethUpdateTime(new Date());
					int j = tfHistoryService.updateHistory(h2);
					if (j != 1) {
						log.error("TfArticleController/articleDetail======更新历史记录失败，更新数据失败");
					}
					if (h.size() > 1) {
						for (int i = 1; i < h.size(); i++) {
							int k = tfHistoryService.deleteHistoryById(h.get(i).getHistoryId());
							if (k != 1) {
								log.error("TfArticleController/articleDetail======删除重复的历史记录失败");
								break;
							}
						}
					}
				}
			}
		}
		TfCollection tfCollection = collectionService.selectCountByUserIdAndKeyIdAndType(userId,
				TransformUtils.toInt(articleId), TfCollection.cn_type_article);
		if (tfCollection != null) {// 说明该条件是收藏
			tfArticle.setIsCollection(true);
		}
		// 更新文章的阅读量
		TfArticle uptfTfArticle = new TfArticle();
		uptfTfArticle.setArticleId(TransformUtils.toInt(articleId));
		uptfTfArticle.setaReadAmount(tfArticle.getaReadAmount() + 1);// 阅读量+1
		uptfTfArticle.setaUpdateTime(new Date());
		int i = tfArticleService.updateByPrimaryKeySelective(uptfTfArticle);
		if (i < 1) {
			log.error("TfArticleController/detail=============更新文章阅读量");
			ResponseVo.send106Code(response, "更新文章阅读量失败");
			return;
		}
		tfArticle.setaReadAmount(tfArticle.getaReadAmount() + 1);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject dataJson = new JSONObject();
		JSONObject userJson = JSONObject.fromObject(tfArticle, config);
		dataJson.put("tfArticle", userJson);
		ResponseVo.common("1", "操作成功", dataJson, response);
	}

	/**
	 * 文章详情
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public void articleList(HttpServletRequest request, HttpServletResponse response) {
		log.info("======获取文章列表");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Integer pageNumber = TransformUtils.toInt(request.getParameter("page"));
		if (pageNumber == null || pageNumber == 0) {
			pageNumber = 1;
		}
		Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		TfArticle tfArticle = new TfArticle();
		String masterId = request.getParameter("masterId");
		if (StringUtil.notEmpty(masterId)) {
			tfArticle.setaMasterId(TransformUtils.toInt(masterId));
		}
		String aType = request.getParameter("type");
		if (StringUtil.notEmpty(aType)) {
			//tfArticle.setaType(aType);
			String[] array = aType.split(",");
			tfArticle.setaTypeArray(array);
		}
		String userId = request.getParameter("userId");
		if (StringUtil.notEmpty(userId)) {
			tfArticle.setQueryUserId(TransformUtils.toInt(userId));
		}
		int statrNumber = 0;//开始获取数据的下标
		if(pageNumber!=1){
			statrNumber = pageNumber*pageSize;
		}
		List<TfArticle> page = tfArticleService.getTfArticleAndImgRecord(statrNumber, pageSize, tfArticle);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray data = JSONArray.fromObject(page, config);
		JSONObject modelObject = new JSONObject();
		modelObject.put("articleList", data);
		ResponseVo.common("1", "操作成功", modelObject, response);
	}

	/**
	 * 文章删除
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		log.info("======文章删除");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String articleId = request.getParameter("articleId");
		String masterId = request.getParameter("masterId");
		String realm = request.getHeader("Realm");
		if (StringUtil.notEmpty(realm) && "master".equals(realm)) {// 判断是否为大师端的操作
			if (StringUtil.empty(masterId)) {
				ResponseVo.send101Code(response, "masterId为空");
				return;
			}
			TfMaster tfMaster = tfMasterService.selectByPrimaryKey(TransformUtils.toInt(masterId));
			if (tfMaster == null) {
				ResponseVo.send102Code(response, "该大师不存在为空");
				return;
			}
			if (tfMaster.getmDeleted()) {
				ResponseVo.send121Code(response, "该大师被禁用拉黑");
				;
				return;
			}
		} else {
			log.error("TfArticleController/delete=============数据不正确，Realm为空或者Realm不为大师端" + realm);
			ResponseVo.send103Code(response, "数据不正确，Realm为空或者Realm不为大师端" + realm);
			return;
		}
		if (StringUtil.empty(articleId)) {
			log.error("TfArticleController/delete=============articleId为空");
			ResponseVo.send101Code(response, "articleId为空");
			return;
		}
		TfArticle tfArticle = tfArticleService.selectByPrimaryKey(TransformUtils.toInt(articleId));
		if (tfArticle == null) {
			log.error("TfArticleController/delete=============tfArticle为空,该文章不存在");
			ResponseVo.send102Code(response, "tfArticle为空");
			return;
		}
		// 判断是否属于该大师
		if (tfArticle.getaMasterId() != TransformUtils.toInt(masterId)) {
			log.error("TfArticleController/delete=============该文章不属于该大师，不允许删除操作");
			ResponseVo.send103Code(response, "该文章不属于该大师，不允许删除操作");
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
//		int i = tfArticleService.deleteByPrimaryKey(TransformUtils.toInt(articleId));
//		if (i < 1) {
//			ResponseVo.send106Code(response, "删除失败，数据库操作失败");
//			return;
//		}
//		// 删除完记录后删除文件信息并删除附件
//		List<TfImgTmp> list = tfImgTmpService.getListByArticle(TransformUtils.toInt(articleId), TfArticle.type_article);
//		TfImgTmp tfImgTmp = null;
//		if (list != null && list.size() != 0) {// 循环清除数据并删除文件
//			for (int j = 0; j < list.size(); j++) {
//				tfImgTmp = list.get(j);
//				// 删除数据
//				int k = tfImgTmpService.deleteByPrimaryKey(tfImgTmp.getImgTmpId());
//				if (k < 1) {
//					log.error(
//							"删除文件失败=================改文件ID为" + tfImgTmp.getImgTmpId() + ",进度为" + j + "/" + list.size());
//				} else {// 删除文件
//					File f = new File(tfImgTmp.getItAbsolute());
//					if (f.exists() && f.isDirectory()) {
//						f.delete();
//					}
//				}
//			}
//		}
		ResponseVo.common("1", "操作成功", new JSONObject(), response);
	}

	
}
