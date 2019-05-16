package com.udn.ntpc.od.core.service.data.out;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface CsvDataWriter extends DataWriter {

    void writeWithoutHeader(List<Map<String, Object>> datas, OutputStream out, String encoding, boolean isPrettyPrint) throws IOException;

}
