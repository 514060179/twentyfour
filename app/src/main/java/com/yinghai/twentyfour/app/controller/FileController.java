package com.yinghai.twentyfour.app.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mysql.jdbc.StreamingNotifiable;
import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.model.TfImgTmp;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfImgTmpService;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.EnumUtil;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.RandomUtil;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 文件管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app/file")
public class FileController {
	private Logger log = Logger.getLogger(this.getClass());
	private static String savePath = Constant.filepath;

	@Autowired
	private TfImgTmpService tfImgTmpService;
	@Autowired
	private TfMasterService tfMasterService;
	@Autowired
	private TfUserService tfUserService;

	/**
	 * 1.判断keyId关联ID是否存在，不存在则根据type在指定表中新增一条数据 2、保存图片信息，并关联keyId，返回数据
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "imageDown", method = RequestMethod.POST)
	public void imageDown(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
		// String savePath =
		// this.getServletContext().getRealPath("/WEB-INF/upload");
		String size = request.getParameter("size");// 图片尺寸
		// 上传时生成的临时文件保存目录
		String type = request.getParameter("type");// 1为大师头像；2为用户头像，3为文章；4为产品
		if (StringUtil.empty(type)) {
			ResponseVo.send101Code(response, "type类型为空");
			return;
		}
		String appPath = request.getParameter("appPath");// app文件路径
		int userId = 0;
		int masterId = 0;
		String realPath = "";// 文件夹路径
		String path = "";// 文件路径
		String fileName = "";// 文件名字
		TfUser tfUser = null;
		TfMaster tfMaster = null;
		if (Constant.IMAGE_TYPE_USER == TransformUtils.toInt(type)) {// 用户头像图片上传
			userId = TransformUtils.toInt(request.getParameter("userId"));
			if (userId == 0) {
				ResponseVo.send101Code(response, "userId用户ID为空");
				return;
			}
			tfUser = tfUserService.selectByPrimaryKey(userId);
			if (tfUser == null) {// 找不到对应的大师
				ResponseVo.send101Code(response, "该用户不存在");
				return;
			}
		} else {// 其他类型是需要接收masterID大师ID
			masterId = TransformUtils.toInt(request.getParameter("masterId"));
			if (masterId == 0) {
				ResponseVo.send101Code(response, "masterId大师ID为空");
				return;
			}
			tfMaster = tfMasterService.selectByPrimaryKey(masterId);
			if (tfMaster == null) {// 找不到对应的大师
				ResponseVo.send101Code(response, "该大师未登记");
				return;
			}
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				ResponseVo.send120Code(response, "无文件提交");
				return;
			}

		}
		// 转换成多部分request
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		// 取得request中的所有文件名
		Iterator<String> iter = multiRequest.getFileNames();
		TfImgTmp tfImgTmp = null;
		while (iter.hasNext()) {
			// 记录上传过程起始时的时间，用来计算上传时间
			// int pre = (int) System.currentTimeMillis();
			// 取得上传文件
			MultipartFile file = multiRequest.getFile(iter.next());
			if (file != null) {
				// 取得当前上传文件的文件名称
				String myFileName = file.getOriginalFilename();
				// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
				if (myFileName.trim() != "") {
					System.out.println(myFileName);
					// 重命名上传后的文件名
					switch (TransformUtils.toInt(type)) {
					case Constant.IMAGE_TYPE_MASTER:
						fileName = tfMaster.getmTel() +new Date().getTime() +"." + file.getOriginalFilename().split("\\.")[1];
						realPath = Constant.filepath + Constant.masterimage;
						path = realPath + fileName;
						break;
					case Constant.IMAGE_TYPE_USER:
						fileName = tfUser.getuTel() +new Date().getTime()+ "." + file.getOriginalFilename().split("\\.")[1];
						realPath = Constant.filepath + Constant.userimage;
						path = realPath + fileName;
						break;
					case Constant.IMAGE_TYPE_ARTICLE:
						fileName = RandomUtil.getrandomString(5, EnumUtil.both) + "_" + myFileName;// 文件重命名，防止文件名重复导致数据库一个文件绑定多个数据
						realPath = Constant.filepath + Constant.articleimage + "/" + masterId+"/";
						path = realPath + fileName;
						break;
					case Constant.IMAGE_TYPE_PRODUCT:
						fileName = RandomUtil.getrandomString(5, EnumUtil.both) + "_" + myFileName;// 文件重命名，防止文件名重复导致数据库一个文件绑定多个数据
						realPath = Constant.filepath + Constant.productimage + "/" + masterId+"/";
						path = realPath + fileName;
						break;
					}
					// 根据类型获取文件名
					// 定义上传路径
					File fileIo = new File(realPath);
					// 创建文件夹
					if (!fileIo.exists()) {
						fileIo.mkdirs();
					}
					File localFile = new File(path);
					file.transferTo(localFile);
					tfImgTmp = new TfImgTmp();
					// 对于头像来说直接上传完做更新用户或者大师操作
					if (Constant.IMAGE_TYPE_USER == TransformUtils.toInt(type)) {// 用户头像，更新用户信息
						TfUser updateTfUser = new TfUser();
						updateTfUser.setUserId(userId);
						updateTfUser.setuUpdateTime(new Date());
						updateTfUser.setuImgUrl(Constant.imageUrl +Constant.userimage + fileName);
						int i = tfUserService.updateByPrimaryKeySelective(updateTfUser);
						if (i < 1) {
							log.error("FileControl/imageDown===================图片上传成功，更新用户数据失败");
							ResponseVo.send106Code(response, "图片上传成功，更新用户数据失败");
							return;
						}
						JSONObject dataJson = new JSONObject();
						dataJson.put("imgUrl", Constant.imageUrl + "/" + Constant.userimage + fileName);
						ResponseVo.common("1", "图片上传成功", dataJson, response);
						return;

					} else if (Constant.IMAGE_TYPE_MASTER == TransformUtils.toInt(type)) {
						TfMaster updateTfMaster = new TfMaster();
						updateTfMaster.setMasterId(masterId);
						updateTfMaster.setmUpdateTime(new Date());
						updateTfMaster.setmHeadImg(Constant.imageUrl + "/" + Constant.masterimage + fileName);
						int i = tfMasterService.updateByPrimaryKeySelective(updateTfMaster);
						if (i < 1) {
							log.error("FileControl/imageDown===================图片上传成功，更新大师数据失败");
							ResponseVo.send106Code(response, "图片上传成功，更新大师数据失败");
							return;
						}
						JSONObject dataJson = new JSONObject();
						dataJson.put("imgUrl", Constant.imageUrl + "/" + Constant.masterimage + fileName);
						ResponseVo.common("1", "图片上传成功", dataJson, response);
						return;
					} else {
						if (StringUtil.notEmpty(appPath)) {
							tfImgTmp.setItAppPath(appPath);
						}
						if (Constant.IMAGE_TYPE_PRODUCT == TransformUtils.toInt(type)) {
							tfImgTmp.setItType(TfImgTmp.itType_protect);
							tfImgTmp.setItUrl(
									Constant.imageUrl + "/" + Constant.productimage + "/" + masterId + "/" + fileName);
						} else {
							tfImgTmp.setItType(TfImgTmp.itType_article);
							tfImgTmp.setItUrl(
									Constant.imageUrl + "/" + Constant.articleimage + "/" + masterId + "/" + fileName);
						}
						if(StringUtil.notEmpty(size)){
							tfImgTmp.setItSize(size);
						}
						tfImgTmp.setItMasterId(masterId);
						tfImgTmp.setItAbsolute(path);
						tfImgTmp.setItKeyId(0);
//						tfImgTmp.setItUrl(
//								Constant.imageUrl + "/" + Constant.productimage + "/" + masterId + "/" + fileName);
						tfImgTmp.setItCreateTime(new Date());
						tfImgTmp.setItIsUser(false);
						int i = tfImgTmpService.insertSelective(tfImgTmp);
						if (i < 1) {
							log.error("FileControl/imageDown===================图片上传成功，保存图片数据失败");
							ResponseVo.send106Code(response, "图片上传成功，保存图片数据失败");
							return;
						}
						JsonConfig config = new JsonConfig();
						config.registerJsonValueProcessor(Date.class,
								new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
						JSONObject dataJson = new JSONObject();
						JSONObject userJson = JSONObject.fromObject(tfImgTmp, config);
						dataJson.put("tfImgTmp", userJson);
						ResponseVo.common("1", "图片上传成功", dataJson, response);
						return;
					}

				}

			}
		}

	}

	@RequestMapping(value = "test", method = RequestMethod.POST)
	public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String test = request.getParameter("test");
		System.out.println("==================================================" + test);
		if (!ServletFileUpload.isMultipartContent(request)) {
			// 按照传统方式获取数据
			ResponseVo.send120Code(response, "无文件提交");
			return;
		}
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
					String fileName = RandomUtil.getrandomString(5, EnumUtil.both) + "_" + myFileName;
					// 定义上传路径
					String realPath = Constant.filepath + Constant.productimage + "\\test";
					File fileIo = new File(realPath);
					// 创建文件夹
					if (!fileIo.exists()) {
						fileIo.mkdirs();
					}
					String path = realPath + "\\" + fileName;
					File localFile = new File(path);
					file.transferTo(localFile);
				}
			}
		}

		ResponseVo.common("1", "成功", new JSONObject(), response);
	}
	
	
	
	@RequestMapping(value = "getProvince", method = RequestMethod.POST)
	public void getProvince(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File file = new File(Constant.provincePath);
		StringBuilder result = new StringBuilder();
		 try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
		 JSONObject json= new JSONObject();
		 json.put("date",  result.toString().replace("\r\n", ""));
		ResponseVo.common("1", "成功", json, response);
	}
	
	public static void main(String[] args) {
		/*File file = new File(Constant.provincePath);
		StringBuilder result = new StringBuilder();
		 try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){   
	            e.printStackTrace(); 
	        }
		 JSONObject json= new JSONObject();
		 json.put("date",  result.toString().replace("\r\n", ""));
		 System.out.println(json);*/
		System.out.println("+"+new Date().getTime());
	}
}
