package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Board;
import com.haedal.haedalweb.domain.Post;
import com.haedal.haedalweb.domain.PostType;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.dto.request.CreatePostDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;
    private final UserService userService;

    @Transactional
    public void createPost(Long boardId, CreatePostDTO createPostDTO) { // createPost 리팩토링 해야함.
        Board board = boardService.findBoardById(boardId);
        PostType postType;

        try {
            postType = PostType.valueOf(createPostDTO.getPostType());
            if (postType != PostType.ACTIVITY) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND_POST_TYPE);
        }

        LocalDate activityDate = LocalDate.parse(createPostDTO.getPostActivityDate(), DateTimeFormatter.ISO_DATE);
        LocalDateTime createDate = LocalDateTime.now();
        User creator = userService.getLoggedInUser();

        Post post = Post.builder()
                .title(createPostDTO.getPostTitle())
                .content(createPostDTO.getPostContent())
                .imageUrl(createPostDTO.getPostImageUrl()) // 이미지 생성 Controller 만들기
                .views(0L)
                .postType(postType)
                .activityDate(activityDate)
                .createDate(createDate)
                .user(creator)
                .board(board) // 만약 boardId를 안 받았으면, 공지사항 or 이베트 게시글임
                .build();

        postRepository.save(post);
    }

    @Transactional
    public void createPost(CreatePostDTO createPostDTO) {
        PostType postType;

        try {
            postType = PostType.valueOf(createPostDTO.getPostType());
            if (postType != PostType.NOTICE && postType != PostType.EVENT)
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND_POST_TYPE);
        }

        LocalDate activityDate = LocalDate.parse(createPostDTO.getPostActivityDate(), DateTimeFormatter.ISO_DATE);
        LocalDateTime createDate = LocalDateTime.now();
        User creator = userService.getLoggedInUser();

        Post post = Post.builder()
                .title(createPostDTO.getPostTitle())
                .content(createPostDTO.getPostContent())
                .imageUrl(createPostDTO.getPostImageUrl()) // 이미지 생성 Controller 만들기
                .views(0L)
                .postType(postType)
                .activityDate(activityDate)
                .createDate(createDate)
                .user(creator)
                .build();

        postRepository.save(post);
    }
}
