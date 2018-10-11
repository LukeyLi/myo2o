package com.imooc.myo2o.dao;

import com.imooc.myo2o.entity.Area;

import java.util.List;

public interface AreaDao {
    /**
     * 列出区域列表
     * @return List
     */
    List<Area> queryArea();
}
