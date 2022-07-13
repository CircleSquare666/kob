//用于实现障碍物这个类
import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject {
    //传入第r行第c列以及
    constructor(r, c, gamemap) {
        super();
        this.r = r;
        this.c = c;
        this.gamemap = gamemap;

    }

    update() {
        this.render();
    }
    render() {
        const color = "#8E4C29";
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.fillStyle = color;
        ctx.fillRect(this.c * L, this.r * L, L, L);
    }
}