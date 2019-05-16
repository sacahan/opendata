package com.udn.ntpc.od.model.cfg.dto;

import com.udn.ntpc.od.common.converter.StringToSortObjectConverter;
import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataCfgConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey;
import com.udn.ntpc.od.model.common.ConnectionParam.DBKey;
import com.udn.ntpc.od.model.domain.ValueObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Transactional
@Slf4j
public class DataCfgConnectionParams extends ArrayList<DataCfgConnectionParam> implements ValueObject {

    private static final long serialVersionUID = -770183654690728232L;

    /**
     * 轉換成前端 ValueObject 使用
     */
    private static final DataCfgConnectionParams DEFAULT_VALUE_OBJECT = defaultInstance(true);

    private static StringToSortObjectConverter stringToSortConverter;
    
    @Autowired
    private StringToSortObjectConverter tmpStringToSortConverter;

    protected DataCfg dataCfg;
    
    @PostConstruct
    private void postConstruct() {
        log.debug(getClass().getName() + " constructed");
        stringToSortConverter = tmpStringToSortConverter;
    }

    public static DataCfgConnectionParams defaultInstance(boolean genDefaultParams) {
        DataCfgConnectionParams result = new DataCfgConnectionParams();
        if (genDefaultParams) {
            result.defaultParams();
        }
        return result;
    }
    
    protected DataCfgConnectionParams() {
    }

    public DataCfgConnectionParams(DataCfg dataCfg) {
        this.dataCfg = dataCfg;
        defaultParams(dataCfg);
    }

    public DataCfgConnectionParams(Collection<DataCfgConnectionParam> params) {
        for (DataCfgConnectionParam param: params) {
            if (param.getDataCfg() != null) {
                this.dataCfg = param.getDataCfg();
                break;
            }
        }
        addAll(params);
    }

    public DataCfgConnectionParams(Map<String, String> params) {
        fromMap(params);
    }

    /**
     * check paramKey
     */
    @Override
    public boolean contains(Object o) {
        if (o != null) {
            for (DataCfgConnectionParam p : this) {
                if (p.getParamKey().equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getValue(DBKey key) {
        return getValue(key.getKey());
    }

    public String getValue(APIKey key) {
        return getValue(key.getKey());
    }
    
    public DataCfgConnectionParam get(DBKey key) {
        return get(key.getKey());
    }
    
    public DataCfgConnectionParam get(APIKey key) {
        return get(key.getKey());
    }
    
    public void set(DataCfgConnectionParam param) {
        if (param != null) {
            setValue(param.getParamKey(), param.getParamValue());
        }
    }
    
    public DataCfgConnectionParam setValue(DBKey key, String value) {
        return setValue(key.getKey(), value);
    }

    public DataCfgConnectionParam setValue(APIKey key, String value) {
        return setValue(key.getKey(), value);
    }

    public void validateParams() throws OpdException {
        String sort = getValue(ConnectionParam.KEY_SORT);
        Sort s = stringToSortConverter.convert(sort);
        if (s == null)
            setValue(ConnectionParam.KEY_SORT, ConnectionParam.DEFAULT);
        else
            setValue(ConnectionParam.KEY_SORT, s.toString());

        for (DataCfgConnectionParam param: this) {
            if (StringUtils.isBlank(param.getParamValue())) {
                throw new OpdException(String.format("%s 不可空白", param.getParamKey()));
            }
            if (param.getDataCfg() == null) {
                if (dataCfg == null)
                    throw new OpdException(String.format("%s DataCfg 不可空白", param.getParamKey()));
                param.setDataCfg(dataCfg);
            }
        }
    }
    
    public void updateDataCfgConnectionParams(DataCfg dataCfg) {
        this.dataCfg = dataCfg;
        for (DataCfgConnectionParam param: this) {
            param.setDataCfg(dataCfg);
        }
        dataCfg.getDataCfgConnectionParams().clear();
        dataCfg.getDataCfgConnectionParams().addAll(this);
    }

    public Map<String, String> toMap() {
        Map<String, String> results = new LinkedHashMap<>();
        for (DataCfgConnectionParam param: this) {
            results.put("dataCfgOid", param.getDataCfgOid());
            results.put(param.getParamKey(), param.getParamValue());
        }
        return results;
    }
    
    public void fromMap(Map<String, String> source) {
        clear();
        defaultParams();        
        for (String paramKey: source.keySet()) {
            if (!DBKey.resovleByKey(paramKey).equals(DBKey.DATA_CFG_OID)) {
                String value = source.get(paramKey);
                this.setValue(DBKey.resovleByKey(paramKey), value);
            }
        }
    }
    
    /**
     * 轉換成 Map<paramKey, paramValue>
     */
    @Override
    public Map<String, String> genDefaultInstance() {
        return DEFAULT_VALUE_OBJECT.toMap();
    }
    
    private String getValue(String key) {
        for (DataCfgConnectionParam param: this) {
            if (param.getParamKey().equals(key)) {
                return param.getParamValue();
            }
        }
        return null;
    }
    
    private DataCfgConnectionParam get(String key) {
        for (DataCfgConnectionParam param: this) {
            if (param.getParamKey().equals(key)) {
                return param;
            }
        }
        return null;
    }
    
    private DataCfgConnectionParam setValue(String key, String value) {
        for (DataCfgConnectionParam param: this) {
            if (param.getParamKey().equals(key)) {
                param.setParamValue(value);
                return param;
            }
        }
        DataCfgConnectionParam param = new DataCfgConnectionParam(key, value);
        param.setDataCfg(dataCfg);
        add(param);
        return param;
    }

    protected void defaultParams() {
        DataCfg dataCfg = null;
        defaultParams(dataCfg);
    }
    
    protected void defaultParams(DataCfg dataCfg) {
        for (DBKey param: ConnectionParam.DBKey.values()) {
            add(new DataCfgConnectionParam(dataCfg, param.getKey(), param.getDefaultValue()));
        }
    }

}
