package com.yinghai.twentyfour.common.service.impl;

import java.util.Date;
import java.util.Map;

import com.yinghai.twentyfour.common.constant.Constant;
import com.yinghai.twentyfour.common.dao.TfImUserMapper;
import com.yinghai.twentyfour.common.dao.TfOrderMapper;
import com.yinghai.twentyfour.common.im.constant.App;
import com.yinghai.twentyfour.common.im.method.Push;
import com.yinghai.twentyfour.common.im.method.User;
import com.yinghai.twentyfour.common.im.model.CodeSuccessResult;
import com.yinghai.twentyfour.common.im.model.TokenResult;
import com.yinghai.twentyfour.common.im.model.UserTag;
import com.yinghai.twentyfour.common.model.TfImUser;
import com.yinghai.twentyfour.common.vo.MasterAndUserIm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yinghai.twentyfour.common.dao.TfUserMapper;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class TfUserServiceImpl implements TfUserService {

	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private TfUserMapper tfUserMapper;
	@Autowired
	private TfImUserMapper tfImUserMapper;
	@Autowired
	private TfOrderMapper tfOrderMapper;
	@Override
	public TfUser findUserById(Integer userId) {
		return tfUserMapper.selectByPrimaryKey(userId);
	}
	@Override
	public TfUser findByTel(String countryCode, String tel) {
		// TODO Auto-generated method stub
		return tfUserMapper.findByTel(countryCode, tel);
	}

	@Override
	public int deleteByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return tfUserMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(TfUser record) {
		// TODO Auto-generated method stub
		return tfUserMapper.insert(record);
	}

	@Transactional(propagation= Propagation.REQUIRED)
	@Override
	public int insertSelective(TfUser record) {
		// TODO Auto-generated method stub
		//新增IM账号
		User user = new User();
		int topId = tfUserMapper.findTopId();
		String im = "user"+topId;
		String token = "";
		//加入IM
		try {
			TokenResult tokenResult = user.getToken(im,record.getuNick(),record.getuImgUrl()==null?"":record.getuImgUrl());
			if (200!=tokenResult.getCode()){
				log.error("新增用户创建IM失败"+tokenResult);
				throw new RuntimeException("创建IM失败！"+tokenResult);
			}else{
				token = tokenResult.getToken();
				record.setuToken(token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("创建IM失败！",e);
		}
		//添加标签
		Push push = new Push();
		UserTag userTag = new UserTag(new String[]{App.tag,App.masterTag},im);
		try {
			CodeSuccessResult codeSuccessResult = push.setUserPushTag(userTag);
			if (200!=codeSuccessResult.getCode()){
				log.error("新增用户添加标签失败！"+codeSuccessResult);
				throw new RuntimeException("新增用户添加标签失败！"+codeSuccessResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("新增用户添加标签失败！",e);
		}
		record.setuIm(im);
		TfImUser tfImUser = new TfImUser();
		tfImUser.setIuUserId(im);
		tfImUser.setIuToken(token);
		tfImUser.setIuName(record.getuNick());
		tfImUser.setIuImg(Constant.http+record.getuImgUrl());
		tfImUser.setIuCreateTime(new Date());
		tfImUser.setIuType(2);//用户端
		int i = tfImUserMapper.insertSelective(tfImUser);
		i += tfUserMapper.insertSelective(record);
		return i;
	}

	@Override
	public TfUser selectByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return tfUserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(TfUser record) {
		// TODO Auto-generated method stub
		return tfUserMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TfUser record) {
		// TODO Auto-generated method stub
		return tfUserMapper.updateByPrimaryKey(record);
	}
	@Override
	public Page<TfUser> getTfUserRecord(int pageNumber, int pageStartSize, TfUser map) {
		PageHelper.startPage(pageNumber,pageStartSize);	
		// TODO Auto-generated method stub
		return tfUserMapper.getTfUserRecord(map);
	}

	@Override
	public MasterAndUserIm findByOrderNo(String orderNo) {
		return tfOrderMapper.getByOrderNo(orderNo);
	}

	@Override
	public MasterAndUserIm findByOrderTotalNo(String orderTotalNo) {
		return tfOrderMapper.getByOrderTotalNo(orderTotalNo);
	}

}
