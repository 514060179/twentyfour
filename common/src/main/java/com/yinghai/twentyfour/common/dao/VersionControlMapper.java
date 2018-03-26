package com.yinghai.twentyfour.common.dao;

import com.yinghai.twentyfour.common.model.VersionControl;
import com.yinghai.twentyfour.common.util.Page;

public interface VersionControlMapper {
    int insert(VersionControl record);

    int insertSelective(VersionControl record);

    VersionControl findVersion(VersionControl record);

    Page<VersionControl> findByCondion(VersionControl record);

    int updateByPrimaryKeySelective(VersionControl versionControl);

	Page<VersionControl> queryVersion(VersionControl version);

	VersionControl findById(Integer id);
}