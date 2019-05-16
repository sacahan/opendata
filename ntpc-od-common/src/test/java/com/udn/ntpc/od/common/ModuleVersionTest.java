package com.udn.ntpc.od.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class ModuleVersionTest extends AbstractJUnit4TestCase {

    @Value("${module.version}")
    private String moduleVersion;

    @Test
    public void testVersion() {
        checkResult(String.format("找不到版本資訊, %s", "module.version.test"), moduleVersion);
    }

}
