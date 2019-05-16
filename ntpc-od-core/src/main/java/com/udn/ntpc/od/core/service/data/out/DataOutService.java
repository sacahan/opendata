package com.udn.ntpc.od.core.service.data.out;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface DataOutService {

    /**
     * output one page datas to OutputStream
     * @param dataSetOid
     * @param filters
     * @param out
     * @throws java.io.IOException
     */
    void outputXmlData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException;
    String outputXmlData(String dataSetOid, String filters, Pageable p) throws IOException;
    void outputXmlData(List<Map<String, Object>> datas, OutputStream out) throws IOException;
    void outputXmlData(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException;
    void outputXmlData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException;

    /**
     * output one page datas to OutputStream
     * @param dataSetOid
     * @param filters
     * @param out
     * @throws java.io.IOException
     */
    void outputJsonData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException;
    String outputJsonData(String dataSetOid, String filters, Pageable p) throws IOException;
    void outputJsonData(List<Map<String, Object>> datas, OutputStream out) throws IOException;
    void outputJsonData(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException;
    void outputJsonData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException;

    /**
     * output all datas to OutputStream
     * @param dataSetOid
     * @param filters
     * @param out
     * @throws java.io.IOException
     */
    void outputCsvData(String dataSetOid, String filters, OutputStream out) throws IOException;
    /**
     * output one page datas to OutputStream
     * @param dataSetOid
     * @param filters
     * @param out
     * @throws java.io.IOException
     */
    void outputCsvData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException;
    String outputCsvData(String dataSetOid, String filters, Pageable p) throws IOException;
    void outputCsvData(List<Map<String, Object>> datas, OutputStream out) throws IOException;
    void outputCsvData(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException;
    void outputCsvData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException;

    /**
     * output all datas to OutputStream
     * @param dataSetOid
     * @param filters
     * @param out
     * @throws java.io.IOException
     */
    void outputKmlData(String dataSetOid, String filters, OutputStream out) throws IOException;
    /**
     * output one page datas to OutputStream
     * @param dataSetOid
     * @param filters
     * @param out
     * @throws java.io.IOException
     */
    void outputKmlData(String dataSetOid, String filters, Pageable p, OutputStream out) throws IOException;
    String outputKmlData(String dataSetOid, String filters, Pageable p) throws IOException;
    void outputKmlData(String dataSetOid, String filters, Pageable p, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException;
    void outputKmlData(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint, Map<String, Object> options) throws IOException;

}
