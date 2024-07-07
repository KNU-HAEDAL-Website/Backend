package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Activity;
import com.haedal.haedalweb.domain.Board;
import com.haedal.haedalweb.domain.Participant;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.request.CreateBoardDTO;
import com.haedal.haedalweb.dto.response.BoardDTO;
import com.haedal.haedalweb.dto.response.ParticipantDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final ActivityService activityService;
    private final UserService userService;
    private final S3Service s3Service;

    public boolean isActivityPresent(Long activityId) {
        return boardRepository.existsByActivityId(activityId);
    }

    @Transactional
    public void createBoard(Long activityId, CreateBoardDTO createBoardDTO) {
        Activity activity = activityService.findActivityById(activityId);
        User creator = userService.getLoggedInUser();
        List<String> participantIds = new ArrayList<>(createBoardDTO.getParticipants());
        List<User> participants = userService.findUserByIds(participantIds);

        validateParticipants(participants, participantIds);

        Board board = Board.builder()
                .name(createBoardDTO.getBoardName())
                .intro(createBoardDTO.getBoardIntro())
                .imageUrl(createBoardDTO.getBoardImageUrl())
                .user(creator)
                .participants(new ArrayList<>())
                .activity(activity)
                .build();

        addParticipantsToBoard(board, participants);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardDTO> getBoardDTOs(Long activityId, Pageable pageable) {
        Page<Board> boardPage = boardRepository.findBoardsByActivityId(activityId, pageable);

        return boardPage.map(this::convertToBoardDTO);
    }

    private void addParticipantsToBoard(Board board, List<User> participants) {
        for (User user : participants) {
            Participant participant = Participant.builder()
                    .board(board)
                    .user(user)
                    .build();
            board.addParticipant(participant);
        }
    }

    private void validateParticipants(List<User> users, List<String> userIds) {
        if (users.size() != userIds.size()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER_ID);
        }

        users.forEach(user -> {
            UserStatus userStatus = user.getUserStatus();
            if (userStatus != UserStatus.ACTIVE) {
                throw new BusinessException(ErrorCode.NOT_FOUND_USER_ID);
            }
        });
    }

    private BoardDTO convertToBoardDTO(Board board) {
        return BoardDTO.builder()
                .boardId(board.getId())
                .boardName(board.getName())
                .boardIntro(board.getIntro())
                .boardImageUrl(s3Service.generatePreSignedGetUrl(board.getImageUrl())) // presigned-url 변환 후 값 넣어주기
                .participants(convertParticipants(board.getParticipants())) // List<Participants>로 List<participantDTO> 만들기
                .build();
    }

    private List<ParticipantDTO> convertParticipants(List<Participant> participants) {
        return participants.stream()
                .map(this::convertToParticipantDTO)
                .collect(Collectors.toList());
    }

    private ParticipantDTO convertToParticipantDTO(Participant participant) {
        return ParticipantDTO.builder()
                .participantId(participant.getId())
                .userId(participant.getUser().getId())
                .userName(participant.getUser().getName())
                .build();
    }
}
