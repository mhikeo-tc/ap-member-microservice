package com.appirio.service.member.util;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.google.common.base.Supplier;
import com.google.gson.GsonBuilder;

public class ESClient {

    private static final String SERVICE = "es";
    private static final String REGION = "us-east-1";

  public static JestClient get(String esUri) {
    DefaultAWSCredentialsProviderChain awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();
    final Supplier<LocalDateTime> clock = () -> LocalDateTime.now(ZoneOffset.UTC);
    final AWSSigner awsSigner = new AWSSigner(awsCredentialsProvider, REGION, SERVICE, clock);
    final AWSSigningRequestInterceptor requestInterceptor = new AWSSigningRequestInterceptor(awsSigner);

    final JestClientFactory factory = new JestClientFactory() {
        @Override
        protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
            builder.addInterceptorLast(requestInterceptor);
            return builder;
        }

        @Override
        protected HttpAsyncClientBuilder configureHttpClient(HttpAsyncClientBuilder builder) {
            builder.addInterceptorLast(requestInterceptor);
            return builder;
        }
    };


    factory.setHttpClientConfig(new HttpClientConfig.Builder(esUri)
            .multiThreaded(true)
//    	            .connTimeout(connTimeout)
//    	            .readTimeout(readTimeout)
            .maxTotalConnection(50)
            .discoveryEnabled(false)
            .gson(new GsonBuilder().create())
            .build());

    return factory.getObject();
  }
}
