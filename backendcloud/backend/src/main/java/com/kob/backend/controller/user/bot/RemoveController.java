package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RemoveController {
    @Autowired
    private RemoveService removeService;

    @DeleteMapping("/user/bot/remove")
    public Map<String, String> remove(@RequestParam Map<String, String> data){
        for (String s : data.keySet()) {
            System.out.print(s + " ");
            System.out.println(data.get(s));
        }
        return removeService.remove(data);
    }
}
