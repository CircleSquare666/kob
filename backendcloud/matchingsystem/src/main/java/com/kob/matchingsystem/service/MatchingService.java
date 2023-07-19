package com.kob.matchingsystem.service;

public interface MatchingService {
    /**
     * @description: 添加玩家进匹配池
     * @param userId
     * @param rating 天梯分
     * @return
     */
    String addPlayer(Integer userId, Integer rating);

    String removePlayer(Integer userId);
}
