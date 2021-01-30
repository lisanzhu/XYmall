package com.xymall.baby.controller.admin;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.Result;
import com.xymall.baby.Utils.ResultGenerator;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.entity.Carousel;
import com.xymall.baby.service.CarouselManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class CarouselManageController {
    @Autowired
    CarouselManageService carouselManageService;

    @GetMapping("/carousels")
    public String carouselPage(HttpServletRequest request) {
        request.setAttribute("path", "carouselManage");
        return "admin/carouselManage";
    }

    //轮播图保存
    @ResponseBody
    @PostMapping("/carousels/save")
    public Result save(@RequestBody Carousel carousel){
        System.out.println(carousel);
        if (StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        String res=carouselManageService.saveCarousel(carousel);
        System.out.println(res);
        if(res.equals(ServiceResultEnum.SUCCESS.getResult())){
            return ResultGenerator.getSuccessResult();
        }else{
            return ResultGenerator.getFailResult(res);
        }
    }

    @RequestMapping(value = "/carousels/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.getSuccessResult(carouselManageService.getCarouselPage(pageUtil));
    }

    @RequestMapping(value = "/carousels/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Carousel carousel) {
        if (Objects.isNull(carousel.getCarouselId())
                || StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        String result = carouselManageService.updateCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult(result);
        }
    }

    @RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        if (carouselManageService.batchDeleteCarousel(ids)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("删除失败");
        }
    }
}
