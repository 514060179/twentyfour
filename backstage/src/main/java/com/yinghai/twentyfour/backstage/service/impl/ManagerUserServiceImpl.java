package com.yinghai.twentyfour.backstage.service.impl;

import com.yinghai.twentyfour.backstage.dao.ManagerUserMapper;
import com.yinghai.twentyfour.backstage.model.ManagerUser;
import com.yinghai.twentyfour.backstage.service.ManagerUserService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */
public class ManagerUserServiceImpl implements ManagerUserService{

    @Autowired
    private ManagerUserMapper managerUserMapper;
    @Override
    public ManagerUser findOneById(Integer id) {
        return managerUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public ManagerUser findByName(String username) {
        ManagerUser managerUser = new ManagerUser();
        managerUser.setUsername(username);
        List<ManagerUser> list = managerUserMapper.findByCondition(managerUser);
        if(list.size()<=0){
            return null;
        }
        return list.get(0);
    }

	@Override
	public ManagerUser selectByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		return managerUserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int saveManagerUser(ManagerUser user) {
		// TODO Auto-generated method stub
		return managerUserMapper.insertSelective(user);
	}

	@Override
	public int updateManagerUser(ManagerUser user) {
		// TODO Auto-generated method stub
		return managerUserMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public int deleteManagerUser(Integer userId) {
		// TODO Auto-generated method stub
		return managerUserMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public Page<ManagerUser> findListPage(Integer pageNumber, Integer pageStartSize, ManagerUser user) {
		// TODO Auto-generated method stub
		 PageHelper.startPage(pageNumber,pageStartSize);
		return managerUserMapper.findByCondition(user);
	}

	@Override
	public List<ManagerUser> appFindList(ManagerUser user) {
		// TODO Auto-generated method stub
		return managerUserMapper.findByCondition(user);
	}

	@Override
	public List<ManagerUser> findAllManagerUser() {
		// TODO Auto-generated method stub
		return managerUserMapper.findByCondition(null);
	}

	@Override
	public ManagerUser findByEmail(String email) {
		// TODO Auto-generated method stub
        ManagerUser managerUser = new ManagerUser();
        managerUser.setEmail(email);
        List<ManagerUser> list = managerUserMapper.findByCondition(managerUser);
        if(list.size()<=0){
            return null;
        }
        return list.get(0);
	}
}
