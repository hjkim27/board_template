package com.github.hjkim27.bean.dto;

import com.github.hjkim27.bean.type.SignMessageEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <pre>
 *     ajax 호출에 대한 성공여부 응답 관련
 * </pre>
 *
 * @author hjkim27
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@ToString
public class AjaxResponseDTO {

    /**
     * 로그인상태
     */
    private boolean login;

    /**
     * 성공에러코드   (0: 성공, 그 외: 에러)
     */
    private int status = 0;

    /**
     * 에러메시지    (status != 0 일 경우)
     */
    private String message;

    /**
     * status 에 따라 반환할 url
     */
    private String url;

    public void setSignMessage(SignMessageEnum signMessage) {
        this.status = signMessage.getStatus();
        this.message = signMessage.getMessage();
        this.login = signMessage.isLogin();
    }
}
