package com.lp.aspect;

import com.lp.annotation.Log;
import com.lp.core.RedisTemplate;
import com.lp.entity.YdlOperLog;
import com.lp.service.YdlOperLogService;
import com.lp.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @author LuoPing
 * @date 2022/10/14 16:29
 */
@Component
@Aspect
@Slf4j
//public class LogAspect implements BeanFactoryAware {
public class LogAspect {

    @Resource
    private YdlOperLogService ydlOperLogService;
    @Resource
    private RedisTemplate redisTemplate;
//    @Resource
//    private ExecutorService executorService;
//    private BeanFactory beanFactory;
//
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.beanFactory = beanFactory;
//    }
    //增强处理
    /**
     * 处理完请求后执行
     *
     *
     */
    @AfterReturning("@annotation(operLog)")
    public void afterReturning(JoinPoint joinPoint,Log operLog){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        YdlOperLog ydlOperLog = createOperLog(joinPoint, request, operLog, null);
//        LogAspect logAspect = beanFactory.getBean(this.getClass());
//        logAspect.logHandler(ydlOperLog);
        //这个方法是异步的
        ydlOperLogService.insert(ydlOperLog);
        log.info("{},执行了【{}】,操作",ydlOperLog.getOperName(),ydlOperLog.getTitle());
    }
    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(operLog)",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Log operLog,Exception e){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        YdlOperLog ydlOperLog = createOperLog(joinPoint, request, operLog, e);
//        LogAspect logAspect = beanFactory.getBean(this.getClass());
//        logAspect.logHandler(ydlOperLog);
        ydlOperLogService.insert(ydlOperLog);
        log.error("{},执行了【{}】,操作",ydlOperLog.getOperName(),ydlOperLog.getTitle(),e);
    }
    /**
     * 真正执行添加日志的操作
     *
     *
     */
//    @Async("lp-logger")
//    public void logHandler(YdlOperLog ydlOperLog){
//
//            //保存日志
//            ydlOperLogService.insert(ydlOperLog);
//
//    }
    public YdlOperLog createOperLog(JoinPoint joinPoint, HttpServletRequest request, Log operLog, Exception e) {
        //        提交任务
//根据现场信息，封装日志实例
        log.info("log-----"+Thread.currentThread().getName());
        YdlOperLog ydlOperLog = new YdlOperLog();
        ydlOperLog.setTitle(operLog.title());
        ydlOperLog.setBusinessType(operLog.businessType());
        if (e != null){
            //截取异常中0-2000的字符存入
            ydlOperLog.setErrormsg(e.getMessage().length()>2000 ?
                    e.getMessage().substring(0,2000):e.getMessage());//三元运算符
            ydlOperLog.setStatus(500);
        }else {
            ydlOperLog.setStatus(200);
        }
        ydlOperLog.setMethod(joinPoint.getSignature().getName());//获取执行的方法
        ydlOperLog.setOperIp(request.getRemoteAddr());//操作ip
        //注意空指针的问题
        if (AuthUtil.getLoginUser(redisTemplate) != null && AuthUtil.getLoginUser(redisTemplate).getYdlUser() != null){
            ydlOperLog.setOperName(AuthUtil.getLoginUser(redisTemplate).getYdlUser().getUserName());
        }
        ydlOperLog.setOperUrl(request.getRequestURI());//请求地址
        ydlOperLog.setOpertime(new Date());//操作日期
        ydlOperLog.setRequestMethod(request.getMethod());//请求方法
        return ydlOperLog;
    }
    /**
     * 真正执行添加日志的操作
     * @param joinPoint
     * @param request
     * @param log
     * @param e
     */
//    private void logHandler(JoinPoint joinPoint, HttpServletRequest request,Log log,Exception e){
//        //new Thread 当大量的请求，会导致线程池突增
////        new Thread(() ->{
////
////        });
//        //不行，每次请求，都会创建一个线程池
////        ExecutorService executorService = Executors.newFixedThreadPool(10);
////        executorService.execute(() ->{
////
////        });
////        提交任务
////        System.out.println("log--"+Thread.currentThread().getName());
////            System.out.println("log--"+Thread.currentThread().getName());
////根据现场信息，封装日志实例
//        YdlOperLog ydlOperLog = new YdlOperLog();
//        ydlOperLog.setTitle(log.title());
//        ydlOperLog.setBusinessType(log.businessType());
//        if (e != null){
//            //截取异常中0-2000的字符存入
//            ydlOperLog.setErrormsg(e.getMessage().length()>2000 ?
//                    e.getMessage().substring(0,2000):e.getMessage());//三元运算符
//            ydlOperLog.setStatus(500);
//        }else {
//            ydlOperLog.setStatus(200);
//        }
//        ydlOperLog.setMethod(joinPoint.getSignature().getName());//获取执行的方法
//        ydlOperLog.setOperIp(request.getRemoteAddr());//操作ip
//        //注意空指针的问题
//        if (AuthUtil.getLoginUser(redisTemplate) != null && AuthUtil.getLoginUser(redisTemplate).getYdlUser() != null){
//            ydlOperLog.setOperName(AuthUtil.getLoginUser(redisTemplate).getYdlUser().getUserName());
//        }
//
//        ydlOperLog.setOperUrl(request.getRequestURI());//请求地址
//        ydlOperLog.setOpertime(new Date());//操作日期
//        ydlOperLog.setRequestMethod(request.getMethod());//请求方法
//        executorService.execute(()->{
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException interruptedException) {
//                interruptedException.printStackTrace();
//            }
//            //保存日志
//            ydlOperLogService.insert(ydlOperLog);
//        });
//
//    }
}
