package com.udn.ntpc.od.common.util;

import com.udn.ntpc.od.common.AbstractJUnit4TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FileUtilsTests extends AbstractJUnit4TestCase {

    @Test
    public void featchFile() throws IOException, RestClientException, URISyntaxException {
        // test.gz
        File tempFile = FileUtils.featchFile(URI.create("https://drive.google.com/uc?export=download&id=1k-cIx9V7xG40NmBEZIKDHr_Q3X_5rt6N").toURL());
        try {
            Assert.assertTrue("下載檔案失敗", tempFile.length() == 1979);
        } finally {
            FileUtils.forceDelete(tempFile);
        }
    }

	@Test
	public void ungzToFile() throws URISyntaxException, IOException {
	    URL testGZ = this.getClass().getResource("/test.gz");
	    File gz = new File(testGZ.toURI());
	    File ungzFile = FileUtils.ungzToFile(gz);
        try {
            Assert.assertTrue("GZ 解壓縮失敗", ungzFile.length() == 21028);
        } finally {
            FileUtils.forceDelete(ungzFile);
        }
	}
    
}
