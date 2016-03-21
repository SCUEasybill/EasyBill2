package com.scu.easybill.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by guyu on 2016/2/1.
 */
public class MyConnector {
    Socket socket = null;
    public DataInputStream din = null;
    public DataOutputStream dout = null;

   public MyConnector(String address, int port){
       try {
           socket = new Socket(address,port);
           din = new DataInputStream(socket.getInputStream());//输入流
           dout = new DataOutputStream(socket.getOutputStream());//输出流
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    /**
     * 方法：断开连接，释放资源
     */
    public void sayBye(){
        try {
            dout.writeUTF("<#USER_LOGOUT#>");
            din.close();
            dout.close();
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
