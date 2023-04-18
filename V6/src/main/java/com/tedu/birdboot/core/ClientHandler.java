package com.tedu.birdboot.core;

import com.tedu.birdboot.http.HttpServletRequest;

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

    public final static int CR = 13;//迴車符
    public final static int LF = 10;//換行符

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
            int statusCode;
            String statusReason;
            if (file.isFile()) {//如果file非目錄且非空
                statusCode = 200;
                statusReason = "OK";
            } else {//404
                statusCode = 404;
                statusReason = "NotFound";
            }

            /**
             * 3.發送響應
             */
//                一則響應的大致格式
//                HTTP/1.1 200 OK(CRLF) <---狀態行
//                Content-Type: text/html(CRLF) <---響應頭
//                Content-Length: 2546(CRLF)(CRLF) <---響應頭
//                1011101010101010101......(index.html页面内容) <---響應正文
            //3.0建立輸入(響應正文的實體對象)與輸出(socket.getOutputStream)的流
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file)//從響應正文的實體對象的輸入流
            );

            BufferedOutputStream bos = new BufferedOutputStream(
                    socket.getOutputStream()//藉由socket響應給瀏覽器的輸出流
            );

            //3.1發送狀態行
            println("HTTP/1.1" + " " + statusCode + " " + statusReason);

            //3.2發送響應頭
            println("Content-Type: text/html");
            println("Content-Length: " + file.length());//響應正文實體對象的文件大小
            println("");//響應頭末尾須單獨發送一行迴車換行

            //3.3發送響應正文
            int d;
            while ((d = bis.read()) != -1) {
                bos.write((char) d);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void println(String line) {
        try {
            //建立流
            BufferedOutputStream bos = new BufferedOutputStream(
                    socket.getOutputStream()//藉由socket響應給瀏覽器的輸出流
            );
            bos.write(line.getBytes(StandardCharsets.ISO_8859_1));
            bos.write(CR);
            bos.write(LF);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
