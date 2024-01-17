package com.book.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA Auditing 활성화
public class JavaConfig {
    /**
     * @EnableJavaAuditing 을 사용하기 위해서는 최소 하나의 @Entity 클래스가 필요하다.
     * 그런데 @WebMvcTest 의 경우 당연히 사용하지 않게된다.
     * 기존에는 @EnableJavaAuditing 가 Application.java(@SpringBootApplication 과 함께 있음) 에 들어있다보니
     * HelloControllerTest 에서 @WebMvcTest 를 할 경우 스캔을 같이 해버리므로 이로 인한 오류가 생기지 않도록 둘을 분리했다.
     */
}
