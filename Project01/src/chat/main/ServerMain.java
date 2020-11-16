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
	 * 포트넘버 50001로 새로운 소켓을 생성한다.
	 */
	@Override
	public void setSocket() throws IOException {
		serverSocket = new ServerSocket(50001);
		updateView(Main.TIME_FORM.format(System.currentTimeMillis()) + "서버 대기중...");
		
		socket = serverSocket.accept();
		updateView(Main.TIME_FORM.format(System.currentTimeMillis()) + "클라이언트가 접속했습니다...");

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
