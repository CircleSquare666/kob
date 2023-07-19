package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {//用来维护玩家的位置信息
    private Integer id;
    private Integer sx;//起始x坐标
    private Integer sy;//起始y坐标
    private List<Integer> steps; //行驶路线

    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx;
        int y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (Integer d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            step++;
            if(!check_tail_increasing(step)){
                res.remove(0);
            }
        }
        return res;
    }
    public boolean check_tail_increasing(int steps) { //检验当前回合，蛇的长度是否增加
        if (steps <= 10) return true;
        if (steps % 3 == 1) return true;
        return false;
    }

    /**
     * @description 将steps转成字符串保存
     */
    public String getStepsString(){
//        StringBuilder res = new StringBuilder();
//        for (Integer step : steps) {
//            res.append(step);
//        }
//        return res.toString();
        String res = JSON.toJSONString(steps);
        return res;
    }
}
