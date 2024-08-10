package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Board;
import com.haedal.haedalweb.domain.Post;
import com.haedal.haedalweb.domain.PostType;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.dto.request.CreatePostDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.BoardRepository;
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
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final S3Service s3Service;

    @Transactional
    public void createPost(Long boardId, CreatePostDTO createPostDTO) { // createPost 리팩토링 해야함. // 게시판 참여자만 게시글을 쓸 수 있게 해야하나?
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BOARD_ID));
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
                .board(board)
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

    @Transactional
    public void deletePost(Long boardId, Long postId) { // 활동 게시글 삭제 method
        Post post = postRepository.findByBoardIdAndId(boardId, postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_POST_ID));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BOARD_ID));

        User loggedInUser = userService.getLoggedInUser();
        User postCreator = post.getUser();
        User boardCreator = board.getUser();

        validateAuthorityOfPostManagement(loggedInUser, postCreator, boardCreator);

        s3Service.deleteObject(post.getImageUrl());
        postRepository.delete(post);
    }

    private void validateAuthorityOfPostManagement(User loggedInUser, User postCreator, User boardCreator) {
        String loggedInUserId = loggedInUser.getId();
        String postCreatorId = postCreator.getId();
        String boardCreatorId = boardCreator.getId();

        if (!postCreatorId.equals(loggedInUserId)
        && !boardCreatorId.equals(loggedInUserId)
        && loggedInUser.getRole() != Role.ROLE_ADMIN
        && loggedInUser.getRole() != Role.ROLE_WEB_MASTER) {
            throw new BusinessException(ErrorCode.FORBIDDEN_UPDATE);
        }
    }
}
