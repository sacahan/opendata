package com.udn.ntpc.od.core.service.data.out.impl;

import com.udn.ntpc.od.core.service.data.out.XmlDataWriter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Service
public class XmlDataWriterImpl extends AbstractDataWriter implements XmlDataWriter {

    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas) throws IOException {
        return write(datas, "UTF-8");
    }
    
    /**
     * @return org.dom4j.Document
     */
    @SuppressWarnings("unchecked")
    @Override
    public <O extends Object> O write(List<Map<String, Object>> datas, String encoding) throws IOException {
        Document xml = DocumentHelper.createDocument();
        xml.setXMLEncoding(encoding);
        Element elementData = xml.addElement("data");
        
        for (Map<String, Object> data: datas) {
            Element row = elementData.addElement("row");
            
            String[] keys = data.keySet().toArray(new String[0]);
            
            for (String key : keys) {
                Element value = row.addElement(key);
                if (data.get(key) != null) {
                    value.addText(data.get(key).toString());
                } else {
                    value.addText("");
                }
            }
        }

        return (O) xml;
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
        Document xml = write(datas);
        
        OutputFormat format = outputFormat(isPrettyPrint);
        XMLWriter writer = new XMLWriter(out, format);
        try {
            writer.write(xml);
        } finally {
            writer.close();
        }
    }

    private OutputFormat outputFormat(boolean prettyPrint) {
        if (prettyPrint) {
            return OutputFormat.createPrettyPrint();
        }
        return OutputFormat.createCompactFormat();
    }

}
