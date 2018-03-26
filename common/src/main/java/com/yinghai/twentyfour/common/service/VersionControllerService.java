package com.yinghai.twentyfour.common.service;

import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.util.Page;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface VersionControllerService {

    int saveVersionController(VersionControl versionControl);

    VersionControl findByCondition(VersionControl versionControl);

    Page<VersionControl> findByPage(Integer pageNumber, Integer pageStartSize, VersionControl versionControl);

    int updateVersionController(VersionControl versionControl);
    /**
     * 根据条件分页查询版本记录
     * @param pageSize
     * @param pageNo
     * @param version
     * @return
     */
	Page<VersionControl> queryVersion(int pageSize, int pageNo, VersionControl version);
	/**
	 * 根据id查询版本记录
	 * @param id
	 * @return
	 */
	VersionControl findById(Integer id);
}
