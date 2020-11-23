package com.xymall.baby.dao;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.entity.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    List<Category> queryCategoryList(PageQueryUtil pageQueryUtil);
    int countCategory(PageQueryUtil queryUtil);
    Category selectCategoryByLevelAndName(@Param("categoryLevel") Byte categoryLevel,@Param("categoryName") String categoryName);
//    int insertCategory(Category category);
    int insertCategorySelective(Category category);
    Category selectCategoryById(Long categoryId);
    int updateCategoryById(Category category);
    int deleteCategory(Integer[] ids);
    List<Category> queryCategoryByLevelAndParentId(@Param("parentId") List<Long> parentId,@Param("level") int level);
    List<Category> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds, @Param("categoryLevel") int categoryLevel, @Param("number") int number);

}
