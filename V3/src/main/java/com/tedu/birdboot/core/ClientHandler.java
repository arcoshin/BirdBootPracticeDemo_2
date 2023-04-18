package com.tedu.birdboot.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
             * 1.解析請求
             */

            //1.1解析請求行
            String line = readLine();
            System.out.println("請求行: " + line);//GET /index.html HTTP/1.1

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

            //1.2解析消息頭
            Map<String, String> headers = new HashMap<>();//存放消息頭的鍵值對
            //讀取消息頭
            while (true) {
                line = readLine();
                if (line.isEmpty()) {
                    break;
                }
                System.out.println("消息頭: " + line);

                //解析消息頭
                split = line.split(":\\s");//以": "為分割依據

                String key = split[0];
                String value = split[1];
                headers.put(key, value);
            }
            System.out.println("headers:" + headers);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLine() throws IOException {
        /**
         * 建立輸入流，接收用戶的數據
         * 對一個socket實例連續調用getInputStream()返回的始終都是同一條輸入流
         * 因而造成無法連續讀取
         */
        InputStream in = socket.getInputStream();

        //獲得一行請求
        int d;
        char now = 0;//當前的讀取字符
        char pre = 0;//上一個讀取字符
        StringBuilder builder = new StringBuilder();//字符串容器
        while ((d = in.read()) != -1) {
            now = (char) d;
            if (pre == CR && now == LF) {
                break;
            }
            builder.append((char) d);
            pre = now;
        }

        return builder.toString().trim();
    }
}
