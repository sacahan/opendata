package com.udn.ntpc.od.frontend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

// 接收參數總結: https://www.jianshu.com/p/ed44e89a6f79
@Slf4j
@Controller
public class PageController {

    public static final String USER_KEY = "USER_ACCOUNT";

    @GetMapping("/login.html")
    public String loginIndex(Model model, HttpSession session) {
        // 加入Request Scope變數
        model.addAttribute("welcome", "登入表單");
        // 移除登入Session
        session.invalidate();
        return "login";
    }

    @PostMapping("/login/checkLogin")
    @ResponseBody
    public Map<String, String> checkLogin(@RequestParam String username,
                                          @RequestParam String password,
                                          HttpSession session) {
        log.info("{} : {}", username, password);
        session.setAttribute(USER_KEY, username);
        Map<String, String> map = new HashMap<>();
        map.put("result", "success");
        return map;
    }

    @GetMapping("/dashboard.html")
    public String dashboardIndex() {
        return "dashboard";
    }

    @GetMapping("/demo/table.html")
    public String tableIndex() {
        return "demo/table";
    }

}

