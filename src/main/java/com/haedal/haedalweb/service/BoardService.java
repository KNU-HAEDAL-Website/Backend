package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Activity;
import com.haedal.haedalweb.domain.Board;
import com.haedal.haedalweb.domain.Participant;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.request.CreateBoardDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final ActivityService activityService;
    private final UserService userService;

    @Transactional
    public void createBoard(Long activityId, CreateBoardDTO createBoardDTO) {
        Activity activity = activityService.findActivityById(activityId);
        User creator = userService.getLoggedInUser();
        List<String> participantIds = createBoardDTO.getParticipants();
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

    public boolean isActivityPresent(Long activityId) {
        return boardRepository.existsByActivityId(activityId);
    }
}
