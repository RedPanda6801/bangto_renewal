package com.example.banto.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactRoutingController {	
	@GetMapping(value = "/{path:^(?!api|static|images|index\\.html$).*}/**")
    public String redirect() {
        return "forward:/index.html";
    }
}

//@Controller
//public class WebController implements ErrorController {
//    @GetMapping({"/", "/error"})
//    public String index(HttpServletRequest request) throws Exception {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
////        if(status.toString().equals("403")) {
////            return "접근불가(로그인이전이거나 해당페이지롤이 아닙니다)";
////        }
//            if(status.toString().equals("404")) {
//            return "index.html";
//        }
//            return "index.html";
//    }
//}