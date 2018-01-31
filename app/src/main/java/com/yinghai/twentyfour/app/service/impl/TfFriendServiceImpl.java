package com.yinghai.twentyfour.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.yinghai.twentyfour.app.service.TfFriendService;
import com.yinghai.twentyfour.common.constant.PushCode;
import com.yinghai.twentyfour.common.dao.TfImUserMapper;
import com.yinghai.twentyfour.common.dao.TfRelationMapper;
import com.yinghai.twentyfour.common.dao.TfUserMapper;
import com.yinghai.twentyfour.common.im.constant.App;
import com.yinghai.twentyfour.common.im.method.User;
import com.yinghai.twentyfour.common.im.model.CodeSuccessResult;
import com.yinghai.twentyfour.common.im.msg.ContactNtfMessage;
import com.yinghai.twentyfour.common.im.msg.TxtMessage;
import com.yinghai.twentyfour.common.im.util.ImIdUtil;
import com.yinghai.twentyfour.common.model.TfImUser;
import com.yinghai.twentyfour.common.model.TfMaster;
import com.yinghai.twentyfour.common.model.TfRelation;
import com.yinghai.twentyfour.common.model.TfUser;
import com.yinghai.twentyfour.common.model.TfUserFriend;
import com.yinghai.twentyfour.common.service.TfUserService;
import com.yinghai.twentyfour.common.util.JsonDateValueProcessor;
import com.yinghai.twentyfour.common.util.PageHelper;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class TfFriendServiceImpl implements TfFriendService {
	@Autowired
	private TfImUserMapper tfImUserMapper;
	@Autowired
	private TfRelationMapper tfRelationMapper;
	@Autowired
	private TfUserMapper tfUserMapper;
	@Override
	@Transactional
	public int addFriend(Integer userId, Integer friendId, Integer type,String m) {
		String uId = null;
		String fId = null;
		if(type!=1){
			return -6;
		}
		if(userId==null||friendId==null||type==null||type!=1||userId<0||friendId<0||userId==friendId){
			return -7;
		}
		uId = ImIdUtil.getImId(userId, type);
		fId = ImIdUtil.getImId(friendId, type);
		TfImUser user = new TfImUser();
		user.setIuUserId(uId);
		List<TfImUser> l1 = tfImUserMapper.selectBySelective(user);
		if(l1==null||l1.size()<1){
			return -1;//用户不存在
		}
		user.setIuUserId(fId);
		List<TfImUser> l2 = tfImUserMapper.selectBySelective(user);
		if(l2==null||l2.size()<1){
			return -2;
		}
		int result = 0;
		//查询好友关系记录
		List<TfRelation> l = tfRelationMapper.selectByUserAndFriend(uId, fId);
		if(l==null||l.size()==0){//新增好友关系记录
			TfRelation r = new TfRelation();
			r.setrCreateTime(new Date());
			r.setrFriendId(fId);
			r.setrUserId(uId);
			r.setrAdd(1);
			int i = tfRelationMapper.insertSelective(r);
			if(i!=1){
				throw new RuntimeException("新增好友关系数据出错");
			}
			try {
				result = Integer.valueOf(r.getrFriendId().replace("user", ""));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new RuntimeException("friendId格式错误");
			}
		}else{
			if(l.size()>1){
				return -5;
			}
			if(l.get(0).getrIsValidate()&&l.get(0).getrDelete()==0){//已经是好友了
				return -3;
			}
			if(l.get(0).getrIsValidate()&&l.get(0).getrDelete()!=0){//已经拉黑了
				return -4;
			}
			try {
				if(uId.equals(l.get(0).getrUserId())){
					result = Integer.valueOf(l.get(0).getrFriendId().replace("user", ""));
				}else{
					if(l.get(0).getrAdd()==1){//当只有A添加B时，更新rAdd为相互添加的状态
						TfRelation record = new TfRelation();
						record.setRelationId(l.get(0).getRelationId());
						record.setrAdd(2);
						record.setrUpdateTime(new Date());
						int j = tfRelationMapper.updateByPrimaryKeySelective(record);
						if(j!=1){
							throw new RuntimeException("更新好友关系数据出错");
						}
					}
					result = Integer.valueOf(l.get(0).getrUserId().replace("user", ""));
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new RuntimeException("friendId格式错误");
			}
		}
		//推送系统消息，添加好友
		TfUser user1 = tfUserMapper.selectByPrimaryKey(userId);
		User u = new User();
		String[] toUserId = {""+fId};
		JSONObject jsonObject = new JSONObject();
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONObject res = new JSONObject();
		JSONObject us = JSONObject.fromObject(user1, config);
		res.put("user", us);
		res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		jsonObject.put("code", PushCode.UFriendApply);
		jsonObject.put("msg", PushCode.UFriendApplyMsg);
		jsonObject.put("data", res);
		jsonObject = JSONObject.fromObject(jsonObject, config);
		
		//ContactNtfMessage message = new ContactNtfMessage(jsonObject.toString(), null, App.admin, fId, m);
		TxtMessage message = new TxtMessage(jsonObject.toString(), null);
		try {
			CodeSuccessResult pushM = u.PublishSystem(App.admin, toUserId, message, null, null, null, null);
			//System.out.println("==========推送消息："+pushM.toString());
			if(200!=pushM.getCode()){
				throw new RuntimeException("推送好友申请消息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("推送好友申请消息失败");
		}
		return result;//返回目标用户的用户Id
	}
	@Override
	public int deleteFriend(Integer userId, Integer friendId, Integer type,Integer t) {
		//检查用户是否存在
		String uId = null;
		String fId = null;
		if(type!=1){
			return -6;
		}
		uId = ImIdUtil.getImId(userId, type);
		fId = ImIdUtil.getImId(friendId, type);
		TfImUser user = new TfImUser();
		user.setIuUserId(uId);
		List<TfImUser> l1 = tfImUserMapper.selectBySelective(user);
		if(l1==null||l1.size()<1){
			return -1;//用户IM不存在
		}
		user.setIuUserId(fId);
		List<TfImUser> l2 = tfImUserMapper.selectBySelective(user);
		if(l2==null||l2.size()<1){
			return -2;//用户IM不存在
		}
		//查看好友关系记录是否存在
		List<TfRelation> list = tfRelationMapper.selectByUserAndFriend(uId, fId);
		if(list==null||list.size()<1){
			return -3;//非好友关系
		}
		if(list.size()>1){
			return -5;//存在多条好友关系记录
		}
		TfRelation r = list.get(0);
		if(r.getrIsValidate()==false){
			return -3;//非好友关系
		}else{
			if(t==1){
				//删除好友信息
				int i = tfRelationMapper.deleteByPrimaryKey(r.getRelationId());
				if(i!=1){
					return -4;//删除好友数据失败
				}
				return i;
			}else if(t==2){
				//拉黑好友，更新好友关系列表，同时IM中对用户进行拉黑
				if(r.getrDelete()==0){
					TfRelation record = new TfRelation();
					record.setRelationId(r.getRelationId());
					if(userId.equals(r.getrUserId())){
						record.setrDelete(1);
					}else{
						record.setrDelete(2);
					}
					record.setrUpdateTime(new Date());
					int j = tfRelationMapper.updateByPrimaryKeySelective(record);
					if(j!=1){
						return -4;//拉黑好友失败
					}
					return j;
				}else{
					return 1;
				}
			}else{
				return -6;
			}
		}
	}
	@Override
	public List<TfUserFriend> getFriendList(Integer pageNum,Integer pageSize,Integer userId, Integer type) {
		
		//检查用户是否存在
		String uId = ImIdUtil.getImId(userId, type);
		TfImUser user = new TfImUser();
		user.setIuUserId(uId);
		List<TfImUser> list = tfImUserMapper.selectBySelective(user);
		if(list==null||list.size()<1){
			throw new RuntimeException("userId用户不存在");
		}
		if(list!=null&&list.size()>1){
			throw new RuntimeException("userId有多条数据");
		}
		PageHelper.startPage(pageNum, pageSize);
		//当type==1时，只查询用户
		List<TfUserFriend> l = tfRelationMapper.findFriendList(uId,null);
		return l;
	}
	@Override
	public List<TfUserFriend> getFriend(Integer userId, Integer type, String nick) {
		//检查用户是否存在
		String uId = ImIdUtil.getImId(userId, type);
		TfImUser user = new TfImUser();
		user.setIuUserId(uId);
		List<TfImUser> list = tfImUserMapper.selectBySelective(user);
		if(list==null||list.size()<1){
			throw new RuntimeException("userId用户不存在");
		}
		if(list!=null&&list.size()>1){
			throw new RuntimeException("userId有多条数据");
		}
		//当type==1时，只查询用户
		List<TfUserFriend> l = tfRelationMapper.findFriendList(uId,nick);
		return l;
	}
	@Override
	public List<TfUserFriend> getUser(Integer userId, String nick,Integer sourceId) {
		List<TfUserFriend> l1 = tfImUserMapper.findUser(userId,nick,sourceId);
		return l1;
	}
	@Override
	public List<TfMaster> getMaster(Integer userId, String nick) {
		List<TfMaster> l2 = tfImUserMapper.findMaster(userId,nick);
		return l2;
	}
	@Override
	@Transactional
	public int dealFriend(Integer uId, Integer fId, Integer type, Integer t) {
		if(uId==null||fId==null||type==null||t==null){
			return -1;//参数为空
		}
		if(type!=1||(t!=1&&t!=2)||uId<0||fId<0){
			return -1;//参数错误
		}
		String userId = ImIdUtil.getImId(uId, type);
		String friendId = ImIdUtil.getImId(fId, type);
		//判断好友关系是否存在
		List<TfRelation> list = tfRelationMapper.selectByUserAndFriend(userId, friendId);
		if(list==null||list.size()<1){
			return -2;//不存在好友关系
		}
		if(list!=null&&list.size()>1){
			return -3;//好友关系数据有多条
		}
		int i=0;
		//如果为同意申请则更新好友关系
		if(t==1){//同意申请
			if(!list.get(0).getrIsValidate()){
				if(userId.equals(list.get(0).getrUserId())){
					if(list.get(0).getrAdd()==1){
						return -5;//数据出错，即A向B申请好友，同时A同意了好友申请
					}
				}
				TfRelation r = new TfRelation();
				r.setRelationId(list.get(0).getRelationId());
				r.setrIsValidate(true);
				r.setrUpdateTime(new Date());
				i = tfRelationMapper.updateByPrimaryKeySelective(r);
				if(i!=1){
					throw new RuntimeException("更新数据失败");
				}
			}else{
				i=1;
			}
				
		}else{//拒绝申请
			if(list.get(0).getrIsValidate()){//已经是好友
				return -4;
			}else{
				if(userId.equals(list.get(0).getrUserId())){
					if(list.get(0).getrAdd()==1){
						return -5;//数据出错，即A拒绝A自己的请求
					}
				}
			}
		}
		//发送推送消息给申请者
		TfUser user = tfUserMapper.selectByPrimaryKey(uId);
		ContactNtfMessage message1 = null;
		//ContactNtfMessage message2 = null;
		ContactNtfMessage message3 = null;
		//ContactNtfMessage message4 = null;
		TxtMessage message5 = null;
		TxtMessage message6 = null;
		CodeSuccessResult result1=null;
		//CodeSuccessResult result2=null;
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		if(t==1){//同意好友申请
			JSONObject jsonObject = new JSONObject();
			JSONObject res = new JSONObject();
			JSONObject us = JSONObject.fromObject(user, config);
			res.put("user", us);
			res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			jsonObject.put("code", PushCode.UFriendAgree);
			jsonObject.put("msg", PushCode.UFriendAgreeMsg);
			jsonObject.put("data", res);
			jsonObject = JSONObject.fromObject(jsonObject, config);
			message1 = new ContactNtfMessage(jsonObject.toString(), null,App.admin, friendId, "你们已经是好友了，请打个招呼吧");
			//message2 = new ContactNtfMessage(jsonObject.toString(), null,friendId, userId, "你们已经是好友了，请打个招呼吧");
			System.out.println("消息："+message1);
			message5 = new TxtMessage(jsonObject.toString(), null);
			String[] s1 = {""+friendId};
			//String[] s2 = {""+userId};
			try {
				User u = new User();
				result1 = u.PublishSystem(App.admin, s1, message5, null, null, null, null);
				//result2 = u.PublishSystem(friendId, s2, message2, null, null, 1, 1);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("同意好友申请推送消息失败");
			}
			if(result1==null||result1.getCode()!=200){//||result2==null||result2.getCode()!=200
				throw new RuntimeException("同意好友申请推送消息失败");
			}
			return 1;
		}else{//拒绝好友申请
			JSONObject jsonObject = new JSONObject();
			JSONObject res = new JSONObject();
			JSONObject us = JSONObject.fromObject(user, config);
			res.put("user", us);
			res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			jsonObject.put("code", PushCode.UFriendRefuse);
			jsonObject.put("msg", PushCode.UFriendRefuseMsg);
			jsonObject.put("data", res);
			jsonObject = JSONObject.fromObject(jsonObject, config);
			//message3 = new ContactNtfMessage(jsonObject.toString(), null,App.admin, friendId, "您的好友申请被对方拒绝");
			//message4 = new ContactNtfMessage(jsonObject.toString(), null,friendId, userId, "您已拒绝了对方的好友申请");
			message6 = new TxtMessage(jsonObject.toString(), null);
			String[] s = {""+friendId};
			try {
				User u = new User();
				result1 = u.PublishSystem(App.admin, s, message3, null, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("拒绝好友申请推送消息失败");
			}
			if(result1==null||result1.getCode()!=200){
				throw new RuntimeException("拒绝好友申请推送消息失败");
			}
			return 2;
		}
		
	}
	@Override
	public int isFriend(Integer uId, Integer fId, Integer type) {
		if(uId==null||fId==null||type==null||type!=1||uId<0||fId<0){
			return -1;
		}
		String userId = ImIdUtil.getImId(uId, type);
		String friendId = ImIdUtil.getImId(fId, type);
		List<TfRelation> l = tfRelationMapper.selectByUserAndFriend(userId, friendId);
		if(l==null||l.size()<1){
			return 2;//不是好友关系
		}
		if(l!=null&&l.size()>1){
			return -2;//好友关系数据有多条
		}
		TfRelation r = l.get(0);
		if(!r.getrIsValidate()){
			return 2;//不是好友关系
		}else{
			if(r.getrDelete()!=0){
				return 3;//是好友关系，但被拉黑
			}
			return 1;
		}
	}

}
