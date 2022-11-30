package com.lp.service.impl;

import com.lp.entity.YdlOperLog;
import com.lp.dao.YdlOperLogDao;
import com.lp.service.YdlOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 操作日志(YdlOperLog)表服务实现类
 *
 * @author makejava
 * @since 2022-10-07 18:08:37
 */
@Service("ydlOperLogService")
@Slf4j
public class YdlOperLogServiceImpl implements YdlOperLogService {
    @Resource
    private YdlOperLogDao ydlOperLogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param operId 主键
     * @return 实例对象
     */
    @Override
    public YdlOperLog queryById(Integer operId) {
        return this.ydlOperLogDao.queryById(operId);
    }

    /**
     * 分页查询
     *
     * @param ydlOperLog 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<YdlOperLog> queryByPage(YdlOperLog ydlOperLog, PageRequest pageRequest) {
        long total = this.ydlOperLogDao.count(ydlOperLog);
        return new PageImpl<>(this.ydlOperLogDao.queryAllByLimit(ydlOperLog, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param ydlOperLog 实例对象
     * @return 实例对象
     */
    @Override
    @Async("lp-logger")
    public void insert(YdlOperLog ydlOperLog) {
        log.info("log--线程："+Thread.currentThread().getName());
        int insert = this.ydlOperLogDao.insert(ydlOperLog);
        if (insert>0){
            log.info("插入日志插入成功："+insert);
        }

//        return ydlOperLog;
    }

    /**
     * 修改数据
     *
     * @param ydlOperLog 实例对象
     * @return 实例对象
     */
    @Override
    public YdlOperLog update(YdlOperLog ydlOperLog) {
        this.ydlOperLogDao.update(ydlOperLog);
        return this.queryById(ydlOperLog.getOperId());
    }

    /**
     * 通过主键删除数据
     *
     * @param operId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer operId) {
        return this.ydlOperLogDao.deleteById(operId) > 0;
    }
}
