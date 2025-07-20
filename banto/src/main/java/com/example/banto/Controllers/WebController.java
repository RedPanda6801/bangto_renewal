package com.example.banto.Controllers;

// WebController
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController implements ErrorController {
    @GetMapping({"/", "/error"})
    public String index(HttpServletRequest request) throws Exception {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        if(status.toString().equals("403")) {
//            return "접근불가(로그인이전이거나 해당페이지롤이 아닙니다)";
//        }
            if(status.toString().equals("404")) {
            return "index.html";
        }
            return "index.html";
    }
}