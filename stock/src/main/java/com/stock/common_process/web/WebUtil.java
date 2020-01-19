package com.stock.common_process.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class WebUtil {

  @Autowired
  private HttpServletRequest request;

  public HttpServletRequest getRequestServlet() {
    // リクエストデータ確認用
//    System.out.println(Thread.currentThread().getName());
//    System.out.println(request);
//    System.out.println(request.getMethod());
//    System.out.println(request.getServletPath());
//    System.out.println(request.getParameter("orderDetails[0].createProductId"));
//    System.out.println(request.getParameterMap());

    return request;
  }


  public static Map<String, String[]> getRequestBodyMap(HttpServletRequest request) {
    return request.getParameterMap();
  }

}
