package org.ksm;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Data
@Slf4j
public class RestApiClient implements Serializable {
    private int conTimeout;
    private int readTimeout;
    private String charset;
    private Map<String, String> headers;
    private Map<String, String> queryParameters;

    public RestApiClient(int conTimeout, int readTimeout, String charset, Map<String, String> headers
            , Map<String, String> queryParameters) {
        this.conTimeout = conTimeout;
        this.readTimeout = readTimeout;

        this.headers = headers;
        if (StringUtils.isBlank(charset)) {
            this.charset = "UTF-8";
        } else {
            this.charset = charset;
        }
        this.queryParameters = queryParameters;
    }

    public void addHeaders(HttpUriRequest req) {
        if (Objects.nonNull(headers)) {
            Set<String> strings = headers.keySet();
            for (String key : strings) {
                req.setHeader(key, headers.getOrDefault(key, ""));
            }
        }
    }

    public String processGETRequest(String url) throws URISyntaxException {
        log.info("executing get request");
        HttpGet req = new HttpGet(url);
        addHeaders(req);
        URIBuilder uriBuilder = new URIBuilder(req.getURI());
        if (Objects.nonNull(queryParameters)) {
            for (Map.Entry<String, String> entries : queryParameters.entrySet()) {
                uriBuilder.addParameter(entries.getKey(), entries.getValue());
            }
        }
        URI uri = uriBuilder
                .build();
        ((HttpRequestBase) req).setURI(uri);
        return getRequestData(req);
    }

    public String processPOSTRequest(String url, String body) throws UnsupportedEncodingException {
        HttpPost req = new HttpPost(url);
        log.info("executing post request");
        addHeaders(req);
        if (body != null && !body.isEmpty()) {
            try {
                req.setEntity(new StringEntity(body));
            } catch (UnsupportedEncodingException e) {
                log.error("Exception caught : {}", e.toString());
                throw e;
            }
        }
        return getRequestData(req);
    }

    public String getRequestData(HttpUriRequest request) {
        HttpResponse response;
        String content = "";
        try {
            TrustStrategy trustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };

           /**
            * this can be used to use the jks file
            * load keystore and create custom sslcontext and use in ssl factory
           KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); //default is jks type
            keyStore.load(new FileInputStream("jks file path"), "password to key store".toCharArray()));
            SSLContexts.custom().
                    loadKeyMaterial(keyStore, "password to key store".toCharArray()).
                    loadTrustMaterial(null, trustStrategy).build();
            */

            SSLContext sslContext = SSLContextBuilder.
                    create().
                    loadTrustMaterial(null, trustStrategy).build();

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(conTimeout)
                    .setConnectionRequestTimeout(readTimeout)
                    .build();
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            httpClientBuilder.setDefaultRequestConfig(requestConfig);
            httpClientBuilder.setSSLSocketFactory(socketFactory);

            //this is for proxy settings
            httpClientBuilder.useSystemProperties();
            HttpClient httpClient = httpClientBuilder.build();

            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                content = IOUtils.toString(httpEntity.getContent(), charset);
            } else {
                log.error("Failed with return Code : " + response.getStatusLine().getStatusCode());
                throw new RuntimeException("API returned non success code " +
                        response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("exception caught : {}", e.toString());

        }
        return content;
    }
}
