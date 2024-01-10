package com.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    // DB Layer 접근자
    // JpaRepository<Entity 클래스, PK 타입>을 상속하면 기본적인 CRUD 메소드가 자동으로 생성된다.
    // 중요! Entity 클래스와 기본 Entity Repository 는 함께 위치해야한다.
}
