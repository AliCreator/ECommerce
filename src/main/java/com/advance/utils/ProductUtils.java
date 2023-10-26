package com.advance.utils;

import org.springframework.beans.BeanUtils;

import com.advance.entity.Product;
import com.advance.entity.ProductCard;

public class ProductUtils {

	public static ProductCard convertToProductCard(Product product) {
		ProductCard card = new ProductCard();
		BeanUtils.copyProperties(product, card);
		return card;
	}
}
