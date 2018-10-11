package com.imooc.myo2o.exceptions;

public class ShopOperationException extends RuntimeException {

    private static final long serialVersionUID = -7010756506006861443L;

    public ShopOperationException(String msg){
        super(msg);
    }
}
