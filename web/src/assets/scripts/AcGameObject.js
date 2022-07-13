// 代码脚本一般放到assets里面的scripts

/**
 * 这是一个基类，所有别的游戏类都要继承这个类
 */

//把所有游戏对象存进数组里
const AC_GAME_OBJECTS = [];

//把这个类export出去，使得别人可以调用这个类
export class AcGameObject{
    constructor(){
        AC_GAME_OBJECTS.push(this);
        this.timedelta = 0;//这帧执行的时刻距离上一帧执行的时刻的时间间隔
        this.has_called_start = false;//当前是否调用了start函数

    }

    start(){ //只执行一次

    }

    update(){ //除了第一帧之外，每一帧执行一次

    }

    on_destroy(){ //删除之前执行
    
    }


    destroy(){
        //用in遍历的是下标
        for(let i in AC_GAME_OBJECTS){
            const obj = AC_GAME_OBJECTS[i];
            if(obj == this){
                AC_GAME_OBJECTS.splice(i);//从对象数组中删除当前对象
                break;
            }
        }
    }

}

/**
 * 实现每秒钟所有游戏对象刷新60次
*/

let last_timestamp; //上一次执行的时刻
// timestamp表示当前执行的时刻
const step = timestamp => {
    //用of遍历的是值
    for(let obj of AC_GAME_OBJECTS){
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }else{
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    //一般浏览器每秒钟默认刷新60次，这个函数可以将step在每次渲染前执行一遍
    requestAnimationFrame(step)
}

//一般浏览器每秒钟默认刷新60次，这个函数可以将step在每次渲染前执行一遍
requestAnimationFrame(step)
