//用于实现地图这个类
//有的时候import要用大括号括起来，有的时候不需要，区别在于如果是export default的话，就不需要括起来，如果是export的话，就要用大括号括起来
//export default每个文件只能有一个，类似于java中的public
import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    //parent是画布的父元素，用来动态修改画布的长宽
    constructor(ctx, parent) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;//表示绝对距离，L表示一个单位长度

        this.rows = 13;
        this.cols = 13;

        //内部障碍物的数量
        this.inner_walls_count = 40;

        //所有的障碍物包括边界墙
        this.walls = [];
    }
    create_walls() {
        new Wall(0, 0, this);

        const g = [];
        for (let r = 0; r < this.rows; r++) {
            g[r] = [];
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false;
            }
        }

        //给四周加上墙
        for (let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true;
        }
        for (let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true;
        }


        //对称创建随机障碍物
        for (let i = 0; i < this.inner_walls_count / 2; i++) {
            for (let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if (g[r][c] == true || g[c][r] == true) continue;
                if (r == this.rows - 2 && c == 1 || c == this.cols - 2 && r == 1) continue;//两条蛇的起点
                g[r][c] = g[c][r] = true;
                break;
            }
        }

        const copy_g = JSON.parse(JSON.stringify(g));//深拷贝
        if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false;

        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c] == true) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;

    }
    //用Flood Fill算法判断对称图是否连通
    /**
     * 
     * @param {*} g 地图
     * @param {*} sx 起点x坐标
     * @param {*} sy 起点y坐标
     * @param {*} tx 终点x坐标
     * @param {*} ty 终点y坐标
     */
    check_connectivity(g, sx, sy, tx, ty){
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;//标记当前位置走过了
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for(let i = 0; i < 4; i++){
            let x = sx + dx[i], y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g,x,y,tx,ty)) return true;
        }
        return false;
    }
    start() {
        for(let i = 0; i < 1000; i++){
            if(this.create_walls()){
                break;
            }
        }
    }

    //用于更新每一帧的边长（要在PlayGround区域中，找到一个最大的矩形区域）
    update_size() {
        // clientWidth是用来求当前div的宽度
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        this.render();
    }

    //渲染，用来把图像画到画布上
    render() {
        //奇、偶数格格子的颜色
        const color_even = "#002FA7", color_odd = "#008C8D";
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                //偶数
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
}
