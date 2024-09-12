package com.MD.core;

import com.MD.constant.Constant;
import com.MD.util.FileUtils;
import com.MD.util.HttpUtils;
import com.MD.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
下载器
 */
public class Downloader {
    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    public void download(String url) {
        //获取文件名
        String httpFileName = HttpUtils.getHttpFileName(url);

        //设置文件下载目录
        httpFileName = Constant.PATH + httpFileName;
        System.out.println(httpFileName);

        //获取文件的大小
        long localFileLength = FileUtils.getFileContentLength(httpFileName);


        //获取连接对象
        HttpURLConnection httpURLConnection = null;
        DownloadInfoThread downloadInfoThread = null; //因为后面需要用到dIT的方法所以需要把命名提出来 这样才能使用 在try里面是用不了的

        try {
            httpURLConnection= HttpUtils.getHttpURLConnection(url);
            int contentLength = httpURLConnection.getContentLength();//获取文件的总大小
            //对本地文件盒获取文件的大小进行判断 如果大小一致 说明已经下载完了(过了)
            if (localFileLength == contentLength) {
                LogUtils.info("已下载完，无需重新下载", httpFileName);
                return; //如果下载过了 就直接return就好了~
            } else if (localFileLength > 0) {
                LogUtils.info("文件未下载完，继续下载", httpFileName);
            }

            //创建获取下载信息的任务对象
            downloadInfoThread = new DownloadInfoThread(contentLength);
            //因为每秒打印一次 所以使用新线程 每隔1s执行一次
            scheduledExecutorService.scheduleAtFixedRate(downloadInfoThread, 1, 1, TimeUnit.SECONDS);
            //不要忘了释放线程 在finally里面关闭就行

        } catch (IOException e) {
            e.printStackTrace();
        }

        //通过连接对象拿到输入流
        try(
                InputStream input = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                FileOutputStream fos = new FileOutputStream(httpFileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
                //这里IO流结束可以自动关闭
                ){
            int len = -1; //?
            byte[] buffer = new byte[Constant.BYTE_SIZE];
            while ((len = bis.read()) != -1) {
                downloadInfoThread.downSize += len; //这里可以加封装 调用setget方法 后面再来完成
                //这里的作用就相当于一个累加器
                bos.write(buffer, 0, len);    //本质上就是利用IO流 在网络上需要拿到对象 连接后和IO流后续的操作一样
                //IO流下载的具体实现方法 在这里思考如何计算累计的下载大小
                //思考这个buffer大小的问题 IO流里buffer默认大小是8192 完全不够用 所以需要自己新建一个更大的
            }

        }catch (FileNotFoundException e){
//            System.out.println("下载的文件不存在{}" + url);
            LogUtils.error("下载的文件不存在{}", url);
        }catch (Exception e){
            LogUtils.error("下载失败");
            e.printStackTrace();
        }finally {
            System.out.print("\r");
            System.out.println("下载完成");
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
                // 这里没法自动关 所以要手动写
            }

            //关闭日志打印线程
            scheduledExecutorService.shutdown();
        }

    }
}
