package com.xymall.baby.controller.mall;

import com.xymall.baby.common.Constant;
import com.xymall.baby.service.CarouselManageService;
import com.xymall.baby.service.CategoryService;
import com.xymall.baby.vo.IndexCarouselVO;
import com.xymall.baby.vo.IndexCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    CarouselManageService carouselManageService;

    @Autowired
    CategoryService categoryService;

//    @Autowired
//    IndexConfigService indexConfigService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<IndexCategoryVO> categories = categoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error500";
        }
        List<IndexCarouselVO> carousels = carouselManageService.getCarouselForIndex(Constant.INDEX_CAROUSEL_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        return "user/index";
    }
}
