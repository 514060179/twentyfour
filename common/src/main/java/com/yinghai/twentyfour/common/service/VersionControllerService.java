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
}
