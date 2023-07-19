package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchingServiceImpl implements MatchingService {
    public static final MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        log.info("addPlayer: userId = {}, rating = {}", userId, rating);
        matchingPool.addPlayer(userId, rating);
        return "success";
    }

    @Override
    public String removePlayer(Integer userId) {
        log.info("removePlayer: userId = {}", userId);
        matchingPool.removePlayer(userId);
        return "success";
    }
}
