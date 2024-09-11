package com.MD.learn;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {
    public static void main(String[] args) {
        //参数1 创建一个核心线程
        ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        s.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis());
            //在任务中模拟耗时操作
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, 3, TimeUnit.SECONDS);
        //需要注意的是 在scheduleAtFixedRate方法里 period里会包含sleep的时间
        //scheduleWithFixedDelay方法会严格按照定好的时间执行，不会覆盖，写法是一样的
//
//        //延时2s后再执行任务 第一个参传的lambda表达式
//        s.schedule(() -> System.out.println(Thread.currentThread().getName()), 2, TimeUnit.SECONDS);
//
//        //记得用完后关闭线程
//        s.shutdown();
//        这个方法只是延时，所以不满足1s一次的方法

    }


}
