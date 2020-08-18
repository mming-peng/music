package com.s17201120.player.client;

import com.s17201120.player.server.Server;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class ServerClient {
    public static void main(String[] args) {
        //获取端口
        int port = 8088;
        //创建Server并且启动
        Server server = new Server(port);
        new Thread(server).start();
    }





}
