package PcMenu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ClientFrame extends JFrame implements ActionListener{
	UserDao dao = null;
	JPanel loginPanel, loginTextFieldPanel, loginTextAreaPanel, loginButtonPanel;
	JPanel loginTextPanel;
	JTextArea loginIdTextArea, loginPasswordTextArea; 
	JTextField loginIdTextField, loginPasswordTextField;
	JButton loginButton;
	JButton createIdButton,findIdButton;

	private Socket socket;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	
	/**
	 * ip = local host ("127.0.0.1")
	 * ��Ʈ��ȣ = 50000 ���� ���ϰ� ��Ʈ���� �����Ѵ�.
	 */
	public void OrderServerConnection() {
		try {
			socket = new Socket("127.0.0.1",50000);
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * �α��� panel�� �����ϴ� �۾��� �Ѵ�.
	 * loginTextAreaPanel = ȭ�鿡�� "���̵�","��й�ȣ" text�� �����ش�.
	 * loginTextFieldPanel = ȭ�鿡�� ����ڰ� "���̵�"�� "��й�ȣ"�� �Է��� �� �ְ� �Ѵ�.
	 * 		loginButton = �α��ο� ������ �� �ֵ��� �ϸ�, ���� Panel�� ���Ѵ�.
	 * loginButtonPanel = ȭ�鿡�� "ȸ������"�� "���̵�ã��"�� ��ư�� ��밡���ϰ� �Ѵ�.
	 * 
	 * createIdButton(ȸ������), findIdButton(���̵�ã��), loginButton(�α���)
	 * 	��ư�� ActionListener�� �߰������μ�, ��ư�� ����� �߰��Ѵ�.
	 */
	public void setLoginPanel() {
		loginPanel = new JPanel();
		loginTextFieldPanel = new JPanel();
		loginTextAreaPanel = new JPanel();
		loginButtonPanel = new JPanel();
		loginTextPanel = new JPanel();
		//LoginTextAreaPanel
		loginIdTextArea = new JTextArea("���̵�");
		loginPasswordTextArea = new JTextArea("��й�ȣ");
		loginIdTextArea.setEditable(false);
		loginPasswordTextArea.setEditable(false);
		//LoginTextFieldPanel
		loginIdTextField = new JTextField();
		loginPasswordTextField = new JTextField();

		loginButton = new JButton("�α���");
		//LoginButtonPanel
		createIdButton = new JButton("ȸ������");
		findIdButton = new JButton("���̵�ã��");

		//////////////////////////////////////////
		loginPanel.setLayout(new BorderLayout());
		//CENTER (sub)
		loginTextFieldPanel.setLayout(new GridLayout(2,1));
		loginTextFieldPanel.add(loginIdTextField);
		loginTextFieldPanel.add(loginPasswordTextField);

		loginTextAreaPanel.setLayout(new GridLayout(2,1));
		loginTextAreaPanel.add(loginIdTextArea);
		loginTextAreaPanel.add(loginPasswordTextArea);
		//CENTER (main)
		loginTextPanel.setLayout(new BorderLayout());
		loginTextPanel.add(loginTextFieldPanel,BorderLayout.CENTER);
		loginTextPanel.add(loginTextAreaPanel,BorderLayout.WEST);

		loginPanel.add(loginTextPanel,BorderLayout.CENTER);	
		//EAST (BUTTON)
		loginPanel.add(loginButton,BorderLayout.EAST);
		//south (BUTTON)
		loginButtonPanel.setLayout(new GridLayout(1,2));
		loginButtonPanel.add(createIdButton);
		loginButtonPanel.add(findIdButton);
		loginPanel.add(loginButtonPanel,BorderLayout.SOUTH);

		//���̵� ������ action�߰�
		createIdButton.addActionListener(this);
		findIdButton.addActionListener(this);
		loginButton.addActionListener(this);
	}

	/**
	 * ������ ��ư�� ����� �߰��Ѵ�.
	 * 1. createIdButton
	 * 		- createIdWindow(���̵���� â)�� �����Ų��.
	 * 2. findIdButton
	 * 		- findIdWindow(���̵� ã�� â)�� �����Ų��.
	 * 3. loginButton
	 * 		- loginIdTextField�� loginPasswordTextField�� �Էµ� ����,
	 * 			user database�� ����Ǿ� �ִ� ���� ���Ͽ� �ùٸ� id�� password���� ���Ѵ�.
	 * 			�ùٸ� ������� ClientMainFrame�� �����Ű��,
	 * 			�ùٸ��� ���� ������� loginFailWindow�� �����Ų��.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == createIdButton) {
			new createIdWindow();
			return;
		}
		if(o == findIdButton) {
			new findIdWindow();
			return;
		}
		if(o == loginButton) {
			//OrderServerConnection();// ���ϻ���
			String id = loginIdTextField.getText();
			String password = loginPasswordTextField.getText();
			///���⿡ �α��� ���� �´��� Ȯ��

			try {
				dao = UserDao.getInstance();
				System.out.println(id);
				System.out.println(password);
				System.out.println(dao.login(id, password));
				if(dao.login(id, password)) {
					dispose();
					new ClientMainFrame();
				}
				else {					
					new loginFailWindow();
				}
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
	}

	/**
	 * ������ ������loginPanel�� �������� �Ѵ�.
	 * �⺻ ũ��� 300,130�̸� ȭ�� ������� �����ȴ�.
	 */
	public ClientFrame() {
		setLoginPanel();
		setSize(300	,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(loginPanel,BorderLayout.CENTER);

		setVisible(true);
	}
}
/**
 * �α��� ���� ������
 * �Էµ� ����(���̵�� ��й�ȣ)�� �ùٸ��� ���� ��� ����ȴ�.
 * loginFailLabel�� loginFailButton���� �����Ǿ� ������, ��ư�� ������ â�� ������� �ȴ�.
 */
class loginFailWindow extends JFrame{
	JPanel loginFailPanel;
	JLabel loginFailLabel;
	JButton loginFailButton;
	loginFailWindow() {
		loginFailLabel = new JLabel("��ġ�ϴ� ȸ�������� �����ϴ�.");
		loginFailButton = new JButton("Ȯ��");

		loginFailButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		loginFailPanel = new JPanel();
		loginFailPanel.setLayout(new BorderLayout());
		loginFailPanel.add(loginFailLabel,BorderLayout.CENTER);
		loginFailPanel.add(loginFailButton,BorderLayout.SOUTH);
		setSize(300,150);
		setLocationRelativeTo(null);
		setResizable(false);
		add(loginFailPanel);
		setVisible(true);
	}
}


/**
 * ȸ������ ������
 * ȸ������ ��ư�� ���� ��� ����ȴ�.
 * idCheck = ���̵� �ߺ�Ȯ�� ���� ���ο� ���� ��
 * 		ȸ������ ��ư�� ������ ������ -1�� ���� �־�����.
 * 		�ߺ��� �ƴ϶�� 1, ���̶�� 0�� ���� �־�����.
 * idPanel = idLabel�� ����ڰ� id�� �Է��� �� �ִ� idTextField,
 * 			 �ߺ�Ȯ���� ���� ��ư idCheckButton���� �����Ǿ� �ִ�.
 * namePanel = nameLabel�� ����ڰ� �̸��� �Է��� �� �ִ� nameTextField�� �����Ǿ� �ִ�.
 * passwordPanel = passwordLabel�� ����ڰ� �н����带 �Է��� �� �ִ� passwordTextField�� �����Ǿ� �ִ�.
 * emailPanel = emailLabel�� ����ڰ� �̸����� �Է��� �� �ִ� emailTextField�� �����Ǿ� �ִ�.
 * phonePanel = phoneLabel�� ����ڰ� ��ȭ��ȣ�� �Է��� �� �ִ� phoneTextField�� �����Ǿ� �ִ�.
 * buttonPanel = registButton(����) ��ư�� cancelButton(���) ��ư���� �����Ǿ� �ִ�.
 */
class createIdWindow extends JFrame implements ActionListener {
	// ��ư�� �������� ��������� �� â�� ������ Ŭ����
	UserDao dao = null;

	JPanel idPanel,passwordPanel,namePanel,buttonPanel, emailPanel, phonePanel;
	JLabel idLabel,nameLabel,passwordLabel, emailLabel ,phoneLabel;
	JTextField idTextField,nameTextField,passwordTextField, emailTextField, phoneTextField;
	JButton idCheckButton;
	JButton registButton, cancelButton;

	int idCheck = -1; 
	
	private final int STRINGNUM = 18;


	createIdWindow() {
		idLabel = new JLabel("���̵�");
		idTextField = new JTextField(STRINGNUM);
		idCheckButton = new JButton("�ߺ� Ȯ��");
		idPanel=new JPanel();
		idPanel.setLayout(new BorderLayout());
		idPanel.add(idLabel,BorderLayout.WEST);
		idPanel.add(idTextField,BorderLayout.CENTER);
		idPanel.add(idCheckButton,BorderLayout.EAST);

		//�ߺ�Ȯ�� ��ư�� �׼� �߰�.
		idCheckButton.addActionListener(this);


		nameLabel = new JLabel("�̸�");
		nameTextField = new JTextField(STRINGNUM);
		namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameLabel,BorderLayout.WEST);
		namePanel.add(nameTextField,BorderLayout.CENTER);

		passwordLabel = new JLabel("�н�����");
		passwordTextField = new JTextField(STRINGNUM);
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new BorderLayout());
		passwordPanel.add(passwordLabel,BorderLayout.WEST);
		passwordPanel.add(passwordTextField,BorderLayout.CENTER);

		emailLabel = new JLabel("�̸���");
		emailTextField = new JTextField(STRINGNUM);
		emailPanel = new JPanel();
		emailPanel.setLayout(new BorderLayout());
		emailPanel.add(emailLabel,BorderLayout.WEST);
		emailPanel.add(emailTextField,BorderLayout.CENTER);

		phoneLabel = new JLabel("��ȭ��ȣ");
		phoneTextField = new JTextField(STRINGNUM);
		phonePanel = new JPanel();
		phonePanel.setLayout(new BorderLayout());
		phonePanel.add(phoneLabel,BorderLayout.WEST);
		phonePanel.add(phoneTextField,BorderLayout.CENTER);

		registButton = new JButton("����");
		cancelButton = new JButton("���");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(registButton);
		buttonPanel.add(cancelButton);

		//cancelButton�� ���� �̺�Ʈ �߰�
		cancelButton.addActionListener(this);
		//registButton�� ���� �̺�Ʈ �߰�
		registButton.addActionListener(this);

		setTitle("ȸ�� ����");
		JPanel newCreateIdWindow = new JPanel();
		//        newCreateIdWindow.setLayout(new GridLayout(ROW,COL));
		setContentPane(newCreateIdWindow);
		setSize(350,200);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridLayout(6,1));
		add(namePanel);
		add(idPanel);
		add(passwordPanel);
		add(emailPanel);
		add(phonePanel);
		add(buttonPanel);

		setVisible(true);

	}
	/**
	 * ������ ��ư�� ����� �߰��Ѵ�.
	 * 1. idCheckButton
	 * 		- ���̵� �ߺ�Ȯ�� ��ư
	 * 		- ���̵� �ߺ��� �ƴ϶�� idCheck�� 1�� �����ϸ� IdCheckOkWindow�� �����Ѵ�.
	 * 		- ���̵� �ߺ��̶�� idCheck�� 0�� �����ϸ� IdCheckFailWindow�� �����Ѵ�.
	 * 2. registButton
	 * 		- ���̵� ������ư
	 * 		- dao��ü�� �����Ͽ�, idCheck�� 1�̶�� IdRegistOkWindow(���̵� ���� ����)�� �����ϸ� dao�� ���� ���̵� �����Ѵ�.
	 * 		- idCheck�� 1�̶��, IdRegistFailWindow(���̵� ���� ����)�� �����Ѵ�.
	 * 3. cancelButton
	 * 		- ��� ��ư
	 * 		- â�� ������� �Ѵ�.
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if(o == idCheckButton) {
			//�ߺ�Ȯ��
			try {
				dao = UserDao.getInstance();
				String id = idTextField.getText();
				if(!dao.idCheck(id)) {
					new IdCheckOkWindow();
					idCheck = 1;
				}else {
					new IdCheckFailWindow();
					idCheck = 0;
				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		if(o==registButton) {
			//���̵� ����
			try {
				dao = UserDao.getInstance();
				String name = nameTextField.getText();
				String id = idTextField.getText();
				String password = passwordTextField.getText();
				String email = emailTextField.getText();
				String phone = phoneTextField.getText();
				if(idCheck == 1) {
					dao.regist(name, id, password, email, phone);
					new IdRegistOkWindow();
					dispose();
				}
				else {
					//���̵� ���� ����
					new IdRegistFailWindow();
				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		if(o==cancelButton) {
			//���
			dispose();
			return;
		}
	}
}
/**
 * �ߺ�Ȯ��(���̵� ��������) ������
 * IdCheckOkLabel(���� ����)�� IdCheckOkButton(Ȯ��)���� �̷���� �ִ�.
 * Ȯ�� ��ư�� ������ â�� �������.
 */
class IdCheckOkWindow extends JFrame{
	JPanel IdCheckOkPanel;
	JLabel IdCheckOkLabel;
	JButton IdCheckOkButton;

	IdCheckOkWindow(){
		IdCheckOkLabel = new JLabel("���� ����");
		IdCheckOkButton = new JButton("Ȯ��");

		IdCheckOkPanel = new JPanel();
		IdCheckOkPanel.setLayout(new GridLayout(2,1));
		IdCheckOkPanel.add(IdCheckOkLabel);
		IdCheckOkPanel.add(IdCheckOkButton);

		//Ȯ�� ��ư�� dispose�߰�
		IdCheckOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("�ߺ� Ȯ��");
		add(IdCheckOkPanel);
		setVisible(true);
	}
}
/**
 * �ߺ�Ȯ��(���̵� ���� �Ұ���) ������
 * IdCheckFailLabel(���� �Ұ�)�� IdCheckFailButton(Ȯ��)���� �̷���� �ִ�.
 * Ȯ�� ��ư�� ������ â�� �������.
 */
class IdCheckFailWindow extends JFrame{
	JPanel IdCheckFailPanel;
	JLabel IdCheckFailLabel;
	JButton IdCheckFailButton;

	IdCheckFailWindow(){
		IdCheckFailLabel = new JLabel("���� �Ұ�");
		IdCheckFailButton = new JButton("Ȯ��");

		IdCheckFailPanel = new JPanel();
		IdCheckFailPanel.setLayout(new GridLayout(2,1));
		IdCheckFailPanel.add(IdCheckFailLabel);
		IdCheckFailPanel.add(IdCheckFailButton);

		//Ȯ�� ��ư�� dispose�߰�
		IdCheckFailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("�ߺ� Ȯ��");
		add(IdCheckFailPanel);
		setVisible(true);
	}
}
/**
 * ���̵� ���� �Ϸ� ������
 * IdRegistOkLabel(���� �Ϸ�)�� IdRegistOkButton(Ȯ��)���� �̷���� �ִ�.
 * Ȯ�� ��ư�� ������ â�� �������.
 */
class IdRegistOkWindow extends JFrame{
	JPanel IdRegistOkPanel;
	JLabel IdRegistOkLabel;
	JButton IdRegistOkButton;

	IdRegistOkWindow(){
		IdRegistOkLabel = new JLabel("���� �Ϸ�");
		IdRegistOkButton = new JButton("Ȯ��");

		IdRegistOkPanel = new JPanel();
		IdRegistOkPanel.setLayout(new GridLayout(2,1));
		IdRegistOkPanel.add(IdRegistOkLabel);
		IdRegistOkPanel.add(IdRegistOkButton);

		//Ȯ�� ��ư�� dispose�߰�
		IdRegistOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("���� �Ϸ�");
		add(IdRegistOkPanel);
		setVisible(true);
	}
}
/**
 * ���̵� ���� ���� ������
 * IdRegistFailLabel(���̵� �ߺ�Ȯ���� �ϼ���)�� IdRegistFailButton(Ȯ��)���� �̷���� �ִ�.
 * Ȯ�� ��ư�� ������ â�� �������.
 */
class IdRegistFailWindow extends JFrame{
	JPanel IdRegistFailPanel;
	JLabel IdRegistFailLabel;
	JButton IdRegistFailButton;

	IdRegistFailWindow(){
		IdRegistFailLabel = new JLabel("���̵� �ߺ�Ȯ���� �ϼ���");
		IdRegistFailButton = new JButton("Ȯ��");

		IdRegistFailPanel = new JPanel();
		IdRegistFailPanel.setLayout(new GridLayout(2,1));
		IdRegistFailPanel.add(IdRegistFailLabel);
		IdRegistFailPanel.add(IdRegistFailButton);

		//Ȯ�� ��ư�� dispose�߰�
		IdRegistFailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("���� �Ұ�");
		add(IdRegistFailPanel);
		setVisible(true);
	}
}
/**
 * ���̵� ã�� ������
 * namePanel = nameLabel�� �̸� �Է��� ������ nameTextField ���� ����
 * emailPanel = emailLabel�� �̸��� �Է��� ������ emailTextField ���� ����
 * phonePanel = phoneLabel�� ��ȭ��ȣ �Է��� ������ phoneTextField ���� ����
 * buttonPanel = findButton(ã���ư)�� cancelButton(��ҹ�ư) ���� ����
 */
class findIdWindow extends JFrame implements ActionListener{
	JPanel namePanel,buttonPanel, emailPanel, phonePanel;
	JLabel nameLabel,emailLabel ,phoneLabel;
	JTextField nameTextField, emailTextField, phoneTextField;
	JButton findButton, cancelButton;
	UserDao dao = null;

	private final int STRINGNUM = 18;

	findIdWindow() {
		nameLabel = new JLabel("�̸�");
		nameTextField = new JTextField(STRINGNUM);
		namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameLabel,BorderLayout.WEST);
		namePanel.add(nameTextField,BorderLayout.CENTER);

		emailLabel = new JLabel("�̸���");
		emailTextField = new JTextField(STRINGNUM);
		emailPanel = new JPanel();
		emailPanel.setLayout(new BorderLayout());
		emailPanel.add(emailLabel,BorderLayout.WEST);
		emailPanel.add(emailTextField,BorderLayout.CENTER);

		phoneLabel = new JLabel("��ȭ��ȣ");
		phoneTextField = new JTextField(STRINGNUM);
		phonePanel = new JPanel();
		phonePanel.setLayout(new BorderLayout());
		phonePanel.add(phoneLabel,BorderLayout.WEST);
		phonePanel.add(phoneTextField,BorderLayout.CENTER);

		findButton = new JButton("ã��");
		cancelButton = new JButton("���");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(findButton);
		buttonPanel.add(cancelButton);
		//findButton�� �̺�Ʈ �߰�
		findButton.addActionListener(this);

		//cancelButton�� ���� �̺�Ʈ �߰�
		cancelButton.addActionListener(this);

		setTitle("ID ã��");
		JPanel newCreateIdWindow = new JPanel();
		//        newCreateIdWindow.setLayout(new GridLayout(ROW,COL));
		setContentPane(newCreateIdWindow);
		setSize(350,150);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridLayout(4,1));
		add(namePanel);
		add(emailPanel);
		add(phonePanel);
		add(buttonPanel);

		setVisible(true);

	}
	/**
	 * ������ ��ư�� ����� �߰��Ѵ�.
	 * 1. findButton
	 * 		- ���̵� ã�� ��ư
	 * 		- �Է¹��� name, email, phone�� UserDao�� ���� �ùٸ� �������� Ȯ���Ѵ�.
	 * 		- �ùٸ� ������� findIdResultWindow(findIdOkString)�� �����Ѵ�.
	 * 		- �ùٸ��� ���� ������� findIdResultWindow(findIdfailString)�� �����Ѵ�.
	 * 2. cancelButton
	 * 		- ��� ��ư
	 * 		- â�� ������� �Ѵ�.
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if(o == findButton) {
			//���̵� ã��
			try {
				dao = UserDao.getInstance();
				String name = nameTextField.getText();
				String email = emailTextField.getText();
				String phone = phoneTextField.getText();
				if(null != dao.findId(name, email, phone)) {
					String findIdOkString = dao.findId(name, email, phone).toString();
					new findIdResultWindow(findIdOkString);
				}else {
					String findIdfailString = "��ġ�ϴ� ȸ�������� �����ϴ�.";
					new findIdResultWindow(findIdfailString);
				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		
		if(o==cancelButton) {
			//���
			dispose();
			return;
		}
	}
}
/**
 * ���̵� ã�� ������
 * findIdResultWindow(String findIdOkString)
 * 		- �־��� String �� ��Ÿ���� â�� ������.
 */
class findIdResultWindow extends JFrame{
	JPanel findIdResultPanel;
	JLabel findIdResultLabel;
	JButton findIdResultButton;

	findIdResultWindow(String findIdOkString) {
		findIdResultLabel = new JLabel(findIdOkString);
		findIdResultButton = new JButton("Ȯ��");
		findIdResultPanel = new JPanel();
		findIdResultPanel.setLayout(new BorderLayout());
		findIdResultPanel.add(findIdResultLabel,BorderLayout.CENTER);
		findIdResultPanel.add(findIdResultButton,BorderLayout.SOUTH);

		setSize(300,150);
		setLocationRelativeTo(null);
		setResizable(false);
		add(findIdResultPanel);
		setVisible(true);

		setDisposeActionListener(findIdResultButton);
	}
	public void setDisposeActionListener(JButton button) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}

