package com.yinghai.twentyfour.common.service;

import javax.servlet.http.HttpServletRequest;

import com.yinghai.twentyfour.common.model.TfLuck;
import com.yinghai.twentyfour.common.model.TfLuckKey;
import com.yinghai.twentyfour.common.model.TfLuckWithBLOBs;
import com.yinghai.twentyfour.common.util.Page;

public interface LuckService {
	Page<TfLuck> findListPage(Integer pageNumber, Integer pageStartSize, TfLuckKey key);
	
	TfLuckWithBLOBs findTfLuckWithBLOBsById(Integer luckId);
	
	TfLuckWithBLOBs findLuck(TfLuckKey key);
	
	int addLuck(TfLuckWithBLOBs luck);
	
	int updateLuck(TfLuckWithBLOBs luck);

	int deleteLuck(Integer luckId);
	/**
	 * 上传excel并保存数据
	 * @param request
	 * @return
	 */
	int uploadExcelData(HttpServletRequest request);
}
