package com.udn.ntpc.od.common.exception;

import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.common.message.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = -6522410790915858984L;
	
	/**
     * 錯誤碼代號
     */
    protected ErrorCodeEnum errorCode;

    /**
     * To contain dynamic data for construct final error message
     */
    protected List<String> params = new ArrayList<>();

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用 error code 建立一個無額外訊息之Exception。
     *
     * @param errorCode
     *            Error Code
     */
    public BaseException(ErrorCodeEnum errorCode) {
        this(errorCode, new String[0]);
    }

    /**
     * 使用 error code 建立一個有代換訊息之Exception。
     *
     * @param param
     *            欲代換之錯誤資訊
     * @param errorCode
     *            Error Code
     */
    public BaseException(ErrorCodeEnum errorCode, String param) {
        this(errorCode, new String[]{param});
    }
    
    /**
     * 使用 error code 建立一個有代換訊息之Exception。
     *
     * @param errorCode
     *            Error Code
     * @param params
     *            欲代換之錯誤資訊
     * 
     */
    public BaseException(ErrorCodeEnum errorCode, String[] params) {
        super(MessageUtil.getMessage(errorCode, params)); 
        this.errorCode = errorCode;
        if (ArrayUtils.isNotEmpty(params))
            this.params = Arrays.asList(params);
    }

    /**
     * 使用預設錯誤碼代號，建立一個有代換訊息及原生錯誤物件之Exception
     *
     * @param errorCode
     *            Error Code
     * @param cause
     *            原生之 Exception
     */
    public BaseException(ErrorCodeEnum errorCode, Throwable cause) {
        this(errorCode, new String[0], cause);
    }

    /**
     * 使用 error code，建立一個有原生錯誤物件之Exception
     *
     * @param errorCode
     *            Error Code
     * @param messages
     *            欲代換之錯誤資訊
     * @param cause
     *            原生之 Exception
     */
    public BaseException(ErrorCodeEnum errorCode, String param, Throwable cause) {
        this(errorCode, new String[]{param}, cause);
    }
    
    /**
     * 使用 error code，建立一個有代換訊息及原生錯誤物件之Exception
     *
     * @param errorCode
     *            Error Code
     * @param cause
     *            原生之 Exception
     * @param arguments
     *            欲代換之錯誤資訊
     */
    public BaseException(ErrorCodeEnum errorCode, String[] params, Throwable cause) {
        super(MessageUtil.getMessage(errorCode, params), cause);
        this.errorCode = errorCode;
        if (ArrayUtils.isNotEmpty(params))
            this.params = Arrays.asList(params);
    }
    
    public BaseException(ErrorCodeEnum errorCode, List<String> params, Throwable cause) {
        super(MessageUtil.getMessage(errorCode, params.toArray()), cause);
        this.errorCode = errorCode;
        if (CollectionUtils.isNotEmpty(params))
            this.params = params;
    }
    
    /**
     * Gets error code
     *
     * @return error code
     * @see ErrorCodeEnum
     */
    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String getErrorCodeString() {
        if (errorCode != null)
            return errorCode.toString();
        return null;
    }

    /**
     * Get parameters
     *
     * @return parameters
     */
    public List<String> getParameters() {
        return params;
    }
    
    /**
     * Gets error message with super exception's message
     *
     * @return error message
     */
/*
    @Override
    public String getMessage() {
        if (errorCode == null)
            return super.getMessage();
        String superMessage = StringUtils.isNotBlank(super.getMessage())? super.getMessage(): StringUtils.EMPTY;
        String message = MessageUtil.getMessage(errorCode, params.toArray());
        if (StringUtils.isNotBlank(superMessage))
            message += ", " + superMessage;
        return message;
    }
*/
    /**
     * Gets localized error message with default locale
     *
     * @return error message
     */
/*
    @Override
    public String getLocalizedMessage() {
        if (errorCode == null)
            return super.getLocalizedMessage();
        String superMessage = StringUtils.isNotBlank(super.getLocalizedMessage())? super.getLocalizedMessage(): StringUtils.EMPTY;
        String message = MessageUtil.getMessage(errorCode, params.toArray());
        if (StringUtils.isNotBlank(superMessage))
            message += ", " + superMessage;
        return message;
    }
*/

}
