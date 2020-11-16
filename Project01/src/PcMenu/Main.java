package PcMenu;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import chat.main.ServerMain;
import stream.clientStartThread;


public class Main {
	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				ServerMain.main(null);
			}
		}).start();

		clientStartThread cst = new clientStartThread();
		cst.start();

		JFrame frame = new JFrame("������");
		JPanel chatPanel;
		JTextArea orderTextArea;
		orderTextArea = new JTextArea();
		JScrollPane orderTextAreaPanel = new JScrollPane(orderTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		frame.setSize(250,400);
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());

		frame.add(chatPanel);
		frame.add(orderTextAreaPanel);
		frame.setVisible(true);


		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;

		try{
			String orderMessage ="";
			serverSocket = new ServerSocket(50000);
			socket = serverSocket.accept();
			System.out.println("Ŭ���̾�Ʈ�� �����߽��ϴ�.");
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream());

			System.out.println("��Ʈ�� ����");

			while(true) {
				String message = bufferedReader.readLine();
				if(message == null) {
					System.out.println("Ŭ���̾�Ʈ ���� ����!");
					break;
				}
				System.out.println(message);
				orderMessage+=message+"\n";
				orderTextArea.setText(orderMessage);
			}

			System.out.println("������ �����մϴ�.");

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(null != printWriter) {
					printWriter.close();
				}
				if(null != bufferedReader) {
					bufferedReader.close();
				}
				if(null != socket) {
					socket.close();
				}
				if(null != serverSocket) {
					serverSocket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
