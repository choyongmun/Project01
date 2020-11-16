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
	String orderCashMenu[]= {"1000원","5000원","10000원","50000원"};
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
	 * JButton을 상속받은 객체.
	 * menuCategoryButton(String name,int price)를 통해 각각의 버튼에 name과 price값을 지정한다.
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
	 * 9개의 음식 버튼과 각각 11개의 음료, 간식, 과자류 버튼을 생성한다.
	 */
	menuCategoryButton[] menuFoodButton = new menuCategoryButton[9];
	menuCategoryButton[] menuDrinkButton = new menuCategoryButton[11];
	menuCategoryButton[] menuSnackButton = new menuCategoryButton[11];
	menuCategoryButton[] menuChipButton = new menuCategoryButton[11];
	/**
	 * 카운터와 채팅, product 검색을 위한 Panel 
	 * searchPanel = sendButton과 searchTextField, searchButton으로 구성
	 * sendButton을 통해 카운터와 채팅을 한다.
	 * searchButton을 통해 searchTextField에 입력된 product를 검색한다.
	 */
	public void setNorthMainPanel(){
		sendButton = new JButton("카운터와 채팅");
		searchLabel = new JLabel("상기 이미지는 실제와 다를수 있습니다.");
		searchTextField = new JTextField("검색",15);
		searchButton = new JButton("검색");

		searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.add(sendButton,BorderLayout.WEST);
		searchPanel.add(searchTextField,BorderLayout.CENTER);
		searchPanel.add(searchButton,BorderLayout.EAST);

		sendButton.addActionListener(this);
		searchButton.addActionListener(this);
	}
	/**
	 * orderCheckArea = 사용자가 주문 중인 상품 목록을 확인한다.
	 * orderCash = 사용자의 주문 방법(현금,카드)를 확인한다.
	 * orderCard = 사용자의 주문 방법(현금,카드)를 확인한다.
	 * orderComboBox = 사용자가 현금으로 계산을 할 경우, 어느 지폐를 사용할지 확인한다.
	 * cancelPanel = orderTextArea(요청 사항)과 cancelButton(주문 취소)으로 구성
	 * orderCompletePanel = orderCompleteButton(주문 완료)와 orderCompleteLabel(최종 금액)으로 구성
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
		orderCheckTextArea[0].setText("상품명");
		orderCheckTextArea[1].setText("판매금액");
		orderCheckTextArea[2].setText("수량");
		orderCheckTextArea[3].setText("최종금액");

		orderCash = new Checkbox("현금",orderCheckboxGroup,true);
		orderCard = new Checkbox("카드",orderCheckboxGroup,false);
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
		orderTextArea = new JTextArea("요청 사항");
		cancelButton = new JButton("주문 취소");
		cancelPanel.add(orderTextArea);
		cancelPanel.add(cancelButton,BorderLayout.SOUTH);
		cancelButton.addActionListener(this);

		orderCashPanel = new JPanel();
		orderCashPanel.setLayout(new GridLayout(2,1));
		orderCashPanel.add(orderCashCardPanel);
		orderCashPanel.add(cancelPanel);

		orderCompleteButton = new JButton("주문 완료");
		orderCompleteLabel = new JLabel("최종 금액 : "+orderFinalPrice+"원");
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
	 * 각각의 버튼에 기능을 추가한다.
	 * 1.sendButton
	 * 		- 카운터와 대화 버튼
	 * 		- 새로운 스래드를 생성하여, ClientMain를 실행한다.
	 * 2.searchButton
	 * 		- 물품 검색 버튼
	 * 		- searchTextField에 입력받은 값으로 searchResultWindow를 실행한다.
	 * 3.orderCompleteButton
	 * 		- 주문완료 버튼
	 * 		- ClientFrmae에서 생성된 소켓과 포트넘버로 카운터에 주문 정보를 전달한다.
	 * 		- 주문 완료 후에는 orderReset를 통해 모든 정보를 초기화한다.
	 * 4.cancelButton
	 * 		- 주문 취소 버튼
	 * 		- 모든 주문 정보를 초기화한다.
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
				String orderString="\n==========주문==========\n";
				for(int i = 4;i<MAXORDER*4+4;i+=4) {
					if(!"".equals(orderCheckTextArea[i].getText())) {
						orderString += orderCheckTextArea[i].getText()+" / 수량 : "+
								orderCheckTextArea[i+2].getText()+"개\n";
						if(i==MAXORDER*4) {
							if(orderCash.getState()){//현금이라면
								orderString +="결제 수단 : 현금 "+orderComboBox.getSelectedItem().toString()+"권\n";

							}
							else {
								orderString +="결제 수단 : 카드\n";
							}

							orderString +=orderCompleteLabel.getText()+"\n";
							orderString += "요청사항 : "+orderTextArea.getText();
							break;
						}
					}
					else {
						if(orderCash.getState()){//현금이라면
							orderString +="결제 수단 : 현금 "+orderComboBox.getSelectedItem().toString()+"권\n";

						}
						else {
							orderString +="결제 수단 : 카드\n";
						}

						orderString +=orderCompleteLabel.getText()+"\n";
						orderString += "요청사항 : "+orderTextArea.getText();
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
			orderTextArea.setText("요청 사항");
			orderFinalPrice=0;
			orderCompleteLabel.setText("최종 금액 : "+orderFinalPrice+"원");
		}

	}
	/**
	 * orderReset
	 * 		- 주문 리셋
	 * 		- 주문 완료 후, 모든 주문 정보를 초기화한다.
	 */
	public void orderReset() {
		for(int i=4;i<MAXORDER*4+4;i++) {
			if("".equals(orderCheckTextArea[i])) {
				break;
			}
			orderCheckTextArea[i].setText("");
		}
		orderTextArea.setText("요청 사항");
		orderFinalPrice=0;
		orderCompleteLabel.setText("최종 금액 : "+orderFinalPrice+"원");
	}
	/**
	 * -dao객체를 생성하며, menuTabPanel과 menuPanel 객체를 생성한다.
	 * -ProductVo list를 생성한다.
	 * 		dao.getProduct를 통해 각각의 버튼에 정보를 부여한다.
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
				menuFoodButton[i].setText(menuFoodButton[i].name+"  /  "+menuFoodButton[i].price+"원");
				menuPanel[FOOD].add(menuFoodButton[i]);

			}
			list = dao.getProduct(2);
			for(int i=0;i<list.size();i++) {
				menuDrinkButton[i]=new menuCategoryButton(list.get(i).getName(),list.get(i).getPrice());
				setMenuActionListener(menuDrinkButton[i]);
				menuDrinkButton[i].setText(menuDrinkButton[i].name+"  /  "+menuDrinkButton[i].price+"원");
				menuPanel[DRINK].add(menuDrinkButton[i]);

			}
			list = dao.getProduct(3);
			for(int i=0;i<list.size();i++) {
				menuSnackButton[i]=new menuCategoryButton(list.get(i).getName(),list.get(i).getPrice());
				setMenuActionListener(menuSnackButton[i]);
				menuSnackButton[i].setText(menuSnackButton[i].name+"  /  "+menuSnackButton[i].price+"원");
				menuPanel[SNACK].add(menuSnackButton[i]);

			}
			list = dao.getProduct(4);
			for(int i=0;i<list.size();i++) {
				menuChipButton[i]=new menuCategoryButton(list.get(i).getName(),list.get(i).getPrice());
				setMenuActionListener(menuChipButton[i]);
				menuChipButton[i].setText(menuChipButton[i].name+"  /  "+menuChipButton[i].price+"원");
				menuPanel[CHIP].add(menuChipButton[i]);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menuTabPanel.addTab("음식", menuPanel[FOOD]);
		menuTabPanel.addTab("음료", menuPanel[DRINK]);
		menuTabPanel.addTab("간식", menuPanel[SNACK]);
		menuTabPanel.addTab("과자", menuPanel[CHIP]);
		setOrderMainPanel();
		mainPanel=new JPanel();
		mainPanel.setLayout(new GridLayout(2,1));
		mainPanel.add(menuTabPanel);
		mainPanel.add(orderPanel);
	}
	/**
	 * 각각의 메뉴에 actionListener를 추가한다.
	 * 버튼이 클릭될 경우, orderCheckTextArea에 상품명, 수량, 가격이 입력되며, 이미 입력된 물품의 경우 수량이 증가된다.
	 * 각각의 다른 메뉴는 총 MAXORDER개가 주문 가능하며, 이를 초괴할 경우 maxOrderWindow가 생성된다.
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
						orderCompleteLabel.setText("최종 금액 : "+orderFinalPrice+"원");
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
						orderCompleteLabel.setText("최종 금액 : "+orderFinalPrice+"원");
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
			//주문 프레임이 만들어 질 때, 값을 주어줌
			socket= new Socket("127.0.0.1",50000);
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	//이게 실행되는 시점에 accept 실행

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
 * 생성 초과 윈도우
 * MAXORDER을 초과하여 주문할 경우, 생성된다.
 * 확인 버튼을 누르면 사라진다.
 */
class maxOrderWindow extends JFrame{
	JLabel maxOrderLabel;
	JButton maxOrderButton;
	maxOrderWindow(){
		maxOrderLabel = new JLabel("한번에 가능한 최대 메뉴 종류는 20개 입니다.");
		maxOrderButton = new JButton("확인");
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
 * dao객체를 생성하고, searchTextFieldString에 입력받은 값으로 searchResultWindow를 실행한다.
 * dao객체의 searchProduct를 통해, 해당하는 product가 있는지 확인하고 값을 출력하는 윈도우를 생성한다.
 * 확인 버튼을 누르면 사라진다.
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
				searchResultString += "일치하는 메뉴가 없습니다.";
			}

			searchResultTextArea = new JTextArea();
			searchResultTextArea.setText(searchResultString);
			searchResultTextArea.setEditable(false);
			searchResultButton = new JButton("확인");
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





