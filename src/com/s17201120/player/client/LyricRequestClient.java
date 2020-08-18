package com.s17201120.player.client;

import com.s17201120.player.util.PathUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class LyricRequestClient {
    private String reqPath;

    public LyricRequestClient() {
    }

    public LyricRequestClient(String reqPath) {
        this.reqPath = reqPath;
    }

    public void requestLyric(String reqPath){
        try{
            //System.out.println(reqPath);
            String encoderUrl = URLEncoder.encode(reqPath, "UTF-8");
            //encoderUrl = encoderUrl.replaceAll("-","%20-%20");
            encoderUrl = encoderUrl.replaceAll("\\+", "%20");
            URL url = new URL("http://127.0.0.1:8088/"+encoderUrl);
            System.out.println(url);
            if(saveFile(url)){
                System.out.println(reqPath+"下载成功！");
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常" + e);
            e.printStackTrace();
        }
    }

    public static boolean saveFile(URL u) throws IOException {
        URLConnection uc = u.openConnection();
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();
        if (contentType.startsWith("text/") || contentLength == -1 ) {
            throw new IOException("This is not a binary file.");
        }

        try (InputStream raw = uc.getInputStream()) {
            InputStream in  = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int offset = 0;
            while (offset < contentLength) {
                int bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) break;
                offset += bytesRead;
            }
            if (offset != contentLength) {
                throw new IOException("Only read " + offset
                        + " bytes; Expected " + contentLength + " bytes");
            }
            String filename = u.getFile();
            filename = filename.substring(1);
            filename = PathUtils.LYRIC_PATH + File.separator+ URLDecoder.decode(filename,"utf-8");
            System.out.println(filename);
            try (FileOutputStream fout = new FileOutputStream(filename)) {
                fout.write(data);
                fout.flush();
            }
            return true;
        }
    }

    public String getReqPath() {
        return reqPath;
    }

    public void setReqPath(String reqPath) {
        this.reqPath = reqPath;
    }
}
