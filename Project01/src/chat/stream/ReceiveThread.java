package chat.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import chat.main.Main;


public class ReceiveThread extends Thread {

	private BufferedReader bReader;
	private Main caller;
	
	public ReceiveThread(Main caller, InputStream inputStream) {
		
		super(caller.getName());
		
		this.caller = caller;
		
		bReader = new BufferedReader(new InputStreamReader(inputStream));
	}
	/**
	 * message�� null�̰ų�, exit�� ��� ������ �����Ѵ�.
	 * �׷��� ���� ���, message�� ȭ�鿡 ����.
	 */
	public void run() {
		try {
			while (caller.isRunning()) {
				String message;

				message = bReader.readLine();
				if (null == message) {
					caller.stop();
				}
				
				//System.out.println(message);
				caller.updateView(message);
				
				if("exit".equalsIgnoreCase(message)) {
					caller.stop();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}
	/**
	 * ���� ����
	 */
	private void close() {
		if (null != bReader)
			try {
				bReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
