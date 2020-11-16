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
	 * 포트번호 = 50000 으로 소켓과 스트림을 생성한다.
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
	 * 로그인 panel을 설정하는 작업을 한다.
	 * loginTextAreaPanel = 화면에서 "아이디","비밀번호" text를 보여준다.
	 * loginTextFieldPanel = 화면에서 사용자가 "아이디"와 "비밀번호"를 입력할 수 있게 한다.
	 * 		loginButton = 로그인에 접근할 수 있도록 하며, 위의 Panel에 속한다.
	 * loginButtonPanel = 화면에서 "회원가입"과 "아이디찾기"의 버튼을 사용가능하게 한다.
	 * 
	 * createIdButton(회원가입), findIdButton(아이디찾기), loginButton(로그인)
	 * 	버튼에 ActionListener를 추가함으로서, 버튼의 기능을 추가한다.
	 */
	public void setLoginPanel() {
		loginPanel = new JPanel();
		loginTextFieldPanel = new JPanel();
		loginTextAreaPanel = new JPanel();
		loginButtonPanel = new JPanel();
		loginTextPanel = new JPanel();
		//LoginTextAreaPanel
		loginIdTextArea = new JTextArea("아이디");
		loginPasswordTextArea = new JTextArea("비밀번호");
		loginIdTextArea.setEditable(false);
		loginPasswordTextArea.setEditable(false);
		//LoginTextFieldPanel
		loginIdTextField = new JTextField();
		loginPasswordTextField = new JTextField();

		loginButton = new JButton("로그인");
		//LoginButtonPanel
		createIdButton = new JButton("회원가입");
		findIdButton = new JButton("아이디찾기");

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

		//아이디 생성에 action추가
		createIdButton.addActionListener(this);
		findIdButton.addActionListener(this);
		loginButton.addActionListener(this);
	}

	/**
	 * 각각의 버튼에 기능을 추가한다.
	 * 1. createIdButton
	 * 		- createIdWindow(아이디생성 창)을 실행시킨다.
	 * 2. findIdButton
	 * 		- findIdWindow(아이디 찾기 창)을 실행시킨다.
	 * 3. loginButton
	 * 		- loginIdTextField와 loginPasswordTextField에 입력된 값과,
	 * 			user database에 저장되어 있는 값을 비교하여 올바른 id와 password인지 비교한다.
	 * 			올바른 정보라면 ClientMainFrame를 실행시키고,
	 * 			올바르지 않은 정보라면 loginFailWindow를 실행시킨다.
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
			//OrderServerConnection();// 소켓생성
			String id = loginIdTextField.getText();
			String password = loginPasswordTextField.getText();
			///여기에 로그인 정보 맞는지 확인

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
	 * 위에서 생성된loginPanel을 보여지게 한다.
	 * 기본 크기는 300,130이며 화면 정가운데에 생성된다.
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
 * 로그인 실패 윈도우
 * 입력된 정보(아이디와 비밀번호)가 올바르지 않을 경우 실행된다.
 * loginFailLabel과 loginFailButton으로 구성되어 있으며, 버튼을 누르면 창이 사라지게 된다.
 */
class loginFailWindow extends JFrame{
	JPanel loginFailPanel;
	JLabel loginFailLabel;
	JButton loginFailButton;
	loginFailWindow() {
		loginFailLabel = new JLabel("일치하는 회원정보가 없습니다.");
		loginFailButton = new JButton("확인");

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
 * 회원가입 윈도우
 * 회원가입 버튼을 누를 경우 실행된다.
 * idCheck = 아이디 중복확인 실행 여부에 대한 값
 * 		회원가입 버튼이 눌러질 때마다 -1의 값이 주어진다.
 * 		중복이 아니라면 1, 중이라면 0의 값이 주어진다.
 * idPanel = idLabel과 사용자가 id를 입력할 수 있는 idTextField,
 * 			 중복확인을 위한 버튼 idCheckButton으로 구성되어 있다.
 * namePanel = nameLabel과 사용자가 이름을 입력할 수 있는 nameTextField로 구성되어 있다.
 * passwordPanel = passwordLabel과 사용자가 패스워드를 입력할 수 있는 passwordTextField로 구성되어 있다.
 * emailPanel = emailLabel과 사용자가 이메일을 입력할 수 있는 emailTextField로 구성되어 있다.
 * phonePanel = phoneLabel과 사용자가 전화번호를 입력할 수 있는 phoneTextField로 구성되어 있다.
 * buttonPanel = registButton(생성) 버튼과 cancelButton(취소) 버튼으로 구성되어 있다.
 */
class createIdWindow extends JFrame implements ActionListener {
	// 버튼이 눌러지면 만들어지는 새 창을 정의한 클래스
	UserDao dao = null;

	JPanel idPanel,passwordPanel,namePanel,buttonPanel, emailPanel, phonePanel;
	JLabel idLabel,nameLabel,passwordLabel, emailLabel ,phoneLabel;
	JTextField idTextField,nameTextField,passwordTextField, emailTextField, phoneTextField;
	JButton idCheckButton;
	JButton registButton, cancelButton;

	int idCheck = -1; 
	
	private final int STRINGNUM = 18;


	createIdWindow() {
		idLabel = new JLabel("아이디");
		idTextField = new JTextField(STRINGNUM);
		idCheckButton = new JButton("중복 확인");
		idPanel=new JPanel();
		idPanel.setLayout(new BorderLayout());
		idPanel.add(idLabel,BorderLayout.WEST);
		idPanel.add(idTextField,BorderLayout.CENTER);
		idPanel.add(idCheckButton,BorderLayout.EAST);

		//중복확인 버튼에 액션 추가.
		idCheckButton.addActionListener(this);


		nameLabel = new JLabel("이름");
		nameTextField = new JTextField(STRINGNUM);
		namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameLabel,BorderLayout.WEST);
		namePanel.add(nameTextField,BorderLayout.CENTER);

		passwordLabel = new JLabel("패스워드");
		passwordTextField = new JTextField(STRINGNUM);
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new BorderLayout());
		passwordPanel.add(passwordLabel,BorderLayout.WEST);
		passwordPanel.add(passwordTextField,BorderLayout.CENTER);

		emailLabel = new JLabel("이메일");
		emailTextField = new JTextField(STRINGNUM);
		emailPanel = new JPanel();
		emailPanel.setLayout(new BorderLayout());
		emailPanel.add(emailLabel,BorderLayout.WEST);
		emailPanel.add(emailTextField,BorderLayout.CENTER);

		phoneLabel = new JLabel("전화번호");
		phoneTextField = new JTextField(STRINGNUM);
		phonePanel = new JPanel();
		phonePanel.setLayout(new BorderLayout());
		phonePanel.add(phoneLabel,BorderLayout.WEST);
		phonePanel.add(phoneTextField,BorderLayout.CENTER);

		registButton = new JButton("생성");
		cancelButton = new JButton("취소");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(registButton);
		buttonPanel.add(cancelButton);

		//cancelButton에 종료 이벤트 추가
		cancelButton.addActionListener(this);
		//registButton에 생성 이벤트 추가
		registButton.addActionListener(this);

		setTitle("회원 가입");
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
	 * 각각의 버튼에 기능을 추가한다.
	 * 1. idCheckButton
	 * 		- 아이디 중복확인 버튼
	 * 		- 아이디가 중복이 아니라면 idCheck를 1로 설정하며 IdCheckOkWindow를 생성한다.
	 * 		- 아이디가 중복이라면 idCheck를 0로 설정하며 IdCheckFailWindow를 생성한다.
	 * 2. registButton
	 * 		- 아이디 생성버튼
	 * 		- dao객체를 생성하여, idCheck가 1이라면 IdRegistOkWindow(아이디 생성 성공)를 생성하며 dao를 통해 아이디를 생성한다.
	 * 		- idCheck가 1이라면, IdRegistFailWindow(아이디 생성 실패)를 생성한다.
	 * 3. cancelButton
	 * 		- 취소 버튼
	 * 		- 창을 사라지게 한다.
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if(o == idCheckButton) {
			//중복확인
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
			//아이디 생성
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
					//아이디 생성 실패
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
			//취소
			dispose();
			return;
		}
	}
}
/**
 * 중복확인(아이디 생성가능) 윈도우
 * IdCheckOkLabel(생성 가능)과 IdCheckOkButton(확인)으로 이루어져 있다.
 * 확인 버튼을 누르면 창이 사라진다.
 */
class IdCheckOkWindow extends JFrame{
	JPanel IdCheckOkPanel;
	JLabel IdCheckOkLabel;
	JButton IdCheckOkButton;

	IdCheckOkWindow(){
		IdCheckOkLabel = new JLabel("생성 가능");
		IdCheckOkButton = new JButton("확인");

		IdCheckOkPanel = new JPanel();
		IdCheckOkPanel.setLayout(new GridLayout(2,1));
		IdCheckOkPanel.add(IdCheckOkLabel);
		IdCheckOkPanel.add(IdCheckOkButton);

		//확인 버튼에 dispose추가
		IdCheckOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("중복 확인");
		add(IdCheckOkPanel);
		setVisible(true);
	}
}
/**
 * 중복확인(아이디 생성 불가능) 윈도우
 * IdCheckFailLabel(생성 불가)과 IdCheckFailButton(확인)으로 이루어져 있다.
 * 확인 버튼을 누르면 창이 사라진다.
 */
class IdCheckFailWindow extends JFrame{
	JPanel IdCheckFailPanel;
	JLabel IdCheckFailLabel;
	JButton IdCheckFailButton;

	IdCheckFailWindow(){
		IdCheckFailLabel = new JLabel("생성 불가");
		IdCheckFailButton = new JButton("확인");

		IdCheckFailPanel = new JPanel();
		IdCheckFailPanel.setLayout(new GridLayout(2,1));
		IdCheckFailPanel.add(IdCheckFailLabel);
		IdCheckFailPanel.add(IdCheckFailButton);

		//확인 버튼에 dispose추가
		IdCheckFailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("중복 확인");
		add(IdCheckFailPanel);
		setVisible(true);
	}
}
/**
 * 아이디 생성 완료 윈도우
 * IdRegistOkLabel(생성 완료)과 IdRegistOkButton(확인)으로 이루어져 있다.
 * 확인 버튼을 누르면 창이 사라진다.
 */
class IdRegistOkWindow extends JFrame{
	JPanel IdRegistOkPanel;
	JLabel IdRegistOkLabel;
	JButton IdRegistOkButton;

	IdRegistOkWindow(){
		IdRegistOkLabel = new JLabel("생성 완료");
		IdRegistOkButton = new JButton("확인");

		IdRegistOkPanel = new JPanel();
		IdRegistOkPanel.setLayout(new GridLayout(2,1));
		IdRegistOkPanel.add(IdRegistOkLabel);
		IdRegistOkPanel.add(IdRegistOkButton);

		//확인 버튼에 dispose추가
		IdRegistOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("생성 완료");
		add(IdRegistOkPanel);
		setVisible(true);
	}
}
/**
 * 아이디 생성 실패 윈도우
 * IdRegistFailLabel(아이디 중복확인을 하세요)과 IdRegistFailButton(확인)으로 이루어져 있다.
 * 확인 버튼을 누르면 창이 사라진다.
 */
class IdRegistFailWindow extends JFrame{
	JPanel IdRegistFailPanel;
	JLabel IdRegistFailLabel;
	JButton IdRegistFailButton;

	IdRegistFailWindow(){
		IdRegistFailLabel = new JLabel("아이디 중복확인을 하세요");
		IdRegistFailButton = new JButton("확인");

		IdRegistFailPanel = new JPanel();
		IdRegistFailPanel.setLayout(new GridLayout(2,1));
		IdRegistFailPanel.add(IdRegistFailLabel);
		IdRegistFailPanel.add(IdRegistFailButton);

		//확인 버튼에 dispose추가
		IdRegistFailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setSize(180,130);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("생성 불가");
		add(IdRegistFailPanel);
		setVisible(true);
	}
}
/**
 * 아이디 찾기 윈도우
 * namePanel = nameLabel과 이름 입력이 가능한 nameTextField 으로 구성
 * emailPanel = emailLabel과 이메일 입력이 가능한 emailTextField 으로 구성
 * phonePanel = phoneLabel과 전화번호 입력이 가능한 phoneTextField 으로 구성
 * buttonPanel = findButton(찾기버튼)과 cancelButton(취소버튼) 으로 구성
 */
class findIdWindow extends JFrame implements ActionListener{
	JPanel namePanel,buttonPanel, emailPanel, phonePanel;
	JLabel nameLabel,emailLabel ,phoneLabel;
	JTextField nameTextField, emailTextField, phoneTextField;
	JButton findButton, cancelButton;
	UserDao dao = null;

	private final int STRINGNUM = 18;

	findIdWindow() {
		nameLabel = new JLabel("이름");
		nameTextField = new JTextField(STRINGNUM);
		namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameLabel,BorderLayout.WEST);
		namePanel.add(nameTextField,BorderLayout.CENTER);

		emailLabel = new JLabel("이메일");
		emailTextField = new JTextField(STRINGNUM);
		emailPanel = new JPanel();
		emailPanel.setLayout(new BorderLayout());
		emailPanel.add(emailLabel,BorderLayout.WEST);
		emailPanel.add(emailTextField,BorderLayout.CENTER);

		phoneLabel = new JLabel("전화번호");
		phoneTextField = new JTextField(STRINGNUM);
		phonePanel = new JPanel();
		phonePanel.setLayout(new BorderLayout());
		phonePanel.add(phoneLabel,BorderLayout.WEST);
		phonePanel.add(phoneTextField,BorderLayout.CENTER);

		findButton = new JButton("찾기");
		cancelButton = new JButton("취소");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(findButton);
		buttonPanel.add(cancelButton);
		//findButton에 이벤트 추가
		findButton.addActionListener(this);

		//cancelButton에 종료 이벤트 추가
		cancelButton.addActionListener(this);

		setTitle("ID 찾기");
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
	 * 각각의 버튼에 기능을 추가한다.
	 * 1. findButton
	 * 		- 아이디 찾기 버튼
	 * 		- 입력받은 name, email, phone을 UserDao를 통해 올바른 정보인지 확인한다.
	 * 		- 올바른 정보라면 findIdResultWindow(findIdOkString)를 생성한다.
	 * 		- 올바르지 않은 정보라면 findIdResultWindow(findIdfailString)를 생성한다.
	 * 2. cancelButton
	 * 		- 취소 버튼
	 * 		- 창을 사라지게 한다.
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if(o == findButton) {
			//아이디 찾기
			try {
				dao = UserDao.getInstance();
				String name = nameTextField.getText();
				String email = emailTextField.getText();
				String phone = phoneTextField.getText();
				if(null != dao.findId(name, email, phone)) {
					String findIdOkString = dao.findId(name, email, phone).toString();
					new findIdResultWindow(findIdOkString);
				}else {
					String findIdfailString = "일치하는 회원정보가 없습니다.";
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
			//취소
			dispose();
			return;
		}
	}
}
/**
 * 아이디 찾기 윈도우
 * findIdResultWindow(String findIdOkString)
 * 		- 주어진 String 을 나타내는 창을 만들어낸다.
 */
class findIdResultWindow extends JFrame{
	JPanel findIdResultPanel;
	JLabel findIdResultLabel;
	JButton findIdResultButton;

	findIdResultWindow(String findIdOkString) {
		findIdResultLabel = new JLabel(findIdOkString);
		findIdResultButton = new JButton("확인");
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

