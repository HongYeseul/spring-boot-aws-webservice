package com.book.springboot.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 자동 인식하도록 함
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity 클래스에 Auditing 기능을 포함시킴
public class BaseTimeEntity { // 모든 entity의 상위에서 entity 들의 createdDate, modifiedDate 를 자동으로 관리

    @CreatedDate // 생성되어 저장될 때 시간 자동 저장
    private LocalDateTime createdDate;

    @LastModifiedDate // 조회한 엔티티의 값을 변경할 때 시간 자동 저장
    private LocalDateTime modifiedDate;
}
