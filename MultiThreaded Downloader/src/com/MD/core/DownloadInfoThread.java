package com.MD.core;

import com.MD.constant.Constant;

public class DownloadInfoThread implements Runnable{

    //写点成员变量来计算日志的内容
    private long httpFileContentLength; //文件大小
    public double finishedSize; //本地已下载文件的大小 这个参数后续可以利用来编写断点下载功能
    public volatile double downSize; //本次累计下载的大小 volatile 研究一下这个volatile 强制方法去内存里面读取数据
    public double prevSize; //之前下载的文件大小 用downSize - prevSize就得到了最近的这个时间区间内的下载文件大小 也就可以计算出速度了


    public DownloadInfoThread(long httpFileContentLength) {
        this.httpFileContentLength = httpFileContentLength;
    }

    @Override
    public void run() {
        //先计算总文件大小 进行一个单位换算 IO下载流这里的单位是字节 但是一般都看M或者G
        String httpFileSize = String.format("%.2f", httpFileContentLength / Constant.MB);//由于可能除不尽 所以对小数点进行处理

        //计算每秒的下载速度 只要固定时间为1s 则直接-就可以了 不用再进行其他的运算 MB
        int speed = (int) ((downSize - prevSize) / (1024d * 1024d));
        prevSize = downSize;//每次计算后更新

        //计算剩余文件的大小  总大小-当次下载大小-本地文件大小
        double remainSize = httpFileContentLength - finishedSize - downSize;
        //估算剩余时间
        String remainTime = String.format("%.1f",remainSize / speed);
        //如果时间无限大 则进行特殊情况处理
        if("Infinity".equalsIgnoreCase(remainTime)){
            remainTime = "-";
        }

        //已下载大小
        String currentFileSize = String.format("%.2f", (downSize - finishedSize)/Constant.MB);

        //最后进行字符串拼接生成日志
        String downInfo = String.format("下载进度：%smb/%smb, 速度 %smb/s, 剩余时间 %ss",
                currentFileSize, httpFileSize, remainTime, currentFileSize);

        System.out.println("\r");
        System.out.println(downInfo);






    }
}
