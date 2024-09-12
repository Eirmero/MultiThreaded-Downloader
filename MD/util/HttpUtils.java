package com.MD.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
//    获取HttpURLConnection链接对象
//    @param url 文件的地址
//    @return
    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        //向文件所在的服务器发送标识信息 浏览器的标识是模拟的
        httpURLConnection.setRequestProperty("User-Agent", "\"User-Agent\", \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36\"");
        return httpURLConnection;

    }

//    获取下载文件的文件名 思路是通过字符串进行截取
//    只有拿到下载文件的文件名 后续才能对下载文件进行重新命名
    public static String getHttpFileName(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

}
