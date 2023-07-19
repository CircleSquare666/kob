package com.kob.matchingsystem.service.impl.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class MatchingPool extends Thread {
    private static final ReentrantLock lock = new ReentrantLock();
    private final static String START_GAME_URL = "http://127.0.0.1:3000/pk/start/game";
    private static List<Player> players = new ArrayList<>();
    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    log.info("MatchingPool: players = {}", players);
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @description: 将所有玩家的等待时间加一
     */
    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    public void addPlayer(Integer userId, Integer rating) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }


    /**
     * @description: 尝试匹配玩家
     */
    private void matchPlayers() {
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) {
                continue;
            }
            for (int j = i + 1; j < players.size(); j++) {
                if (checkMatch(players.get(i), players.get(j))) {
                    sendResult(players.get(i), players.get(j));
                    used[i] = true;
                    used[j] = true;
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    /**
     * @param player1
     * @param player2
     * @return
     * @description: 检查两个玩家是否匹配
     */
    private boolean checkMatch(Player player1, Player player2) {
        int ratingDiff = Math.abs(player1.getRating() - player2.getRating());
        return ratingDiff <= Math.min(player1.getWaitingTime(), player2.getWaitingTime()) * 10;
    }

    /**
     * @param player1
     * @param player2
     * @description: 发送匹配结果
     */
    private void sendResult(Player player1, Player player2) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("aId", player1.getUserId().toString());
        data.add("bId", player2.getUserId().toString());
        restTemplate.postForObject(START_GAME_URL, data, String.class);
    }
}
