package com.zain.springwebsocket.Controller;


import com.zain.springwebsocket.Dto.ResponseMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController
{
    // 测试接口
    @GetMapping("hello")
    public ResponseMessage hello()
    {
        return ResponseMessage.success("hello");
    }

}
