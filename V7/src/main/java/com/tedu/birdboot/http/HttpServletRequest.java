package com.tedu.birdboot.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 處理請求
 */
public class HttpServletRequest {
    private Socket socket;

    public final static int CR = 13;//迴車符
    public final static int LF = 10;//換行符

    private String method;//請求方法
    private String uri;//抽象路徑
    private String protocol;//協議版本

    private Map<String, String> headers = new HashMap<>();//存放消息頭的鍵值對

    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;

        /**
         * 1.解析請求
         */

        //1.1解析請求行
        parseRequestLine();

        //1.2解析消息頭
        parseHeaders();

        //1.3解析消息正文
        parseContent();
    }

    private void parseRequestLine() throws IOException {
        String line = readLine();
        System.out.println("請求行: " + line);//GET /index.html HTTP/1.1

        //解析請求行
        String[] split = line.split("\\s");//以空白作為分割依據
        method = split[0];
        uri = split[1];
        protocol = split[2];

        System.out.println("method:" + method);
        System.out.println("uri:" + uri);
        System.out.println("protocol:" + protocol);
    }

    private void parseHeaders() throws IOException {

        //讀取消息頭
        while (true) {
            String line = readLine();
            if (line.isEmpty()) {
                break;
            }
            System.out.println("消息頭: " + line);

            //解析消息頭
            String[] split = line.split(":\\s");//以": "為分割依據

            String key = split[0];
            String value = split[1];
            headers.put(key, value);
        }
        System.out.println("headers:" + headers);
    }

    private void parseContent(){

    }

    /**
     * 讀一行請求的方法
     */
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

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
