package com.udn.ntpc.od.model.common;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.model.cfg.domain.DataCfgMetadata;
import com.udn.ntpc.od.model.set.domain.DataSetMetadata;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Metadata 定義
 *
 */
public class Metadata {

    public static String DISPLAY_NAME = "displayName";
    public static String DATA_SET_OID = "dataSetOid";
    
    /**
     * 資料來源
     */
    public enum DATA_CFG_DATA_IN_TYPE {
        FILE, // 上傳檔案 
        SYSTEM_TRANSFORM, // 系統介接轉入(DB、API)
        SERVICE_PROVIDED // 服務提供 (第三方系統提供)
    }
    
    /**
     * 資料處理模式
     */
    public enum DATA_CFG_DATA_PROCESS_TYPE {
        OVERWRITE, // 覆蓋資料
        APPEND // 增加資料
    }
    
	public static interface DataSet {
		/**
		 * 資料集提供機關聯絡人
		 */
		public final String CONTACT_NAME = "contactName";
		/**
		 * 資料集提供機關聯絡人(對外顯式名稱)
		 */
		public final String CONTACT_SHOW_NAME = "contactShowName";
		/**
		 * 資料集提供機關聯絡人電話
		 */
		public final String CONTACT_TEL = "contactTel";
		/**
		 * 資料集提供機關聯絡人電子郵件
		 */
		public final String CONTACT_EMAIL = "contactEmail";
		/**
		 * 授權方式
		 */
		public final String AUTHORIZE = "authorize";
		/**
		 * 主要欄位說明
		 */
		public final String FIELD_DESCRIPTION = "fieldDescription";
		/**
		 * 計費方式
		 */
		public final String CHARGE = "charge";
		/**
		 * 計費說明網址
		 */
		public final String CHARGE_URL = "chargeURL";
		/**
		 * 資料集類型 
		 */
		public final String DATA_SET_TYPE = "dataSetType";
		/**
		 * 計費法令依據
		 */
		public final String CHARGE_LOW = "chargeLow";
		/**
		 * 更新頻率
		 */
		public final String DATA_SET_UPDATE_FREQ = "dataSetUpdateFreq";
        /**
         * 開始執行時間 yyyy-MM-dd HH:mm:ss
         */
        public final String DATA_SET_UPDATE_START_TIME = "dataSetUpdateStartTime";
		/**
		 * 資料量
		 */
		public final String DATA_CNT = "dataCnt";
		/**
		 * 資料集說明網址
		 */
		public final String DATA_SET_EXPLAIN_URL = "dataSetExplainUrl";
		/**
		 * 資料集說明檔案oid
		 */
		public final String DATA_SET_EXPLAIN_OID = "dataSetExplainOid";
		/**
		 * 資料集說明檔案name
		 */
		public final String DATA_SET_EXPLAIN_NAME = "dataSetExplainName";
		/**
		 * 相似詞
		 */
		public final String SIMILAR_WORD = "similarWord";
		/**
		 * 參考網址
		 */
		public final String DATA_SET_REF_URL = "dataSetRefUrl";
		/**
		 * 備註
		 */
		public final String NOTE = "note";
		/**
		 * 收錄期間
		 */
		public final String RECORD_DATE = "recordDate";
		/**
		 * 編碼
		 */
		public final String ENCODE = "encode";
		/**
		 * 目前審核階段   1:系統管理者 2:單位資訊人員 3:單位承辦人
		 */
		public final String REVIEW_LEVEL = "reviewLevel";
		/**
		 * 目前審核者帳號
		 */
		public final String REVIEW_ACCOUNT = "reviewAccount";
		/**
		 * 是否已經審核
		 *  0:尚未審核 1:已經審核
		 */
		public final String IS_REVIEWED = "isReviewed";
		/**
		 * 拒絕狀態
		 *  0:尚未拒絕 1:審核者拒絕 2:自已拒絕(抽單)
		 */
		public final String REFUSE_STATE = "refuseState";
		/**
		 * 資料集相關連結
		 */
		public final String RELATED_URL = "RELATE_URL";
		/**
		 * 資料管理者帳號
		 * 多筆可依","隔開
		 */
		public final String MANAGER_ID = "manager_id";
		
		//國發會新增欄位
		
		/**
		 * 資料集編號
		 * 系統自動產生，10+6
		 */
		public final String IDENTIFIER = "identifier";
		/**
		 * 授權說明網址
		 */
		public final String LICENSE_URL = "licenseURL";
		/**
		 * 資料集提供機關之上級機關名稱
		 */
		public String ORGANIZATION = "organization";
		/**
		 * 資料集提供機關之上級機關聯絡人姓名
		 */
		public String ORGANIZATION_CONTACT_NAME = "organizationContactName";
		/**
		 * 資料集提供機關之上級機關聯絡人電話
		 */
		public String ORGANIZATION_CONTACT_PHONE = "organizationContactPhone";
		/**
		 * 資料集提供機關之上級機關聯絡人電子郵件
		 */
		public String ORGANIZATION_CONTACT_EMAIL = "organizationContactEmail";
		/**
		 * 開始收錄日期
		 */
		public String TEMPORAL_COVERAGE_FROM = "temporalCoverageFrom";
		/**
		 * 結束收錄日期
		 */
		public String TEMPORAL_COVERAGE_TO = "temporalCoverageTo";
		/**
		 * 資料集空間範圍
		 */
		public String SPATIAL = "spatial";
		/**
		 * 資料集語系
		 */
		public String LANGUAGE = "language";
		/**
		 * 國發發會分類代碼
		 */
		public String CATEGORY_CODE ="categoryCode";
	}
	
	public static interface DataCfg {
		
		/**
		 * 資料類型
		 *   0:資料檔 1:影音檔 2:圖片檔 3:PDF 4:地理圖資
		 */
		public final String DATA_CFG_TYPE = "dataCfgType";
		/**
		 * 資料來源
		 *  0:上傳檔案 1:系統介接轉入 2:服務提供
		 */
		public final String DATA_IN_TYPE = "dataInType";
		/**
		 * 更新頻率
		 * hour:每小時、day:每日、week:每週、month:每月、season:每季、year:每年、x:即時
		 */
		public final String DATA_CFG_UPDATE_FREQ = "dataCfgUpdateFreq";
		
		/**
		 * 開始執行時間 yyyy-MM-dd HH:mm:ss
		 */
		public final String DATA_CFG_UPDATE_START_TIME = "dataCfgUpdateStartTime";
		/**
		 * 資料處理模式
		 * 0:覆蓋資料 1:增加資料
		 */
		public final String DATA_PROCESS_TYPE = "dataProcessType";
		/**
		 * 資料來源狀態
		 * 0:無任何檔案
		 * 1:原本無檔案，指定新檔案
		 * 2:原本有檔案，指定空檔
		 * 3:原本有檔案，指定新檔案
		 */
		public final String DATA_CFG_FILE_STATUS = "dataCfgFileStatus";
		/**
		 * kml檔案，名稱欄位
		 */
		public final String KML_NAME_FIELD = "kmlNameField";
		/**
		 * kml檔案，地址欄位
		 */
		public final String KML_ADDR_FIELD = "kmlAddrField";
		/**
		 * kml檔案，緯度欄位
		 */
		public final String KML_LAT_FIELD = "kmlLatField";
		/**
		 * kml檔案，經度欄位
		 */
		public final String KML_LON_FIELD = "kmlLonField";
		/**
		 * 服務提供網址
		 */
		public final String SERVICE_URL = "serviceUrl";
		/**
		 * 台北外掛網址
		 */
		public final String PLUG_TPA_URL = "plugTpaUrl";
		/**
		 * rdf link
		 */
		public final String RDF_URL = "rdfUrl";
		
		//國發會新增欄位
		
		/**
		 * 資料資源編號
		 * 10+6+3
		 */
		public final String RESOURCE_ID = "resourceID";
		/**
		 * 資料資源最後更新時間
		 */
		public final String RESOURCE_MODIFIED = "resourceModified";
		/**
		 * 領域別詮釋資料參考網址
		 */
		public final String METADATASOURCEOFDATA = "metadataSourceOfData";
		/**
		 * 結構化資料編號
		 */
		public final String SUB_RESOURCE_ID = "subResourceIDs";
		
        // 分群欄位名稱
        public final String GROUPING_FIELDS = "groupingFields";
        
        // 子資料集分群欄位內容
        public final String GROUPING_VALUE = "groupingValue";
	}
	
	public static interface DataCategory {
		/**
		 * 檔案名稱
		 */
		public final String FILE_NAME = "fileName";
		/**
		 * 檔案大小
		 */
		public final String FILE_SIZE = "fileSize";
		/**
		 * 推薦標籤oid
		 */
		public final String TAG_OIDS = "tagOids";
		
	}

	/**
	 * 資料來源
	 * @param dataCfg
	 * @return
	 */
	public static DATA_CFG_DATA_IN_TYPE getDataInType(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
        String metaValue = Metadata.getDataCfgMetaValue(Metadata.DataCfg.DATA_IN_TYPE, dataCfg);
        if (metaValue == null) {
			throw new OpdException(String.format("未設定 %s: %s", Metadata.DataCfg.DATA_IN_TYPE, dataCfg.getOid()));
		}
        return DATA_CFG_DATA_IN_TYPE.values()[Integer.valueOf(metaValue)];
    }

	/**
	 * 資料處理模式
	 * @param dataCfg
	 * @return
	 */
	public static DATA_CFG_DATA_PROCESS_TYPE getDataProcessType(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
	    String metaValue = Metadata.getDataCfgMetaValue(Metadata.DataCfg.DATA_PROCESS_TYPE, dataCfg);
	    if (metaValue == null) {
			throw new OpdException(String.format("未設定 %s: %s", Metadata.DataCfg.DATA_PROCESS_TYPE, dataCfg.getOid()));
		}
	    return DATA_CFG_DATA_PROCESS_TYPE.values()[Integer.valueOf(metaValue)];
	}

	/**
	 * 更新頻率
	 * @param dataCfg
	 * @return
	 */
	public static DATA_CFG_UPDATE_FREQ getDataCfgUpdateFreq(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
	    String metaValue = Metadata.getDataCfgMetaValue(Metadata.DataCfg.DATA_CFG_UPDATE_FREQ, dataCfg);
	    if (metaValue == null) {
			throw new OpdException(String.format("未設定 %s: %s", Metadata.DataCfg.DATA_CFG_UPDATE_FREQ, dataCfg.getOid()));
		}
	    return DATA_CFG_UPDATE_FREQ.resovleByValue(metaValue);
	}

	/**
	 * 取得分群欄位名稱
     * @param dataCfg: parent or child
	 * @return
	 */
	public static String getGroupingFields(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
	    return Metadata.getDataCfgMetaValue(Metadata.DataCfg.GROUPING_FIELDS, dataCfg);
	}

	/**
	 * 儲存分群欄位名稱
	 * @param dataCfg: parent or child
	 * @return
	 */
	public static void setGroupingFields(String groupingFields, com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
	    newDataCfgMetadata(Metadata.DataCfg.GROUPING_FIELDS, groupingFields, dataCfg);
	}

	/**
	 * 取得分群欄位值
	 * @param childDataCfg
	 * @return
	 */
	public static String getGroupingValue(com.udn.ntpc.od.model.cfg.domain.DataCfg childDataCfg) {
	    return Metadata.getDataCfgMetaValue(Metadata.DataCfg.GROUPING_VALUE, childDataCfg);
	}

	/**
	 * 儲存分群欄位值
	 * @param childDataCfg
	 * @return
	 */
	public static void setGroupingValue(String groupingValue, com.udn.ntpc.od.model.cfg.domain.DataCfg childDataCfg) {
	    newDataCfgMetadata(Metadata.DataCfg.GROUPING_VALUE, groupingValue, childDataCfg);
	}

    public static boolean isKmlDataSet(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
        String dataCfgType = Metadata.getDataCfgMetaValue(Metadata.DataCfg.DATA_CFG_TYPE, dataCfg);
        if (StringUtils.isBlank(dataCfgType)) {
            return false;
        }
        return DATA_CFG_TYPES.KML.equals(DATA_CFG_TYPES.resovleByValue(dataCfgType));
    }

    /***
     * 取得國發會資料開平台資料集-資源編號
     * @param dataCfg
     * @return
     */
    public static String getResourceId(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
        return getDataCfgMetaValue(Metadata.DataCfg.RESOURCE_ID, dataCfg);
    }

    /***
     * 設定國發會資料開平台資料集-資源編號
     * @param dataCfg
     * @param value
     */
    public static void setResourceId(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg, String value) {
        newDataCfgMetadata(Metadata.DataCfg.RESOURCE_ID, value, dataCfg);
    }

    /***
     * 取得國發會資料開平台資料集-其他資源編號
     * @param dataCfg
     * @return
     */
    public static String getSubResourceIds(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
        return getDataCfgMetaValue(Metadata.DataCfg.SUB_RESOURCE_ID, dataCfg);
    }

    /***
     * 設定國發會資料開平台資料集-其他資源編號
     * @param dataCfg
     * @param value
     */
    public static void setSubResourceIds(com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg, String value) {
        newDataCfgMetadata(Metadata.DataCfg.SUB_RESOURCE_ID, value, dataCfg);
    }

	/**
	 * 取得 metadata value
	 * @param key
	 * @param dataCfg
	 * @return
	 */
    public static String getDataCfgMetaValue(String key, com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
        DataCfgMetadata metadata = getDataCfgMetadata(key, dataCfg);
        if (metadata == null) {
			return null;
		}
        return metadata.getMetadataValue();
    }

    /**
     * 取得 metadata
     * @param key
     * @param dataCfg
     * @return
     */
    public static DataCfgMetadata getDataCfgMetadata(String key, com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
        if (dataCfg == null || CollectionUtils.isEmpty(dataCfg.getDataCfgMetadatas())) {
			return null;
		}
        for (DataCfgMetadata cfgMeta : dataCfg.getDataCfgMetadatas()) {
			if (key.equals(cfgMeta.getMetadataKey())) {
				return cfgMeta;
			}
		}
        return null;
    }

    /**
     * 設定 Metadata 若不存在則自動新增一筆
     * @param key
     * @param value
     * @param dataCfg
     * @return
     */
    public static DataCfgMetadata newDataCfgMetadata(String key, String value, com.udn.ntpc.od.model.cfg.domain.DataCfg dataCfg) {
        DataCfgMetadata metadata = getDataCfgMetadata(key, dataCfg);
        if (metadata == null) {
			metadata = new DataCfgMetadata();
		}
        metadata.setMetadataKey(key);
/*
        if (StringUtils.isBlank(value)) {
            metadata.setMetadataValue("");
        } else {
            metadata.setMetadataValue(value);
        }
*/
        // 除了 Null 以外，其餘皆保留原始傳入的內容，如：""、" "、"  "..
        if (value == null) {
            value = "";
        }
        metadata.setMetadataValue(value);
        metadata.setDataCfg(dataCfg);
        return metadata;
    }

    /***
     * 取得國發會資料開平台資料集編號
     * @param dataSet
     * @return
     */
    public static String getIdentifier(com.udn.ntpc.od.model.set.domain.DataSet dataSet) {
        return getDataSetMetadataValue(Metadata.DataSet.IDENTIFIER, dataSet);
    }

    /***
     * 設定國發會資料開平台資料集編號
     * @param dataSet
     * @param value
     */
    public static void setIdentifier(com.udn.ntpc.od.model.set.domain.DataSet dataSet, String value) {
        newDataSetMetadata(Metadata.DataSet.IDENTIFIER, value, dataSet);
    }
    
    /**
     * 取得 metadata value
     * @param key
     * @param dataSet
     * @return
     */
    public static String getDataSetMetadataValue(String key, com.udn.ntpc.od.model.set.domain.DataSet dataSet) {
        DataSetMetadata metadata = getDataSetMetadata(key, dataSet);
        if (metadata == null) {
			return null;
		}
        return metadata.getMetadataValue();
    }

    /**
     * 取得 metadata
     * @param key
     * @param dataSet
     * @return
     */
    public static DataSetMetadata getDataSetMetadata(String key, com.udn.ntpc.od.model.set.domain.DataSet dataSet) {
        if (dataSet == null || CollectionUtils.isEmpty(dataSet.getDataSetMetadatas())) {
			return null;
		}
        for (DataSetMetadata meta : dataSet.getDataSetMetadatas()) {
			if (key.equals(meta.getMetadataKey())) {
				return meta;
			}
		}
        return null;
    }

    /**
     * 設定 Metadata 若不存在則自動新增一筆
     * @param key
     * @param value
     * @param dataSet
     * @return
     */
    public static DataSetMetadata newDataSetMetadata(String key, String value, com.udn.ntpc.od.model.set.domain.DataSet dataSet) {
        DataSetMetadata metadata = getDataSetMetadata(key, dataSet);
        if (metadata == null) {
			metadata = new DataSetMetadata();
		}
        metadata.setMetadataKey(key);
/*
        if (StringUtils.isBlank(value)) {
            metadata.setMetadataValue("");
        } else {
            metadata.setMetadataValue(value);
        }
*/
        // 除了 Null 以外，其餘皆保留原始傳入的內容，如：""、" "、"  "..
        if (value == null) {
            value = "";
        }
        metadata.setMetadataValue(value);
        metadata.setDataSet(dataSet);
        return metadata;
    }


/*
    public static DataSetMetadataApply newDataSetMetaApply(String key, String value, DataSetApply po) {
        DataSetMetadataApply meta = new DataSetMetadataApply();
        meta.setMetadataKey(key);
        if (StringUtils.isEmpty(value)) {
            meta.setMetadataValue("");
        } else {
            meta.setMetadataValue(value);
        }
        meta.setDataSetApply(po);
        return meta;
    }

    public static DataCfgMetadataApply newDataCfgMetaApply(String key, String value, DataCfgApply po) {
        DataCfgMetadataApply meta = new DataCfgMetadataApply();
        meta.setMetadataKey(key);
        if (StringUtils.isEmpty(value)) {
            meta.setMetadataValue("");
        } else {
            meta.setMetadataValue(value);
        }
        meta.setDataCfgApply(po);
        return meta;
    }

    public static DataSetMetadataApply getDataSetMetaApply(String key, DataSetApply po) {
        for (DataSetMetadataApply setMeta : po.getDataSetMetadataList()) {
            if (key.equals(setMeta.getMetadataKey())) {
                return setMeta;
            }
        }
        return null;
    }

    public static DataCfgMetadataApply getDataCfgMetaApply(String key, DataCfgApply po) {
        for (DataCfgMetadataApply cfgMeta : po.getDataCfgMetadataApplyList()) {
            if (key.equals(cfgMeta.getMetadataKey())) {
                return cfgMeta;
            }
        }
        return null;
    }

    public static DataSetMetadata getDataSetMeta(String key, DataSet po) {
        for (DataSetMetadata setMeta : po.getDataSetMetadataList()) {
            if (key.equals(setMeta.getMetadataKey())) {
                return setMeta;
            }
        }
        return null;
    }

    public static DataCfgMetadata getDataCfgMeta(String key, DataCfg po) {
        try {// 不明資料無cfgMeta會出現null pointer
            for (DataCfgMetadata cfgMeta : po.getDataCfgMetadataList()) {
                if (key.equals(cfgMeta.getMetadataKey())) {
                    return cfgMeta;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    
    public static DataCfgMetadata getDataCfgMeta(String key, List<DataCfgMetadata> metaList) {
        for (DataCfgMetadata cfgMeta : metaList) {
            if (key.equals(cfgMeta.getMetadataKey())) {
                return cfgMeta;
            }
        }
        return null;
    }

    public static String getDataCfgMetaValue(String key, List<DataCfgMetadata> metaList) {
        if (metaList == null || metaList.size() == 0)
            return null;
        for (DataCfgMetadata cfgMeta : metaList)
            if (key.equals(cfgMeta.getMetadataKey()))
                return cfgMeta.getMetadataValue();
        return null;
    }
    
    public static void updateDataCfgMeta(String key, String value, DataCfg po) {
        for (DataCfgMetadata cfgMeta : po.getDataCfgMetadataList()) {
            if (key.equals(cfgMeta.getMetadataKey())) {
                cfgMeta.setMetadataValue(value);
                break;
            }
        }
    }

    public static void updateDataSetMetaApply(String key, String value, DataSetApply po) {
        for (DataSetMetadataApply setMeta : po.getDataSetMetadataList()) {
            if (key.equals(setMeta.getMetadataKey())) {
                setMeta.setMetadataValue(value);
                return;
            }
        }
        DataSetMetadataApply newMata = new DataSetMetadataApply();
        newMata.setCommon(false);
        newMata.setDataSetApply(po);
        newMata.setMetadataKey(key);
        newMata.setMetadataValue(value);
        po.getDataSetMetadataList().add(newMata);
    }

    public static void updateDataCfgMetaApply(String key, String value, DataCfgApply po) {
        for (DataCfgMetadataApply cfgMeta : po.getDataCfgMetadataApplyList()) {
            if (key.equals(cfgMeta.getMetadataKey())) {
                cfgMeta.setMetadataValue(value);
                return;
            }
        }
        DataCfgMetadataApply newMata = new DataCfgMetadataApply();
        newMata.setCommon(false);
        newMata.setDataCfgApply(po);
        newMata.setMetadataKey(key);
        newMata.setMetadataValue(value);
        po.getDataCfgMetadataApplyList().add(newMata);
    }

    public static DataCategoryMetadata getDataCategoryMeta(String key, DataCategory po) {
        for (DataCategoryMetadata meta : po.getDataCategoryMetadataList()) {
            if (key.equals(meta.getMetadataKey())) {
                return meta;
            }
        }
        return null;
    }

    public static DataCategoryMetadata newDataCategoryMeta(String key, String value, DataCategory po) {
        DataCategoryMetadata meta = new DataCategoryMetadata();
        meta.setMetadataKey(key);
        if (StringUtils.isEmpty(value)) {
            meta.setMetadataValue("");
        } else {
            meta.setMetadataValue(value);
        }
        meta.setDataCategory(po);
        return meta;
    }

    public static void updateDataCategoryMeta(String key, String value, DataCategory po) {
        for (DataCategoryMetadata meta : po.getDataCategoryMetadataList()) {
            if (key.equals(meta.getMetadataKey())) {
                meta.setMetadataValue(value);
                break;
            }
        }
    }

    public static String getCfgMetaValue(List<DataCfgMetadata> metaList, String key) {
        for (DataCfgMetadata meta : metaList)
            if (meta.getMetadataKey().equals(key))
                return meta.getMetadataValue();
        return "";
    }

    public static String getSetMetaValue(List<DataSetMetadata> metaList, String key) {
        for (DataSetMetadata meta : metaList)
            if (meta.getMetadataKey().equals(key))
                return meta.getMetadataValue();

        return "";
    }
*/
}
