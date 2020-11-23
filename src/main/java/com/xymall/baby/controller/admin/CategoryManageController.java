package com.xymall.baby.controller.admin;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.Result;
import com.xymall.baby.Utils.ResultGenerator;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.entity.Category;
import com.xymall.baby.service.CategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class CategoryManageController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/category")
    public String categoryPage(HttpServletRequest httpServletRequest,
                               @RequestParam("categoryLevel") Byte categoryLevel,
                               @RequestParam("parentId") Long parentId,
                               @RequestParam("backParentId") Long backParentId){
        if(categoryLevel==null|| categoryLevel<1||categoryLevel>3){
            return "error/error500";
        }
        httpServletRequest.setAttribute("path", "categoryManage");
        httpServletRequest.setAttribute("categoryLevel",categoryLevel);
        httpServletRequest.setAttribute("parentId", parentId);
        httpServletRequest.setAttribute("backParentId", backParentId);
        return "admin/categoryManage";
    }

    @GetMapping("/category/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.getSuccessResult(categoryService.queryCategoryList(pageQueryUtil));
    }

    @ResponseBody
    @PostMapping("/category/save")
    public Result save(@RequestBody Category category){
        if(Objects.isNull(category.getCategoryLevel())||
                Objects.isNull(category.getParentId())||Objects.isNull(category.getCategoryRank())||
                StringUtils.isEmpty(category.getCategoryName())){
            return ResultGenerator.getFailResult("参数异常");
        }
        String res=categoryService.saveCategory(category);
        if(ServiceResultEnum.SUCCESS.getResult().equals(res)){
            return ResultGenerator.getSuccessResult(res);
        }else{
            return ResultGenerator.getFailResult(res);
        }
    }

    @ResponseBody
    @PostMapping("/category/update")
    public Result updateCategory(@RequestBody Category category){
        if(Objects.isNull(category.getCategoryId())||Objects.isNull(category.getCategoryLevel())||
                Objects.isNull(category.getCategoryRank())||StringUtils.isEmpty(category.getCategoryName())||
        Objects.isNull(category.getParentId())){
            return ResultGenerator.getFailResult("参数异常");
        }
        String res=categoryService.updateCategory(category);
        if(ServiceResultEnum.SUCCESS.getResult().equals(res)){
            return ResultGenerator.getSuccessResult(res);
        }else{
            return ResultGenerator.getFailResult(res);
        }
    }

    @ResponseBody
    @PostMapping("/category/delete")
    public Result deleteCategory(@RequestBody Integer[] ids){
        if(ids.length<=0){
            return ResultGenerator.getFailResult("参数异常");
        }
        String res=categoryService.deleteCategory(ids);
        if(ServiceResultEnum.SUCCESS.getResult().equals(res)){
            return ResultGenerator.getSuccessResult(res);
        }else{
            return ResultGenerator.getFailResult(res);
        }
    }

    @ResponseBody
    @GetMapping("/category/listForSelect")
    public Result listForSelect(@Param("categoryId") Long categoryId){
        if(categoryId==null||categoryId<1){
            return ResultGenerator.getFailResult("缺少参数！");
        }
        Category category = categoryService.queryCategoryById(categoryId);
        //既不是一级分类也不是二级分类则为不返回数据
        if (category == null || category.getCategoryLevel() == 3) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        Map result=new HashMap(2);
        if(category.getCategoryLevel()==1){
            List<Category> secondCategoryList=categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(category.getCategoryId()), 2);
            if(secondCategoryList.size()>=1){
                //查询二级分类列表中第一个实体的所有三级分类
                List<Category> thirdCategoryList = categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(secondCategoryList.get(0).getCategoryId()),3);
                result.put("secondLevelCategories", secondCategoryList);
                result.put("thirdLevelCategories", thirdCategoryList);
            }
        }
        if(category.getCategoryLevel()==2){
            List<Category> thirdCategoryList = categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(category.getCategoryId()),3);
            result.put("thirdLevelCategories", thirdCategoryList);
        }
        return ResultGenerator.getSuccessResult(result);

    }

}
