package com.haedal.haedalweb.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.net.URL;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDTO {
    private Long activityId;

    private Long boardId;

    private String boardName;

    private String boardIntro;

    private URL boardImageUrl;

    private List<ParticipantDTO> participants;
}
