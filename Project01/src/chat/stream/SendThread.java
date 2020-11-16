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
	 * caller가 실행중일 경우, 저장된 message를 전달한다.
	 * 전달된 s(String)이 exit일 경우 연결을 해제한다.
	 * @param s 전달받은 String
	 */
	public void sendMessage(String s) {
		if (caller.isRunning()) {
			if("exit".equalsIgnoreCase(s) ){
				caller.updateView("통신을 종료합니다...");
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
