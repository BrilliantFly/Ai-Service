package com.know.knowboot.config;

import com.know.knowboot.properties.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ElasticsearchConfiguration  {

    @Resource
    private ElasticsearchProperties elasticsearchProperties;

    @Bean(destroyMethod = "close", name = "client")
    public RestHighLevelClient restHighLevelClient() {

        //如果没配置密码就可以不用下面这两部
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                //如果是集群再配置多个
                RestClient.builder(new HttpHost(elasticsearchProperties.getHost(), elasticsearchProperties.getPort(), "http"))
//                        .setHttpClientConfigCallback(httpClientBuilder -> {
//                            httpClientBuilder.disableAuthCaching();
//                            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
//                        })

        );

        return restHighLevelClient;
    }

}
