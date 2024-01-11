package com.book.springboot.service.posts;

import com.book.springboot.domain.posts.Posts;
import com.book.springboot.domain.posts.PostsRepository;
import com.book.springboot.web.dto.PostsResponseDto;
import com.book.springboot.web.dto.PostsSaveRequestDto;
import com.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        /**
         * 해당 메서드에서 update 쿼리를 날리는 부분이 없는데, Entity 가 영속성 컨텍스트에 포함되어 있기 때문에
         * 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영한다.
         */
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        return new PostsResponseDto(entity);
    }
}