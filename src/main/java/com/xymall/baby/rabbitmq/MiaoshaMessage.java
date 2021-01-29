package com.xymall.baby.rabbitmq;


import com.xymall.baby.entity.User;

public class MiaoshaMessage {
	private User user;
	private Long goodsId;
	private int num;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
