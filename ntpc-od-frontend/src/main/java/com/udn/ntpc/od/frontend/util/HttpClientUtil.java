package com.udn.ntpc.od.frontend.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

@Slf4j
@Service
public class HttpClientUtil {

    @Value("${http.client.connect.timeout}")
    private int HTTP_CLIENT_CONNECT_TIMEOUT;
    @Value("${http.client.read.timeout}")
    private int HTTP_CLIENT_READ_TIMEOUT;
    @Value("${http.client.socket.timeout}")
    private int HTTP_CLIENT_SOCKET_TIMEOUT;
    @Value("${http.client.fail.retry}")
    private int HTTP_CLIENT_FAIL_RETRY;

    public CloseableHttpClient build() {
        // 1. 設定連線時限
        RequestConfig.Builder builder = RequestConfig.custom()
                                                     .setConnectTimeout(HTTP_CLIENT_CONNECT_TIMEOUT)
                                                     .setConnectionRequestTimeout(HTTP_CLIENT_READ_TIMEOUT)
                                                     .setSocketTimeout(HTTP_CLIENT_SOCKET_TIMEOUT);
        // 2. 設定retry次數
        HttpRequestRetryHandler retryHandler;
        if (HTTP_CLIENT_FAIL_RETRY > 0) {
            retryHandler = (exception, executionCount, context) -> {
                // Do not retry if over max retry count
                if (executionCount >= HTTP_CLIENT_FAIL_RETRY) return false;
                // Timeout
                if (exception instanceof InterruptedIOException) return false;
                // Unknown host
                if (exception instanceof UnknownHostException) return false;
                // Connection refused
                if (exception instanceof ConnectTimeoutException) return false;
                // SSL handshake exception
                if (exception instanceof SSLException) return false;

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                // Retry if the request is considered idempotent
                return idempotent;
            };
        } else {
            retryHandler = (exception, executionCount, context) -> false;
        }

        // 3. 自定忽略SSL驗證
        SSLConnectionSocketFactory sslConnectionSocketFactory;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return HttpClients.custom()
                          .setDefaultRequestConfig(builder.build())
                          .setRetryHandler(retryHandler)
                          .setSSLSocketFactory(sslConnectionSocketFactory)
                          .build();
    }

}
