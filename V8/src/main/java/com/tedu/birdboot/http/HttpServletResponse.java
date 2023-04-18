package com.tedu.birdboot.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 處理響應
 */
public class HttpServletResponse {
    private Socket socket;

    public final static int CR = 13;//迴車符
    public final static int LF = 10;//換行符

    private int statusCode = 200;
    private String statusReason = "OK";

    private File contentFile;//響應正文對應的實體化對象

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }

    public void response() throws IOException {

        //3.1發送狀態行
        sendStatusLine();

        //3.2發送響應頭
        sendHeaders();

        //3.3發送響應正文
        sendContent();
    }

    private void sendStatusLine(){
        println("HTTP/1.1" + " " + statusCode + " " + statusReason);

    }

    private void sendHeaders(){
        println("Content-Type: text/html");
        println("Content-Length: " + contentFile.length());//響應正文實體對象的文件大小
        println("");//響應頭末尾須單獨發送一行迴車換行

    }

    private void sendContent() throws IOException {
        //建立輸入(響應正文的實體對象)與輸出(socket.getOutputStream)的流
        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(contentFile)//從響應正文的實體對象的輸入流
        );

        BufferedOutputStream bos = new BufferedOutputStream(
                socket.getOutputStream()//藉由socket響應給瀏覽器的輸出流
        );

        int d;
        while ((d = bis.read()) != -1) {
            bos.write((char) d);
            bos.flush();
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


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }


}
