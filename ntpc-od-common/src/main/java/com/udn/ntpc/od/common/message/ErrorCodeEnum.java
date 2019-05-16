package com.udn.ntpc.od.common.message;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 封裝錯誤代碼類別</br>
 * 
 * 訊息:從WEB-INF/resources/properties/message_zh_TW.properties抄過來，若無請新增</br>
 * 命名方式 ${MODULE簡稱}_${ERRORCODE}_EXCEPTION</br>
 * 使用方式如new OpdEsception(ErrorCodeEnum.ERR_1001000_EXCEPTION);</br>
 * 
 * */
@EqualsAndHashCode
public class ErrorCodeEnum implements Serializable {

	/**
	 * 訊息:從WEB-INF/resources/properties/message_zh_TW.properties抄過來，若無請新增
	 * 命名方式 ${MODULE簡稱}_${ERRORCODE}_EXCEPTION
	 * 使用方式如new OpdEsception(ErrorCodeEnum.ERR_1001000_EXCEPTIONN);	
	 */
	
	private static final long serialVersionUID = 400809445413800153L;
	
	/*========================================系統共用訊息 start========================================*/	
    /** 資料處理發生錯誤，請洽系統管理人員 */
    public static final ErrorCodeEnum DEFAULT_EXCEPTION = new ErrorCodeEnum("1000000");        
	
    /** 傳入類別不得為NULL*/
    public static final ErrorCodeEnum ERR_1000100_EXCEPTION = new ErrorCodeEnum("1000100");
        
    /** 資料類別轉換處理錯誤*/
    public static final ErrorCodeEnum ERR_1000500_EXCEPTION = new ErrorCodeEnum("1000500");
    
    /** JSON轉換XML格式發生錯誤*/
    public static final ErrorCodeEnum ERR_1000600_EXCEPTION = new ErrorCodeEnum("1000600");
    
    /** XML轉換JSON格式發生錯誤*/
    public static final ErrorCodeEnum ERR_1000700_EXCEPTION = new ErrorCodeEnum("1000700");
    
    /** CSV轉換JSON格式發生錯誤*/
    public static final ErrorCodeEnum ERR_1000800_EXCEPTION = new ErrorCodeEnum("1000800");
    
    /** 檔案不存在*/
    public static final ErrorCodeEnum ERR_1000900_EXCEPTION = new ErrorCodeEnum("1000900");
    
    /** 查無符合之資料 */
    public static final ErrorCodeEnum ERR_1001000_EXCEPTION = new ErrorCodeEnum("1001000");
    
    /** Class不存在 */
    public static final ErrorCodeEnum ERR_1001100_EXCEPTION = new ErrorCodeEnum("1001100");
    
    /** 系統排程發生錯誤 */
    public static final ErrorCodeEnum ERR_1001200_EXCEPTION = new ErrorCodeEnum("1001200");
    
    /** int、double or long格式錯誤 */
    public static final ErrorCodeEnum ERR_1001300_EXCEPTION = new ErrorCodeEnum("1001300");
    
    /** 日期格式錯誤，正確格式為yyyy-MM-dd HH:mm:ss */
    public static final ErrorCodeEnum ERR_1001301_EXCEPTION = new ErrorCodeEnum("1001301");
    /*========================================系統共用訊息 end========================================*/

    /*=============================權限 start===========================*/
    /* 2010：使用者相關錯誤 
     * 2011：單位相關錯誤
     * 2012：角色相關錯誤
     * 2013：功能相關錯誤
     * 2014：查詢條件相關錯誤
     * */
    /** 使用者登入帳號重複 */
    public static final ErrorCodeEnum ERR_2010001_EXCEPTION = new ErrorCodeEnum("2010001");
    /** 使用者email重複 */
    public static final ErrorCodeEnum ERR_2010002_EXCEPTION = new ErrorCodeEnum("2010002");
    /** 單位名稱重複 */
    public static final ErrorCodeEnum ERR_2011001_EXCEPTION = new ErrorCodeEnum("2011001");
    /** 角色名稱重複 */
    public static final ErrorCodeEnum ERR_2012001_EXCEPTION = new ErrorCodeEnum("2012001");
    /** 功能名稱重複 */
    public static final ErrorCodeEnum ERR_2013001_EXCEPTION = new ErrorCodeEnum("2013001");
    /** 查詢條件不得空白 */
    public static final ErrorCodeEnum ERR_2014001_EXCEPTION = new ErrorCodeEnum("2014001");
    /*=============================權限 end===========================*/
    
    
    
    /*========================================邏輯處理訊息 by {Module} start========================================*/
	
    
    /*=============================資料集管理-資料設定 start===========================*/
    /** 無法申請--欲修改的資料設定已在申請中 */
    public static final ErrorCodeEnum ERR_2020001_EXCEPTION = new ErrorCodeEnum("2020001");
    /** 無法申請--欲上架的資料設定已上架 */
    public static final ErrorCodeEnum ERR_2020002_EXCEPTION = new ErrorCodeEnum("2020002");
    /** 無法申請--欲下架的資料設定已下架 */
    public static final ErrorCodeEnum ERR_2020003_EXCEPTION = new ErrorCodeEnum("2020003");
    /** 無法申請--參數不正確 */
    public static final ErrorCodeEnum ERR_2020004_EXCEPTION = new ErrorCodeEnum("2020004");
    /** 無法審核--資料集未通過審核，資料設定不可審核   */
    public static final ErrorCodeEnum ERR_2020005_EXCEPTION = new ErrorCodeEnum("2020005");    
    /*=============================資料集管理-資料設定 end===========================*/
    /*=============================資料集管理-資料標籤管理 start===========================*/
    /** 無法刪除--欲刪除的標籤，目前有資料集在使用 */
    public static final ErrorCodeEnum ERR_2030001_EXCEPTION = new ErrorCodeEnum("2030001");
    /** 無法刪除--欲刪除的標籤，目前有申請的資料集在使用 */
    public static final ErrorCodeEnum ERR_2030002_EXCEPTION = new ErrorCodeEnum("2030002");
    /*=============================資料集管理-資料標籤管理 end===========================*/
    /*=============================資料集管理-資料分類管理 start===========================*/
    /** 無法刪除--欲刪除的分類，目前有資料集在使用 */
    public static final ErrorCodeEnum ERR_2040001_EXCEPTION = new ErrorCodeEnum("2040001");
    /** 無法刪除--欲刪除的分類，目前有申請的資料集在使用 */
    public static final ErrorCodeEnum ERR_2040002_EXCEPTION = new ErrorCodeEnum("2040002");
    /*=============================資料集管理-資料分類管理 end===========================*/
    /*=============================資料查詢 start===========================*/
    /** 分類名稱的關鍵字不可為空 */
    public static final ErrorCodeEnum ERR_2060001_EXCEPTION = new ErrorCodeEnum("2060001");
    /** 標籤名稱的關鍵字不可為空 */
    public static final ErrorCodeEnum ERR_2060002_EXCEPTION = new ErrorCodeEnum("2060002");
    /** 開始時間與結束時間必須同時為空或不為空 */
    public static final ErrorCodeEnum ERR_2060003_EXCEPTION = new ErrorCodeEnum("2060003");
    /*=============================資料查詢 end=============================*/
    /*=============================Data In start===========================*/
    /** 無法建表-未設定資料設定的oid */
    public static final ErrorCodeEnum ERR_2070001_EXCEPTION = new ErrorCodeEnum("2070001");
    /** 無法建表-未設定欄位定義 */
    public static final ErrorCodeEnum ERR_2070002_EXCEPTION = new ErrorCodeEnum("2070002");
    /** 無法建表-資料設定不存在 */
    public static final ErrorCodeEnum ERR_2070003_EXCEPTION = new ErrorCodeEnum("2070003");
    /** 無法建表-產生SQL失敗 */
    public static final ErrorCodeEnum ERR_2070004_EXCEPTION = new ErrorCodeEnum("2070004");
    /** 無法建表-執行建表SQL失敗 */
    public static final ErrorCodeEnum ERR_2070005_EXCEPTION = new ErrorCodeEnum("2070005");
    /** 無法新增資料-未設定資料設定的oid */
    public static final ErrorCodeEnum ERR_2070006_EXCEPTION = new ErrorCodeEnum("2070006");
    /** 無法新增資料-未設定要新增的資料 */
    public static final ErrorCodeEnum ERR_2070007_EXCEPTION = new ErrorCodeEnum("2070007");
    /** 無法新增資料-未設定要新增的資料 */
    public static final ErrorCodeEnum ERR_2070008_EXCEPTION = new ErrorCodeEnum("2070008");
    /** 無法新增資料-建表資訊不存在 */
    public static final ErrorCodeEnum ERR_2070009_EXCEPTION = new ErrorCodeEnum("2070009");
    /** 無法新增資料-table schema不存在 */
    public static final ErrorCodeEnum ERR_2070010_EXCEPTION = new ErrorCodeEnum("2070010");
    /** 無法新增資料-建表資訊缺少資料設定的oid */
    public static final ErrorCodeEnum ERR_2070011_EXCEPTION = new ErrorCodeEnum("2070011");
    /** 無法更新資料-未設定資料設定的oid */
    public static final ErrorCodeEnum ERR_2070012_EXCEPTION = new ErrorCodeEnum("2070012");
    /** 無法更新資料-未設定要更新的資料 */
    public static final ErrorCodeEnum ERR_2070013_EXCEPTION = new ErrorCodeEnum("2070013");
    /** 無法更新資料-未設定過濾條件 */
    public static final ErrorCodeEnum ERR_2070014_EXCEPTION = new ErrorCodeEnum("2070014");
    /** 無法更新資料-更新資料筆數與過濾條件筆數不一致 */
    public static final ErrorCodeEnum ERR_2070015_EXCEPTION = new ErrorCodeEnum("2070015");
    /** 無法更新資料-資料設定不存在 */
    public static final ErrorCodeEnum ERR_2070016_EXCEPTION = new ErrorCodeEnum("2070016");
    /** 無法更新資料-尚未建表 */
    public static final ErrorCodeEnum ERR_2070017_EXCEPTION = new ErrorCodeEnum("2070017");
    /** 無法更新資料-table schema不存在 */
    public static final ErrorCodeEnum ERR_2070018_EXCEPTION = new ErrorCodeEnum("2070018");
    /** 無法更新資料-建表資訊缺少資料設定的oid */
    public static final ErrorCodeEnum ERR_2070019_EXCEPTION = new ErrorCodeEnum("2070019");
    /** 無法刪除資料-未設定資料設定的oid */
    public static final ErrorCodeEnum ERR_2070020_EXCEPTION = new ErrorCodeEnum("2070020");
    /** 無法刪除資料-未設定過濾條件 */
    public static final ErrorCodeEnum ERR_2070021_EXCEPTION = new ErrorCodeEnum("2070021");
    /** 無法刪除資料-未設定過濾條件 */
    public static final ErrorCodeEnum ERR_2070022_EXCEPTION = new ErrorCodeEnum("2070022");
    /** 無法刪除資料-建表資訊有誤 */
    public static final ErrorCodeEnum ERR_2070023_EXCEPTION = new ErrorCodeEnum("2070023");
    /** 無法計算資料筆數-建表資訊有誤 */
    public static final ErrorCodeEnum ERR_2070024_EXCEPTION = new ErrorCodeEnum("2070024");
    /** 無法新增/更新資料-未通過檢核，第{0}筆資料，欄位名稱為{1}，檢核規則為{2} */
    public static final ErrorCodeEnum ERR_2070025_EXCEPTION = new ErrorCodeEnum("2070025");
    /** 無法建表-欄位數量與舊有的欄位不符合 */
    public static final ErrorCodeEnum ERR_2070026_EXCEPTION = new ErrorCodeEnum("2070026");
    /** 無法建表-欄位名稱與舊有的欄位不符合 */
    public static final ErrorCodeEnum ERR_2070027_EXCEPTION = new ErrorCodeEnum("2070027");
    /** 無法新增/更新資料-未通過檢核，共有{0}筆資料內容無法滿足檢核規則 */
    public static final ErrorCodeEnum ERR_2070028_EXCEPTION = new ErrorCodeEnum("2070028");
    /** 無法建表-欄位數量與欄位定義不符合 */
    public static final ErrorCodeEnum ERR_2070029_EXCEPTION = new ErrorCodeEnum("2070029");
    /** 無法建表-欄位名稱與欄位定義不符合 */
    public static final ErrorCodeEnum ERR_2070030_EXCEPTION = new ErrorCodeEnum("2070030");
    /** 無法轉錄檔案-無法建立暫存資料夾 */
    public static final ErrorCodeEnum ERR_2070031_EXCEPTION = new ErrorCodeEnum("2070031");
    /** 無法轉錄檔案-無法建立暫存檔案 */
    public static final ErrorCodeEnum ERR_2070032_EXCEPTION = new ErrorCodeEnum("2070032");
    /** 無法轉錄檔案-無法寫入暫存檔案 */
    public static final ErrorCodeEnum ERR_2070033_EXCEPTION = new ErrorCodeEnum("2070033");
    /** 無法轉錄檔案-無法刪除暫存資料夾 */
    public static final ErrorCodeEnum ERR_2070034_EXCEPTION = new ErrorCodeEnum("2070034");
    /*=============================Data In end===========================*/
    /*=============================Data Out start===========================*/
    /** 無法取得資料-資料集不存在 */
    public static final ErrorCodeEnum ERR_2080000_EXCEPTION = new ErrorCodeEnum("2080000");
    /** 無法取得資料-資料設定不存在 */
    public static final ErrorCodeEnum ERR_2080001_EXCEPTION = new ErrorCodeEnum("2080001");
    /** 無法取得資料-select與distinct同時存在 */
    public static final ErrorCodeEnum ERR_2080002_EXCEPTION = new ErrorCodeEnum("2080002");
    /** 無法取得資料-top無法轉成整數 */
    public static final ErrorCodeEnum ERR_2080003_EXCEPTION = new ErrorCodeEnum("2080003");
    /** 無法取得資料-skip無法轉成整數 */
    public static final ErrorCodeEnum ERR_2080004_EXCEPTION = new ErrorCodeEnum("2080004");
    /** 無法取得資料-非結構化資料 */
    public static final ErrorCodeEnum ERR_2080005_EXCEPTION = new ErrorCodeEnum("2080005");
    /** 無法取得資料-欄位不存在 */
    public static final ErrorCodeEnum ERR_2080006_EXCEPTION = new ErrorCodeEnum("2080006");
    /** 無法取得資料-order by參數數量錯誤 */
    public static final ErrorCodeEnum ERR_2080007_EXCEPTION = new ErrorCodeEnum("2080007");
    /** 無法取得資料-order by參數錯誤，格式為 column desc或column asc */
    public static final ErrorCodeEnum ERR_2080008_EXCEPTION = new ErrorCodeEnum("2080008");
    /** 無法取得資料-order by的欄位不存在 */
    public static final ErrorCodeEnum ERR_2080009_EXCEPTION = new ErrorCodeEnum("2080009");
    /** 無法取得資料-where的欄位不存在 */
    public static final ErrorCodeEnum ERR_2080010_EXCEPTION = new ErrorCodeEnum("2080010");
    /** 無法取得資料-where的算數運算子不存在 */
    public static final ErrorCodeEnum ERR_2080011_EXCEPTION = new ErrorCodeEnum("2080011");
    /** 無法取得資料-where的邏輯運算子不存在 */
    public static final ErrorCodeEnum ERR_2080012_EXCEPTION = new ErrorCodeEnum("2080012");
    /** 無法取得資料-where的運算子與運算元的數量有錯 */
    public static final ErrorCodeEnum ERR_2080013_EXCEPTION = new ErrorCodeEnum("2080013");
    /** 無法轉換格式-轉換過程發生錯誤 */
    public static final ErrorCodeEnum ERR_2080014_EXCEPTION = new ErrorCodeEnum("2080014");
    /** 無法轉換格式-無法處理指定的格式 */
    public static final ErrorCodeEnum ERR_2080015_EXCEPTION = new ErrorCodeEnum("2080015");
    /** 無法輸出檔案-無法建立暫存資料夾 */
    public static final ErrorCodeEnum ERR_2080016_EXCEPTION = new ErrorCodeEnum("2080016");
    /** 無法輸出檔案-無法建立暫存檔案 */
    public static final ErrorCodeEnum ERR_2080017_EXCEPTION = new ErrorCodeEnum("2080017");
    /** 無法輸出檔案-無法寫入暫存檔案 */
    public static final ErrorCodeEnum ERR_2080018_EXCEPTION = new ErrorCodeEnum("2080018");
    /** 無法輸出檔案-無法刪除暫存資料夾 */
    public static final ErrorCodeEnum ERR_2080019_EXCEPTION = new ErrorCodeEnum("2080019");
    /*=============================Data Out end===========================*/
    /*=============================Data Check start===========================*/
    /** 檢核異常-無法正確執行欄位指定的檢核方法：檢核資訊 {0} */
    public static final ErrorCodeEnum ERR_3000000_EXCEPTION = new ErrorCodeEnum("3000000");
    /** 檢核異常-日期轉換過程中發生異常，無法正確解譯日期格式：原始資訊為{0}，格式資訊為{1} */
    public static final ErrorCodeEnum ERR_3000001_EXCEPTION = new ErrorCodeEnum("3000001");
    /** 檢核異常-日期轉換過程中發生異常，發生解析年份異常：原始資訊為{0}，格式資訊為{1} */
    public static final ErrorCodeEnum ERR_3000002_EXCEPTION = new ErrorCodeEnum("3000002");
    /*=============================Data Out end===========================*/
    /*========================================邏輯處理訊息 by {Module} end========================================*/
    
    
    
    
    /*========================================頁面檢核訊息 start========================================*/	
    /** 欄位輸入錯誤 */
    public static final ErrorCodeEnum VEW_9000000_EXCEPTION = new ErrorCodeEnum("9000000");
    
    /** 欄位為必輸*/
    public static final ErrorCodeEnum VEW_9001000_EXCEPTION = new ErrorCodeEnum("9001000");
        
    
    /*========================================邏輯處理訊息 end========================================*/
    
    
	private final String s;

	private ErrorCodeEnum(String s) {
		this.s = s;
	}

	public final String toString() {
		return s;
    }
    
}
