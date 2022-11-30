package com.lp.controller;

import com.lp.entity.YdlOperLog;
import com.lp.service.YdlOperLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 操作日志(YdlOperLog)表控制层
 *
 * @author makejava
 * @since 2022-10-07 18:08:34
 */
@RestController
@RequestMapping("ydlOperLog")
public class YdlOperLogController {
    /**
     * 服务对象
     */
    @Resource
    private YdlOperLogService ydlOperLogService;

    /**
     * 分页查询
     *
     * @param ydlOperLog 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<YdlOperLog>> queryByPage(YdlOperLog ydlOperLog, PageRequest pageRequest) {
        return ResponseEntity.ok(this.ydlOperLogService.queryByPage(ydlOperLog, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<YdlOperLog> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.ydlOperLogService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param ydlOperLog 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<String> add(YdlOperLog ydlOperLog) {
//        return ResponseEntity.ok(this.ydlOperLogService.insert(ydlOperLog));
        return ResponseEntity.ok().build();
    }

    /**
     * 编辑数据
     *
     * @param ydlOperLog 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<YdlOperLog> edit(YdlOperLog ydlOperLog) {
        return ResponseEntity.ok(this.ydlOperLogService.update(ydlOperLog));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.ydlOperLogService.deleteById(id));
    }

}

