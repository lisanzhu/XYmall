package com.xymall.baby.service;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.entity.Category;
import com.xymall.baby.vo.IndexCategoryVO;
import com.xymall.baby.vo.SearchPageCategoryVO;

import java.util.List;


public interface CategoryService {
    PageResult queryCategoryList(PageQueryUtil pageQueryUtil);
    String saveCategory(Category category);
    String updateCategory(Category category);
    String deleteCategory(Integer[] ids);
    List<Category> queryCategoryByLevelAndParentId(List<Long> parentId, int level);
    Category queryCategoryById(Long categoryId);
    List<IndexCategoryVO> getCategoriesForIndex();
    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);

}
