package stream;

/**
 * 새로운 스래드를 생성하여, ClientFrame을 실행한다.
 */
public class clientStartThread extends Thread {
	public void run() {
		new PcMenu.ClientFrame();
	};
}
