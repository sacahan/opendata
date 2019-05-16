package com.udn.ntpc.od.core.service.data.out.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.core.service.data.DataService;
import com.udn.ntpc.od.core.service.data.out.DataOutService;
import com.udn.ntpc.od.core.service.data.out.JsonDataWriter;
import com.udn.ntpc.od.core.service.data.out.KmlDataWriter;
import com.udn.ntpc.od.core.service.data.out.XmlDataWriter;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.common.Metadata;
import com.udn.ntpc.od.model.domain.DataPage;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DataOutServiceImpl implements DataOutService {
	protected static final Log LOG = LogFactory.getLog(DataOutServiceImpl.class);

    @Value("${batch.size.data.out.csv:100}")
    private int batchSizeCsv;
    
    @Value("${batch.size.data.out.kml:100}")
    private int batchSizeKml;
    
	@Autowired
	private XmlDataWriter xmlDataWriter;
	
	@Autowired
	private JsonDataWriter jsonDataWriter;
	
	@Autowired
	private CsvDataWriterImpl csvDataWriter;
	
	@Autowired
	private KmlDataWriter kmlDataWriter;

    @Autowired
    private DataCfgService dataCfgService;

    @Autowired
    private DataService dataService;

    @Override
    public void outputXmlData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException {
        Page<Map<String, Object>> datas = dataService.queryByDataSetOid(dataSetOid, filters, p);
        outputXmlData(datas.getContent(), out);
    }

    @Override
    public String outputXmlData(String dataSetOid, String filters, Pageable p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputXmlData(dataSetOid, filters, p, out);
        return new String(out.toByteArray(), StandardCharsets.UTF_8.name());
    }

    @Override
    public void outputXmlData(List<Map<String, Object>> datas, OutputStream out) throws IOException {
        boolean isPrettyPrint = false;
        outputXmlData(datas, out, isPrettyPrint);
    }

    @Override
    public void outputXmlData(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException {
        outputXmlData(datas, out, StandardCharsets.UTF_8.name(), isPrettyPrint);
    }

    @Override
    public void outputXmlData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException {
        xmlDataWriter.write(datas, out, encoding, isPrettyPrint);
    }

    @Override
    public void outputJsonData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException {
        Page<Map<String, Object>> datas = dataService.queryByDataSetOid(dataSetOid, filters, p);
        outputJsonData(datas.getContent(), out);
    }

    @Override
    public String outputJsonData(String dataSetOid, String filters, Pageable p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputJsonData(dataSetOid, filters, p, out);
        return new String(out.toByteArray(), StandardCharsets.UTF_8.name());
    }
    
    @Override
    public void outputJsonData(List<Map<String, Object>> datas, OutputStream out) throws IOException {
        boolean isPrettyPrint = false;
        outputJsonData(datas, out, isPrettyPrint);
    }

    @Override
    public void outputJsonData(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException {
        outputJsonData(datas, out, StandardCharsets.UTF_8.name(), isPrettyPrint);
    }
    
    @Override
    public void outputJsonData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException {
        jsonDataWriter.write(datas, out, encoding, isPrettyPrint);
    }
    
    @Override
    public void outputCsvData(String dataSetOid, String filters, OutputStream out) throws IOException {
        String encoding = StandardCharsets.UTF_8.name();
        boolean isPrettyPrint = true;
        boolean isAppend = true;
//        Pageable p = DEFAULT_SETTINGS.DEFAULT_DATA_PAGING;
        Pageable p = PageRequest.of(0, batchSizeCsv);
        DataPage<Map<String, Object>> datas = dataService.queryByDataSetOid(dataSetOid, filters, p);
        if (datas.hasContent()) {
            // Persist content to a temp file to prevent out of memory.
            File tmpFile = File.createTempFile(dataSetOid, ".csv.tmp");
            LOG.debug("Temp file: " + tmpFile.getAbsolutePath());
            try {
                while (datas.hasContent() && (datas.hasNext() || datas.isLast())) {
                    try (OutputStream tmpFileOut = IOUtils.buffer(new FileOutputStream(tmpFile, isAppend));) {
                        if (datas.isFirst()) {
                            csvDataWriter.write(datas.getContent(), tmpFileOut, encoding, isPrettyPrint); // with header
                        } else {
                            csvDataWriter.writeWithoutHeader(datas.getContent(), tmpFileOut, encoding, isPrettyPrint);
                        }
                        if (datas.isLast()) {
                            break;
                        }
                        datas = dataService.queryByDataSetOid(dataSetOid, filters, datas.nextPageable());
                    }
                }

                try (InputStream tmpFileIn = IOUtils.buffer(new FileInputStream(tmpFile));) {
                    IOUtils.copy(tmpFileIn, out);
                }
            } finally {
                FileUtils.deleteQuietly(tmpFile);
            }
        }
    }
    
    @Override
    public void outputCsvData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException {
        Page<Map<String, Object>> datas = dataService.queryByDataSetOid(dataSetOid, filters, p);
        outputCsvData(datas.getContent(), out);
    }

    @Override
    public String outputCsvData(String dataSetOid, String filters, Pageable p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputCsvData(dataSetOid, filters, p, out);
        return new String(out.toByteArray(), StandardCharsets.UTF_8.name());
    }
    
    @Override
    public void outputCsvData(List<Map<String, Object>> datas, OutputStream out) throws IOException {
        boolean isPrettyPrint = false;
        outputCsvData(datas, out, isPrettyPrint);
    }
	
    @Override
    public void outputCsvData(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException {
        outputCsvData(datas, out, StandardCharsets.UTF_8.name(), isPrettyPrint);
    }
    
    @Override
    public void outputCsvData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException {
        csvDataWriter.write(datas, out, encoding, isPrettyPrint);
    }
    
    @Override
    public void outputKmlData(String dataSetOid, String filters, OutputStream out) throws IOException {
//        Pageable p = DEFAULT_SETTINGS.DEFAULT_DATA_PAGING;
        Pageable p = PageRequest.of(0, batchSizeKml);
        DataPage<Map<String, Object>> datas = dataService.queryByDataSetOid(dataSetOid, filters, p);
        if (datas.hasContent()) {
            Map<String, Object> options = genKmlOptions(dataSetOid);
            
            String displayName = (String) options.get(Metadata.DISPLAY_NAME);
            
            final Kml kml = new Kml();
            Document doc = kml.createAndSetDocument().withName(displayName).withOpen(Boolean.TRUE);
            doc.setId(dataSetOid);
            
            while (datas.hasContent() && (datas.hasNext() || datas.isLast())) {
                kmlDataWriter.write(doc, datas.getContent(), options);
                if (datas.isLast())
                    break;
                datas = dataService.queryByDataSetOid(dataSetOid, filters, datas.nextPageable());
            }
            kml.marshal(out);
        }
    }
    
    @Override
    public void outputKmlData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException {
        boolean isPrettyPrint = false;
        outputKmlData(dataSetOid, filters, p, out, StandardCharsets.UTF_8.name(), isPrettyPrint);
    }

    @Override
    public String outputKmlData(String dataSetOid, String filters, Pageable p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputKmlData(dataSetOid, filters, p, out);
        return new String(out.toByteArray(), StandardCharsets.UTF_8.name());
    }
    
    @Override
    public void outputKmlData(String dataSetOid, String filters, Pageable p, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException {
        Map<String, Object> options = genKmlOptions(dataSetOid);
        
        DataPage<Map<String, Object>> results = dataService.queryByDataSetOid(dataSetOid, filters, p);
        if (results.hasContent())
            outputKmlData(results.getContent(), out, encoding, isPrettyPrint, options);
    }

    @Override
    public void outputKmlData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint, Map<String, Object> options) throws IOException {
        kmlDataWriter.write(datas, out, encoding, isPrettyPrint, options);
    }

    private Map<String, Object> genKmlOptions(String dataSetOid) {
        DataCfg dataCfg = dataCfgService.findByDataSetOid(dataSetOid);
        
        String nameKey = Metadata.getDataCfgMetaValue(Metadata.DataCfg.KML_NAME_FIELD, dataCfg);
        String latKey = Metadata.getDataCfgMetaValue(Metadata.DataCfg.KML_LAT_FIELD, dataCfg);
        String lonKey = Metadata.getDataCfgMetaValue(Metadata.DataCfg.KML_LON_FIELD, dataCfg);

        if (StringUtils.isBlank(nameKey))
            throw new OpdException(Metadata.DataCfg.KML_NAME_FIELD, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        if (StringUtils.isBlank(latKey))
            throw new OpdException(Metadata.DataCfg.KML_LAT_FIELD, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        if (StringUtils.isBlank(lonKey))
            throw new OpdException(Metadata.DataCfg.KML_LON_FIELD, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        
        Map<String, Object> options = new LinkedHashMap<>();
        options.put(Metadata.DATA_SET_OID, dataSetOid);
        options.put(Metadata.DISPLAY_NAME, dataCfg.getName());
        options.put(Metadata.DataCfg.KML_NAME_FIELD, nameKey);
        options.put(Metadata.DataCfg.KML_LAT_FIELD, latKey);
        options.put(Metadata.DataCfg.KML_LON_FIELD, lonKey);

        return options;
    }
}
