package com.testmanage.service.login;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CORSAuthenticationFilter extends FormAuthenticationFilter {

    public CORSAuthenticationFilter() {
        super();
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //Always return true if the request's method is OPTIONS
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }


   /* @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Content-type","application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = res.getWriter();
            Map<String, Object> map = new HashMap<>();
            map.put("code", 702);
            map.put("msg", "未登录");
            JsonObject jsonObject = new JsonObject(map);
            writer.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }*/
}
