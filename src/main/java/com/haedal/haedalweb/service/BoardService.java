package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Activity;
import com.haedal.haedalweb.domain.Board;
import com.haedal.haedalweb.domain.Participant;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.request.CreateBoardDTO;
import com.haedal.haedalweb.dto.request.UpdateBoardDTO;
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
        Activity activity = activityService.findActivityById(activityId);
        Page<Board> boardPage = boardRepository.findBoardsByActivity(activity, pageable);

        return boardPage.map(board -> convertToBoardDTO(board, activityId));
    }

    @Transactional(readOnly = true)
    public BoardDTO getBoardDTO(Long activityId, Long boardId) {
        Board board = boardRepository.findByActivityIdAndId(activityId, boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BOARD_ID));

        return convertToBoardDTO(board, activityId);
    }

    @Transactional
    public void deleteBoard(Long activityId, Long boardId) {
        Board board = boardRepository.findByActivityIdAndId(activityId, boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BOARD_ID));

        User loggedInUser = userService.getLoggedInUser();
        User creator = board.getUser();

        validateAuthorityOfBoardManagement(loggedInUser, creator);

        // 게시글 존재 시 삭제 불가 로직 추가 예정
        s3Service.deleteObject(board.getImageUrl());
        boardRepository.delete(board);
    }

    @Transactional
    public void updateBoardImage(Long activityId, Long boardId, String newImageUrl) {
        Board board = boardRepository.findByActivityIdAndId(activityId, boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BOARD_ID));

        User loggedInUser = userService.getLoggedInUser();
        User creator = board.getUser();

        validateAuthorityOfBoardManagement(loggedInUser, creator);

        s3Service.deleteObject(board.getImageUrl());
        board.setImageUrl(newImageUrl);
        boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(Long activityId, Long boardId, UpdateBoardDTO updateBoardDTO) {
        Board board = boardRepository.findByActivityIdAndId(activityId, boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BOARD_ID));

        User loggedInUser = userService.getLoggedInUser();
        User creator = board.getUser();

        validateAuthorityOfBoardManagement(loggedInUser, creator);

        List<String> participantIds = new ArrayList<>(updateBoardDTO.getParticipants());
        List<User> participants = userService.findUserByIds(participantIds);

        validateParticipants(participants, participantIds);

        board.setName(updateBoardDTO.getBoardName());
        board.setIntro(updateBoardDTO.getBoardIntro());
        board.setParticipants(new ArrayList<>());
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

    private BoardDTO convertToBoardDTO(Board board, Long activityId) {
        return BoardDTO.builder()
                .activityId(activityId)
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

    private void validateAuthorityOfBoardManagement(User loggedInUser, User creator) {
        if (loggedInUser.getRole() == Role.ROLE_TEAM_LEADER && !loggedInUser.getId().equals(creator.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_UPDATE);
        }
    }
}
