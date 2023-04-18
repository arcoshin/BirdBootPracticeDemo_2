package com.tedu.birdboot.core;

import com.tedu.birdboot.http.HttpServletRequest;

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

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            /**
             * 1.解析請求
             */
            HttpServletRequest request = new HttpServletRequest(socket);
            String path = request.getUri();//抽象路徑
            System.out.println("抽象路徑:" + path);

            /**
             * 2.處理請求
             */

            /**
             * 3.發送響應
             */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
