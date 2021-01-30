package com.xymall.baby.service.impl;

import com.xymall.baby.Utils.BeanUtil;
import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.Utils.ResultGenerator;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.dao.GoodsManageMapper;
import com.xymall.baby.entity.Goods;
import com.xymall.baby.service.GoodsManageService;
import com.xymall.baby.vo.SearchGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoodsManageServiceImpl implements GoodsManageService {

    @Autowired
    GoodsManageMapper goodsManageMapper;

    @Override
    public String saveGoods(Goods goods) {
        if(goodsManageMapper.saveGoodsSelective(goods)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }else{
            return ServiceResultEnum.DB_ERROR.getResult();
        }
    }

    @Override
    public PageResult queryGoodsPage(PageQueryUtil pageQueryUtil) {
        List<Goods> list=goodsManageMapper.queryGoodsPage(pageQueryUtil);
        int total=goodsManageMapper.queryTotalNum();
        PageResult pageResult=new PageResult(list, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Goods queryGoodsById(Long goodsId) {
        Goods goods = goodsManageMapper.queryGoodsById(goodsId);

        return goods;
    }

    @Override
    public int batchUpdateGoodsStatus(Long[] ids, int status) {
        return goodsManageMapper.batchUpdateGoodsStatusById(ids, status);
    }

    @Override
    public Goods queryGoodsInfoById(Long goodsId) {
        return goodsManageMapper.queryGoodsInfoById(goodsId);
    }

    @Override
    public String updateGoods(Goods goods) {
        Goods temp = goodsManageMapper.queryGoodsById(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsManageMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public PageResult searchGoods(PageQueryUtil pageUtil) {
        List<Goods> goodsList = goodsManageMapper.getGoodsListBySearch(pageUtil);
        int total = goodsManageMapper.getTotalBySearch(pageUtil);
        List<SearchGoodsVO> searchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            searchGoodsVOS = BeanUtil.copyList(goodsList, SearchGoodsVO.class);
            for (SearchGoodsVO searchGoodsVO : searchGoodsVOS) {
                String goodsName = searchGoodsVO.getGoodsName();
                String goodsIntro = searchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    searchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    searchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(searchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }


}
