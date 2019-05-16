package com.udn.ntpc.od.core.service.system;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.system.domain.SysParam;

public interface SysParamService extends CustomService<SysParam, String> {

    /**
     * 用 id 取
     * @param id
     * @return
     */
    String parameter(String id);
    
    String parameter(String id, String defaultValue);

    /**
     * 儲存參數
     * @param id
     * @param value
     * @return SysParamPo
     */
    SysParam saveParameter(String id, String value);

    /**
     * 取得機關代碼
     * @return
     */
    String getUnitCode();
    
    /**
     * childSerialNumber + 1 取得最新一個子資料集流水編號<br>
     * 機關代號(10碼)-G子資料集流水編號(5碼)<br>
     * identifier: 382000000A-G00001
     * @return
     */
    String genChildSerialNumber();
    
    /**
     * 取得目前這個子資料集流水編號<br>
     * 機關代號(10碼)-G子資料集流水編號(5碼)<br>
     * identifier: 382000000A-G00001
     * @return
     */
    String getCurrentChildSerialNumber();
    
    /**
     * 取得最新一個子資料集流水編號<br>
     * 機關代號(10碼)-子資料集流水編號(6碼)-流水編號(3碼)<br>
     * 預設 001<br>
     *     identifier-001
     * @param identifier
     * @return
     */
    String genResourceId(String identifier);
    
    /**
     * 取得最新三個子資料集流水編號<br>
     * 機關代號(10碼)-子資料集流水編號(6碼)-流水編號(3碼)<br>
     * 預設<br>
     *     identifier-002,identifier-003,identifier-004<br>
     * @param identifier
     * @return
     */
    String genSubResourceIds(String identifier);
    
    /**
     * 取得地圖檢視時要顯示名稱的欄位
     * @return
     */
    String[] getCoordinateMappingFieldNames();

}
