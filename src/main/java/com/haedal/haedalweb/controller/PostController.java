package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.request.CreatePostDTO;
import com.haedal.haedalweb.dto.response.PostDTO;
import com.haedal.haedalweb.dto.response.PostSliderDTO;
import com.haedal.haedalweb.dto.response.PostSummaryDTO;
import com.haedal.haedalweb.dto.response.PreSignedUrlDTO;
import com.haedal.haedalweb.dto.response.common.SuccessResponse;
import com.haedal.haedalweb.service.PostService;
import com.haedal.haedalweb.service.S3Service;
import com.haedal.haedalweb.swagger.ApiErrorCodeExamples;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExample;
import com.haedal.haedalweb.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@Tag(name = "게시글 API")
@RequiredArgsConstructor
@RestController
public class PostController {
    private final S3Service s3Service;
    private final PostService postService;

    @Operation(summary = "PreSignedUrl 게시판 대표 이미지 저장용")
    @GetMapping("/posts/generate-presigned-url")
    public ResponseEntity<PreSignedUrlDTO> generatePreSignedUrl() {
        String objectKey = "posts/" + UUID.randomUUID().toString();
        PreSignedUrlDTO preSignedUrlDTO = s3Service.getPreSignedUrlDTO(objectKey);

        return ResponseEntity.ok(preSignedUrlDTO);
    }

    @Operation(summary = "활동 게시글 생성")
    @ApiSuccessCodeExample(SuccessCode.ADD_POST_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_USER_ID, ErrorCode.NOT_FOUND_BOARD_ID, ErrorCode.NOT_FOUND_POST_TYPE, ErrorCode.INVALID_ARGUMENT})
    @Parameter(name = "boardId", description = "게시글 추가할 게시판 ID")
    @PostMapping("/boards/{boardId}/posts")
    public ResponseEntity<SuccessResponse> addPost(@PathVariable Long boardId, @RequestBody @Valid CreatePostDTO createPostDTO) {
        postService.createPost(boardId, createPostDTO);
        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.ADD_POST_SUCCESS);
    }

    @Operation(summary = "공지사항, 이벤트 게시글 생성")
    @ApiSuccessCodeExample(SuccessCode.ADD_POST_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_USER_ID, ErrorCode.NOT_FOUND_BOARD_ID, ErrorCode.NOT_FOUND_POST_TYPE, ErrorCode.INVALID_ARGUMENT})
    @PostMapping("/posts")
    public ResponseEntity<SuccessResponse> addNoticePost(@RequestBody @Valid CreatePostDTO createPostDTO) {
        postService.createPost(createPostDTO);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.ADD_POST_SUCCESS);
    }

    @Operation(summary = "활동 게시글 삭제")
    @ApiSuccessCodeExample(SuccessCode.DELETE_POST_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_POST_ID, ErrorCode.NOT_FOUND_BOARD_ID, ErrorCode.FORBIDDEN_UPDATE})
    @Parameters({
            @Parameter(name = "boardId", description = "게시글 삭제할 활동 게시판 ID"),
            @Parameter(name = "postId", description = "해당 게시글 ID")
    })
    @DeleteMapping("/boards/{boardId}/posts/{postId}")
    public ResponseEntity<SuccessResponse> deletePost(@PathVariable Long boardId, @PathVariable Long postId) {
        postService.deletePost(boardId, postId);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.DELETE_POST_SUCCESS);
    }

    @Operation(summary = "공지사항, 이벤트 게시글 삭제")
    @ApiSuccessCodeExample(SuccessCode.DELETE_POST_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_POST_ID, ErrorCode.NOT_FOUND_POST_TYPE})
    @Parameters({
            @Parameter(name = "postId", description = "해당 게시글 ID")
    })
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<SuccessResponse> deleteNoticePost(@PathVariable Long postId) {
        postService.deletePost(postId);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.DELETE_POST_SUCCESS);
    }

    @Operation(summary = "활동 게시글 목록 조회")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_BOARD_ID, ErrorCode.NOT_FOUND_POST_ID})
    @Parameters({
            @Parameter(name = "boardId", description = "게시글 조회할 게시판 ID"),
            @Parameter(name = "page", description = "조회 할 page, default: 0"),
            @Parameter(name = "size", description = "한 번에 조회 할 page 수, default: 10")
    })
    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<Page<PostSummaryDTO>>  getActivityPosts(@PathVariable Long boardId,
                                                                      @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                      @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Page<PostSummaryDTO> posts = postService.getPosts(boardId, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));

        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "공지사항, 이벤트 게시글 목록 조회")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_POST_TYPE, ErrorCode.NOT_FOUND_POST_ID})
    @Parameters({
            @Parameter(name = "page", description = "조회 할 page, default: 0"),
            @Parameter(name = "size", description = "한 번에 조회 할 page 수, default: 10")
    })
    @GetMapping("/posts")
    public ResponseEntity<Page<PostSummaryDTO>>  getActivityPosts(@RequestParam(name = "postType") String postType,
                                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Page<PostSummaryDTO> posts = postService.getPosts(postType, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));

        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 단일 조회")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_POST_ID})
    @Parameters({
            @Parameter(name = "postId", description = "해당 게시글 ID")
    })
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        PostDTO post = postService.getPost(postId);

        return ResponseEntity.ok(post);
    }

    @Operation(summary = "이벤트 게시글 슬라이더 조회")
    @Parameters({
            @Parameter(name = "page", description = "조회 할 page, default: 0"),
            @Parameter(name = "size", description = "한 번에 조회 할 page 수, default: 10")
    })
    @GetMapping("/posts/slider")
    public ResponseEntity<Page<PostSliderDTO>> getSliderPosts(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<PostSliderDTO> posts = postService.getSliderPosts(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));

        return ResponseEntity.ok(posts);
    }
}
