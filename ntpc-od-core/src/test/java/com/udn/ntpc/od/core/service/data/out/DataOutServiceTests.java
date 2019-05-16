package com.udn.ntpc.od.core.service.data.out;

import com.udn.ntpc.od.common.util.FileUtils;
import com.udn.ntpc.od.core.AbstractJUnit4TestCase;
import com.udn.ntpc.od.core.service.data.DataService;
import com.udn.ntpc.od.model.cfg.domain.DataCfgZipFile;
import com.udn.ntpc.od.common.variables.DEFAULT_SETTINGS;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

@Slf4j
public class DataOutServiceTests extends AbstractJUnit4TestCase {

    @Autowired
    private DataService dataService;

    @Autowired
    private DataOutService service;

    private static final String DATA_SET_OID = "D3585300-565D-4AA2-897F-0127D7A1834E"; // 新北市路燈資料
    private static final String FILTERS = "_id eq 2";

    @Test
    public void outputJson() throws IOException {
        final String expected = "[{\"district\":\"板橋區\",\"no\":\"000002\",\"oldNo\":\"000002\",\"address\":\"板橋區柏翠里雙十路三段50號\",\"TWD97X\":\"297522.012\",\"TWD97Y\":\"2769332.143\"}]";
        final String actual = service.outputJsonData(DATA_SET_OID, "_id eq 2", DEFAULT_SETTINGS.DEFAULT_DATA_PAGING);
        Assert.assertEquals(expected,actual);
        log.debug(actual);
    }

    @Test
    public void outputXml() throws IOException {
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<data><row><district>板橋區</district><no>000002</no><oldNo>000002</oldNo><address>板橋區柏翠里雙十路三段50號</address><TWD97X>297522.012</TWD97X><TWD97Y>2769332.143</TWD97Y></row></data>";
        final String actual = service.outputXmlData(DATA_SET_OID, "_id eq 2", DEFAULT_SETTINGS.DEFAULT_DATA_PAGING);
        Assert.assertEquals(expected,actual);
        log.debug(actual);
    }

    @Test
    public void outputCsv() throws IOException {
        final String expected = "\uFEFF\"district\",\"no\",\"oldNo\",\"address\",\"TWD97X\",\"TWD97Y\"\n" +
                                "\"板橋區\",\"000002\",\"000002\",\"板橋區柏翠里雙十路三段50號\",\"297522.012\",\"2769332.143\"\n";
        final String actual = service.outputCsvData(DATA_SET_OID, "_id eq 2", DEFAULT_SETTINGS.DEFAULT_DATA_PAGING);
        Assert.assertEquals(expected,actual);
        log.debug(actual);
    }

    @Test
    public void outputKml() throws IOException {
        final String dataSetOid = "64025643-34BF-489D-AC5C-13B90DDD7629"; // 新北市立博物館群
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:kml xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:ns2=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n" +
                "    <ns2:Document id=\"64025643-34BF-489D-AC5C-13B90DDD7629\">\n" +
                "        <ns2:name>新北市立博物館群</ns2:name>\n" +
                "        <ns2:open>1</ns2:open>\n" +
                "        <ns2:Schema>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"title\"/>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"address\"/>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"tel\"/>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"fax\"/>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"twd97X\"/>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"twd97Y\"/>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"wgs84aX\"/>\n" +
                "            <ns2:SimpleField type=\"string\" name=\"wgs84aY\"/>\n" +
                "        </ns2:Schema>\n" +
                "        <ns2:Placemark>\n" +
                "            <ns2:name>新北市立鶯歌陶瓷博物館</ns2:name>\n" +
                "            <ns2:ExtendedData>\n" +
                "                <ns2:SchemaData>\n" +
                "                    <ns2:SimpleData name=\"title\">新北市立鶯歌陶瓷博物館</ns2:SimpleData>\n" +
                "                    <ns2:SimpleData name=\"address\">新北市鶯歌區文化路 200 號</ns2:SimpleData>\n" +
                "                    <ns2:SimpleData name=\"tel\">86772727</ns2:SimpleData>\n" +
                "                    <ns2:SimpleData name=\"fax\">86774104</ns2:SimpleData>\n" +
                "                    <ns2:SimpleData name=\"twd97X\">285567.23</ns2:SimpleData>\n" +
                "                    <ns2:SimpleData name=\"twd97Y\">2760211.19</ns2:SimpleData>\n" +
                "                    <ns2:SimpleData name=\"wgs84aX\">121.352214387446</ns2:SimpleData>\n" +
                "                    <ns2:SimpleData name=\"wgs84aY\">24.9493281566843</ns2:SimpleData>\n" +
                "                </ns2:SchemaData>\n" +
                "            </ns2:ExtendedData>\n" +
                "            <ns2:Point>\n" +
                "                <ns2:coordinates>121.352214387446,24.9493281566843</ns2:coordinates>\n" +
                "            </ns2:Point>\n" +
                "        </ns2:Placemark>\n" +
                "    </ns2:Document>\n" +
                "</ns2:kml>\n";
        final String actual = service.outputKmlData(dataSetOid, "_id eq 2", DEFAULT_SETTINGS.DEFAULT_DATA_PAGING);
        Assert.assertEquals(expected,actual);
        log.debug(actual);
    }

    @Test
    public void outputCsvZipFile() throws IOException {
        String filters = null;
        DataCfgZipFile zipFile = dataService.exportCsvZipFile(DATA_SET_OID, filters);
        Assert.assertNotNull(zipFile);
        String zipFileOid = zipFile.getOid();
        log.info("New zip file: " + zipFileOid);
        String md5Expected = getZipEntryMd5(zipFile);
        log.info("New zip file md5: " + md5Expected);

        zipFile = dataService.exportCsvZipFile(DATA_SET_OID, filters);
        Assert.assertNotNull(zipFile);
//        Assert.assertTrue(StringUtils.equals(zipFileOid, zipFile.getOid()));
        log.info("Update zip file: " + zipFile.getOid());
        String md5Actual = getZipEntryMd5(zipFile);
        log.info("Update zip file md5: " + md5Actual);
        Assert.assertEquals(md5Expected, md5Actual);
    }

    private String getZipEntryMd5(DataCfgZipFile zipFile) {
        File compressedFile = FileUtils.writeByteArrayToFile(zipFile.getContentFile());
        try {
            return FileUtils.zipEntryMd5Hex(compressedFile);
        } finally {
            FileUtils.deleteQuietly(compressedFile);
        }
    }

}
