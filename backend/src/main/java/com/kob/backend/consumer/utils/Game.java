package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {//用来管理整个游戏流程
    final private Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count; //存储障碍物的总个数
    private final int[][] gameMap; //存储游戏地图
    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.gameMap = new int[rows][cols];
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

    //用Flood Fill算法判断对称图是否连通

    /**
     * @param {*} g 地图
     * @param {*} sx 起点x坐标
     * @param {*} sy 起点y坐标
     * @param {*} tx 终点x坐标
     * @param {*} ty 终点y坐标
     */
    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        gameMap[sx][sy] = 1;//标记当前位置走过了
        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if(x >= 0 && x < this.rows && y >= 0 && y < this.cols && gameMap[x][y] == 0){
                if (this.check_connectivity(x, y, tx, ty)) {
                    gameMap[sx][sy] = 0;
                    return true;
                }
            }
        }
        gameMap[sx][sy] = 0;
        return false;
    }

}
