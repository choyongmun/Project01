package chat.main;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import chat.stream.ReceiveThread;
import chat.stream.SendThread;


public class ClientMain extends Main {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientMain() {
		super("Client");
		setLocation(1300, 100);
	}

	@Override
	/**
	 * localhost("127.0.0.1")의 포트넘버 50001으로 새로운 소켓을 생성한다.
	 */
	public void setSocket() throws UnknownHostException, IOException {
		super.socket = new Socket("127.0.0.1", 50001);

		updateView(Main.TIME_FORM.format(System.currentTimeMillis()) + "서버와 접속했습니다...");

		sThread = new SendThread(this, socket.getOutputStream());
		rThread = new ReceiveThread(this, socket.getInputStream());

		rThread.start();
	}
	/**
	 * 연결 해제
	 */
	@Override
	public void closeSocket() {
		try {
			if (null != socket)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new ClientMain();
	}
}
