package com.MD.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
框架中有很多已经写好的工具类
 */
public class LogUtils {
    //只想这个方法在这个类中使用 所以使用了private
    public static void info(String msg, Object... args) {
        print(msg, "-info-", args);
    }

    public static void error(String msg, Object... args) {
        print(msg, "-error-", args);

    }

    private static void print(String msg, String level, Object... args){
        if(args != null && args.length > 0){
            //传参后 进行字符串拼接
            msg = String.format(msg.replace("{}","%s"), args);
        }

        //多线程打印需要使用当前线程的名字
        String name = Thread.currentThread().getName();
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + name + level + ": " + msg);
    }

}
