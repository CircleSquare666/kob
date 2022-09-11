package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Game extends Thread {//用来管理整个游戏流程，extends Thread类并实现run函数即可让Game支持多线程
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count; //存储障碍物的总个数
    private final int[][] gameMap; //存储游戏地图
    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private Integer nextStepA;
    private Integer nextStepB;
    private ReentrantLock lock = new ReentrantLock();//Java中的加锁
    private String status = "playing";//表示当前状态，"playing" 和 "finished"
    private String loser = "";//all：平局，A：A输，B：B输

    public void setNextStepA(Integer nextStepA) { //因为这边会写，WebSocketServer那边会读，所以读写冲突，要加锁
        lock.lock(); //操作前先锁起来
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock(); //操作前先锁起来
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.gameMap = new int[rows][cols];
        playerA = new Player(idA, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, this.cols - 2, new ArrayList<>());
    }

    public int[][] getGameMap() {
        return gameMap;
    }

    private boolean drawGameMap() { //画地图，并判断是否连通
        //初始化
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                gameMap[i][j] = 0; // 0表示空地，1表示墙
            }
        }

        //给四周加上墙
        for (int r = 0; r < this.rows; r++) {
            gameMap[r][0] = 1;
            gameMap[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            gameMap[0][c] = 1;
            gameMap[this.rows - 1][c] = 1;
        }

        //中心对称创建随机障碍物，使游戏保证公平
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (gameMap[r][c] == 1 || gameMap[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;//已经是障碍物的地方不能再是障碍物
                if (r == this.rows - 2 && c == 1 || c == this.cols - 2 && r == 1)
                    continue;//两条蛇的起点不能是障碍物
                gameMap[r][c] = 1;
                gameMap[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }


    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (drawGameMap()) {
                break;
            }
        }
    }

    /**
     * @param sx 起点x坐标
     * @param sy 起点y坐标
     * @param tx 终点x坐标
     * @param ty 终点y坐标
     * @description 用Flood Fill算法判断对称图是否连通
     */
    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        gameMap[sx][sy] = 1;//标记当前位置走过了
        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && gameMap[x][y] == 0) {
                if (this.check_connectivity(x, y, tx, ty)) {
                    gameMap[sx][sy] = 0;
                    return true;
                }
            }
        }
        gameMap[sx][sy] = 0;
        return false;
    }

    /**
     * @description 向两名玩家公布结果
     */
    public void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");//表示当前传递是哪种消息
        resp.put("loser", loser);
        saveToDataBase();
        sendAllMessage(resp.toJSONString());
    }

    /**
     * @description 向两名玩家发送广播
     */
    public void sendAllMessage(String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    /**
     * @description 判断蛇细胞是否和墙重叠，或者和另外一条蛇重叠
     */
    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (gameMap[cell.getX()][cell.getY()] == 1) return false;
        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).getX() == cell.getX() && cellsA.get(i).getY() == cell.getY()) return false;
        }
        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).getX() == cell.getX() && cellsB.get(i).getY() == cell.getY()) return false;
        }
        return true;
    }


    /**
     * @description 判断两名玩家下一步操作是否合法
     */
    public void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    /**
     * @description 向两个Client传递移动信息
     */
    public void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @description 将地图转成String
     */
    public String getMapString() {
//        StringBuilder res = new StringBuilder();
//        for(int i = 0; i < rows; i++){
//            for (int j = 0; j < cols; j++) {
//                res.append(gameMap[i][j]);
//            }
//        }
//        return res.toString();
        String res = JSON.toJSONString(gameMap);
//        log.info("地图转换成字符串为： " + res);
        return res;
    }

    /**
     * @description 将对局记录存储到数据库
     */
    public void saveToDataBase() {
        Record record = new Record(null,
                playerA.getId(),
                playerB.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    @Override
    public void run() { //入口函数
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {
                judge();
                if ("playing".equals(status)) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;//游戏结束
            }
        }
    }

    private boolean nextStep() { //等待两名玩家的下一步操作
        try {
            Thread.sleep(200); //前端200ms渲染一格，所以要等待一下，否则前端可能会丢失部分的步骤
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 50; i++) { //读5s，看看能否读到
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
