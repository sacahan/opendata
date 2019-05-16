package com.udn.ntpc.od.common;

import com.udn.ntpc.od.test.AbstractTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonApplication.class})
@ContextConfiguration(locations={
//        "classpath*:spring-beans-config.xml",
})
@Slf4j
public abstract class AbstractJUnit4TestCase extends AbstractTestCase {

}
