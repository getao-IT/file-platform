package cn.aircas.fileManager.tcpupload;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Servers {

    public static void main(String[] args) throws IOException {

        FileOutputStream fileOs = null;
        InputStream is = null;
        Socket socket = null;
        OutputStream outputStream = null;

        // 1. 建立服务端口
        ServerSocket serverSocket = new ServerSocket(8088);

        while (true) {
            // 2. 获取客户连接
            socket = serverSocket.accept();

            // 3. 处理输入流
            is = socket.getInputStream();
            String path = "D:\\temp\\nio\\socket\\葛涛亲测可用ideaIU-2020.1.exe";
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fileOs = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                fileOs.write(bytes, 0, len);
            }

            // 4. 通知客户我接受完了
            outputStream = socket.getOutputStream();
            outputStream.write("您好客户，我接收完了".getBytes(StandardCharsets.UTF_8));

            // 5. 关闭资源
            outputStream.close();
            fileOs.close();
            is.close();
            socket.close();
        }
    }
}
