package com.udn.ntpc.od.core.service.data.out.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udn.ntpc.od.core.service.data.out.JsonDataWriter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Service
public class JsonDataWriterImpl extends AbstractDataWriter implements JsonDataWriter {

    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas) throws IOException {
        return write(datas, "UTF-8");
    }
    
    /**
     * @return net.sf.json.JSONArray
     */
    @SuppressWarnings("unchecked")
    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas, String encoding) throws IOException {
        JSONArray json = new JSONArray();
        for (Map<String, Object> data : datas) {
            // 直接將 map 丟給 JSONObject 處理就可以了
            JSONObject row = new JSONObject();
            row.accumulateAll(data);
            json.add(row);
        }
        return (O) json;
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
//        JSONArray json = write(datas);
        
//      out.write(WebUtils.toString(json).getBytes());
//      out.write(json.toString().getBytes());
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(out, datas);
    }

}
