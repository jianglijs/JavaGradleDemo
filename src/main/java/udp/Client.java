package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Client {

    private static DatagramSocket mSocket;

    public static void main(String[] args) {
        try {
            // 1.初始化DatagramSocket
            mSocket = new DatagramSocket();
            mSocket = new DatagramSocket();
            // 2.创建用于发送消息的DatagramPacket
            InetSocketAddress address = new InetSocketAddress("localhost", 8888);
            byte[] sendData = ("hello,我是客户端" + (int) (Math.random() * 100)).getBytes();// 随机数模拟不同的客户端
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address);
            // 3.向服务端发送消息
            mSocket.send(sendPacket);
            // 4.创建用于接收消息的DatagramPacket
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            // 5.接收服务端消息
            mSocket.receive(receivePacket);
            System.out.println("服务端说:" + new String(receiveData));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            if (mSocket != null) {
                mSocket.close();
            }
        }
    }

}