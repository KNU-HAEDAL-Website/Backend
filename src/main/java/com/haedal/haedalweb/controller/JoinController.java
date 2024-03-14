package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.dto.JoinDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haedal.haedalweb.service.JoinService;

@Controller
@ResponseBody
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO){

        joinService.joinProcess(joinDTO);
        return "ok";
    }
}
