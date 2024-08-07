package com.movie.movieinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userManagement")
public class UserManagementController {

    /**ID 찿는 페이지*/
    @GetMapping("/findUserIdView")
    public String findUserIdView() {
        return "findUserId.html";
    }
    

    /**비밀번호 찿기를 위한 이메일 입력 화면*/
    @GetMapping("/resetPasswordView")
    public String resetPasswordView() {
        return "resetPasswordView.html";
    }

    /**비밀 번호 재설정 화면*/
    @GetMapping("/resetNewPasswordView")
    public String resetNewPasswordView() {
        return "resetNewPasswordView.html";
    }


    @GetMapping("/signUpView")
    public String signUpView() {
        return "signUpView.html";
    }
}
