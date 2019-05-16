package com.udn.ntpc.od.model.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectionParam {
    public static String DEFAULT = "--DEFAULT--";
    public static String KEY_SORT = "sort";
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String RAWDATA = "rawData";
    public static String FIELDS = "fields";
    
    @SuppressWarnings("rawtypes")
    public static List<Class> ALL_CONNECTION_PARAMS = new ArrayList<>();
    
    
    static {
        ALL_CONNECTION_PARAMS.add(CONNECTION_TYPE.class);
        ALL_CONNECTION_PARAMS.add(DBKey_DB_TYPE.class);
        ALL_CONNECTION_PARAMS.add(DBKey.class);
        ALL_CONNECTION_PARAMS.add(APIKey_API_TYPE.class);
        ALL_CONNECTION_PARAMS.add(APIKey_API_METHOD.class);
        ALL_CONNECTION_PARAMS.add(APIKey_IS_AUTH.class);
        ALL_CONNECTION_PARAMS.add(APIKey.class);
    }
    
    public enum CONNECTION_TYPE {
        DB,
        API,
        ;
    }
    
    public enum DBKey_DB_TYPE {
        MySQL,
        SQLServer,
        H2
    }
    
    public enum DBKey {
        DATA_CFG_OID("dataCfgOid", DEFAULT),
        DATA_IN_TYPE("dataInType", CONNECTION_TYPE.DB.name()),
        DB_TYPE("dbType", DBKey_DB_TYPE.MySQL.name()),
        DB_IP("dbIp", DEFAULT),
        DB_NAME("dbName", DEFAULT),
        DB_USER("dbUser", DEFAULT),
        DB_PASSWORD("dbPassword", DEFAULT),
        TABLE_NAME("tableName", DEFAULT),
        SORT(KEY_SORT, DEFAULT),
        START_TIME("startTime", "2099-12-31 00:00:00")
        ;
        
        private final String key;
        private final String defaultValue;
        
        private DBKey(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
        
        @Override
        public String toString() {
            return key;
        }
        
        public String getKey() {
            return key;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
        
        public static DBKey resovleByKey(String key) {
            for (DBKey k: values()) {
                if (k.getKey().toUpperCase().equalsIgnoreCase(key.toUpperCase())) {
                    return k;
                }
            }
            throw new EnumConstantNotPresentException(DBKey.class, key);
        }
        
        public static Map<String, String> toMap() {
            return getEnumValueObject(DBKey.class);
        }
    }
    
    public enum APIKey_API_TYPE {
        JSON,
        XML,
        CSV,
        EXCEL,
        RSS,
        KML
    }

    public enum APIKey_API_METHOD {
        GET,
        POST,
    }
    
    public enum APIKey_IS_AUTH {
        Y,
        N,
    }
    
    public enum APIKey {
        DATA_CFG_OID("dataCfgOid", DEFAULT),
        DATA_IN_TYPE("dataInType", CONNECTION_TYPE.API.name()),
        API_URL("apiUrl", DEFAULT),
        API_TYPE("apiType", APIKey_API_TYPE.JSON.name()),
        API_METHOD("apiMethod", APIKey_API_METHOD.GET.name()),
//        API_IS_AUTH("apiIsAuth", APIKey_IS_AUTH.N.name()),
//        API_USER("apiUser", DEFAULT),
//        API_PASSWORD("apiPassword", DEFAULT),
        SORT(KEY_SORT, DEFAULT),
        ROOT_PATH("rootPath", "/"),
        START_TIME("startTime", "2099-12-31 00:00:00")
        ;
        
        private final String key;
        private final String defaultValue;
        private APIKey(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
        
        @Override
        public String toString() {
            return key;
        }
        
        public String getKey() {
            return key;
        }
        
        public String getDefaultValue() {
            return defaultValue;
        }

        public static APIKey resovleByKey(String key) {
            for (APIKey k: values()) {
                if (k.getKey().toUpperCase().equalsIgnoreCase(key.toUpperCase())) {
                    return k;
                }
            }
            throw new EnumConstantNotPresentException(APIKey.class, key);
        }
        
        public static Map<String, String> toMap() {
            return getEnumValueObject(APIKey.class);
        }
    }
    
    private static Map<String, String> getEnumValueObject(Class<? extends Enum<?>> e) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Enum<?> enu: e.getEnumConstants()) {
            result.put(enu.name(), enu.toString());
        }
        return result;
//        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }

}

