package com.kob.matchingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer userId;
    private Integer rating;
    /**
     * 玩家进入匹配池的时间
     */
    private Integer waitingTime;
}
