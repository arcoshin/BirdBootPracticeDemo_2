package com.tedu.birdboot.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 線程任務類--->負責與用戶端進行HTTP交互
 */
public class ClientHandler implements Runnable {
    Socket socket;
    public final static int CR = 13;//迴車符
    public final static int LF = 10;//換行符

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            /**
             * 建立輸入流，接收用戶的數據
             */
            BufferedInputStream bis = new BufferedInputStream(
                    socket.getInputStream()
            );

            /**
             * 1.解析請求
             */

            //獲得請求行
            int d;
            char now = 0;//當前的讀取字符
            char pre = 0;//上一個讀取字符
            StringBuilder builder = new StringBuilder();//字符串容器
            while ((d = bis.read()) != -1) {
                now = (char) d;
                if (pre == CR && now == LF) {
                    break;
                }
                builder.append((char) d);
                pre = now;
            }
            String line = builder.toString().trim();

            //1.1解析請求行
            //解析請求行
            String method;//請求方法
            String uri;//抽象路徑
            String protocol;//協議版本

            String[] split = line.split("\\s");//以空白作為分割依據
            method = split[0];
            uri = split[1];
            protocol = split[2];

            System.out.println("method:" + method);
            System.out.println("uri:" + uri);
            System.out.println("protocol:" + protocol);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
