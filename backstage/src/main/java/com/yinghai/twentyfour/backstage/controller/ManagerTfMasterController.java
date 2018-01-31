package com.yinghai.twentyfour.backstage.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.yinghai.twentyfour.backstage.model.ManagerUser;
import com.yinghai.twentyfour.backstage.model.Menu;
import com.yinghai.twentyfour.backstage.service.ManagerUserService;
import com.yinghai.twentyfour.backstage.service.MenuService;
import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.util.EncryptUtil;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseUtils;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/7/24.
 */
@RequestMapping("/admin/master")
@Controller
public class ManagerTfMasterController {
	@Autowired
	private TfMasterService tfMasterService;
	private Logger log = Logger.getLogger(this.getClass());

	@RequestMapping("/list")
	public String list(HttpServletRequest request, ModelMap model) throws ParseException {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 页码
		Integer pageNo = TransformUtils.toInt(request.getParameter("page"));// 页数
		Integer pageStartSize = TransformUtils.toInt(request.getParameter("pageStartSize"));// 每页存在的条数
		pageNo = pageNo == 0 ? 1 : pageNo;
		pageStartSize = pageStartSize == 0 ? 10 : pageStartSize;

		TfMaster query = new TfMaster();
		// 用户名xxxxxxx
		String nick = request.getParameter("nick");
		if (StringUtil.notEmpty(nick)) {
			query.setmNick("%" + nick.trim() + "%");
		}
		// 手機
		String tel = request.getParameter("tel");
		if (StringUtil.notEmpty(tel)) {
			query.setmTel("%" + tel.trim() + "%");
		}
		// 區號
		String countryCode = request.getParameter("countryCode");
		if (StringUtil.notEmpty(countryCode)) {
			query.setmCountryCode(countryCode.trim());
		}
		// 星座
		String constellation = request.getParameter("constellation");
		if (StringUtil.notEmpty(constellation)) {
			query.setmConstellation(TransformUtils.toInt(constellation));
		}
		Page<TfMaster> page = tfMasterService.getTfMasterRecord(pageNo, pageStartSize, query);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		// 查詢條件數據，模糊查詢的需要去掉第一位與最後一位字節
		if (query.getmNick() != null && query.getmNick().startsWith("%")) {
			query.setmNick(nick.trim());
		}
		if (query.getmTel() != null && query.getmTel().startsWith("%")) {
			query.setmTel(tel.trim());
		}
		model.addAttribute("master", query);
		model.addAttribute("page", page);
		model.addAttribute("pageSize", page.getPageSize());
		model.addAttribute("recordCount", page.getTotal());
		model.addAttribute("pageCount", page.getPages());
		return "tfMaster/list";
	}

	/**
	 * 跳转到新增页面，判断是否是新增还是修改
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String editOrSave(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======edit or new one manager======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String act = request.getParameter("act");
		String result = "tfMaster/edit";
		String masterId = request.getParameter("masterId");
		if (StringUtil.notEmpty(masterId)) {
			if (!StringUtil.empty(masterId)) {
				model.addAttribute("master", tfMasterService.selectByPrimaryKey((Integer.valueOf(masterId))));
			} else {
				log.error("======edit one adv.id can not null======");
				model.addAttribute("msg", "id can not be null");
				return "500";
			}
		}
		return result;
	}

	/**
	 * 處理新增修改
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("/save")
	public void save(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws ParseException, IllegalStateException, IOException {
		log.debug("======新增或修改數據======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String act = "add";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TfMaster tfMaster = new TfMaster();
		TfMaster tfMaster1 = new TfMaster();
		String masterId = request.getParameter("masterId");
		if (StringUtil.notEmpty(masterId)) {
			tfMaster.setMasterId(TransformUtils.toInt(masterId));
			act = "udp";
			tfMaster1 = tfMasterService.selectByPrimaryKey(TransformUtils.toInt(masterId));
		}
		String nick = request.getParameter("mNick");// 昵称
		if (StringUtil.notEmpty(nick)) {
			tfMaster.setmNick(nick);
		}
		String mSex = request.getParameter("mSex");//性別
		if (StringUtil.notEmpty(mSex)) {
			tfMaster.setmSex(TransformUtils.toBoolean(mSex));
		}
		String password = request.getParameter("mPassword");// 密码
		if (StringUtil.notEmpty(password)) {
			tfMaster.setmPassword(EncryptUtil.MD5(EncryptUtil.MD5(password)));
		}
		
		String introduction = request.getParameter("introduction");// 简介
		if (StringUtil.notEmpty(introduction)) {
			tfMaster.setmIntroduction(introduction);
		}
		
		String mLabel = request.getParameter("mLabel");// 大師標籤
		if (StringUtil.notEmpty(mLabel)) {
			tfMaster.setmLabel(mLabel);
		}
		String mAddress = request.getParameter("mAddress");// 住址
		if (StringUtil.notEmpty(mAddress)) {
			tfMaster.setmAddress(mAddress);
		}
		String countryCode = request.getParameter("mCountryCode");// 区号
		if (StringUtil.notEmpty(countryCode)) {
			tfMaster.setmCountryCode(countryCode);
		}
		String tel = request.getParameter("mTel");// 区号
		if (StringUtil.notEmpty(tel)) {
			tfMaster.setmTel(tel);
		} else {// 赋值，用来做文件名
			tel = tfMaster1.getmTel();
		}

		String constellation = request.getParameter("mConstellation");// 星座
		if (StringUtil.notEmpty(constellation)) {
			tfMaster.setmConstellation(TransformUtils.toInt(constellation));
		}
		String deleted = request.getParameter("mDeleted");// 是否拉黑
		if (StringUtil.notEmpty(deleted)) {
			tfMaster.setmDeleted(TransformUtils.toBoolean(deleted));
		}

		// 判断文件是否有做过修改，如果没有就直接跳过文件上传操作
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
							String fileName = tel + "." + file.getOriginalFilename().split("\\.")[1];
							// 定义上传路径
							String realPath = Constant.filepath + Constant.masterimage;
							File fileIo = new File(realPath);
							// 创建文件夹
							if (!fileIo.exists()) {
								fileIo.mkdirs();
							}
							String path = realPath + fileName;
							File localFile = new File(path);
							file.transferTo(localFile);
							tfMaster.setmHeadImg(Constant.imageUrl+Constant.masterimage + fileName);
						}
					}
				}
			}
		}
		if (StringUtil.empty(masterId)) {// 新增
			// 判断该用户名是否存在,新增时才需要判断
			tfMaster1 = tfMasterService.findByTel(countryCode, tel);
			if (tfMaster1 != null) {
				ResponseVo.send1020Code(response, "该大師已存在，请重新输入手机");
				return;
			}
			tfMaster.setmStatus(TfMaster.Master_Offline);
			tfMaster.setmDeviceId("");
			tfMaster.setmDeviceType(0);
			tfMaster.setmCreateTime(new Date());
			int i = tfMasterService.insertSelective(tfMaster);
			if (i < 1) {// i<1说明该数据新增失败
				ResponseVo.send108Code(response, "创建用户失败，数据库新增出错");
				return;
			} else {
				ResponseVo.common("1", "create success", new JSONObject(), response);
				return;
			}
		} else {// 修改
			tfMaster.setmUpdateTime(new Date());
			int i = tfMasterService.updateByPrimaryKeySelective(tfMaster);
			if (i > 0) {
				ResponseVo.common("2", "update success", new JSONObject(), response);
				return;
			}
		}
	}

	/**
	 * 删除功能
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/del")
	public void del(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======删除大師!======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String masterId = request.getParameter("masterId");
		int i = tfMasterService.deleteByPrimaryKey((Integer.valueOf(masterId)));
		if (i <= 0) {
			ResponseVo.send120Code(response, "delete fail");
		} else {
			ResponseVo.common("1", "delete success", new JSONObject(), response);
		}
	}

	/**
	 * 检查该账号是否存在
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/checkTel")
	public void checkTel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		log.debug("======检查该用户是否存在!======");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String tel = request.getParameter("tel");
		// 區號
		String countryCode = request.getParameter("countryCode");
		TfMaster tfMaster = tfMasterService.findByTel(countryCode, tel);
		if (tfMaster != null) {
			ResponseVo.send1020Code(response, "該賬號已存在");
			return;
		}
	}
}
