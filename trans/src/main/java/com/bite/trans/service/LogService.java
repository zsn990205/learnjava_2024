package com.bite.trans.service;

import com.bite.trans.mapper.LogInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {
    @Autowired
    private LogInfoMapper logInfoMapper;

    @Transactional(propagation = Propagation.NESTED)
    public Integer insertLog(String userName,String op) {
        logInfoMapper.insert(userName,op);
        int a = 10 / 0;
        return 1;
    }
}
