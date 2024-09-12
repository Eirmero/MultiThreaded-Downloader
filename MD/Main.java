package com.MD;

import com.MD.core.Downloader;
import com.MD.util.LogUtils;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //下载地址
        String url = null;
        if(args == null || args.length == 0) {
            //防止用户没有输出url的情况 写个死循环
            for(; ; ){
                LogUtils.info("请输入下载地址");
//                System.out.println("请输入下载链接");
                Scanner scanner = new Scanner(System.in);
                url = scanner.next();
                if(url != null) {
                    break;
                }
            }
        }else {
            //不再对地址进行校验
            url = args[0];
        }

        Downloader downloader = new Downloader();
        downloader.download(url);
        //接下来测试下载是否能成功
        //测试用url https://github.com/Zilize/DrawCV/archive/refs/heads/main.zip
        //测试用url https://dldir1.qq.com/qqfile/qq/QQNT/Windows/QQ_9.9.15_240902_x64_01.exe
        //测试成功 bug1(空指针报错)已修复

    }
}