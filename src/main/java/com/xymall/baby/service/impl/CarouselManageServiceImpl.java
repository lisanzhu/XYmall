package com.xymall.baby.service.impl;

import com.xymall.baby.Utils.BeanUtil;
import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.dao.CarouselManageMapper;
import com.xymall.baby.entity.Carousel;
import com.xymall.baby.service.CarouselManageService;
import com.xymall.baby.vo.IndexCarouselVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CarouselManageServiceImpl implements CarouselManageService {

    @Autowired
    CarouselManageMapper carouselManageMapper;

    @Override
    public String saveCarousel(Carousel carousel) {
        if(carouselManageMapper.insertCarouselSelective(carousel)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }else{
            return ServiceResultEnum.DB_ERROR.getResult();
        }
    }

    @Override
    public PageResult getCarouselPage(PageQueryUtil pageQueryUtil) {
        List<Carousel> list=carouselManageMapper.queryCarouselList(pageQueryUtil);
        int total=carouselManageMapper.queryCarouselTotalNum();
        return new PageResult(list,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
    }

    @Override
    public String updateCarousel(Carousel carousel) {
        Carousel temp=carouselManageMapper.selectCarouselById(carousel.getCarouselId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setCarouselRank(carousel.getCarouselRank());
        temp.setRedirectUrl(carousel.getRedirectUrl());
        temp.setCarouselUrl(carousel.getCarouselUrl());
        temp.setUpdateTime(new Date());

        if(carouselManageMapper.updateCarouselSelectiveById(temp)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }else{
            return ServiceResultEnum.DB_ERROR.getResult();
        }
    }

    @Override
    public Boolean batchDeleteCarousel(Integer[] ids) {
        if(ids.length<1) return false;
        if(carouselManageMapper.batchDeleteCarousel(ids)>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<IndexCarouselVO> getCarouselForIndex(int num) {
        List<IndexCarouselVO> indexCarouselVOS=new ArrayList<>(num);
        List<Carousel> carousels=carouselManageMapper.findCarouselsByNum(num);
        if (!CollectionUtils.isEmpty(carousels)) {
            indexCarouselVOS = BeanUtil.copyList(carousels, IndexCarouselVO.class);
        }
        return indexCarouselVOS;
    }


}
