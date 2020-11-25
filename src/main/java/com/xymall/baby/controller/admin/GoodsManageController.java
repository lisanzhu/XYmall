package com.xymall.baby.controller.admin;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.Utils.Result;
import com.xymall.baby.Utils.ResultGenerator;
import com.xymall.baby.common.Constant;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.entity.Category;
import com.xymall.baby.entity.Goods;
import com.xymall.baby.service.CategoryService;
import com.xymall.baby.service.GoodsManageService;
import com.xymall.baby.vo.SearchPageCategoryVO;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class GoodsManageController {
    @Autowired
    GoodsManageService goodsManageService;

    @Autowired
    CategoryService categoryService;


    @GetMapping("/goodsUpload")
    public String uploadGoods(HttpServletRequest request){
        request.setAttribute("path", "goodsUpload");
        List<Category> list= categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(0L),1);
        if(!CollectionUtils.isEmpty(list)){
            List<Category> list2=categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(list.get(0).getCategoryId()),2);
            if(!CollectionUtils.isEmpty(list2)){
                List<Category> list3=categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(list2.get(0).getCategoryId()), 3);
                request.setAttribute("firstLevelCategories", list);
                request.setAttribute("secondLevelCategories", list2);
                request.setAttribute("thirdLevelCategories", list3);
                request.setAttribute("path", "goodsUpload");
                return "admin/goodsUpload";
            }
        }
        return "error/error500";
    }

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "goodsManage");
        return "admin/goodsManage";
    }

    @ResponseBody
    @PostMapping("/goods/save")
    public Result goodsSave(@RequestBody Goods goods){
        if (StringUtils.isEmpty(goods.getGoodsName())
                || StringUtils.isEmpty(goods.getGoodsIntro())
                || StringUtils.isEmpty(goods.getTag())
                || Objects.isNull(goods.getOriginalPrice())
                || Objects.isNull(goods.getGoodsCategoryId())
                || Objects.isNull(goods.getSellingPrice())
                || Objects.isNull(goods.getStockNum())
                || Objects.isNull(goods.getGoodsSellStatus())
                || StringUtils.isEmpty(goods.getGoodsCoverImg())
                || StringUtils.isEmpty(goods.getGoodsDetailContent())) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        String res=goodsManageService.saveGoods(goods);
        if(res.equals(ServiceResultEnum.SUCCESS.getResult())){
            return ResultGenerator.getSuccessResult();
        }else{
            return ResultGenerator.getFailResult(res);
        }
    }

    @ResponseBody
    @GetMapping("/goods/list")
    public Result list(@RequestParam Map<String,Object> map){
        if(StringUtils.isEmpty(map.get("limit"))||StringUtils.isEmpty(map.get("page"))){
            return ResultGenerator.getFailResult("参数异常");
        }
        PageQueryUtil pageQueryUtil=new PageQueryUtil(map);
        PageResult pageResult =goodsManageService.queryGoodsPage(pageQueryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }



    //TODO 现在还不知道富文本编辑器中的内容是否能够保存并回显
    @GetMapping("/goods/goodsUpload/{goodsId}")
    public String edit(HttpServletRequest request,@PathVariable("goodsId") Long goodsId){
        //这里需要路径回显，所以和商品管理模块中显示信息不一样，在获取其他信息的同时获取详细信息
        Goods goods = goodsManageService.queryGoodsInfoById(goodsId);
        if(goods==null){
            return "error/error400";
        }
        if(goods.getGoodsCategoryId()>0){
            Category currCategory=categoryService.queryCategoryById(goods.getGoodsCategoryId());
            if(currCategory!=null&&currCategory.getCategoryLevel()==Constant.LEVEL3){
                List<Category> firstLevel=categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(0L), Constant.LEVEL1);
                List<Category> thirdLevel=categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(currCategory.getParentId()),Constant.LEVEL3);
                Category secondCategory=categoryService.queryCategoryById(currCategory.getParentId());
                if(secondCategory!=null){
                    List<Category> secondLevel=categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(secondCategory.getParentId()),Constant.LEVEL2);
                    Category firstCategory=categoryService.queryCategoryById(secondCategory.getParentId());
                    request.setAttribute("firstLevelCategories",firstLevel);
                    request.setAttribute("secondLevelCategories",secondLevel);
                    request.setAttribute("thirdLevelCategories",thirdLevel);
                    request.setAttribute("firstLevelCategoryId", firstCategory.getCategoryId());
                    request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                    request.setAttribute("thirdLevelCategoryId", currCategory.getCategoryId());
                }
            }
        }
        if(goods.getGoodsCategoryId()==0){
            List<Category> firstLevelCategories = categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(0L), Constant.LEVEL1);
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<Category> secondLevelCategories = categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), Constant.LEVEL2);
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<Category> thirdLevelCategories = categoryService.queryCategoryByLevelAndParentId(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), Constant.LEVEL3);
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", goods);
        request.setAttribute("path", "goodsUpload");
        return "admin/goodsUpload";
    }

    @PutMapping("/goods/status/{status}")
    @ResponseBody
    public Result status(@RequestBody Long[] ids,@PathVariable("status") int status){
        if(ids.length<1) return ResultGenerator.getFailResult("参数异常");
        if(status!=Constant.SELLUP&&status!=Constant.SELLDOWN){
            return ResultGenerator.getFailResult("状态异常");
        }
        if(goodsManageService.batchUpdateGoodsStatus(ids,status)>0){
            return ResultGenerator.getSuccessResult();
        }else{
            return ResultGenerator.getFailResult(ServiceResultEnum.DB_ERROR.getResult());
        }
    }


    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Goods goods) {
        if (Objects.isNull(goods.getGoodsId())
                || StringUtils.isEmpty(goods.getGoodsName())
                || StringUtils.isEmpty(goods.getGoodsIntro())
                || StringUtils.isEmpty(goods.getTag())
                || Objects.isNull(goods.getOriginalPrice())
                || Objects.isNull(goods.getSellingPrice())
                || Objects.isNull(goods.getGoodsCategoryId())
                || Objects.isNull(goods.getStockNum())
                || Objects.isNull(goods.getGoodsSellStatus())
                || StringUtils.isEmpty(goods.getGoodsCoverImg())
                || StringUtils.isEmpty(goods.getGoodsDetailContent())) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        String result = goodsManageService.updateGoods(goods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult(result);
        }
    }

//    @GetMapping("/goods/info/{id}")
//    @ResponseBody
//    public Result info(@PathVariable("id") Long id) {
//        Goods goods = goodsManageService.queryGoodsById(id);
//        if (goods == null) {
//            return ResultGenerator.getFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
//        }
//        return ResultGenerator.getSuccessResult(goods);
//    }



}
