package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class Server {
    private static DatagramSocket mSocket;

    public static void main(String[] args) {
        try {
            // 1.初始化DatagramSocket，指定端口号
            mSocket = new DatagramSocket(8888);
            // 2.创建用于接收消息的DatagramPacket，指定接收数据大小

            // 3.接收客户端消息
            System.out.println("***************服务器开始监听消息***************");
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                mSocket.receive(receivePacket);// 在接收到信息之前，一直保持阻塞状态

                System.out.println("客户端说:" + new String(receiveData)+" ip  "+ receivePacket.getAddress().getHostAddress());
                HandleThread handleThread = new HandleThread(receivePacket);
                handleThread.setPriority(4);
                handleThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSocket.close();// 关闭资源
        }
    }

    static class HandleThread extends Thread {
        private DatagramPacket mPacket;
        private DatagramSocket mSocket;

        public HandleThread(DatagramPacket packet) {
            super();
            mPacket = packet;
        }

        @Override
        public void run() {
            try {
                // 4.创建用于发送消息的DatagramPacket
                byte[] sendData = "hello,我是服务端".getBytes();
                SocketAddress remoteAddress = mPacket.getSocketAddress();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, remoteAddress);
                // 5.向客户端发送消息
                mSocket = new DatagramSocket();
                mSocket.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mSocket != null) {
                    mSocket.close();// 关闭资源
                }
            }
        }
    }
}