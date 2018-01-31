package com.yinghai.twentyfour.backstage.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.model.TfLuck;
import com.yinghai.twentyfour.common.model.TfLuckKey;
import com.yinghai.twentyfour.common.model.TfLuckWithBLOBs;
import com.yinghai.twentyfour.common.service.LuckService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import net.sf.json.JSONObject;

/**
 * 运势管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/luck")
public class BackLuckController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private LuckService tfLuckService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("======进入运势管理列表======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		//页码
		Integer pageNo = TransformUtils.toInt(request.getParameter("page"));//页数
		Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));//每页存在的条数
		pageNo = pageNo == 0 ? 1 : pageNo;
		pageSize = pageSize == 0 ? 10 : pageSize;
		TfLuckKey key = new TfLuckKey();
		String date = request.getParameter("date");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(!StringUtil.empty(date)){
				try {
					Date d = sdf.parse(date);
					key.setlDate(d);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error("运势管理列表——解析日期出错"+date);
				}
		}
		String c = request.getParameter("constellation");
		Integer constellation=null;
		if(!StringUtil.empty(c)){
			constellation = Integer.valueOf(c);
		}
		if(constellation!=null&&constellation>0&&constellation<13){
			key.setlConstellation(constellation);
		}
		Page<TfLuck> page = tfLuckService.findListPage(pageNo, pageSize, key);
		model.addAttribute("page", page);
		model.addAttribute("pageNo", page.getPageNum());
		model.addAttribute("luckKey", key);
		model.addAttribute("pageSize", page.getPageSize());
        model.addAttribute("recordCount", page.getTotal());
        model.addAttribute("pageCount", page.getPages());
		return "luck/list";
	}
	
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		logger.info("======进入查看/新增/编辑运势信息界面======");
		try {
            request.setCharacterEncoding("utf-8");
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		String look = request.getParameter("look");
		if(!StringUtil.empty(look)){
			model.addAttribute("look", look);
		}
		/*String lDate = request.getParameter("lDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d=null;
		try {
			if(!StringUtil.empty(lDate)){
				d = sdf.parse(lDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String lConstellation = request.getParameter("lConstellation");
		Integer constellation = null;
		if(!StringUtil.empty(lConstellation)){
			constellation  = Integer.valueOf(lConstellation);
		}
		TfLuckKey key = new TfLuckKey();
		key.setlConstellation(constellation);
		key.setlDate(d);*/
		String id = request.getParameter("id");
		Integer luckId = null;
		if(!StringUtil.empty(id)){
			luckId = Integer.valueOf(id);
		}
		TfLuckWithBLOBs l=null;
		if(luckId!=null){
			l = tfLuckService.findTfLuckWithBLOBsById(luckId);
			System.out.println(l);
		}
		//TfLuckWithBLOBs l = tfLuckService.findLuck(key);
		model.addAttribute("l", l);
		//System.out.println(l);
		return "luck/edit";
	}
	
	@RequestMapping("/save")
	public void save(HttpServletRequest request,HttpServletResponse response){
		logger.debug("======新增或修改數據======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

        TfLuckWithBLOBs luck = new TfLuckWithBLOBs();
        //获取参数
        String act = request.getParameter("act");
        System.out.println("act:"+act);
        if(StringUtil.empty(act)){
        	ResponseVo.send101Code(response, "act为空或有误");
        	return;
        }
        String date = request.getParameter("lDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(StringUtil.empty(date)){
        	ResponseVo.send101Code(response, "运势日期为空");
        	return;
        }
        Date lDate = null;
		try {
			lDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			ResponseVo.send104Code(response, "运势日期格式有误");
			return;
		}
        luck.setlDate(lDate);
        String constellation = request.getParameter("lConstellation");
        Integer lConstellation = null;
        if(!StringUtil.empty(constellation)){
        	lConstellation = TransformUtils.toInteger(constellation);
        }else{
        	ResponseVo.send101Code(response, "幸运星座为空");
        	return;
        }
        luck.setlConstellation(lConstellation);
        String lUndertaking = request.getParameter("lUndertaking");
        if(StringUtil.empty(lUndertaking)){
        	ResponseVo.send101Code(response, "幸运事业为空");
        	return;
        }
        luck.setlUndertaking(lUndertaking);
        String lEmotion = request.getParameter("lEmotion");
        if(StringUtil.empty(lEmotion)){
        	ResponseVo.send101Code(response, "幸运感情为空");
        	return;
        }
        luck.setlEmotion(lEmotion);
        String lColor = request.getParameter("lColor");
        if(StringUtil.empty(lColor)){
        	ResponseVo.send101Code(response, "幸运颜色为空");
        	return;
        }
        luck.setlColor(lColor);
        Integer lNumber = null;
        String number = request.getParameter("lNumber");
        if(StringUtil.empty(number)){
        	ResponseVo.send101Code(response, "幸运数字为空");
        	return;
        }else{
        	lNumber = TransformUtils.toInteger(number);
        	if(lNumber<0){
        		ResponseVo.send104Code(response, "幸运数字格式有误");
    			return;
        	}
        }
        luck.setlNumber(lNumber);
        //System.out.println("number:"+number);
        String lSuit = request.getParameter("lSuit");
        if(StringUtil.empty(lSuit)){
        	ResponseVo.send101Code(response, "运势适宜为空");
        	return;
        }
        luck.setlSuit(lSuit);
        String lUnsuitable = request.getParameter("lUnsuitable");
        if(StringUtil.empty(lUnsuitable)){
        	ResponseVo.send101Code(response, "运势不宜为空");
        	return;
        }
        luck.setlUnsuitable(lUnsuitable);
        String lMore = request.getParameter("lMore");
        luck.setlMore(lMore);
        if("add".equals(act)){//新增
        	Date d1 = new Date();
        	luck.setlCreateTime(d1);
        	luck.setlUpdateTime(d1);
        	TfLuckKey key = new TfLuckKey();
        	key.setlConstellation(luck.getlConstellation());
        	key.setlDate(luck.getlDate());
        	TfLuckWithBLOBs l = tfLuckService.findLuck(key);
        	if(l!=null){
        		ResponseVo.send1020Code(response, "数据已存在，不能重复新增");
        		return;
        	}
        	int i = tfLuckService.addLuck(luck);
        	if(i!=1){
        		ResponseVo.send106Code(response, "新增运势信息失败");
        		return;
        	}else{
        		ResponseVo.common("1", "create success", new JSONObject(), response);
        		return;
        	}
        }else if("upd".equals(act)){//修改
        	Date d2 = new Date();
        	luck.setlUpdateTime(d2);
        	int i = tfLuckService.updateLuck(luck);
        	if(i!=1){
        		ResponseVo.send106Code(response, "修改运势信息失败");
        		return;
        	}else{
        		ResponseVo.common("2", "update success", new JSONObject(), response);
        		return;
        	}
        }else{
        	ResponseVo.send101Code(response, "act为空或有误");
        	return;
        }
	}
	
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response){
		logger.debug("======新增或修改數據======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //获取参数
        String id = request.getParameter("id");
        Integer luckId = null;
        if(StringUtil.empty(id)){
        	ResponseVo.send101Code(response, "id为空");
        	return;
        }else{
        	luckId = TransformUtils.toInteger(id);
        	if(luckId<0){
        		ResponseVo.send104Code(response, "id格式错误");
            	return;
        	}
        }
        int i = tfLuckService.deleteLuck(luckId);
        if(i!=1){
        	ResponseVo.send106Code(response, "删除数据失败");
        	return;
        }else{
        	ResponseVo.send1Code(response, "删除成功", new JSONObject());
        }
        
	}
	@RequestMapping("/download")
	public void downloadExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException{
		logger.info("======下载运势信息模板======");
		String s = Constant.filepath + Constant.lucktemplate;
		String str = s+"/template.xlsx";
        File f = new File(str);
        //设置response参数
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        /**
         * Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME 用户代理如何显示附加的文件。
         * Content-disposition其实可以控制用户请求所得的内容存为一个文件的时候提供一个默认的文件名，文件直接在浏览器上显示或者在访问时弹出文件下载对话框。
         * disposition-type是以什么方式下载，如attachment为以附件方式下载
         * Content-Disposition: attachment; filename="filename.xls" 
         * 
         */
        try {
			response.setHeader("Content-Disposition", 
					"attachment;filename="+ new String(("运势信息标准模板" + ".xlsx").getBytes(), "iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
			bis = new BufferedInputStream(new FileInputStream(f));
			bos = new BufferedOutputStream(out);
			byte[] buf = new byte[2048];
			int bytesRead;
			while((bytesRead=bis.read(buf, 0, buf.length))!=-1){
				bos.write(buf, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}finally {
			if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
		}
        
	}
	
	@RequestMapping("/import")
	public void importLuckExcel(HttpServletRequest request,HttpServletResponse response){
		logger.info("======批量导入运势信息======");
		//上传并将数据保存到数据库
		int i = 0;
		try {
			i = tfLuckService.uploadExcelData(request);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseVo.send106Code(response, e.getMessage());
			return;
		}
		//System.out.println("批量导入运势信息"+i);
		if(i==1){
			ResponseVo.send1Code(response, "success", new JSONObject());
			return;
		}else if(i==-5){
			ResponseVo.send101Code(response, "数据为空");
			return;
		}else{
			ResponseVo.send106Code(response, "数据出错");
		}
	}
	@RequestMapping("/uploadExcel")
	public String uploadExcel(HttpServletRequest request,HttpServletResponse response){
		return "luck/upexcel";
	}
}
