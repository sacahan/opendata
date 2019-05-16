package com.udn.ntpc.od.core.service.data.out.impl;

import com.opencsv.CSVWriter;
import com.udn.ntpc.od.core.service.data.out.CsvDataWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CsvDataWriterImpl extends AbstractCsvDataWriter implements CsvDataWriter {

    private static final String UTF8_BOM = "\uFEFF";
    
    /**
     * @return List<List<String>>, first List is header, default encoding UTF-8
     */
    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas) throws IOException {
        return write(datas, StandardCharsets.UTF_8.displayName());
    }
    
    /**
     * @return List<List<String>>, first List is header
     */
    @SuppressWarnings("unchecked")
    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas, String encoding) throws IOException {
        boolean isIncludeHeader = true;
        return write(datas, isIncludeHeader);
    }
    
    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out) throws IOException {
        boolean isPrettyPrint = false;
        write(datas, out, isPrettyPrint);
    }

    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException {
        write(datas, out, StandardCharsets.UTF_8.displayName(), isPrettyPrint);
    }
    
    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException {
        List<List<String>> csv = write(datas, encoding);
        if (CollectionUtils.isNotEmpty(csv) && 
                (StringUtils.equalsIgnoreCase(StandardCharsets.UTF_8.displayName(), encoding) || StringUtils.equalsIgnoreCase("UTF8", encoding))) {
            out.write(UTF8_BOM.getBytes(StandardCharsets.UTF_8));
        }
        writeToStream(csv, out, encoding);
    }

    @Override
    public void writeWithoutHeader(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException {
        boolean isIncludeHeader = false;
        List<List<String>> csv = write(datas, isIncludeHeader);
        writeToStream(csv, out, encoding);
    }
 
    /**
     * @return List<List<String>>
     */
    @SuppressWarnings("unchecked")
    private <O extends Object> O write(List<Map<String, Object>> datas, boolean isIncludeHeader) {
        List<List<String>> csv = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(datas)) {
            if (isIncludeHeader) {
                // 標頭
                Set<String> firstRow = datas.get(0).keySet();
                List<String> header = new ArrayList<>();
                for (String key : firstRow) {
                    header.add(key);
                }
                csv.add(header);
            }
            // 內容
            for (Map<String, Object> data : datas) {
                List<String> row = new ArrayList<>();
                for (Object value : data.values()) {
                    if (value != null) {
                        row.add(value.toString());
                    } else {
                        row.add("");
                    }
                }
                csv.add(row);
            }
        }
        return (O) csv;
    }
    
    private void writeToStream(List<List<String>> csv, OutputStream outputStream, String encoding) throws IOException {
        if (CollectionUtils.isNotEmpty(csv)) {
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream, encoding));
            try {
                for (List<String> row: csv)
                    writer.writeNext(row.toArray(new String[0]));
            } finally {
                writer.close();
            }
        }
    }
}
