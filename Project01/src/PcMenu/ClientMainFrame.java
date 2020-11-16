package PcMenu;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.main.ClientMain;

public class ClientMainFrame extends JFrame implements ActionListener{
	private final int CATEGORY = 4;
	private final int MAXORDER = 20;
	private final int FOOD = 0;
	private final int DRINK = 1;
	private final int SNACK = 2;
	private final int CHIP = 3;
	private final int TABNUM = 4;
	ProductDao dao = null;
	//WEST
	JButton[] categoryButton = new JButton[CATEGORY];
	JPanel categoryPanel;
	//NORTH
	JButton chatButton;
	JLabel searchLabel;
	JTextField searchTextField;
	JButton searchButton;
	JPanel searchPanel;
	//Center
	JPanel[] menuPanel = new JPanel[TABNUM];
	//SOUTH
	JPanel orderCheckPanel, orderCashPanel, orderCompletePanel, orderPanel;
	JTextArea[] orderCheckTextArea = new JTextArea[4*MAXORDER+4];
	JPanel orderCheckboxPanel,orderCashCardPanel;
	CheckboxGroup orderCheckboxGroup = new CheckboxGroup();
	Checkbox orderCash, orderCard;
	JComboBox orderComboBox;
	String orderCashMenu[]= {"1000��","5000��","10000��","50000��"};
	JTextArea orderTextArea;
	JButton orderCompleteButton;
	JLabel orderCompleteLabel;
	JPanel orderCashCardCompletePanel;
	JTabbedPane menuTabPanel;
	JButton cancelButton;
	JPanel cancelPanel;

	Socket socket;
	PrintWriter printWriter;
	BufferedReader bufferedReader;

	JButton sendButton;
	int orderFinalPrice=0;

	JPanel mainPanel;

	/**
	 * JButton�� ��ӹ��� ��ü.
	 * menuCategoryButton(String name,int price)�� ���� ������ ��ư�� name�� price���� �����Ѵ�.
	 */
	class menuCategoryButton extends JButton{
		String name;
		int price;
		public menuCategoryButton(String name,int price) {
			super("");
			this.name=name;
			this.price=price;
		}
	}
	/**
	 * 9���� ���� ��ư�� ���� 11���� ����, ����, ���ڷ� ��ư�� �����Ѵ�.
	 */
	menuCategoryButton[] menuFoodButton = new menuCategoryButton[9];
	menuCategoryButton[] menuDrinkButton = new menuCategoryButton[11];
	menuCategoryButton[] menuSnackButton = new menuCategoryButton[11];
	menuCategoryButton[] menuChipButton = new menuCategoryButton[11];
	/**
	 * ī���Ϳ� ä��, product �˻��� ���� Panel 
	 * searchPanel = sendButton�� searchTextField, searchButton���� ����
	 * sendButton�� ���� ī���Ϳ� ä���� �Ѵ�.
	 * searchButton�� ���� searchTextField�� �Էµ� product�� �˻��Ѵ�.
	 */
	public void setNorthMainPanel(){
		sendButton = new JButton("ī���Ϳ� ä��");
		searchLabel = new JLabel("��� �̹����� ������ �ٸ��� �ֽ��ϴ�.");
		searchTextField = new JTextField("�˻�",15);
		searchButton = new JButton("�˻�");

		searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.add(sendButton,BorderLayout.WEST);
		searchPanel.add(searchTextField,BorderLayout.CENTER);
		searchPanel.add(searchButton,BorderLayout.EAST);

		sendButton.addActionListener(this);
		searchButton.addActionListener(this);
	}
	/**
	 * orderCheckArea = ����ڰ� �ֹ� ���� ��ǰ ����� Ȯ���Ѵ�.
	 * orderCash = ������� �ֹ� ���(����,ī��)�� Ȯ���Ѵ�.
	 * orderCard = ������� �ֹ� ���(����,ī��)�� Ȯ���Ѵ�.
	 * orderComboBox = ����ڰ� �������� ����� �� ���, ��� ���� ������� Ȯ���Ѵ�.
	 * cancelPanel = orderTextArea(��û ����)�� cancelButton(�ֹ� ���)���� ����
	 * orderCompletePanel = orderCompleteButton(�ֹ� �Ϸ�)�� orderCompleteLabel(���� �ݾ�)���� ����
	 * 
	 */
	public void setOrderMainPanel() {
		orderCheckPanel = new JPanel();
		orderCheckPanel.setLayout(new GridLayout(MAXORDER+1,4));
		for(int i=0;i<orderCheckTextArea.length;i++) {
			orderCheckTextArea[i]=new JTextArea();
			orderCheckTextArea[i].setEditable(false);
			orderCheckPanel.add(orderCheckTextArea[i]);
		}
		orderCheckTextArea[0].setText("��ǰ��");
		orderCheckTextArea[1].setText("�Ǹűݾ�");
		orderCheckTextArea[2].setText("����");
		orderCheckTextArea[3].setText("�����ݾ�");

		orderCash = new Checkbox("����",orderCheckboxGroup,true);
		orderCard = new Checkbox("ī��",orderCheckboxGroup,false);
		orderCash.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				orderComboBox.setEnabled(true);
			}
		});
		orderCard.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				orderComboBox.setEnabled(false);
			}
		});
		orderCheckboxPanel = new JPanel();
		orderCheckboxPanel.setLayout(new GridLayout(1,2));
		orderCheckboxPanel.add(orderCash);
		orderCheckboxPanel.add(orderCard);

		//
		orderComboBox = new JComboBox(orderCashMenu);
		orderComboBox.setEditable(false);

		orderCashCardPanel = new JPanel();
		orderCashCardPanel.setLayout(new GridLayout(2,1));
		orderCashCardPanel.add(orderCheckboxPanel);
		orderCashCardPanel.add(orderComboBox);

		cancelPanel = new JPanel();
		cancelPanel.setLayout(new BorderLayout());
		orderTextArea = new JTextArea("��û ����");
		cancelButton = new JButton("�ֹ� ���");
		cancelPanel.add(orderTextArea);
		cancelPanel.add(cancelButton,BorderLayout.SOUTH);
		cancelButton.addActionListener(this);

		orderCashPanel = new JPanel();
		orderCashPanel.setLayout(new GridLayout(2,1));
		orderCashPanel.add(orderCashCardPanel);
		orderCashPanel.add(cancelPanel);

		orderCompleteButton = new JButton("�ֹ� �Ϸ�");
		orderCompleteLabel = new JLabel("���� �ݾ� : "+orderFinalPrice+"��");
		orderCompletePanel = new JPanel();
		orderCompletePanel.setLayout(new GridLayout(2,1));
		orderCompletePanel.add(orderCompleteLabel);
		orderCompletePanel.add(orderCompleteButton);

		orderCompleteButton.addActionListener(this);

		orderCashCardCompletePanel=new JPanel();
		orderCashCardCompletePanel.setLayout(new GridLayout(1,2));
		orderCashCardCompletePanel.add(orderCashPanel);
		orderCashCardCompletePanel.add(orderCompletePanel);

		orderPanel = new JPanel();
		orderPanel.setLayout(new GridLayout(1,2));
		orderPanel.add(orderCheckPanel);
		orderPanel.add(orderCashCardCompletePanel);
	}
	/**
	 * ������ ��ư�� ����� �߰��Ѵ�.
	 * 1.sendButton
	 * 		- ī���Ϳ� ��ȭ ��ư
	 * 		- ���ο� �����带 �����Ͽ�, ClientMain�� �����Ѵ�.
	 * 2.searchButton
	 * 		- ��ǰ �˻� ��ư
	 * 		- searchTextField�� �Է¹��� ������ searchResultWindow�� �����Ѵ�.
	 * 3.orderCompleteButton
	 * 		- �ֹ��Ϸ� ��ư
	 * 		- ClientFrmae���� ������ ���ϰ� ��Ʈ�ѹ��� ī���Ϳ� �ֹ� ������ �����Ѵ�.
	 * 		- �ֹ� �Ϸ� �Ŀ��� orderReset�� ���� ��� ������ �ʱ�ȭ�Ѵ�.
	 * 4.cancelButton
	 * 		- �ֹ� ��� ��ư
	 * 		- ��� �ֹ� ������ �ʱ�ȭ�Ѵ�.
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if(o==sendButton) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					ClientMain.main(null);
				}
			}).start();
			return;
		}
		if(o==searchButton) {
			new searchResultWindow(searchTextField.getText());
			return;
		}
		if(o==orderCompleteButton) {
			while(true) {
				String orderString="\n==========�ֹ�==========\n";
				for(int i = 4;i<MAXORDER*4+4;i+=4) {
					if(!"".equals(orderCheckTextArea[i].getText())) {
						orderString += orderCheckTextArea[i].getText()+" / ���� : "+
								orderCheckTextArea[i+2].getText()+"��\n";
						if(i==MAXORDER*4) {
							if(orderCash.getState()){//�����̶��
								orderString +="���� ���� : ���� "+orderComboBox.getSelectedItem().toString()+"��\n";

							}
							else {
								orderString +="���� ���� : ī��\n";
							}

							orderString +=orderCompleteLabel.getText()+"\n";
							orderString += "��û���� : "+orderTextArea.getText();
							break;
						}
					}
					else {
						if(orderCash.getState()){//�����̶��
							orderString +="���� ���� : ���� "+orderComboBox.getSelectedItem().toString()+"��\n";

						}
						else {
							orderString +="���� ���� : ī��\n";
						}

						orderString +=orderCompleteLabel.getText()+"\n";
						orderString += "��û���� : "+orderTextArea.getText();
						break;
					}
				}
				printWriter.println(orderString);
				printWriter.flush();
				orderReset();
				break;

			}
			return;
		}
		if(o==cancelButton) {
			for(int i=4;i<MAXORDER*4+4;i++) {
				if("".equals(orderCheckTextArea[i])) {
					break;
				}
				orderCheckTextArea[i].setText("");
			}
			orderTextArea.setText("��û ����");
			orderFinalPrice=0;
			orderCompleteLabel.setText("���� �ݾ� : "+orderFinalPrice+"��");
		}

	}
	/**
	 * orderReset
	 * 		- �ֹ� ����
	 * 		- �ֹ� �Ϸ� ��, ��� �ֹ� ������ �ʱ�ȭ�Ѵ�.
	 */
	public void orderReset() {
		for(int i=4;i<MAXORDER*4+4;i++) {
			if("".equals(orderCheckTextArea[i])) {
				break;
			}
			orderCheckTextArea[i].setText("");
		}
		orderTextArea.setText("��û ����");
		orderFinalPrice=0;
		orderCompleteLabel.setText("���� �ݾ� : "+orderFinalPrice+"��");
	}
	/**
	 * -dao��ü�� �����ϸ�, menuTabPanel�� menuPanel ��ü�� �����Ѵ�.
	 * -ProductVo list�� �����Ѵ�.
	 * 		dao.getProduct�� ���� ������ ��ư�� ������ �ο��Ѵ�.
	 * 		1 = FOOD
	 * 		2 = DRINK
	 * 	 	3 = SNACK
	 * 		4 = CHIP
	 */
	public void setCenterMainPanel() {
		try {
			dao = ProductDao.getInstance();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		menuTabPanel = new JTabbedPane();
		for(int i=0;i<menuPanel.length;i++) {
			menuPanel[i]=new JPanel(); 
		}

		for(int i=0;i<menuPanel.length;i++) {
			menuPanel[i]=new JPanel();
			menuPanel[i].setLayout(new GridLayout(3,4));
		}


		
		ArrayList<ProductVo> list = new ArrayList<>();
		try {
			list = dao.getProduct(1);

			for(int i=0;i<list.size();i++) {
				menuFoodButton[i]=new menuCategoryButton(list.get(i).getName(),list.get(i).getPrice());
				setMenuActionListener(menuFoodButton[i]);
				menuFoodButton[i].setText(menuFoodButton[i].name+"  /  "+menuFoodButton[i].price+"��");
				menuPanel[FOOD].add(menuFoodButton[i]);

			}
			list = dao.getProduct(2);
			for(int i=0;i<list.size();i++) {
				menuDrinkButton[i]=new menuCategoryButton(list.get(i).getName(),list.get(i).getPrice());
				setMenuActionListener(menuDrinkButton[i]);
				menuDrinkButton[i].setText(menuDrinkButton[i].name+"  /  "+menuDrinkButton[i].price+"��");
				menuPanel[DRINK].add(menuDrinkButton[i]);

			}
			list = dao.getProduct(3);
			for(int i=0;i<list.size();i++) {
				menuSnackButton[i]=new menuCategoryButton(list.get(i).getName(),list.get(i).getPrice());
				setMenuActionListener(menuSnackButton[i]);
				menuSnackButton[i].setText(menuSnackButton[i].name+"  /  "+menuSnackButton[i].price+"��");
				menuPanel[SNACK].add(menuSnackButton[i]);

			}
			list = dao.getProduct(4);
			for(int i=0;i<list.size();i++) {
				menuChipButton[i]=new menuCategoryButton(list.get(i).getName(),list.get(i).getPrice());
				setMenuActionListener(menuChipButton[i]);
				menuChipButton[i].setText(menuChipButton[i].name+"  /  "+menuChipButton[i].price+"��");
				menuPanel[CHIP].add(menuChipButton[i]);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menuTabPanel.addTab("����", menuPanel[FOOD]);
		menuTabPanel.addTab("����", menuPanel[DRINK]);
		menuTabPanel.addTab("����", menuPanel[SNACK]);
		menuTabPanel.addTab("����", menuPanel[CHIP]);
		setOrderMainPanel();
		mainPanel=new JPanel();
		mainPanel.setLayout(new GridLayout(2,1));
		mainPanel.add(menuTabPanel);
		mainPanel.add(orderPanel);
	}
	/**
	 * ������ �޴��� actionListener�� �߰��Ѵ�.
	 * ��ư�� Ŭ���� ���, orderCheckTextArea�� ��ǰ��, ����, ������ �ԷµǸ�, �̹� �Էµ� ��ǰ�� ��� ������ �����ȴ�.
	 * ������ �ٸ� �޴��� �� MAXORDER���� �ֹ� �����ϸ�, �̸� �ʱ��� ��� maxOrderWindow�� �����ȴ�.
	 */
	public void setMenuActionListener(menuCategoryButton button) {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				for(int i=0;i<MAXORDER*4+4;i+=4) {
					if(i%4 == 0 && button.name.equals(orderCheckTextArea[i].getText())) {
						int count = Integer.parseInt(orderCheckTextArea[i+2].getText());
						count++;
						orderCheckTextArea[i+2].setText(count+"");
						int finalPrice = Integer.parseInt(orderCheckTextArea[i+1].getText())
								* Integer.parseInt(orderCheckTextArea[i+2].getText());
						orderCheckTextArea[i+3].setText(finalPrice+"");
						orderFinalPrice += Integer.parseInt(orderCheckTextArea[i+1].getText());
						orderCompleteLabel.setText("���� �ݾ� : "+orderFinalPrice+"��");
						break;
					}
					else if(i%4 == 0 && "".equals(orderCheckTextArea[i].getText())) {
						orderCheckTextArea[i].setText(button.name);
						orderCheckTextArea[i+1].setText(button.price+"");
						orderCheckTextArea[i+2].setText("1");
						int finalPrice = Integer.parseInt(orderCheckTextArea[i+1].getText())
								* Integer.parseInt(orderCheckTextArea[i+2].getText());
						orderCheckTextArea[i+3].setText(finalPrice+"");
						orderFinalPrice += button.price;
						orderCompleteLabel.setText("���� �ݾ� : "+orderFinalPrice+"��");
						break;
					}
					if(i == MAXORDER*4) {
						new maxOrderWindow();
					}
				}
			}
		});
	}

	public ClientMainFrame() {

		try {
			//�ֹ� �������� ����� �� ��, ���� �־���
			socket= new Socket("127.0.0.1",50000);
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	//�̰� ����Ǵ� ������ accept ����

		setSize(1200,800);

		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setNorthMainPanel();
		add(searchPanel,BorderLayout.NORTH);
		setCenterMainPanel();
		add(mainPanel,BorderLayout.CENTER);
		setVisible(true);
	}

	public static void main(String[] args) {

	}
}
/**
 * ���� �ʰ� ������
 * MAXORDER�� �ʰ��Ͽ� �ֹ��� ���, �����ȴ�.
 * Ȯ�� ��ư�� ������ �������.
 */
class maxOrderWindow extends JFrame{
	JLabel maxOrderLabel;
	JButton maxOrderButton;
	maxOrderWindow(){
		maxOrderLabel = new JLabel("�ѹ��� ������ �ִ� �޴� ������ 20�� �Դϴ�.");
		maxOrderButton = new JButton("Ȯ��");
		setSize(350,150);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new BorderLayout());
		add(maxOrderLabel);
		add(maxOrderButton,BorderLayout.SOUTH);
		setVisible(true);

		maxOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
/**
 * dao��ü�� �����ϰ�, searchTextFieldString�� �Է¹��� ������ searchResultWindow�� �����Ѵ�.
 * dao��ü�� searchProduct�� ����, �ش��ϴ� product�� �ִ��� Ȯ���ϰ� ���� ����ϴ� �����츦 �����Ѵ�.
 * Ȯ�� ��ư�� ������ �������.
 */
class searchResultWindow extends JFrame{
	JTextArea searchResultTextArea;
	JButton searchResultButton;
	ProductDao dao = null;

	public searchResultWindow(String searchTextFieldString)  {
		try {
			dao = ProductDao.getInstance();
			ArrayList<ProductVo> list = new ArrayList<>();
			list = dao.searchProduct(searchTextFieldString);
			String searchResultString ="";
			for(ProductVo vo : list) {
				searchResultString+=vo.toString()+"\n";
			}
			if("".equals(searchResultString)) {
				searchResultString += "��ġ�ϴ� �޴��� �����ϴ�.";
			}

			searchResultTextArea = new JTextArea();
			searchResultTextArea.setText(searchResultString);
			searchResultTextArea.setEditable(false);
			searchResultButton = new JButton("Ȯ��");
			setSize(350,300);
			setLocationRelativeTo(null);
			setResizable(false);
			setLayout(new BorderLayout());
			add(searchResultTextArea,BorderLayout.CENTER);
			add(searchResultButton,BorderLayout.SOUTH);
			setVisible(true);
			searchResultButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}





