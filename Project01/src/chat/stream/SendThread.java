package chat.stream;

import java.io.OutputStream;
import java.io.PrintWriter;

import chat.main.Main;

public class SendThread {

	private PrintWriter writer;
	private Main caller;
	private String name;

	public SendThread(Main caller, OutputStream outputStream) {
		name = caller.getName();
		this.caller = caller;
		writer = new PrintWriter(outputStream);
	}
	/**
	 * caller�� �������� ���, ����� message�� �����Ѵ�.
	 * ���޵� s(String)�� exit�� ��� ������ �����Ѵ�.
	 * @param s ���޹��� String
	 */
	public void sendMessage(String s) {
		if (caller.isRunning()) {
			if("exit".equalsIgnoreCase(s) ){
				caller.updateView("����� �����մϴ�...");
				caller.stop();
				return;
			}
			String message = Main.TIME_FORM.format(System.currentTimeMillis()) + " " + name + " : " + s;
			writer.write(message+ "\n");
			writer.flush();
			caller.updateView(message);
		}
		else
			writer.close();
	}
	
	
}
