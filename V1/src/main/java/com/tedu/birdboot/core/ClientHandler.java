package com.tedu.birdboot.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

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
             * 建立輸入流，接收用戶的數據
             */
            BufferedInputStream bis = new BufferedInputStream(
                    socket.getInputStream()
            );

            int d;
            while ((d = bis.read()) != -1) {
                System.out.print((char) d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
