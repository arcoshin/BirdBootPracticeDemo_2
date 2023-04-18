package com.tedu.birdboot.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BirdBootApplication {
    private ServerSocket serverSocket;

    public BirdBootApplication() {
        try {
            /**
             * 啟動服務器
             */
            System.out.println("正在啟動服務器......");
            serverSocket = new ServerSocket(9188);
            System.out.println("服務器已啟動完畢!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        try {
            while (true) {
                /**
                 * 主線程負責不斷連接用戶端連接
                 */
                System.out.println("正在等待用戶端連接......");
                Socket socket = serverSocket.accept();
                System.out.println("一個用戶端已連接......");

                /**
                 * 創建另一線程負責收發數據
                 */
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BirdBootApplication().start();
    }
}
