package vn.tungnt.research.studyspringretry.config;

import lombok.Data;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author java dev be team on 2019-04-14
 * @project study-spring-retry
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "http")
public class HttpRESTConnectionPoolConfig {

    private Integer maxPerRoute;
    private Integer maxTotal;
    private Integer connectionRequestTimeout;
    private Integer connectTimeout;
    private Integer socketTimeout;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setDefaultMaxPerRoute(this.getMaxPerRoute());
        result.setMaxTotal(this.getMaxTotal());
        return result;
    }

    @Bean
    public RequestConfig requestConfig() {
        RequestConfig result = RequestConfig.custom()
                .setConnectionRequestTimeout(this.getConnectionRequestTimeout())
                .setConnectTimeout(this.getConnectTimeout())
                .setSocketTimeout(this.getSocketTimeout())
                .build();
        return result;
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
                                          RequestConfig requestConfig) {
        CloseableHttpClient result = HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        return result;
    }

    @Bean
    public RestTemplate restClient(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new
                HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setHttpClient(httpClient);
        return new RestTemplate(httpRequestFactory);
    }
}

    