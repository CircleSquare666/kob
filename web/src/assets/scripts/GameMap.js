//用于实现地图这个类
//有的时候import要用大括号括起来，有的时候不需要，区别在于如果是export default的话，就不需要括起来，如果是export的话，就要用大括号括起来
//export default每个文件只能有一个，类似于java中的public
import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    //parent是画布的父元素，用来动态修改画布的长宽
    constructor(ctx, parent, store) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;//表示绝对距离，L表示一个单位长度

        this.rows = 13;
        this.cols = 14;

        //内部障碍物的数量
        this.inner_walls_count = 20;

        //所有的障碍物包括边界墙
        this.walls = [];

        //创建两条蛇
        this.snakes = [new Snake({
            id: 0,
            color: "#AAD752",
            r: this.rows - 2,
            c: 1
        }, this),
        new Snake({
            id: 1,
            color: "#F94848",
            r: 1,
            c: this.cols - 2
        }, this)
        ];

    }
    create_walls() {
        //     new Wall(0, 0, this);

        //     const g = [];
        //     for (let r = 0; r < this.rows; r++) {
        //         g[r] = [];
        //         for (let c = 0; c < this.cols; c++) {
        //             g[r][c] = false;
        //         }
        //     }

        //     //给四周加上墙
        //     for (let r = 0; r < this.rows; r++) {
        //         g[r][0] = g[r][this.cols - 1] = true;
        //     }
        //     for (let c = 0; c < this.cols; c++) {
        //         g[0][c] = g[this.rows - 1][c] = true;
        //     }


        //     //中心对称创建随机障碍物，使游戏保证公平
        //     for (let i = 0; i < this.inner_walls_count / 2; i++) {
        //         for (let j = 0; j < 1000; j++) {
        //             let r = parseInt(Math.random() * this.rows);
        //             let c = parseInt(Math.random() * this.cols);
        //             if (g[r][c] == true || g[this.rows - 1 - r][this.cols - 1 - c] == true) continue;
        //             if (r == this.rows - 2 && c == 1 || c == this.cols - 2 && r == 1) continue;//两条蛇的起点
        //             g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
        //             break;
        //         }
        //     }

        //     const copy_g = JSON.parse(JSON.stringify(g));//深拷贝
        //     if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false;
        const g = this.store.state.pk.gamemap;

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
    // check_connectivity(g, sx, sy, tx, ty) {
    //     if (sx == tx && sy == ty) return true;
    //     g[sx][sy] = true;//标记当前位置走过了
    //     let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
    //     for (let i = 0; i < 4; i++) {
    //         let x = sx + dx[i], y = sy + dy[i];
    //         if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) return true;
    //     }
    //     return false;
    // }

    add_listening_events() {
        this.ctx.canvas.focus();//canvas要先聚焦
        // const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            // if (e.key === 'w') snake0.set_direction(0);
            // else if (e.key === 'd') snake0.set_direction(1);
            // else if (e.key === 's') snake0.set_direction(2);
            // else if (e.key === 'a') snake0.set_direction(3);
            // else if (e.key === 'ArrowUp') snake1.set_direction(0);
            // else if (e.key === 'ArrowRight') snake1.set_direction(1);
            // else if (e.key === 'ArrowDown') snake1.set_direction(2);
            // else if (e.key === 'ArrowLeft') snake1.set_direction(3);

            let d = -1;
            if (e.key === 'w') d = 0;
            else if (e.key === 'd') d = 1;
            else if (e.key === 's') d = 2;
            else if (e.key === 'a') d = 3;

            if (d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }));
            }
        });
    }

    start() {
        for (let i = 0; i < 1000; i++) {
            if (this.create_walls()) {
                break;
            }
        }
        this.add_listening_events();
    }

    //用于更新每一帧的边长（要在PlayGround区域中，找到一个最大的矩形区域）
    update_size() {
        // clientWidth是用来求当前div的宽度
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
    //由裁判判断两条蛇是否准备好下一回合了
    check_ready() {
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }

    next_step() {//让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }

    check_valid(cell) { //检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.c === cell.c && wall.r === cell.r) {
                return false;
            }
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {//特判蛇尾，只有当蛇增加长度的时候，才考虑蛇尾元素
                k--;
            }
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
                    return false;
                }
            }
        }
        return true;
    }

    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
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
