package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * @description 表示组成蛇的每一个单元
 */
public class Cell {
    private Integer x;
    private Integer y;
    public void testLog(){
        int k = 9;
        log.info("a{}",k);
    }
}
