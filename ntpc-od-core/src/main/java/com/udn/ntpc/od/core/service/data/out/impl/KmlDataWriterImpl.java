package com.udn.ntpc.od.core.service.data.out.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.core.service.data.out.KmlDataWriter;
import com.udn.ntpc.od.model.common.Metadata;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Schema;
import de.micromata.opengis.kml.v_2_2_0.SchemaData;
import de.micromata.opengis.kml.v_2_2_0.SimpleData;
import de.micromata.opengis.kml.v_2_2_0.SimpleField;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class KmlDataWriterImpl extends AbstractDataWriter implements KmlDataWriter {
    
    private static final Map<String, Object> DEFAULT_OPTIONS = new LinkedHashMap<>();
    static {
        DEFAULT_OPTIONS.put(Metadata.DATA_SET_OID, "not-exists");
        DEFAULT_OPTIONS.put(Metadata.DISPLAY_NAME, "changeme");
        DEFAULT_OPTIONS.put(Metadata.DataCfg.KML_NAME_FIELD, "name");
        DEFAULT_OPTIONS.put(Metadata.DataCfg.KML_LAT_FIELD, "wgs84aX");
        DEFAULT_OPTIONS.put(Metadata.DataCfg.KML_LON_FIELD, "wgs84aY");
    }

    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas) throws IOException {
        return write(datas, "UTF-8");
    }
    
    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas, String encoding) throws IOException {
        return write(datas, encoding, DEFAULT_OPTIONS);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas, String encoding, Map<String, Object> options) throws IOException {
        verifyOptions(options);

        String dataSetOid = (String) options.get(Metadata.DATA_SET_OID);
        String displayName = (String) options.get(Metadata.DISPLAY_NAME);

        final Kml kml = new Kml();
        Document doc = kml.createAndSetDocument().withName(displayName).withOpen(Boolean.TRUE);
        doc.setId(dataSetOid);
        
        write(doc, datas, options);
        
        return (O) kml;
    }
    
    @Override
    public void write(Document doc, List<Map<String, Object>> datas, Map<String, Object> options) throws IOException {
        verifyOptions(options);
        
        String nameKey = (String) options.get(Metadata.DataCfg.KML_NAME_FIELD);
        String latKey = (String) options.get(Metadata.DataCfg.KML_LAT_FIELD);
        String lonKey = (String) options.get(Metadata.DataCfg.KML_LON_FIELD);
        
        Schema schema = null;
        
        for (Map<String, Object> data: datas) {
            String name = String.valueOf(data.get(nameKey));
            String latStr = String.valueOf(data.get(latKey));
            double lat = 0;
            if (StringUtils.isNotBlank(latStr) && !StringUtils.containsIgnoreCase(latStr, "null")) {
                try {
                    lat = Double.parseDouble(latStr.trim());
                } catch (NumberFormatException e) {
                }
            }
            String lonStr = String.valueOf(data.get(lonKey));
            double lon = 0;
            if (StringUtils.isNotBlank(lonStr) && !StringUtils.containsIgnoreCase(lonStr, "null")) {
                try {
                    lon = Double.parseDouble(lonStr.trim());
                } catch (NumberFormatException e) {
                }
            }
            
            if (schema == null) {
                schema = new Schema();
//                String schemaId = "s" + "_" + doc.getId();
//                schema.withId(schemaId).withName(schemaId);
                for (String fieldName: data.keySet()) {
                    schema.addToSimpleField((new SimpleField()).withName(fieldName).withType("string"));
                }
                doc.addToSchema(schema);
            }
            
            Placemark placemark = doc.createAndAddPlacemark();
            placemark.withName(name);
            placemark.createAndSetPoint().addToCoordinates(lat, lon);
            
            ExtendedData extendedData = new ExtendedData();
            SchemaData schemaData = new SchemaData();
//            schemaData.setSchemaUrl("#" + schema.getId());
/*
            for (String fieldName: data.keySet()) {
                String fieldValue = String.valueOf(data.get(fieldName));
                schemaData.addToSimpleData((new SimpleData(fieldName)).withValue(fieldValue));
            }
*/
            for (Map.Entry<String, Object> entry: data.entrySet()) {
                String fieldValue = String.valueOf(entry.getValue());
                schemaData.addToSimpleData((new SimpleData(entry.getKey())).withValue(fieldValue));
            }
            extendedData.getSchemaData().add(schemaData);
            placemark.withExtendedData(extendedData);
        }
    }
    
    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out) throws IOException {
        boolean isPrettyPrint = false;
        write(datas, out, isPrettyPrint);
    }

    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException {
        write(datas, out, "UTF-8", isPrettyPrint);
    }

    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException {
        write(datas, out, encoding, isPrettyPrint, DEFAULT_OPTIONS);
    }

    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint, Map<String, Object> options) throws IOException {
        Kml kml = write(datas, encoding, options);
        kml.marshal(out);
    }

    private void verifyOptions(Map<String, Object> options) {
        if (MapUtils.isEmpty(options)) {
            String message = String.format("KML 需要欄位參數，%s=顯示名稱、%s=name(座標名稱)、%s=wgs84aX、%s=wgs84aY", Metadata.DISPLAY_NAME,
                    Metadata.DataCfg.KML_NAME_FIELD, Metadata.DataCfg.KML_LAT_FIELD, Metadata.DataCfg.KML_LON_FIELD);
            throw new OpdException(message, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        }
    }
}
