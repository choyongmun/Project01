package stream;

/**
 * ���ο� �����带 �����Ͽ�, ClientFrame�� �����Ѵ�.
 */
public class clientStartThread extends Thread {
	public void run() {
		new PcMenu.ClientFrame();
	};
}
