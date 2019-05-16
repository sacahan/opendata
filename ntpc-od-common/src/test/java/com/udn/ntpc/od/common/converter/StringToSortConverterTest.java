package com.udn.ntpc.od.common.converter;

import com.udn.ntpc.od.common.AbstractJUnit4TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

@Slf4j
public class StringToSortConverterTest extends AbstractJUnit4TestCase {

    @Autowired
    private StringToSortObjectConverter converter;

    @Test
    public void testConvert() {
        Sort expected = Sort.by(new Sort.Order(Sort.Direction.DESC, "field1"),
                                new Sort.Order(Sort.Direction.ASC, "field2"));

        final String parameters = "field1:desc,field2:asc";
        Sort actual = converter.convert(parameters);

        checkResult(String.format("無法轉換成 Sort, %s", parameters), actual);

        Assert.assertEquals(expected, actual);
    }

}
