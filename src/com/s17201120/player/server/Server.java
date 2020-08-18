package com.s17201120.player.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class Server implements Runnable{
    //端口号
    private int port;
    public static String resourcePath = "C:\\Users\\Ming\\Desktop\\歌词\\";

    public Server() {
    }

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        //线程的主程序
        //申明ServerSocket对象
        ServerSocket serverSocket = null;
        try{
            //创建ServerSocket对象
            serverSocket = new ServerSocket(port);
            //开始监听
            System.out.println("开始监听.....");
            System.out.println("监听端口:"+port);
            System.out.println("静态资源的路径:"+Server.resourcePath);
            boolean isRun = true;
            while(isRun){//监听端口
                Socket socket = serverSocket.accept();
                System.out.println("接收到请求.....");
                //将socket交给ReqestExecute处理
                RequestExecute re = new RequestExecute(socket);
                re.start();
            }
            //运行到这里说明程序要停止
            //关闭ServerSocket
            serverSocket = null;
            System.out.println("服务监听停止!");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("端口"+port+"监听失败."+e.getMessage());
        }

    }
}
