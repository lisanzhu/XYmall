package com.xymall.baby.dao;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.entity.Goods;
import com.xymall.baby.entity.StockNumDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsManageMapper {
    int saveGoodsSelective(Goods goods);
    int queryTotalNum();
    List<Goods> queryGoodsPage(PageQueryUtil pageQueryUtil);
    Goods queryGoodsById(Long goodsId);
    int batchUpdateGoodsStatusById(Long[] ids,int status);
    //包含了详细信息
    Goods queryGoodsInfoById(Long goodsId);
    int updateByPrimaryKeySelective(Goods goods);
    List<Goods> getGoodsListBySearch(PageQueryUtil pageQueryUtil);
    int getTotalBySearch(PageQueryUtil pageQueryUtil);
    List<Goods> selectByPrimaryKeys(List<Long> goodsIds);
    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}
