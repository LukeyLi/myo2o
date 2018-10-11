package com.imooc.myo2o.exceptions;

public class ProductOperationException extends RuntimeException {
    private static final long serialVersionUID = -7778382311930767867L;

    public ProductOperationException(String msg){
        super(msg);
    }
}
