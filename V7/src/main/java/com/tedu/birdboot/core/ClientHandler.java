package com.tedu.birdboot.core;

import com.tedu.birdboot.http.HttpServletRequest;
import com.tedu.birdboot.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
             * 1.解析請求(V4->HttpServletRequest)
             */
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);

            /**
             * 2.處理請求
             */
            String path = request.getUri();//抽象路徑
            System.out.println("抽象路徑:" + path);

            //定位當前目錄的類加載路徑
            File baseDir = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );

            //定位類加載路徑下的static目錄
            File staticDir = new File(baseDir, "static");

            //定位static目錄下的index.html
            File file = new File(staticDir, path);

            /**
             * 判定是否需要進入404的分支
             */
            if (file.isFile()) {//如果file非目錄且非空
                response.setStatusCode(200);
                response.setStatusReason("OK");
                response.setContentFile(file);
            } else {//404
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                response.setContentFile(new File(staticDir,"404.html"));
            }

            /**
             * 3.發送響應
             */
            response.response();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
        }
    }


}
