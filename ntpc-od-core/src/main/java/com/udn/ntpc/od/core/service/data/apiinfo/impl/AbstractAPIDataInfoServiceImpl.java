package com.udn.ntpc.od.core.service.data.apiinfo.impl;

import com.udn.ntpc.od.common.util.FileUtils;
import com.udn.ntpc.od.core.service.data.apiinfo.APIDataInfoService;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey_API_METHOD;
import com.udn.ntpc.od.model.domain.JsonFieldInfoParser;
import com.udn.ntpc.od.model.domain.TableFields;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public abstract class AbstractAPIDataInfoServiceImpl implements APIDataInfoService {

    protected static final Log LOG = LogFactory.getLog(AbstractAPIDataInfoServiceImpl.class);

    @Override
    public Map<String, TableFields> getFieldConfigs(String data) {
        String json = convertToJson(data);
        return getFieldConfigsFromJson(json);
    }

    @Override
    public Map<String, TableFields> getFieldConfigsFromJson(String data) {
        return JsonFieldInfoParser.getRootFieldList(data);
    }

    @Override
    public String apiData(String url, APIKey_API_METHOD method, Object... params) {
        switch (method) {
            case GET:
                return featchAPIData(url, HttpMethod.GET, params);
            case POST:
                return featchAPIData(url, HttpMethod.POST, params);
            default:
                return featchAPIData(url, HttpMethod.GET, params);
        }
    }

    @Override
    public String ungzApiData(String url) throws IOException, URISyntaxException {
        return featchAndUngzAPIData(url);
    }

    protected abstract List<MediaType> getMediaTypes();

/*
    protected String getAPIData(String url, Object... params) {
        HttpHeaders headers = new HttpHeaders();
        setAccept(headers);
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class, params);
        return response.getBody();
    }

    protected String postAPIData(String url, Object... params) {
        HttpHeaders headers = new HttpHeaders();
        setAccept(headers);
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<String>(headers), String.class, params);
        return response.getBody();
    }
*/

    protected String featchAPIData(String url, HttpMethod method, Object... params) {
        HttpHeaders headers = new HttpHeaders();
        setAccept(headers);
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, method, new HttpEntity<String>(headers), String.class, params);
        return response.getBody();
    }

    protected String featchAndUngzAPIData(String url, Object... params) throws IOException, URISyntaxException {
        File tempFile = tempFile = FileUtils.featchFile(URI.create(url).toURL());
        try {
            File dataFile = FileUtils.ungzToFile(tempFile);
            try (InputStream in = IOUtils.buffer(new FileInputStream(dataFile));) {
                return IOUtils.toString(in, StandardCharsets.UTF_8);
            } finally {
                FileUtils.forceDelete(dataFile);
            }
        } finally {
            FileUtils.forceDelete(tempFile);
        }
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(getHttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    /**
     * default ignore SSL Issues
     * @return
     */
    private HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();

            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return clientHttpRequestFactory;
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error(e.getLocalizedMessage(), e);
        }
//        return new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        return new HttpComponentsClientHttpRequestFactory();
    }

    private void setAccept(HttpHeaders headers) {
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        headers.setAccept(getMediaTypes());
        List<MediaType> accept = headers.getAccept();
        accept.add(MediaType.ALL);
        headers.setAccept(accept);
    }

}
