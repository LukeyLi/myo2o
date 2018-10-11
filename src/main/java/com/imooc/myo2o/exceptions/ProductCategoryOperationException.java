package com.imooc.myo2o.exceptions;

public class ProductCategoryOperationException extends RuntimeException {
    private static final long serialVersionUID = 2721501568462692989L;

    public ProductCategoryOperationException(String msg){
        super(msg);
    }
}
