package com.haedal.haedalweb.dto.response;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDTO {
    @Schema(description = "게시글 id")
    private Long postId;

    @Schema(description = "게시글 제목")
    private String postTitle;

    @Schema(description = "게시글 내용")
    private String postContent;

    @Schema(description = "게시글 대표 이미지 파일 Url")
    private URL postImageUrl;

    @Schema(description = "게시글 조회수")
    private Long postViews;

    @Schema(description = "활동 시작일 (이벤트와 활동은 필수, 공지사항은 생략)", example = "yyyy-MM-dd (2024-07-24)")
    private LocalDate postActivityStartDate;

    @Schema(description = "활동 종료일 (생략 가능)", example = "yyyy-MM-dd (2024-07-24)")
    private LocalDate postActivityEndDate;

    @Schema(description = "게시글 생성일")
    private LocalDateTime postCreateDate;

    @Schema(description = "유저 아이디", example = "haedal12")
    private String userId;

    @Schema(description = "유저 이름", example = "조대성")
    private String userName;

    @Schema(description = "게시판 id")
    private Long boardId;

    @Schema(description = "게시판 이름")
    private String boardName;
}
