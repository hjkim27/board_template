package com.example.demo.controller;

import com.example.demo.bean.SignMessageEnum;
import com.example.demo.bean.dto.AdminRequestDTO;
import com.example.demo.bean.dto.AjaxResponse;
import com.example.demo.config.GeneralConfig;
import com.example.demo.service.AdminInfoService;
import com.example.demo.util.LoginUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 *     login, logout
 * </pre>
 *
 * @author hjkim27
 * @date 2024. 07. 07
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class SignController {

    private final AdminInfoService adminInfoService;

    /**
     * <pre>
     *     login page
     * </pre>
     *
     * @param request {@link HttpServletRequest}
     */
    @RequestMapping("/sign/sign-in")
    public ModelAndView signIn(HttpServletRequest request) {
        log.info(GeneralConfig.START);
        ModelAndView mav = new ModelAndView("/sign/signIn");
        if (LoginUtil.isLogin(request)) {
            return new ModelAndView(new RedirectView(request.getContextPath() + GeneralConfig.MAIN_URL));
        }
        return mav;
    }

    /**
     * <pre>
     *     login submit
     * </pre>
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param dto      loginId, loginPw 를 담은 {@link AdminRequestDTO} 객체
     * @return {@link AjaxResponse}
     */
    @ResponseBody
    @RequestMapping(value = "/sign/sign-in", method = RequestMethod.POST)
    public AjaxResponse signIn(
            HttpServletRequest request, HttpServletResponse response,
            AdminRequestDTO dto
    ) {
        log.info(GeneralConfig.START);
        log.debug("parameter : {}", dto);
        AjaxResponse responseDTO = new AjaxResponse();

        AdminRequestDTO adminRequestIDCheck = new AdminRequestDTO();
        adminRequestIDCheck.setLoginId(dto.getLoginId());

        boolean existLoginId = adminInfoService.isExistAdmin(adminRequestIDCheck);
        if (existLoginId) {
            int adminSid = adminInfoService.getAdminSid(dto);
            if (adminSid > 0) {
                LoginUtil.setLogin(request, response, adminSid);
                responseDTO.setUrl(request.getContextPath() + GeneralConfig.MAIN_URL);
                responseDTO.setSignMessage(SignMessageEnum.SUCCESS);
            } else {
                responseDTO.setSignMessage(SignMessageEnum.NOT_MATCH_PASSWORD);
            }
        } else {
            responseDTO.setSignMessage(SignMessageEnum.NOT_EXIST_USER);
        }
        log.debug("return : {}", responseDTO);
        return responseDTO;
    }


    /**
     * <pre>
     *     register page
     * </pre>
     */
    @RequestMapping("/sign/sign-up")
    public ModelAndView signUp() {
        log.info(GeneralConfig.START);
        return new ModelAndView("sign/signUp");
    }

    /**
     * <pre>
     *     register submit
     * </pre>
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param dto      loginId, name, loginPw, loginPwCheck 를 담은 {@link AdminRequestDTO} 객체
     * @return {@link AjaxResponse}
     */
    @ResponseBody
    @RequestMapping(value = "/sign/sign-up", method = RequestMethod.POST)
    public AjaxResponse signUp(
            HttpServletRequest request, HttpServletResponse response,
            AdminRequestDTO dto
    ) {
        log.info(GeneralConfig.START);
        log.debug("parameter : {}", dto);

        AjaxResponse responseDTO = new AjaxResponse();

        // 비밀번호 일치
        if (dto.getLoginPw().equals(dto.getLoginPwCheck())) {
            AdminRequestDTO adminRequestIDCheck = new AdminRequestDTO();
            adminRequestIDCheck.setLoginId(dto.getLoginId());

            // 계정 추가
            int insertLoginId = adminInfoService.insertAdmin(dto);
            if (insertLoginId > 0) {
                LoginUtil.setLogin(request, response, insertLoginId);
                responseDTO.setSignMessage(SignMessageEnum.SUCCESS);
            } else {
                responseDTO.setSignMessage(SignMessageEnum.NOT_USED_ID);
            }
        }
        // 비밀번호 불일치
        else {
            responseDTO.setSignMessage(SignMessageEnum.NOT_MATCH_PASSWORD);
        }
        log.debug("return : {}", responseDTO);
        return responseDTO;
    }

}