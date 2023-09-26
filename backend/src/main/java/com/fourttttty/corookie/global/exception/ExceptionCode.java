package com.fourttttty.corookie.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@Getter
public enum ExceptionCode {

    ISSUE_NOT_FOUND(BAD_REQUEST, "ISSUE_001", "이슈를 찾을 수 없습니다"),
    ISSUE_PROGRESS_INVALID(BAD_REQUEST, "ISSUE_002", "유효하지 않은 이슈 진행입니다."),
    ISSUE_PRIORITY_INVALID(BAD_REQUEST, "ISSUE_003", "유효하지 않은 이슈 중요도입니다."),
    ISSUE_CATEGORY_INVALID(BAD_REQUEST, "ISSUE_004", "유효하지 않은 이슈 분류입니다."),
    ISSUE_FILTER_TYPE_INVALID(BAD_REQUEST, "ISSUE_005", "유효하지 않은 이슈 필터 타입입니다."),

    PROJECT_NOT_FOUND(BAD_REQUEST, "PROJECT_001", "프로젝트를 찾을 수 없습니다."),
    PROJECT_NOT_ENABLED(BAD_REQUEST, "PROJECT_002", "더 이상 사용되지 않는 프로젝트입니다."),
    PROJECT_NOT_DISABLED(BAD_REQUEST, "PROJECT_003", "프로젝트가 비활성화되지 않았습니다."),
    PROJECT_NOT_OPEN_FOR_INVITATION(BAD_REQUEST, "PROJECT_004", "초대를 허용하지 않는 프로젝트입니다."),
    INVITATION_STATUS_CHANGE_REQUEST_INVALID(BAD_REQUEST, "PROJECT_005", "프로젝트 관리자만 링크를 활성화 할 수 있습니다."),

    CHANNEL_PIN_REQUEST_INVALID(BAD_REQUEST, "TEXT_CHANNEL_001", "프로젝트 인원만 채널 고정을 요청할 수 있습니다."),

    PLAN_NOT_FOUND(BAD_REQUEST, "PLAN_001", "일정을 찾을 수 없습니다."),
    PLAN_NOT_ENABLED(BAD_REQUEST,"PLAN_002","더 이상 사용되지 않는 일정입니다."),

    EMOJI_INVALID(BAD_REQUEST, "EMOJI_001", "유효하지 않은 이모지 유형입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
