package chat.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import chat.stream.ReceiveThread;
import chat.stream.SendThread;


public class ServerMain extends Main {
	private ServerSocket serverSocket;
	private Socket socket;

	private static final long serialVersionUID = 1L;

	public ServerMain() {
		super("Server");
		setLocation(1100, 100);
	}

	/**
	 * ��Ʈ�ѹ� 50001�� ���ο� ������ �����Ѵ�.
	 */
	@Override
	public void setSocket() throws IOException {
		serverSocket = new ServerSocket(50001);
		updateView(Main.TIME_FORM.format(System.currentTimeMillis()) + "���� �����...");
		
		socket = serverSocket.accept();
		updateView(Main.TIME_FORM.format(System.currentTimeMillis()) + "Ŭ���̾�Ʈ�� �����߽��ϴ�...");

		sThread = new SendThread(this, socket.getOutputStream());
		rThread = new ReceiveThread(this, socket.getInputStream());

		rThread.start();

	}

	public static void main(String[] args) {
		new ServerMain();
	}
	@Override
	public void closeSocket() {
		try {
			System.out.println("Closing...");
			if (null != socket)
				socket.close();
			if (null != serverSocket)
				serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
