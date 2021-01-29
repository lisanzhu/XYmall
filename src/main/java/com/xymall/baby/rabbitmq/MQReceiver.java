package com.xymall.baby.rabbitmq;


import com.xymall.baby.dao.GoodsManageMapper;
import com.xymall.baby.entity.Order;
import com.xymall.baby.entity.StockNumDTO;
import com.xymall.baby.entity.User;
import com.xymall.baby.redis.RedisService;
import com.xymall.baby.service.GoodsManageService;
import com.xymall.baby.service.OrderService;
import com.xymall.baby.vo.GoodsDetailVO;
import com.xymall.baby.vo.OrderDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
		RedisService redisService;
		
//		@Autowired
//		GoodsService goodsService;

		@Autowired
		GoodsManageMapper goodsManageMapper;
		
		@Autowired
		OrderService orderService;
		
		@Autowired
		GoodsManageService goodsManageService;
		
		@RabbitListener(queues= MQConfig.MIAOSHA_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
			User user = mm.getUser();
			long goodsId = mm.getGoodsId();
			int num=mm.getNum();
			
//			GoodsDetailVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
//			GoodsDetailVO goods = goodsManageService.queryGoodsInfoById(goodsId);
//	    	int stock = goods.getStockCount();
//	    	if(stock <= 0) {
//	    		return;
//	    	}
	    	//判断是否已经秒杀到了
//	    	Order order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
			OrderDetailVO order = orderService.getOrderDetailByOrderNo( ""+goodsId,user.getUserId());
	    	if(order != null) {
	    		return;
	    	}
	    	//减库存 下订单 写入秒杀订单
//	    	miaoshaService.miaosha(user, goods);
			StockNumDTO s=new StockNumDTO();
	    	s.setGoodsCount(num);
	    	s.setGoodsId(goodsId);
			List<StockNumDTO> temp= new ArrayList<StockNumDTO>();
			temp.add(s);
			goodsManageMapper.updateStockNum(temp);
//	    	goodsManageService.updateGoods(goods);
		}
	

//		
		
}
