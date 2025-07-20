package com.ssafy.tosspay.common.infrastructure.external.toss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "toss.payments")
@Data
@Component
public class TossPaymentsProperties {
    private String testSecretKey;
    private String testClientKey;
    private String baseUrl;
}