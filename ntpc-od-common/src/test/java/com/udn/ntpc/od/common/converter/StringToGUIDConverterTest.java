package com.udn.ntpc.od.common.converter;

import com.udn.ntpc.od.common.AbstractJUnit4TestCase;
import com.udn.ntpc.od.common.util.GUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StringToGUIDConverterTest extends AbstractJUnit4TestCase {

    @Autowired
    private StringToGUIDObjectConverter converter;

    @Test
    public void testConvert() {
        final String expected = "000937EB-D195-4FCC-BC4E-14C5B1A70FB4";

        final String oid = "000937eb-d195-4fcc-bc4e-14c5b1a70fb4";
        GUID actual = converter.convert(oid);

        checkResult(String.format("無法轉換成 GUID, %s", oid), actual);

        Assert.assertEquals(expected, actual.toString());
    }

}
