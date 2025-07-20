package com.ssafy.tosspay.common.infrastructure.external.toss;

import com.ssafy.tosspay.infrastructure.external.toss.TossPaymentsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossPaymentsClient {

    private final TossPaymentsApiClient tossPaymentsApiClient;

    // 이 클래스는 TossPaymentsApiClient의 wrapper 역할
    // 필요시 추가적인 로직을 여기에 구현
}