package com.uerugo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		if(response instanceof HttpServletResponse){
			HttpServletResponse resp = ((HttpServletResponse)response);
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
			resp.setHeader("Access-Control-Allow-Credentials", "true");
		}
		filterChain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
