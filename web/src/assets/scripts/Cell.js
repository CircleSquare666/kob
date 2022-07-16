/**
 * 定义一个蛇的格子为Cell
 */
export class Cell{
    constructor(r, c){
        this.r = r;
        this.c = c;
        this.x = c + 0.5;//中心点坐标
        this.y = r + 0.5;//中心点坐标
    }
}