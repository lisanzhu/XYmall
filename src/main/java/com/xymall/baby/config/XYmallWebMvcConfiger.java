package com.xymall.baby.config;

import com.xymall.baby.common.Constant;
import com.xymall.baby.interceptor.XYmallLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.xymall.baby.interceptor.AdminUserLoginInterceptor;

@Configuration
public class XYmallWebMvcConfiger implements WebMvcConfigurer {

    @Autowired
    private AdminUserLoginInterceptor adminUserLoginInterceptor;

    @Autowired
    private XYmallLoginInterceptor xymallLoginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径（后台登陆拦截）
        registry.addInterceptor(adminUserLoginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**")
                .excludePathPatterns("/admin/goods/goodsUpload")
                .excludePathPatterns("/admin/upload/file")
                .excludePathPatterns("/common/**");
        registry.addInterceptor(xymallLoginInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/logout")
                .excludePathPatterns("/common/**")
                .excludePathPatterns("/admin/upload/file")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ Constant.FILE_UPLOAD_PATH);
    }
}
