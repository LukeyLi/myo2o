package com.imooc.myo2o.service.impl;

import com.imooc.myo2o.dao.HeadLineDao;
import com.imooc.myo2o.entity.HeadLine;
import com.imooc.myo2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;
    private static String HLLISTKEY = "headlinelist";

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
      return headLineDao.queryHeadLine(headLineCondition);
    }
}
