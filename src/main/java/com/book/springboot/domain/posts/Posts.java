package com.book.springboot.domain.posts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // lombok: 자동 생성, setter 메서드는 절대 만들지 않음
@NoArgsConstructor // lombok: 기본 생성자 자동 추가
@Entity // JPA: 테이블과 링크될 클래스임을 나타냄. 카멜케이스를 언더스코어 네이밍으로 테이블 이름 매칭(SalesManager > sales_manager)
public class Posts {

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue( strategy = GenerationType.IDENTITY) // PK 생성 규칙, GenerationType.IDENTITY 를 사용하여야 auto increment
    private Long id;

    @Column(length = 500, nullable = false) // 선언하지 않을 때 기본 값은 VARCHAR(255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // 빌더 패턴 클래스 자동 생성
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }
    /**
     * 빌더 패턴 이용시
     * 생성자를 사용한 new Example(a, b)를 사용하기 보다
     * Example.builder()
     *      .a(a)
     *      .b(b)
     *      .build();
     * 로 사용 가능하여 어느 필드에 어떤 값을 채워야 할지 명확하게 인지할 수 있다.
     */
}