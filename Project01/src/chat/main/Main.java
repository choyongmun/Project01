package chat.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.stream.ReceiveThread;
import chat.stream.SendThread;



public abstract class Main extends JFrame {
	public static final SimpleDateFormat TIME_FORM = new SimpleDateFormat("[YYYY/MM/dd HH:mm:ss]");
	private boolean run = true;
	private static final long serialVersionUID = 1L;
	private JTextField field;
	private JButton button;
	private JTextArea textArea;

	protected SendThread sThread;
	protected ReceiveThread rThread;
	protected Socket socket;

	/**
	 * ���� ����
	 */
	public void stop() {
		run = false;
	}
	/**
	 * @return ������ ����Ǿ� �ִٸ� true, �ƴ϶�� false
	 */
	public boolean isRunning() {
		return run;
	}
	/**
	 * name���� Panel�� �����Ѵ�.
	 * �־��� name���� �̸��� �����ȴ�.
	 * @param name
	 */
	public Main(String name) {
		super(name);

		super.setName(name);
		super.setSize(new Dimension(400, 800));
		super.setResizable(false);

		field = initTextField();
		button = initButton();

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(field, BorderLayout.CENTER);
		panel.add(button, BorderLayout.EAST);

		super.setLayout(new BorderLayout());
		super.add(panel, BorderLayout.SOUTH);
		super.add(initMainPanel(), BorderLayout.CENTER);
		// super.pack();
		try {
			setSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.setVisible(true);

	}
	/**
	 * panel�� �ʱ�ȭ �� �����Ѵ�.
	 * @return ������� JPanel�� return
	 */
	private JScrollPane initMainPanel() {
		JScrollPane panel = new JScrollPane(textArea = initTextArea(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return panel;
	}
	/**
	 * textArea�� ���� �� �ʱ�ȭ
	 * @return ������� JTextArea�� return
	 */
	private JTextArea initTextArea() {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		return textArea;
	}
	/**
	 * btn�� ���� �� �ʱ�ȭ
	 * btn�� Ŭ���� ���, send()�� ����
	 * @return	������� JButton�� return
	 */
	private JButton initButton() {
		JButton btn = new JButton("����");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				send();
			}
		});
		return btn;
	}
	/**
	 * textField�� ���� �� �ʱ�ȭ
	 * textField�� Ŭ���� ���, send()�� ����
	 * @return ������� JTextField�� return
	 */
	private JTextField initTextField() {
		JTextField textField = new JTextField();
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
				}
			}
		});

		return textField;
	}
	/**
	 * �־��� message�� �߰��Ͽ� ȭ���� �����Ѵ�.
	 * @param message
	 */
	public void updateView(String message) {
		textArea.setText(textArea.getText() + message + "\n");
	}

	/**
	 * �ش� �����츦 �����Ѵ�.
	 */
	@Override
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			closeSocket();
		}
	}
	/**
	 * textField���� �־��� text�� �����Ѵ�.
	 */
	private void send() {
		String text = field.getText();
		field.setText(null);
		sThread.sendMessage(text);
	}

	public abstract void setSocket() throws UnknownHostException, IOException;

	public abstract void closeSocket();

}
