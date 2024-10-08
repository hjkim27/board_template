package com.github.hjkim27.bean.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <pre>
 *     admin_info
 *     - insert / update / select(요청)
 * </pre>
 *
 * @author hjkim27
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@ToString
public class AdminRequestDTO {

    private String loginId;
    private String loginPw;
    private String loginPwCheck;
    private String name;
}
