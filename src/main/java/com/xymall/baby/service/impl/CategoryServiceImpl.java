package com.xymall.baby.service.impl;

import com.xymall.baby.Utils.BeanUtil;
import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.common.CategoryLevelEnum;
import com.xymall.baby.common.Constant;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.dao.CategoryMapper;
import com.xymall.baby.entity.Category;
import com.xymall.baby.service.CategoryService;
import com.xymall.baby.vo.IndexCategoryVO;
import com.xymall.baby.vo.SearchPageCategoryVO;
import com.xymall.baby.vo.SecondLevelCategoryVO;
import com.xymall.baby.vo.ThirdLevelCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public PageResult queryCategoryList(PageQueryUtil pageQueryUtil) {
        List<Category> list= categoryMapper.queryCategoryList(pageQueryUtil);
        int total =categoryMapper.countCategory(pageQueryUtil);
        return new PageResult(list, total,
                pageQueryUtil.getLimit(),
                pageQueryUtil.getPage());
    }

    @Override
    public String saveCategory(Category category) {
        System.out.println(category.getCategoryLevel());
        System.out.println(category.getCategoryName());
        Category categoryPre= categoryMapper.selectCategoryByLevelAndName(category.getCategoryLevel(),category.getCategoryName());
        if(categoryPre!=null){
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if(categoryMapper.insertCategorySelective(category)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCategory(Category category) {
        Category temp1= categoryMapper.selectCategoryById(category.getCategoryId());
        if(temp1==null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        Category temp2=categoryMapper.selectCategoryByLevelAndName(category.getCategoryLevel(), category.getCategoryName());
        if(temp2!=null&&!temp2.getCategoryId().equals(category.getCategoryId())){
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        category.setUpdateTime(new Date());
        if(categoryMapper.updateCategoryById(category)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }


    //TODO 删除功能等以后再做
    @Override
    public String deleteCategory(Integer[] ids) {

        return null;
    }

    @Override
    public List<Category> queryCategoryByLevelAndParentId(List<Long> parentId, int level) {
        if(parentId.size()<=0) return new ArrayList<>();
        List<Category> res= categoryMapper.queryCategoryByLevelAndParentId(parentId,level);
        return res;
    }

    @Override
    public Category queryCategoryById(Long categoryId) {
        if(categoryId==null) return null;
        return categoryMapper.selectCategoryById(categoryId);
    }

    @Override
    public List<IndexCategoryVO> getCategoriesForIndex() {
        List<IndexCategoryVO> indexCategoryVOS=new ArrayList<>();
        //获取一级分类的固定数量的数据
        List<Category> firstLevelCategories = categoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel(), Constant.INDEX_CATEGORY_NUMBER);
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(Category::getCategoryId).collect(Collectors.toList());
            //获取二级分类的数据
            List<Category> secondLevelCategories = categoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds, CategoryLevelEnum.LEVEL_TWO.getLevel(), 0);
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(Category::getCategoryId).collect(Collectors.toList());
                //获取三级分类的数据
                List<Category> thirdLevelCategories = categoryMapper.selectByLevelAndParentIdsAndNumber(secondLevelCategoryIds, CategoryLevelEnum.LEVEL_THREE.getLevel(), 0);
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    //根据 parentId 将 thirdLevelCategories 分组
                    Map<Long, List<Category>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(Category::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    //处理二级分类
                    for (Category secondLevelCategory : secondLevelCategories) {
                        SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                        BeanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                        //如果该二级分类下有数据则放入 secondLevelCategoryVOS 对象中
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {
                            //根据二级分类的id取出thirdLevelCategoryMap分组中的三级分类list
                            List<Category> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                            secondLevelCategoryVO.setThirdLevelCategoryVOS((BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class)));
                            secondLevelCategoryVOS.add(secondLevelCategoryVO);
                        }
                    }
                    //处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        //根据 parentId 将 thirdLevelCategories 分组
                        Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for (Category firstCategory : firstLevelCategories) {
                            IndexCategoryVO newBeeMallIndexCategoryVO = new IndexCategoryVO();
                            BeanUtil.copyProperties(firstCategory, newBeeMallIndexCategoryVO);
                            //如果该一级分类下有数据则放入 newBeeMallIndexCategoryVOS 对象中
                            if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
                                //根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                                List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                                newBeeMallIndexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                                indexCategoryVOS.add(newBeeMallIndexCategoryVO);
                            }
                        }
                    }
                }
            }
            return indexCategoryVOS;
        } else {
            return null;
        }
    }

    @Override
    public SearchPageCategoryVO getCategoriesForSearch(Long categoryId) {
        SearchPageCategoryVO searchPageCategoryVO = new SearchPageCategoryVO();
        Category thirdLevelGoodsCategory = categoryMapper.selectCategoryById(categoryId);
        if (thirdLevelGoodsCategory != null && thirdLevelGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            //获取当前三级分类的二级分类
            Category secondLevelGoodsCategory = categoryMapper.selectCategoryById(thirdLevelGoodsCategory.getParentId());
            if (secondLevelGoodsCategory != null && secondLevelGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_TWO.getLevel()) {
                //获取当前二级分类下的三级分类List
                List<Category> thirdLevelCategories = categoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelGoodsCategory.getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel(), Constant.SEARCH_CATEGORY_NUMBER);
                searchPageCategoryVO.setCurrentCategoryName(thirdLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setSecondLevelCategoryName(secondLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setThirdLevelCategoryList(thirdLevelCategories);
                return searchPageCategoryVO;
            }
        }
        return null;
    }


}
