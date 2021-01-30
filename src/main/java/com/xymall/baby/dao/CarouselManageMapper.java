package com.xymall.baby.dao;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.entity.Carousel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarouselManageMapper {
    int saveCarouselById(Carousel carousel);
    int insertCarouselSelective(Carousel carousel);
    List<Carousel> queryCarouselList(PageQueryUtil pageQueryUtil);
    int queryCarouselTotalNum();
    Carousel selectCarouselById(Integer carouselId);
    int updateCarouselSelectiveById(Carousel carousel);
    int batchDeleteCarousel(@Param("ids") Integer[] ids);
    List<Carousel> findCarouselsByNum(int num);
}
