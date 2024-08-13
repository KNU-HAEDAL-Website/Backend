package com.haedal.haedalweb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostDTO {
    @Schema(description = "게시글 이름", example = "게시글1")
    @Size(min = 1, max = 50, message = "게시글 이름은 1자 이상 50자 이하여야 합니다.")
    private String postTitle;

    @Size(min = 1, max = 200000, message = "게시글의 메타 정보와 내용은 합쳐서 20만자 이하여야 합니다.")
    private String postContent;

    @Schema(description = "게시글 대표 이미지 파일 Url", example = "posts/abc.jpg")
    private String postImageUrl;

    @Schema(description = "활동 시작일", example = "yyyy-MM-dd (2024-07-24)")
    private String postActivityStartDate;

    @Schema(description = "활동 종료일", example = "yyyy-MM-dd (2024-07-24)")
    private String postActivityEndDate;

    @Schema(description = "게시글 타입", example = "(ACTIVITY, NOTICE, EVENT)")
    private String postType;
//
//    @Schema(description = "게시글 추가할 게시판 ID (공지사항, 이벤트는 생략 가능)")
//    private Long boardId;
}
