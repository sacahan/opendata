package com.udn.ntpc.od.common.message.util;

import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 *  
 * error code與訊息轉換的工具類別
 * 
 * */
@Slf4j
public class MessageUtil {

    private static ReloadableResourceBundleMessageSource resource ;
    
    private static Locale defaultLocale = Locale.getDefault();

    /**
     * Set default locale for message handling
     * @param locale default locale
     */
    public static void setDefaultLocale(Locale locale) {
        defaultLocale = locale;
    }
    
    /**
     * Get default locale for message handling
     * @return default locale
     */
    public static Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * 以key取得對應的ResourceBundle Message
     * 
     * @param key
     * @return 訊息字串
     */
    public static String getMessage(ErrorCodeEnum key) {
        return getMessage(key.toString());
    }

    public static String getMessage(ErrorCodeEnum key, String param) {
        if (StringUtils.isNotBlank(param))
            return getMessage(key.toString(), new Object[]{param});
        return getMessage(key.toString());
    }
    
    public static String getMessage(ErrorCodeEnum key, Object[] params) {
        if (ArrayUtils.isNotEmpty(params))
            return getMessage(key.toString(), params);
        return getMessage(key.toString());
    }
    
    /**
     * 以key, locale取得對應之有參數的ResourceBundle Message
     * 
     * @param key 
     * @param locale 
     * @param params 
     * @return 訊息字串
     */
    public static String getMessage(ErrorCodeEnum key, Locale locale, Object[] params) {
        if (ArrayUtils.isNotEmpty(params))
            return getMessage(key.toString(), locale, params);
        return getMessage(key.toString(), locale);
    }
    
    /**
     * 以key, locale取得對應的ResourceBundle Message
     * 
     * @param key 
     * @param locale 
     * @return 訊息字串
     */
    public static String getMessage(String key, Locale locale) {
        return getMessage(key, locale, new Object[0]);
    }
    
    /**
     * 以key取得對應的ResourceBundle Message
     * 
     * @param key 
     * @return 訊息字串
     */
    public static String getMessage(String key) {
        return getMessage(key, getDefaultLocale(), new Object[0]);
    }
    
    /**
     * 以key取得對應之有參數的ResourceBundle Message
     * 
     * @param key 
     * @param objs
     * @return 訊息字串
     */
    public static String getMessage(String key, Object[] objs) {
        return getMessage(key, getDefaultLocale(), objs);
    }
    
    /**
     * 以key, locale取得對應之有參數的ResourceBundle Message
     * 
     * @param key
     * @param locale
     * @param params
     * @return 訊息字串
     */
    public static String getMessage(String key, Locale locale, Object[] params) {
        String message = "";
        if (key != null) {
            try {
                if (ArrayUtils.isEmpty(params)) {
                    params = ArrayUtils.EMPTY_OBJECT_ARRAY;
                }
                locale = (null == locale) ? getDefaultLocale() : locale;
                message = resource.getMessage(key, params, locale);
            } catch (NullPointerException | java.util.MissingResourceException e) {
                log.error("can't find in specified locale resource bundle, key is:" + key);
                message = key;
            }
        }
        return message;
    }

    public ReloadableResourceBundleMessageSource getResource() {
        return resource;
    }

    /**
     *
     * @param resource The resouce to set.
     */
    public void setResource(ReloadableResourceBundleMessageSource resource) {
        MessageUtil.resource = resource;
    }

}
