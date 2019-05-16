package com.udn.ntpc.od.common.exception;

import com.udn.ntpc.od.common.message.ErrorCodeEnum;

import java.util.List;

public class OpdException extends BaseException {
    
	private static final long serialVersionUID = 4897104293090496652L;
	
    public OpdException(String message) {
        super(message);
    }

    public OpdException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用 error code 建立一個無代換訊息之Exception。
     *
     * @param errorCode Error Code
     */
    public OpdException(ErrorCodeEnum errorCode) {
        super(errorCode);
    }

    /**
     * 使用 error code 建立一個有代換訊息之Exception。
     *
     * @param errorCode Error Code
     * @param param   欲代換之錯誤資訊
     * 
     */
    public OpdException(ErrorCodeEnum errorCode, String param) {
        super(errorCode, param);        
    }

    /**
     * 使用 error code 建立一個有代換訊息之Exception。
     *
     * @param errorCode   Error Code
     * @param params   欲代換之錯誤資訊
     * 
     */
    public OpdException(ErrorCodeEnum errorCode, String[] params) {
        super(errorCode, params);
    }        

    /**
     * 使用 error code 建立一個包含原生物件之Exception。
     *
     * @param errorCode Error Code
     * @param cause     Exception的原生物件
     * 
     */
    public OpdException(ErrorCodeEnum errorCode, Throwable cause) {
        super(errorCode, "", cause);
    }
    
    /**
     * 使用 error code 建立一個有代換訊息與原生物件之Exception。
     *
     * @param errorCode Error Code
     * @param param   欲代換之錯誤資訊
     * @param cause      Exception的原生物件
     * 
     */
    public OpdException(ErrorCodeEnum errorCode, String param, Throwable cause) {
        super(errorCode, param, cause);
    }
    
    /**
     * 使用 error code 建立一個有原生物件與代換訊息之Exception。
     *
     * @param errorCode   Error Code   
     * @param cause       Exception的原生物件
     * @param params   欲代換之錯誤資訊
     * 
     */
    public OpdException(ErrorCodeEnum errorCode, String[] params, Throwable cause) {
        super(errorCode, params, cause);
    }

    /**
     * 使用 error code 建立一個有代換訊息之Exception。
     *
     * @param errorCode Error Code
     * @param params   欲代換之錯誤資訊
     * @param cause     Exception的原生物件
     * 
     */
    public OpdException(ErrorCodeEnum errorCode, List<String> params, Throwable cause) {
        super(errorCode, params, cause);
    }

}
