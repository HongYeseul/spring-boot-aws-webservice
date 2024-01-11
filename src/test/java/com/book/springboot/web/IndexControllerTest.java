package com.book.springboot.web;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인페이지_로딩() {
        // when
        // 해당 코드는 Application.main 이 돌아가고 있었을 때만 Success가 됨 ... 이면 안될것같은데 ... ㅎㅠ
        // 책에서는 getForObject("/", String.class) 의 값을 가져온다고 되어있는데, 그럴경우 테스트가 404가 떠버린다
        // main이 이미 종료되고 난 뒤에 get을 날리는 것으로 추정 되나 확실한 건 모르겠다...
        String body = this.restTemplate.getForObject("http://localhost:8080/", String.class);

        // then
        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }
}