package com.udn.ntpc.od.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.StringJoiner;

@Slf4j
public abstract class AbstractTestCase {

    protected void checkResult(Object result) {
        checkResult("查無資料", result);
    }

    protected void checkResult(String message, Object result) {
        Assert.assertNotNull(message, result);
        log.info(result.toString());
    }

    protected void checkResult(Page<?> result) {
        checkResult("查無資料", result.hasContent());
    }

    protected void checkResult(String message, Page<?> result) {
        Assert.assertTrue(message, result.hasContent());
        checkResult(result.getContent());
    }

    protected void checkResultWithoutDetails(Collection<?> result) {
        Assert.assertTrue("查無資料", CollectionUtils.isNotEmpty(result));
        log.info("共有 " + result.size() + " 筆");
    }

    protected void checkResult(Collection<?> result) {
        checkResultWithoutDetails(result);
        StringJoiner joiner = new StringJoiner(String.format("%n"));
        result.forEach(r -> joiner.add(r.toString()));
        log.info(joiner.toString());
    }

}
