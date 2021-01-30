package com.xymall.baby.service;


import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.entity.Goods;

import java.util.List;

public interface GoodsManageService {
    String saveGoods(Goods goods);
    PageResult queryGoodsPage(PageQueryUtil pageQueryUtil);
    Goods queryGoodsById(Long goodsId);
    int batchUpdateGoodsStatus(Long[] ids,int status);
    Goods queryGoodsInfoById(Long goodsId);
    String updateGoods(Goods goods);
    PageResult searchGoods(PageQueryUtil pageQueryUtil);
}
