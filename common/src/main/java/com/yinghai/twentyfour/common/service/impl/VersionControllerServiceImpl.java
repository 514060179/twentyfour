package com.yinghai.twentyfour.common.service.impl;

import com.yinghai.twentyfour.common.dao.VersionControlMapper;
import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.service.VersionControllerService;
import com.yinghai.twentyfour.common.util.Page;
import com.yinghai.twentyfour.common.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/7/18.
 */
public class VersionControllerServiceImpl implements VersionControllerService {

    @Autowired
    private VersionControlMapper versionControlMapper;

    public int saveVersionController(VersionControl versionControl) {
        return versionControlMapper.insertSelective(versionControl);
    }
    public VersionControl findByCondition(VersionControl versionControl) {
        return versionControlMapper.findVersion(versionControl);
    }

    public Page<VersionControl> findByPage(Integer pageNumber, Integer pageStartSize, VersionControl versionControl) {
        PageHelper.startPage(pageNumber,pageStartSize);
        return versionControlMapper.findByCondion(versionControl);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    public int updateVersionController(VersionControl versionControl) {
        return versionControlMapper.updateByPrimaryKeySelective(versionControl);

    }
}
