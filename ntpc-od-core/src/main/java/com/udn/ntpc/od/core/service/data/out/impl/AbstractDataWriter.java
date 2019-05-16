package com.udn.ntpc.od.core.service.data.out.impl;

import com.udn.ntpc.od.core.service.data.out.DataWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public abstract class AbstractDataWriter implements DataWriter {

    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas, String encoding, Map<String, Object> options) throws IOException {
        // TODO Auto-generated method stub
        throw new IOException("尚未實作");
    }
    
    @Override
    public void write(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint, Map<String, Object> options) throws IOException {
        // TODO Auto-generated method stub
        throw new IOException("尚未實作");
    }

}
