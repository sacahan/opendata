package com.udn.ntpc.od.core.service.data.out;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface DataWriter {

    <O extends Object> O write(List<Map<String, Object>> datas) throws IOException;
    
    <O extends Object> O write(List<Map<String, Object>> datas, String encoding) throws IOException;
    
    <O extends Object> O write(List<Map<String, Object>> datas, String encoding, Map<String, Object> options) throws IOException;
    
    void write(List<Map<String, Object>> datas, OutputStream out) throws IOException;
    
    void write(List<Map<String, Object>> datas, OutputStream out, boolean isPrettyPrint) throws IOException;
    
    void write(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException;
    
    void write(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint, Map<String, Object> options) throws IOException;
    
}
