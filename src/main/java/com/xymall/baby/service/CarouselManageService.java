package com.xymall.baby.service;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.entity.Carousel;
import com.xymall.baby.vo.IndexCarouselVO;

import java.util.List;

public interface CarouselManageService {
    String saveCarousel(Carousel carousel);
    PageResult getCarouselPage(PageQueryUtil pageQueryUtil);
    String updateCarousel(Carousel carousel);
    Boolean batchDeleteCarousel(Integer[] ids);
    List<IndexCarouselVO> getCarouselForIndex(int num);
}
