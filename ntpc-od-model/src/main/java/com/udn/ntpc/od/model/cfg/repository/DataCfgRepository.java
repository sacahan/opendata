package com.udn.ntpc.od.model.cfg.repository;

import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DataCfgRepository extends CustomRespository<DataCfg, String> {


    DataCfg findByDataSetOid(String dataSetOid);
    
    /**
     * 取得 所有資料集內資料設定的總資料量<br/>
     * 其條件必須滿足:公開、有效且上架
     * @return intTotalValue 總計資料量的結果
     */
/*
    @Query(value="SELECT SUM(ODC.data_count) " +
                 "FROM [dbo].[od_data_set] ODS " +
                 "LEFT JOIN [dbo].[od_data_cfg] ODC ON ODC.od_data_set_oid = ODS.oid " +
                 "WHERE " +
                 "    (ODS.is_active = 1 AND ODS.is_enable = 1 AND ODS.is_public = 1) " +
                 "AND (ODC.is_active = 1 AND ODC.is_enable = 1 AND ODC.is_public = 1)",
           nativeQuery = true)
*/
    @Query(value="SELECT SUM(odc.dataCount) " +
                 "FROM DataSet ods, DataCfg odc " +
                 "WHERE " +
                 "    odc.dataSetOid = ods.oid " +
                 "AND (ods.isActive = 1 AND ods.isEnable = 1 AND ods.isPublic = 1) " +
                 "AND (odc.isActive = 1 AND odc.isEnable = 1 AND odc.isPublic = 1)")
    int sumDataCount();
}
