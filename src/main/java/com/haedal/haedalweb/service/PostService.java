package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Board;
import com.haedal.haedalweb.domain.Post;
import com.haedal.haedalweb.domain.PostType;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.dto.request.CreatePostDTO;
import com.haedal.haedalweb.dto.response.PostDTO;
import com.haedal.haedalweb.dto.response.PostSliderDTO;
import com.haedal.haedalweb.dto.response.PostSummaryDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.BoardRepository;
import com.haedal.haedalweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
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

        LocalDate activityStartDate = null;
        LocalDate activityEndDate = null;

        try {
            activityStartDate = LocalDate.parse(createPostDTO.getPostActivityStartDate(), DateTimeFormatter.ISO_DATE);
        } catch (DateTimeException e) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT);
        }

        if (createPostDTO.getPostActivityEndDate() != null) {
            activityEndDate = LocalDate.parse(createPostDTO.getPostActivityEndDate(), DateTimeFormatter.ISO_DATE);
        }

        LocalDateTime createDate = LocalDateTime.now();
        User creator = userService.getLoggedInUser();

        Post post = Post.builder()
                .title(createPostDTO.getPostTitle())
                .content(createPostDTO.getPostContent())
                .imageUrl(createPostDTO.getPostImageUrl()) // 이미지 생성 Controller 만들기
                .views(0L)
                .postType(postType)
                .activityStartDate(activityStartDate)
                .activityEndDate(activityEndDate)
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

        LocalDate activityStartDate = null;
        LocalDate activityEndDate = null;
        LocalDateTime createDate = LocalDateTime.now();
        User creator = userService.getLoggedInUser();

        if (postType == PostType.EVENT) {
            try {
                activityStartDate = LocalDate.parse(createPostDTO.getPostActivityStartDate(), DateTimeFormatter.ISO_DATE);
            } catch (DateTimeException e) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT);
            }

            if (createPostDTO.getPostActivityEndDate() != null) {
                activityEndDate = LocalDate.parse(createPostDTO.getPostActivityEndDate(), DateTimeFormatter.ISO_DATE);
            }
        }

        Post post = Post.builder()
                .title(createPostDTO.getPostTitle())
                .content(createPostDTO.getPostContent())
                .imageUrl(createPostDTO.getPostImageUrl()) // 이미지 생성 Controller 만들기
                .views(0L)
                .postType(postType)
                .activityStartDate(activityStartDate)
                .activityEndDate(activityEndDate)
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

    @Transactional
    public void deletePost(Long postId) { // 이벤트, 공지사항 삭제
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_POST_ID));

        try {
            if (post.getPostType() != PostType.NOTICE && post.getPostType() != PostType.EVENT)
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND_POST_TYPE);
        }

        s3Service.deleteObject(post.getImageUrl());
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryDTO> getPosts(Long boardId, Pageable pageable) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BOARD_ID));
        Page<Post> postPage = postRepository.findPostsByBoard(board, pageable);

        return postPage.map(post -> convertToPostSummaryDTO(post, board));
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryDTO> getPosts(String pType, Pageable pageable) {
        PostType postType;

        try {
            postType = PostType.valueOf(pType.toUpperCase());
            if (postType != PostType.NOTICE && postType != PostType.EVENT)
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NOT_FOUND_POST_TYPE);
        }

        Page<Post> postPage = postRepository.findPostsByPostType(postType, pageable);

        return postPage.map(post -> convertToPostSummaryDTO(post));
    }

    @Transactional
    public PostDTO getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_POST_ID));

        post.setViews(post.getViews()+1);
        postRepository.save(post);

        PostDTO postDTO;
        if (post.getPostType() == PostType.ACTIVITY) {
            postDTO = PostDTO.builder()
                    .postId(post.getId())
                    .postTitle(post.getTitle())
                    .postContent(post.getContent())
                    .postImageUrl(s3Service.generatePreSignedGetUrl(post.getImageUrl()))
                    .postViews(post.getViews())
                    .postCreateDate(post.getCreateDate())
                    .postActivityStartDate(post.getActivityStartDate())
                    .postActivityEndDate(post.getActivityEndDate())
                    .userId(post.getUser().getId())
                    .userName(post.getUser().getName())
                    .boardId(post.getBoard().getId())
                    .boardName(post.getBoard().getName())
                    .build();

            return postDTO;
        }

        postDTO = PostDTO.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postContent(post.getContent())
                .postImageUrl(s3Service.generatePreSignedGetUrl(post.getImageUrl()))
                .postViews(post.getViews())
                .postCreateDate(post.getCreateDate())
                .postActivityStartDate(post.getActivityStartDate())
                .postActivityEndDate(post.getActivityEndDate())
                .userId(post.getUser().getId())
                .userName(post.getUser().getName())
                .build();

        return postDTO;
    }

    @Transactional(readOnly = true)
    public Page<PostSliderDTO> getSliderPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findPostsByPostType(PostType.EVENT, pageable);

        return postPage.map((post) -> convertToPostSliderDTO(post));
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

    private PostSummaryDTO convertToPostSummaryDTO(Post post, Board board) {
        return PostSummaryDTO.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postViews(post.getViews())
                .postActivityStartDate(post.getActivityStartDate())
                .postActivityEndDate(post.getActivityEndDate())
                .postCreateDate(post.getCreateDate())
                .userId(post.getUser().getId())
                .userName(post.getUser().getName())
                .boardId(board.getId())
                .boardName(board.getName())
                .build();
    }

    private PostSummaryDTO convertToPostSummaryDTO(Post post) {
        PostSummaryDTO postSummaryDTO = PostSummaryDTO.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postViews(post.getViews())
                .postActivityStartDate(post.getActivityStartDate())
                .postActivityEndDate(post.getActivityEndDate())
                .postCreateDate(post.getCreateDate())
                .userId(post.getUser().getId())
                .userName(post.getUser().getName())
                .build();

        return postSummaryDTO;
    }

    private PostSliderDTO convertToPostSliderDTO(Post post) {
        PostSliderDTO postSliderDTO = PostSliderDTO.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postImageUrl(s3Service.generatePreSignedGetUrl(post.getImageUrl()))
                .build();

        return postSliderDTO;
    }
}
