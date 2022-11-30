package com.lp.core;

import com.lp.util.XssUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LuoPing
 * @date 2022/10/16 17:32
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {



    //原来的请求
    private final HttpServletRequest orgRequest;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    //重写下面的方法，处理Xss
    @Override
    public String getParameter(String name) {
        return XssUtil.xssEncode(super.getParameter(XssUtil.xssEncode(name)));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        Map<String,String[]> newParameterMap = new HashMap<>(8);
        //遍历
        for (Map.Entry<String,String[]> entry : parameterMap.entrySet()){
            // 处理key的标签
            String keyNew = XssUtil.xssEncode(entry.getKey());
            String[] value = entry.getValue();
//            新建一个value
            String[] newValue = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                newValue[i] = XssUtil.xssEncode(value[i]);
            }
            newParameterMap.put(keyNew,newValue);
        }
        return newParameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValue = super.getParameterValues(XssUtil.xssEncode(name));
        String[] newParameterValue = new String[parameterValue.length];
        for (int i = 0; i < parameterValue.length; i++) {
            newParameterValue[i] = XssUtil.xssEncode(parameterValue[i]);
        }
        return newParameterValue;
    }
    // 如果将来想继续使用原始的request，可以通过这个方法获取
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }
}
