package com.xymall.baby.service;

import com.xymall.baby.entity.ShoppingCartItem;
import com.xymall.baby.vo.ShoppingCartItemVO;

import java.util.List;

public interface ShoppingCartService {
    String saveCartItem(ShoppingCartItem shoppingCartItem);
    String updateCartItem(ShoppingCartItem shoppingCartItem);
    ShoppingCartItem getCartItemById(Long shoppingCartItemId);
    Boolean deleteById(Long shoppingCartItemId);

    List<ShoppingCartItemVO> getMyShoppingCartItems(Long userId);
}
