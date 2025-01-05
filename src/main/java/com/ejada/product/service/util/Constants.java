package com.ejada.product.service.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Constants {

    public static final String SORT_ORDER_DESC = "desc";

    public static final String INVALID_PRODUCT_PRICE_FILTER_ERROR_MESSAGE = "Min price should be less than max price";

}
