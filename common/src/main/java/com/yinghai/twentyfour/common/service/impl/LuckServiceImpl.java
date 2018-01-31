package com.yinghai.twentyfour.common.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.yinghai.twentyfour.common.dao.TfLuckMapper;
import com.yinghai.twentyfour.common.model.TfLuck;
import com.yinghai.twentyfour.common.model.TfLuckKey;
import com.yinghai.twentyfour.common.model.TfLuckWithBLOBs;
import com.yinghai.twentyfour.common.service.LuckService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;
import com.yinghai.twentyfour.common.util.ResponseVo;
import com.yinghai.twentyfour.common.util.StringUtil;
import com.yinghai.twentyfour.common.util.TransformUtils;

import jxl.common.Logger;

public class LuckServiceImpl implements LuckService {
	
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private TfLuckMapper tfLuckMapper;
	@Override
	public Page<TfLuck> findListPage(Integer pageNumber, Integer pageStartSize, TfLuckKey key) {
		PageHelper.startPage(pageNumber, pageStartSize);
		return tfLuckMapper.findByCondition(key);
	}
	
	@Override
	public TfLuckWithBLOBs findTfLuckWithBLOBsById(Integer luckId) {
		return tfLuckMapper.selectById(luckId);
	}

	@Override
	public TfLuckWithBLOBs findLuck(TfLuckKey key) {
		return tfLuckMapper.selectByPrimaryKey(key);
	}

	@Override
	public int addLuck(TfLuckWithBLOBs luck) {
		return tfLuckMapper.insertSelective(luck);
	}

	@Override
	public int updateLuck(TfLuckWithBLOBs luck) {
		return tfLuckMapper.updateByPrimaryKeySelective(luck);
	}

	@Override
	public int deleteLuck(Integer luckId) {
		return tfLuckMapper.deleteById(luckId);
	}

	@Override
	@Transactional
	public int uploadExcelData(HttpServletRequest request){
		//上传excel
		try {
			//上传的请求对象
			MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
			//获取上传的文件
			CommonsMultipartFile multipartFile = (CommonsMultipartFile) req.getFile("upload");
			//获取 文件的名称
			String fileName = multipartFile.getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			//声明excel文件对象
			Workbook wb = null;
			//获取文件上传输入流对象
			InputStream is = multipartFile.getInputStream();
			//判断文件的后缀名称
			
			if(".xls".equalsIgnoreCase(suffix)){
				wb = new HSSFWorkbook(is);
			}else if(".xlsx".equalsIgnoreCase(suffix)){
				wb = new XSSFWorkbook(is);
			}else{
				return -1;//文件类型错误
			}
			
			if(wb!=null){
				//读取内容
				Sheet sheet = wb.getSheetAt(0);
				List<TfLuckWithBLOBs> list = new ArrayList<TfLuckWithBLOBs>();
				//读取行
				//int rowNo = sheet.getPhysicalNumberOfRows();
				int rows = sheet.getLastRowNum();
				for(int i=1;i<=rows;i++){
					Row entity = sheet.getRow(i);
					if(entity!=null){
						TfLuckWithBLOBs luck = new TfLuckWithBLOBs();
						//获取列数
						int colNo = entity.getPhysicalNumberOfCells();
						//遍历列数
						for(int col=0;col<colNo;col++){
							//获取指定列
							Object obj = getCellFormatValue(entity.getCell(col));
							//HSSFCell cell = entity.getCell(col);
							//声明当前列的值
							String value = "";
							Date d = null;
							//判断是否为空
							if(obj!=null){
								if(col==0){
									if(!(obj instanceof Date)){
										log.error("日期格式错误-2"+";col="+col+";row="+i);
										//System.out.println("日期格式错误-2");
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列日期格式错误");
									}else{
										d = (Date)obj;
									}
								}else{
									if(!(obj instanceof String)){
										log.error("数据格式错误-4"+";col="+col+";row="+i);
										//System.out.println("数据格式错误-4");
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列数据格式错误");
									}else{
										value = (String)obj;
									}
								}
								
								if(col==0){//运势日期
									if(d==null){
										log.error("日期格式错误-2"+"|日期为空"+";col="+col+";row="+i);
										//System.out.println("日期格式错误-2"+"|日期为空");
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列日期格式错误");
									}
									luck.setlDate(d);
								}else if(col==1){//星座
									try {
										setConstellationData(luck, value);
									} catch (RuntimeException e) {
										e.printStackTrace();
										log.error("星座错误-2"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("星座错误-2"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列数据错误");
									}
								}else if(col==2){//幸运事业
									if(StringUtil.empty(value)){
										log.error("幸运事业为空-3"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("幸运事业为空-3"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列为空");
									}
									luck.setlUndertaking(value);
								}else if(col==3){//幸运感情
									if(StringUtil.empty(value)){
										log.error("幸运感情为空-3"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("幸运感情为空-3"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列为空");
									}
									luck.setlEmotion(value);
								}else if(col==4){//幸运数字
									if(StringUtil.empty(value)){
										log.error("幸运数字为空-3"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("幸运数字为空-3"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列为空");
									}
									if(Integer.valueOf(value)<0||Integer.valueOf(value)>9){
										log.error("幸运数字错误-2"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("幸运数字错误-2"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列数字错误，应为0~9的数");
									}
									luck.setlNumber(Integer.valueOf(value));
								}else if(col==5){//幸运颜色
									if(StringUtil.empty(value)){
										log.error("幸运颜色为空-3"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("幸运颜色为空-3"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列为空");
									}
									luck.setlColor(value);
								}else if(col==6){//宜
									if(StringUtil.empty(value)){
										log.error("宜为空-3"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("宜为空-3"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+col+"列为空");
									}
									luck.setlSuit(value);
								}else if(col==7){//忌
									if(StringUtil.empty(value)){
										log.error("忌为空-3"+"|value="+value+";col="+col+";row="+i);
										//System.out.println("忌为空-3"+"|value="+value);
										throw new RuntimeException("第"+(i+1)+"行第"+(col+1)+"列为空");
									}
									luck.setlUnsuitable(value);
								}else if(col==8){//更多
									luck.setlMore(value);
								}
							}
						}
						luck.setlCreateTime(new Date());
						//行结束
						list.add(luck);
					}
				}
				if(list.size()==0){
					return -5;
				}
				//数据插入数据库
				for(int k=0;k<list.size();k++){
					int j = 0;
					try {
						j = tfLuckMapper.insertSelective(list.get(k));
					} catch (DuplicateKeyException e) {
						e.printStackTrace();
						throw new RuntimeException("第"+(k+1)+"行为重复数据，导入失败");
					}
					if(j!=1){
						//插入数据出错
						throw new RuntimeException("数据操作出错");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("数据出错,IOException");
		}
		return 1;
	}
	//判断单元格格式
	/** 
     *  
     * 根据Cell类型设置数据 
     *  
     * @param cell 
     * @return 
     * @author zengwendong 
     */  
    private Object getCellFormatValue(Cell cell) {  
        Object cellvalue = "";  
        if (cell != null) {  
            // 判断当前Cell的Type  
            switch (cell.getCellType()) {  
            case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC  
            case Cell.CELL_TYPE_FORMULA: {  
                // 判断当前的cell是否为Date  
                if (DateUtil.isCellDateFormatted(cell)) {  
                    // 如果是Date类型则，转化为Data格式  
                    // data格式是带时分秒的：2013-7-10 0:00:00  
                    // cellvalue = cell.getDateCellValue().toLocaleString();  
                    // data格式是不带带时分秒的：2013-7-10  
                    Date date = cell.getDateCellValue();  
                    cellvalue = date;  
                } else {// 如果是纯数字  
  
                    // 取得当前Cell的数值  
                    cellvalue = String.valueOf((int)cell.getNumericCellValue());  
                }  
                break;  
            }  
            case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING  
                // 取得当前的Cell字符串  
                cellvalue = cell.getRichStringCellValue().getString();  
                break;
            case Cell.CELL_TYPE_BOOLEAN://如果为boolean类型
            	Boolean b = cell.getBooleanCellValue();
            	cellvalue = b.toString();
            	break;
            case Cell.CELL_TYPE_ERROR:
            	cellvalue = "";
            	break;
            default:// 默认的Cell值  
                cellvalue = "";  
            }  
        } else {  
            cellvalue = "";  
        }  
        return cellvalue;  
    }  
	
	
	
	/**
	 * 匹配星座信息
	 * @param luck
	 * @param value
	 * @throws RuntimeException
	 */
	private void setConstellationData(TfLuckWithBLOBs luck, String value) throws RuntimeException {
		switch (value) {
		case "水瓶座":
			luck.setlConstellation(1);
			break;
		case "双鱼座":
			luck.setlConstellation(2);
			break;
		case "白羊座":
			luck.setlConstellation(3);
			break;
		case "金牛座":
			luck.setlConstellation(4);
			break;
		case "双子座":
			luck.setlConstellation(5);
			break;
		case "巨蟹座":
			luck.setlConstellation(6);
			break;
		case "狮子座":
			luck.setlConstellation(7);
			break;
		case "处女座":
			luck.setlConstellation(8);
			break;
		case "天秤座":
			luck.setlConstellation(9);
			break;
		case "天蝎座":
			luck.setlConstellation(10);
			break;
		case "射手座":
			luck.setlConstellation(11);
			break;
		case "摩羯座":
			luck.setlConstellation(12);
			break;
		default:
			//星座数据出错
			throw new RuntimeException("星座数据格式错误");
		}
	}
}
