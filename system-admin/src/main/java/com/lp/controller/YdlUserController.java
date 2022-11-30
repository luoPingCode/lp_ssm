package com.lp.controller;

import com.lp.annotation.HasPermission;
import com.lp.annotation.Log;
import com.lp.annotation.Repeat;
import com.lp.entity.YdlUser;
import com.lp.lpEnum.DeleteFlagEnum;
import com.lp.service.YdlUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户信息表(YdlUser)表控制层
 *
 * @author makejava
 * @since 2022-10-07 18:08:41
 */
@RestController
@RequestMapping("ydlUser")
public class YdlUserController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private YdlUserService ydlUserService;

    /**
     * 分页查询
     *
     * @param ydlUser 筛选条件
     * @para PageRequest.of(ydlUser.getPage(),ydlUser.getSize())      分页对象
     * @return 查询结果
     */
    @GetMapping
    @Log(title = "查询用户",businessType = "用户操作")
//    @Repeat(leftTime = 100)
    public ResponseEntity<Page<YdlUser>> queryByPage(YdlUser ydlUser) {//以JSON方式接收 需加RequestBody，RequestBody是从请求提中获取数据
        return ResponseEntity.ok(this.ydlUserService.queryByPage(ydlUser, PageRequest.of(ydlUser.getPage(),ydlUser.getSize())));
    }

    /**
     * 通过主键查询用户权限和角色信息
     *
     * @return
     */
    @GetMapping("getInfo")
    public HashMap<String, List<String>> getInfo(){
//        System.out.println("进来了");
        return ydlUserService.getInfo();
    }
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
//    @HasPermission("system:user:query")
    public ResponseEntity<YdlUser> queryById(@PathVariable("id") Long id) {
        //system:user:query admin hr 获取登陆用户
//        YdlLoginUser loginUser = getLoginUser();
//        //获取用户，根据用户查询他的角色或权限，去判断接口需要的权限是否包含在用户所拥有的权限当中
//        List<String> hasPerms = redisTemplate.getObject(Constants.PERM_PREFIX + loginUser.getToken(), new TypeReference<>() {
//        });
//        if (hasPerms.contains("system:user:query")){
//            throw new RuntimeException("您没有改权限");
//        }
        return ResponseEntity.ok(this.ydlUserService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param ydlUser 实体
     * @return 新增结果
     */
    @PostMapping
    @Log(title = "新增用户",businessType = "用户操作")
    @Repeat
    public ResponseEntity<String> add(@RequestBody YdlUser ydlUser, HttpServletRequest request) {
        String res = "";
//        设置其他默认值
        ydlUser.setLoginIp(request.getRemoteHost());//登陆IP
        ydlUser.setCreateTime(new Date());//创建时间
        ydlUser.setCreateBy(getLoginUser().getYdlUser().getUserName());//创建人
        ydlUser.setDelFlag(DeleteFlagEnum.NO.getValue());
        ydlUser.setStatus("0");
        if (this.ydlUserService.insert(ydlUser) != 0){
            res = "添加成功";
        }else {
            res = "添加失败";
        };
        return ResponseEntity.ok(res);
    }

    /**
     * 编辑数据
     *
     * @param ydlUser 实体
     * @return 编辑结果
     */
    @PutMapping
    @Log(title = "修改用户",businessType = "用户操作")
    @Repeat(leftTime = 100)
    public ResponseEntity<YdlUser> edit(@RequestBody YdlUser ydlUser) {
        return ResponseEntity.ok(this.ydlUserService.update(ydlUser));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("{id}")
    @Log(title = "删除用户",businessType = "用户操作")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Long id) {
        System.out.println(id);
        return ResponseEntity.ok(this.ydlUserService.deleteById(id));
    }

}

