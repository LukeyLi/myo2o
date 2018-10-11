package com.imooc.myo2o.service;

import com.imooc.myo2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;
}
