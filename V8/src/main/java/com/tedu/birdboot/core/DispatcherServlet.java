package com.tedu.birdboot.core;

import com.tedu.birdboot.http.HttpServletRequest;
import com.tedu.birdboot.http.HttpServletResponse;

import java.io.File;
import java.net.Socket;
import java.net.URISyntaxException;

/**
 * 程序調度員
 */
public class DispatcherServlet {
    private static File baseDir;
    private static File staticDir;
    static {
        try {
            //定位當前目錄的類加載路徑
            baseDir = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );

            //定位類加載路徑下的static目錄
            staticDir = new File(baseDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        //獲得抽象路徑
        String path = request.getUri();
        System.out.println("抽象路徑:" + path);//綁定抽象路徑對應的實體文件

        //定位static目錄下的index.html
        File file = new File(staticDir, path);

        //判定是否需要進入404的分支
        if (file.isFile()) {//如果file非目錄且非空
            response.setContentFile(file);
        } else {//404
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            response.setContentFile(new File(staticDir, "404.html"));
        }
    }
}
