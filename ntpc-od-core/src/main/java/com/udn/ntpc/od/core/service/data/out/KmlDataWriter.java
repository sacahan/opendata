package com.udn.ntpc.od.core.service.data.out;

import de.micromata.opengis.kml.v_2_2_0.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface KmlDataWriter extends DataWriter {

    void write(Document doc, List<Map<String, Object>> datas, Map<String, Object> options) throws IOException;

}
