package com.yinghai.twentyfour.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yinghai.twentyfour.common.dao.TfImUserMapper;
import com.yinghai.twentyfour.common.im.constant.App;
import com.yinghai.twentyfour.common.im.method.Push;
import com.yinghai.twentyfour.common.im.method.User;
import com.yinghai.twentyfour.common.im.model.CodeSuccessResult;
import com.yinghai.twentyfour.common.im.model.TokenResult;
import com.yinghai.twentyfour.common.im.model.UserTag;
import com.yinghai.twentyfour.common.model.TfImUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.TfMasterMapper;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.service.TfMasterService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class TfMasterServiceImpl implements TfMasterService {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TfMasterMapper tfMasterMapper;
	@Autowired
	private TfImUserMapper tfImUserMapper;

	@Override
	public TfMaster findByTel(String countryCode, String tel) {
		// TODO Auto-generated method stub
		return tfMasterMapper.findByTel(countryCode, tel);
	}

	@Override
	public int deleteByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return tfMasterMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(TfMaster record) {
		// TODO Auto-generated method stub
		return tfMasterMapper.insert(record);
	}
	@Transactional(propagation= Propagation.REQUIRED)
	@Override
	public int insertSelective(TfMaster record) {
		// TODO Auto-generated method stub
		//创建IM
		int topId = tfMasterMapper.findTopId();
		String im = "master"+topId;
		String token = "";
		try {
			User user = new User();
			TokenResult tokenResult = user.getToken(im,record.getmNick(),record.getmHeadImg()==null?"":record.getmHeadImg());
			if (200!=tokenResult.getCode()){
				log.error("新增大师创建IM失败"+tokenResult);
				throw new RuntimeException("创建IM失败！"+tokenResult);
			}else{
				token = tokenResult.getToken();
				record.setmToken(token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("创建IM失败！",e);
		}
		//添加标签
		Push push = new Push();
		UserTag userTag = new UserTag(new String[]{App.tag,App.userTag},im);
		try {
			CodeSuccessResult codeSuccessResult = push.setUserPushTag(userTag);
			if (200!=codeSuccessResult.getCode()){
				log.error("新增大师添加标签失败！"+codeSuccessResult);
				throw new RuntimeException("新增大师添加标签失败！"+codeSuccessResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("新增大师添加标签失败！",e);
		}
		record.setmIm(im);
		TfImUser tfImUser = new TfImUser();
		tfImUser.setIuUserId(im);
		tfImUser.setIuToken(token);
		tfImUser.setIuName(record.getmNick());
		tfImUser.setIuImg(record.getmHeadImg());
		tfImUser.setIuCreateTime(new Date());
		tfImUser.setIuType(1);//大师端
		int i = tfImUserMapper.insertSelective(tfImUser);
		i += tfMasterMapper.insertSelective(record);
		return i;
	}

	@Override
	public TfMaster selectByPrimaryKey(Integer masterId) {
		// TODO Auto-generated method stub
		return tfMasterMapper.selectByPrimaryKey(masterId);
	}

	@Override
	public int updateByPrimaryKeySelective(TfMaster record) {
		// TODO Auto-generated method stub
		return tfMasterMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TfMaster record) {
		// TODO Auto-generated method stub
		return tfMasterMapper.updateByPrimaryKey(record);
	}

	@Override
	public Page<TfMaster> getTfMasterRecord(int pageNumber, int pageStartSize, TfMaster record) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNumber,pageStartSize);
		return tfMasterMapper.getTfMasterRecord(record);
	}

	@Override
	public List<TfMaster> getTfMasterAndConSubRecord(int startNumber, int count, TfMaster record) {
		// TODO Auto-generated method stub
		return tfMasterMapper.getTfMasterAndConSubRecord(startNumber, count, record);
	}

}
