package com.know.knowboot.exception;


import com.know.knowboot.enums.ErrorEnum;

/**
 * 支付失败异常
 */
public class PaymentException extends BaseException {

    public PaymentException(String msg) {
        super(ErrorEnum.FAILED.getCode(), msg, ErrorEnum.SHOW_MSG.getCode());
    }

    public PaymentException(String msg, Integer errCode) {
        super(errCode, msg, ErrorEnum.SHOW_MSG.getCode());
    }

}
