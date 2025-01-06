package com.ejada.product.service.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Constants {

    public static final String SORT_ORDER_DESC = "desc";

    public static final String INVALID_PRODUCT_PRICE_FILTER_ERROR_MESSAGE = "Min price should be less than max price";
    public static final String INVALID_CATEGORY_ERROR_MESSAGE = "Invalid category";
    public static final String PRODUCT_ALREADY_EXISTS_ERROR_MESSAGE = "This product already exists";
    public static final String PRODUCT_NAME_IS_REQUIRED_ERROR_MESSAGE = "Product name is required";
    public static final String PRODUCT_PRICE_IS_REQUIRED_ERROR_MESSAGE = "Product price is required";
    public static final String PRODUCT_QUANTITY_IS_REQUIRED_ERROR_MESSAGE = "Product quantity is required";
    public static final String CATEGORY_ID_IS_REQUIRED_ERROR_MESSAGE = "Category ID is required";
    public static final String PRODUCT_NAME_MAX_SIZE_ERROR_MESSAGE = "Product name must not exceed 255 characters";
    public static final String DESCRIPTION_MAX_SIZE_ERROR_MESSAGE = "Description must not exceed 2000 characters";
    public static final String PRODUCT_PRICE_MIN_VALUE_ERROR_MESSAGE = "Price must be greater than or equal to 0.0";
    public static final String PRODUCT_QUANTITY_MIN_VALUE_ERROR_MESSAGE = "Stock quantity must be non-negative";
    public static final String DATABASE_GENERAL_ERROR_MESSAGE = "Error occurred while connecting to database";
    public static final String PRODUCT_SOFT_DELETED_SUCCESSFULLY = "Product deleted successfully.";
    public static final String PRODUCT_NOT_FOUND_ERROR_MESSAGE = "Product not found with id: ";
    public static final String PRODUCT_IS_ALREADY_DELETED = "Product is already deleted.";
    public static final String PRODUCTS_NOT_FOUND = "The following products do not exist: ";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String INSUFFICIENT_STOCK = "Insufficient stock for products: ";

}
