package com.s17201120.player.server;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class  RequestExecute extends Thread{
    //将Socket定义为成员变量，利用构造方法初始化
    private Socket socket ;
    public RequestExecute(Socket socket){
        this.socket = socket;
    }
    public void run() {
        //从socket中取出输入流，然后从输入流中取出数据
        InputStream in = null;  //将字节输入流转换为缓冲字符输入流
        InputStreamReader reader = null;//转换流
        BufferedReader bufferedReader = null;//字符缓冲流

        //申明输出流  输出流是指向客户端的
        OutputStream out = null;
        PrintWriter pw = null;
        try{
            //从socket中获取字节输出流
            out = socket.getOutputStream();
            //将字节输出流包装成字符流
            pw = new PrintWriter(out);
            //从socket中获取字节输入流
            in = socket.getInputStream();
            //转换和包装
            reader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(reader);
            //循环的从字符流中获取字符
            String line = null;
            int lineNum = 1;
            //存储请求路径
            String reqPath = "";
            String host = "";
            while((line = bufferedReader.readLine())!=null){
                System.out.println(line);
                //解析请求行
                if(lineNum==1){//第一行   GET  /xxx/xx.HTML  HTTP1.1   /
                    //使用空格分割字符串
                    String[] infos = line.split(" ");
                    if(infos!=null || infos.length>2){
                        reqPath = infos[1];//请求路径

                    }else{
                        throw new RuntimeException("请求行解析失败:"+line);
                    }
                }else{
                    //解析其他行，取出Host的内容
                    String[] infos = line.split(": ");
                    if(infos!=null || infos.length==2){
                        //取出host
                        if(infos[0].equals("Host"))
                            host = infos[1];
                    }
                }
                lineNum ++;
                if(line.equals(""))//读取到空行就结束，因为HTTP请求是长连接，无法读取到文件的末尾
                    break;
            }

            //输出请求信息
            if(!reqPath.equals("")) {
                reqPath = URLDecoder.decode(reqPath,"utf-8");
                System.out.println("处理请求:http://" + host  + reqPath);

                //根据请求响应客户端   / 直接响应一个欢迎页面  /index.html  就输出对应的文件内容
                if(reqPath.equals("/")){//没有资源的名称
                    pw.println("HTTP/1.1 200 OK");//输出响应行
                    pw.println("Content-Type: text/html;charset=utf-8");
                    //输出空行
                    pw.println();//表示响应头结束，开始响应内容
                    pw.write("<h2>欢迎访问Geek-Server1.0</h2>");
                    pw.flush();
                    System.out.println("响应欢迎页面！");
                }else{
                    //查找对应的资源
                    //取出后缀
                    String ext = reqPath.substring(reqPath.lastIndexOf(".")+1);
                    reqPath = reqPath.substring(1);//去除前面的“/”;
                    System.out.println(reqPath);
                    //判断是在根目录下还是在其他的子目录下
                    if(reqPath.contains("/")){//子目录
                        //判断文件是否存在
                        File file = new File(Server.resourcePath+reqPath);
                        if(file.exists() && file.isFile()){
                            //将文件内容响应到客户端
                            resposne200(out,file.getAbsolutePath(),ext);
                        }else{
                            //响应404页面
                            response404(out);
                        }
                    }else{//根目录
                        //判断这个资源是否存在
                        //获取根目录下的所有的文件的名称
                        File root = new File(Server.resourcePath);
                        if(root.isDirectory()){
                            File[] list = root.listFiles();
                            boolean isExist =false;//标记访问的资源是否存在
                            for(File file : list){
                                if(file.isFile() && file.getName().equals(reqPath)){
                                    //文件存在
                                    isExist = true;
                                    break;
                                }
                            }
                            if(isExist){//文件存在
                                resposne200(out,Server.resourcePath+reqPath,ext);
                            }else{//文件不存在
                                response404(out);
                            }
                        }else{
                            throw new RuntimeException("静态资源目录不存在:"+Server.resourcePath);
                        }
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try{
                if(in!=null)
                    in.close();
                if(reader!=null)
                    reader.close();
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(pw!=null)
                    pw.close();
                if(out!=null)
                    out.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    /*
     *将指定的文件输出到输出流中
     */
    private void resposne200(OutputStream out,String filePath,String ext){
        PrintWriter pw = null;
        //准备输入流读取磁盘上的文件
        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {

            if(ext.equals("lrc")){
                out.write("HTTP/1.1 200 OK\r\n".getBytes());//输出响应行
                out.write("Content-Type: application/octet-stream\r\n".getBytes());
                //输出一个空行
                out.write("\r\n".getBytes());//输出空行，表示响应头结束
                //利用自己而输入流读取文件内容，并且输出到输入流中
                //创建输入流
                in = new FileInputStream(filePath);
                int len = -1;
                byte [] buff = new byte[1024];
                while((len = in.read(buff))!=-1){
                    out.write(buff,0,len);
                    out.flush();
                }
            } else{
                response404(out);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(pw!=null)
                    pw.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * 响应404页面
     */
    private void response404(OutputStream out){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(out);
            pw.println("HTTP/1.1 404");//输出响应行
            pw.println("Content-Type: text/html;charset=utf-8");
            //输出空行
            pw.println();//表示响应头结束，开始响应内容
            pw.write("<h2>欢迎访问Geek-Server1.0</h2>");
            pw.write("<h2>您找的资源跑丢了!</h2>");
            pw.flush();
            System.out.println("响应欢迎页面！");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(pw!=null)
                    pw.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
