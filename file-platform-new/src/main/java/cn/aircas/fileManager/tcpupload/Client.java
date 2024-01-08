/*
package cn.aircas.fileManager.tcpupload;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@Slf4j
public class Client {

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();
        log.info("======================================开始：{}",start);

        // 1. 新建连接
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        Socket socket = new Socket(ip, 8088);

        // 2. 建立输出流
        OutputStream os = socket.getOutputStream();
        
        // 3. 读取文件
        File file = new File("D:\\BaiduNetdiskDownload\\葛涛亲测可用ideaIU-2020.1.exe");
        FileInputStream fileIs = new FileInputStream(file);

        // 4. 写入文件
        byte[] bytes = new byte[1024];
        int len;
        while ((len = fileIs.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }

        // 5. 通知服务器发送结束
        socket.shutdownOutput();

        // 6. 知道服务器接收完了
        InputStream serIs = socket.getInputStream();
        ByteOutputStream byteOs = new ByteOutputStream();
        byte[] bytes1 = new byte[1024];
        int len1;
        while ((len = serIs.read(bytes1)) != -1) {
            byteOs.write(bytes1, 0, len);
        }

        System.out.println(byteOs.toString());

        // 关闭资源
        byteOs.close();
        serIs.close();
        os.close();
        fileIs.close();
        socket.close();
        long consume = System.currentTimeMillis() - start;
        log.info("传输耗时：{} ", consume);
    }
}
*/
