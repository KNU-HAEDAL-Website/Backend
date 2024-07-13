package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.request.CreateBoardDTO;
import com.haedal.haedalweb.dto.response.BoardDTO;
import com.haedal.haedalweb.dto.response.PreSignedUrlDTO;
import com.haedal.haedalweb.dto.response.common.SuccessResponse;
import com.haedal.haedalweb.service.BoardService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@Tag(name = "게시판 API")
@RequiredArgsConstructor
@RestController
public class BoardController {
    private final S3Service s3Service;
    private final BoardService boardService;

    @Operation(summary = "PreSignedUrl 게시판 대표 이미지 저장용")
    @GetMapping("/boards/generate-presigned-url")
    public ResponseEntity<PreSignedUrlDTO> generatePreSignedUrl() {
        String objectKey = "boards/" + UUID.randomUUID().toString();
        PreSignedUrlDTO preSignedUrlDTO = s3Service.getPreSignedUrlDTO(objectKey);

        return ResponseEntity.ok(preSignedUrlDTO);
    }

    @Operation(summary = "게시판 생성")
    @ApiSuccessCodeExample(SuccessCode.ADD_BOARD_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_USER_ID, ErrorCode.NOT_FOUND_ACTIVITY_ID})
    @Parameter(name = "activityId", description = "게시판 추가할 활동 ID")
    @PostMapping("/activities/{activityId}/boards")
    public ResponseEntity<SuccessResponse> addBoard(@PathVariable Long activityId, @RequestBody @Valid CreateBoardDTO createBoardDTO) {
        boardService.createBoard(activityId, createBoardDTO);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.ADD_BOARD_SUCCESS);
    }

    @Operation(summary = "게시판 페이징 조회")
    @Parameters({
            @Parameter(name = "activityId", description = "게시판 조회할 활동 ID"),
            @Parameter(name = "page", description = "조회 할 page, default: 0"),
            @Parameter(name = "size", description = "한 번에 조회 할 page 수, default: 5")
    })
    @GetMapping("/activities/{activityId}/boards")
    public ResponseEntity<Page<BoardDTO>> getBoards(@PathVariable Long activityId,
                                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<BoardDTO> boardDTOs = boardService.getBoardDTOs(activityId, PageRequest.of(page, size, Sort.by(Sort.Order.asc("id"))));

        return ResponseEntity.ok(boardDTOs);
    }

    @Operation(summary = "게시판 단일 조회")
    @Parameters({
            @Parameter(name = "activityId", description = "게시판 조회할 활동 ID"),
            @Parameter(name = "boardId", description = "해당 게시판 ID")
    })
    @GetMapping("/activities/{activityId}/boards/{boardId}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long activityId, @PathVariable Long boardId) {
        BoardDTO boardDTO = boardService.getBoardDTO(activityId, boardId);

        return ResponseEntity.ok(boardDTO);
    }
}
