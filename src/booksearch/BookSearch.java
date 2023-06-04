package booksearch;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class BookSearch extends JFrame implements ActionListener, KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DBHelper dbhelper = new DBHelper();
	
	private CardLayout cardLayout = new CardLayout();
	private JMenuItem adminLogin = new JMenuItem("관리자 로그인");
	private JMenuItem adminLogout = new JMenuItem("사용자 화면");
	private JMenuItem exitMenu = new JMenuItem("종료");
	private JTable bookTable, adminBookTable;
	private JPanel mainPanel, resultPanel, allResultPanel, adminPanel;
	private JPanel searchPanel, insertPanel, modifyPanel, deletePanel, mapPanel;
	private JScrollPane bookScroll, adminBookScroll;
	private JLabel searchLabel;
	private JLabel adminSearchLabel1, adminInsertLabel1, adminInsertLabel2, adminInsertLabel3, adminModifyLabel1, adminModifyLabel2, adminModifyLabel3, adminInsertLabelCount, adminModifyLabelCount;
	private JLabel searchEx, insertEx, insertEx2, modifyEx, modifyEx2, deleteEx, deleteEx2;	//설명부분 레이블
	private JTextField searchTextField;
	private JTextField adminSearch1, adminInsert1, adminInsert2, adminInsert3, adminModify1, adminModify2, adminModify3, adminInsertCount, adminModifyCount;
	private JButton searchButton, printAllButton, backButton, allBackButton, mapBackButton, showMapButton;
	private JButton adminSearchButton, adminInsertButton, adminModifyButton, adminDeleteButton;
	private DefaultTableModel mod, adminMod;
	private String header[] = {"번호", "제목", "책장 위치", "권 수", "비고"};
	private String contents[][];
	private String adminHeader[] = {"번호", "제목", "책장 위치", "권 수", "비고"};
	private String adminContents[][];
	private String adminCheckContents[][];
	
	//결과화면을 위한 요소
	private String resultContents[][];
	private JLabel resultLabel1_1, resultLabel2_1, resultLabel3_1, resultLabel4_1, resultLabel5_1, resultLabel6_1, resultLabel7_1;
	private JLabel resultLabel1_2, resultLabel2_2, resultLabel3_2, resultLabel4_2, resultLabel5_2, resultLabel6_2, resultLabel7_2;
	private JLabel resultLabel1_3, resultLabel2_3, resultLabel3_3, resultLabel4_3, resultLabel5_3, resultLabel6_3, resultLabel7_3;
	private JLabel resultLabel1_4, resultLabel2_4, resultLabel3_4, resultLabel4_4, resultLabel5_4, resultLabel6_4, resultLabel7_4;
	private JLabel header_name, header_bookshelf, header_memo;
	private JButton nextButton, lastButton;
	private int howManyCount = 0;
	private int clickable = 0;
	private int spareLabel = 0;
	private int initIndex = 0;
	private int resultLength = 0;
	private boolean isLastPage = false;
	private boolean isFirstPage = true;
	
	private int tableRowCount, adminTableRowCount;
	private Font mainFont = new Font("배달의민족 도현", Font.PLAIN, 20);
	private JLabel Logo, map;
	
	private final String ADMINPASSWORD = "1026";		//관리자 암호
	
	//엑셀로 백업하는 부분 변수선언
	private JMenuItem backupMenu = new JMenuItem("도서목록 백업");
	JFileChooser backupChooser; //import 해줘야 함

	public BookSearch() {		//GUI 구성

		
		/* 아이콘 이미지
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("icon.png");								//아이콘 이미지
		setIconImage(img);
		*/
		
		setTitle("체셔 도서검색대 v1.0");
		setLayout(cardLayout);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//DBHelper.createTable();		//chesire테이블 없으면 생성

		//메인패널
		Font mainSearchFont = new Font("배달의민족 도현", Font.PLAIN, 35);
		searchLabel = new JLabel("책 제목");	searchLabel.setFont(mainSearchFont);
		searchTextField = new JTextField(50) {public void setBorder(Border border) {} };	searchTextField.setFont(mainSearchFont);
		searchTextField.addActionListener(this);
		JLabel ww = new JLabel("  ");
		JLabel www = new JLabel("  ");
		ww.setOpaque(true);
		www.setOpaque(true);
		ww.setBackground(Color.WHITE);
		www.setBackground(Color.WHITE);
		
		searchButton = new JButton("검색");	searchButton.setFont(mainSearchFont);
		searchButton.addActionListener(this);
		printAllButton = new JButton("도서목록"); printAllButton.setFont(mainSearchFont);
		printAllButton.addActionListener(this);
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		printAllButton.setBorderPainted(false);
		printAllButton.setContentAreaFilled(false);
		
		
		
		ImageIcon mainLogo = new ImageIcon("C:\\Users\\Jiwoo Kim\\Documents\\CheshireLogo\\mainLogo1.png");
		
		
		Logo = new JLabel(mainLogo);
		
		createMenu();
		createJTable();
		createAdminTable();
	
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(new Color(237,211,74));
		mainPanel.add(searchLabel);			searchLabel.setBounds(510, 660, 170, 50);
		mainPanel.add(ww);					ww.setBounds(650, 660, 10, 50);
		mainPanel.add(searchTextField);		searchTextField.setBounds(660, 660, 590, 50);	
		mainPanel.add(www);					www.setBounds(1250, 660, 10, 50);
		mainPanel.add(searchButton);		searchButton.setBounds(1260, 660, 120, 50);
		mainPanel.add(printAllButton);		printAllButton.setBounds(1380, 660, 200, 50);
		
		mainPanel.add(Logo);				Logo.setBounds(750, 100, 500, 500);
		
		//검색 결과 패널
		Font backFont = new Font("배달의민족 도현", Font.PLAIN, 30);
		Font headerFont = new Font("배달의민족 도현", Font.BOLD, 40);
		Font resultFont = new Font("배달의민족 도현", Font.PLAIN, 30);
		Font buttonFont = new Font("배달의민족 도현", Font.BOLD, 40);
		resultPanel = new JPanel();
		resultPanel.setLayout(null);
		resultPanel.setBackground(new Color(237,211,74));
		resultLabel1_1 = new JLabel("1-1");	resultLabel1_2 = new JLabel("1-2");	resultLabel1_3 = new JLabel("1-3");	resultLabel1_4 = new JLabel("1-4");
		resultLabel2_1 = new JLabel("2-1");	resultLabel2_2 = new JLabel("2-2");	resultLabel2_3 = new JLabel("2-3");	resultLabel2_4 = new JLabel("2-4");
		resultLabel3_1 = new JLabel("3-1");	resultLabel3_2 = new JLabel("3-2");	resultLabel3_3 = new JLabel("3-3");	resultLabel3_4 = new JLabel("3-4");
		resultLabel4_1 = new JLabel("4-1");	resultLabel4_2 = new JLabel("4-2");	resultLabel4_3 = new JLabel("4-3");	resultLabel4_4 = new JLabel("4-4");
		resultLabel5_1 = new JLabel("5-1");	resultLabel5_2 = new JLabel("5-2");	resultLabel5_3 = new JLabel("5-3");	resultLabel5_4 = new JLabel("5-4");
		resultLabel6_1 = new JLabel("6-1");	resultLabel6_2 = new JLabel("6-2");	resultLabel6_3 = new JLabel("6-3");	resultLabel6_4 = new JLabel("6-4");
		resultLabel7_1 = new JLabel("7-1");	resultLabel7_2 = new JLabel("7-2");	resultLabel7_3 = new JLabel("7-3");	resultLabel7_4 = new JLabel("7-4");
		resultLabel1_1.setFont(resultFont);	resultLabel1_2.setFont(resultFont);	resultLabel1_3.setFont(resultFont);	resultLabel1_4.setFont(resultFont);
		resultLabel2_1.setFont(resultFont);	resultLabel2_2.setFont(resultFont);	resultLabel2_3.setFont(resultFont);	resultLabel2_4.setFont(resultFont);
		resultLabel3_1.setFont(resultFont);	resultLabel3_2.setFont(resultFont);	resultLabel3_3.setFont(resultFont);	resultLabel3_4.setFont(resultFont);
		resultLabel4_1.setFont(resultFont);	resultLabel4_2.setFont(resultFont);	resultLabel4_3.setFont(resultFont);	resultLabel4_4.setFont(resultFont);
		resultLabel5_1.setFont(resultFont);	resultLabel5_2.setFont(resultFont);	resultLabel5_3.setFont(resultFont);	resultLabel5_4.setFont(resultFont);
		resultLabel6_1.setFont(resultFont);	resultLabel6_2.setFont(resultFont);	resultLabel6_3.setFont(resultFont);	resultLabel6_4.setFont(resultFont);
		resultLabel7_1.setFont(resultFont);	resultLabel7_2.setFont(resultFont);	resultLabel7_3.setFont(resultFont);	resultLabel7_4.setFont(resultFont);
		header_name = new JLabel("제 목");				header_name.setFont(headerFont);
		header_bookshelf = new JLabel("책장 위치");		header_bookshelf.setFont(headerFont);
		header_memo = new JLabel("비 고");				header_memo.setFont(headerFont);
		nextButton = new JButton("▶");			nextButton.setFont(buttonFont);
		lastButton = new JButton("◀");			lastButton.setFont(buttonFont);
		backButton = new JButton("뒤로 가기");		backButton.setFont(backFont);
		showMapButton = new JButton("책장 위치 안내도");		showMapButton.setFont(backFont);
		nextButton.addActionListener(this);
		nextButton.setBorderPainted(false);
		nextButton.setContentAreaFilled(false);
		nextButton.addKeyListener(this);
		lastButton.addActionListener(this);
		lastButton.addKeyListener(this);
		lastButton.setBorderPainted(false);
		lastButton.setContentAreaFilled(false);
		backButton.addActionListener(this);
		backButton.setBorderPainted(false);
		backButton.setContentAreaFilled(false);
		showMapButton.addActionListener(this);
		showMapButton.setBorderPainted(false);
		showMapButton.setContentAreaFilled(false);
		showMapButton.addKeyListener(this);
		
		resultPanel.add(header_name);		header_name.setBounds(460, 120, 150, 50);
		resultPanel.add(header_bookshelf);	header_bookshelf.setBounds(1100, 120, 180, 50);
		resultPanel.add(header_memo);		header_memo.setBounds(1460, 120, 100, 50);
		resultPanel.add(resultLabel1_1);		resultLabel1_1.setBounds(200, 250, 620, 40);
		resultPanel.add(resultLabel1_2);		resultLabel1_2.setBounds(1175, 250, 400, 40);
		resultPanel.add(resultLabel1_3);		resultLabel1_3.setBounds(1430, 250, 400, 40);
		resultPanel.add(resultLabel1_4);		resultLabel1_4.setBounds(820, 250, 400, 40);//
		resultPanel.add(resultLabel2_1);		resultLabel2_1.setBounds(200, 330, 620, 40);
		resultPanel.add(resultLabel2_2);		resultLabel2_2.setBounds(1175, 330, 400, 40);
		resultPanel.add(resultLabel2_3);		resultLabel2_3.setBounds(1430, 330, 400, 40);
		resultPanel.add(resultLabel2_4);		resultLabel2_4.setBounds(820, 330, 400, 40);//
		resultPanel.add(resultLabel3_1);		resultLabel3_1.setBounds(200, 410, 620, 40);
		resultPanel.add(resultLabel3_2);		resultLabel3_2.setBounds(1175, 410, 400, 40);
		resultPanel.add(resultLabel3_3);		resultLabel3_3.setBounds(1430, 410, 400, 40);
		resultPanel.add(resultLabel3_4);		resultLabel3_4.setBounds(820, 410, 400, 40);//
		resultPanel.add(resultLabel4_1);		resultLabel4_1.setBounds(200, 490, 620, 40);
		resultPanel.add(resultLabel4_2);		resultLabel4_2.setBounds(1175, 490, 400, 40);
		resultPanel.add(resultLabel4_3);		resultLabel4_3.setBounds(1430, 490, 400, 40);
		resultPanel.add(resultLabel4_4);		resultLabel4_4.setBounds(820, 490, 400, 40);//
		resultPanel.add(resultLabel5_1);		resultLabel5_1.setBounds(200, 570, 620, 40);
		resultPanel.add(resultLabel5_2);		resultLabel5_2.setBounds(1175, 570, 400, 40);
		resultPanel.add(resultLabel5_3);		resultLabel5_3.setBounds(1430, 570, 400, 40);
		resultPanel.add(resultLabel5_4);		resultLabel5_4.setBounds(820, 570, 400, 40);//
		resultPanel.add(resultLabel6_1);		resultLabel6_1.setBounds(200, 650, 620, 40);
		resultPanel.add(resultLabel6_2);		resultLabel6_2.setBounds(1175, 650, 400, 40);
		resultPanel.add(resultLabel6_3);		resultLabel6_3.setBounds(1430, 650, 400, 40);
		resultPanel.add(resultLabel6_4);		resultLabel6_4.setBounds(820, 650, 400, 40);//
		resultPanel.add(resultLabel7_1);		resultLabel7_1.setBounds(200, 730, 620, 40);
		resultPanel.add(resultLabel7_2);		resultLabel7_2.setBounds(1175, 730, 400, 40);
		resultPanel.add(resultLabel7_3);		resultLabel7_3.setBounds(1430, 730, 400, 40);
		resultPanel.add(resultLabel7_4);		resultLabel7_4.setBounds(820, 730, 400, 40);//
		
		resultPanel.add(lastButton);		lastButton.setBounds(1480, 820, 120, 40);	
		resultPanel.add(nextButton);		nextButton.setBounds(1580, 820, 120, 40);
		resultPanel.add(backButton);		backButton.setBounds(1630, 930, 180, 40);
		resultPanel.add(showMapButton);		showMapButton.setBounds(1275, 930, 270, 40);
		
		
		//전체검색결과 패널
		allResultPanel = new JPanel();
		allBackButton = new JButton("뒤로 가기");	allBackButton.setFont(backFont);
		allBackButton.addActionListener(this);
		allBackButton.setBorderPainted(false);
		allBackButton.setContentAreaFilled(false);
		allResultPanel.setLayout(null);
		allResultPanel.setBackground(new Color(237,211,74));
		
		allResultPanel.add(bookScroll);		bookScroll.setBounds(300, 100, 1350, 780);
		allResultPanel.add(allBackButton);		allBackButton.setBounds(1630, 930, 180, 40);
		
		//관리자 패널
		adminPanel = new JPanel();
		adminPanel.setLayout(null);
		adminPanel.setBackground(new Color(237,211,74));
		
		Font borderFont = new Font("배달의민족 도현", Font.BOLD, 25);
		TitledBorder searchBorder = new TitledBorder("도서 검색"); searchBorder.setTitleFont(borderFont);
		TitledBorder insertBorder = new TitledBorder("도서 추가");	insertBorder.setTitleFont(borderFont);
		TitledBorder modifyBorder = new TitledBorder("도서 수정");	modifyBorder.setTitleFont(borderFont);
		TitledBorder deleteBorder = new TitledBorder("도서 삭제");	deleteBorder.setTitleFont(borderFont);
		searchBorder.setBorder(BorderFactory.createLineBorder(Color.black)); 
		insertBorder.setBorder(BorderFactory.createLineBorder(Color.black));
		modifyBorder.setBorder(BorderFactory.createLineBorder(Color.black));
		deleteBorder.setBorder(BorderFactory.createLineBorder(Color.black));

		
		searchPanel = new JPanel();
		insertPanel = new JPanel();
		modifyPanel = new JPanel();
		deletePanel = new JPanel();
		searchPanel.setBackground(new Color(237,211,74));
		insertPanel.setBackground(new Color(237,211,74));
		modifyPanel.setBackground(new Color(237,211,74));
		deletePanel.setBackground(new Color(237,211,74));
		searchPanel.setBorder(searchBorder);
		insertPanel.setBorder(insertBorder);
		modifyPanel.setBorder(modifyBorder);
		deletePanel.setBorder(deleteBorder);
		searchPanel.setLayout(null);
		insertPanel.setLayout(null);
		modifyPanel.setLayout(null);
		deletePanel.setLayout(null);
		
				//검색부분
		searchEx = new JLabel("※  검색할 도서의 제목을 입력하고 검색 버튼을 누르시오.");	searchEx.setFont(mainFont);
		adminSearchLabel1 = new JLabel("도서 제목");					adminSearchLabel1.setFont(mainFont);
		adminSearch1 = new JTextField(50) {public void setBorder(Border border) {} };	adminSearch1.setFont(mainFont);
		adminSearch1.addActionListener(this);
		adminSearchButton = new JButton("검색");						adminSearchButton.setFont(mainFont);
		adminSearchButton.addActionListener(this);
		adminSearchButton.setContentAreaFilled(false);
		adminSearchButton.setBorderPainted(false);
		
		searchPanel.add(searchEx);				searchEx.setBounds(20, 40, 560, 30);
		searchPanel.add(adminSearchLabel1);		adminSearchLabel1.setBounds(20, 95, 90, 30);
		searchPanel.add(adminSearch1);			adminSearch1.setBounds(130, 95, 350, 30);
		searchPanel.add(adminSearchButton);		adminSearchButton.setBounds(500, 95, 80, 30);
		
				//입력부분
		insertEx = new JLabel("※  추가할 도서의 제목, 책장 위치, 비고(생략가능)를 입력하고");	insertEx.setFont(mainFont);
		insertEx2 = new JLabel("     추가 버튼을 누르시오.");						insertEx2.setFont(mainFont);
		adminInsertLabel1 = new JLabel("도서 제목");		adminInsertLabel1.setFont(mainFont);
		adminInsertLabel2 = new JLabel("책장 위치");		adminInsertLabel2.setFont(mainFont);
		adminInsertLabelCount = new JLabel("권 수");		adminInsertLabelCount.setFont(mainFont);
		adminInsertLabel3 = new JLabel("비 고");			adminInsertLabel3.setFont(mainFont);
		adminInsert1 = new JTextField(50) {public void setBorder(Border border) {} };	adminInsert1.setFont(mainFont);
		adminInsert2 = new JTextField(30) {public void setBorder(Border border) {} };	adminInsert2.setFont(mainFont);
		adminInsertCount = new JTextField(30) {public void setBorder(Border border) {}};	adminInsertCount.setFont(mainFont);
		adminInsert3 = new JTextField(50) {public void setBorder(Border border) {} };	adminInsert3.setFont(mainFont);
		adminInsert3.addActionListener(this);
		adminInsertButton = new JButton("추가");			adminInsertButton.setFont(mainFont);
		adminInsertButton.addActionListener(this);
		adminInsertButton.setContentAreaFilled(false);
		adminInsertButton.setBorderPainted(false);
		
		insertPanel.add(insertEx);				insertEx.setBounds(20, 40, 560, 30);
		insertPanel.add(insertEx2);				insertEx2.setBounds(22, 70, 560, 30);
		insertPanel.add(adminInsertLabel1);		adminInsertLabel1.setBounds(20, 120, 90, 30);
		insertPanel.add(adminInsertLabel2);		adminInsertLabel2.setBounds(20, 157, 90, 30);
		insertPanel.add(adminInsertLabelCount);	adminInsertLabelCount.setBounds(295, 157, 90, 30); 	//카운트 레이블
		insertPanel.add(adminInsertLabel3);		adminInsertLabel3.setBounds(20, 195, 90, 30);
		insertPanel.add(adminInsert1);			adminInsert1.setBounds(130, 120, 350, 30);
		insertPanel.add(adminInsert2);			adminInsert2.setBounds(130, 155, 120, 30);
		insertPanel.add(adminInsertCount);		adminInsertCount.setBounds(360, 155, 120, 30); 		//카운트 텍스트필드
		insertPanel.add(adminInsert3);			adminInsert3.setBounds(130, 190, 350, 30);
		insertPanel.add(adminInsertButton);		adminInsertButton.setBounds(500, 190, 80, 30);
		
				//수정부분
		modifyEx = new JLabel("※  목록에서 수정할 항목을 선택한 뒤 수정할 내용을");			modifyEx.setFont(mainFont);
		modifyEx2 = new JLabel("     입력하고 수정 버튼을 누르시오.");		modifyEx2.setFont(mainFont);
		adminModifyLabel1 = new JLabel("도서 제목");		adminModifyLabel1.setFont(mainFont);
		adminModifyLabel2 = new JLabel("책장 위치");		adminModifyLabel2.setFont(mainFont);
		adminModifyLabelCount = new JLabel("권 수");		adminModifyLabelCount.setFont(mainFont);
		adminModifyLabel3 = new JLabel("비 고");			adminModifyLabel3.setFont(mainFont);
		adminModify1 = new JTextField(50) {public void setBorder(Border border) {} };	adminModify1.setFont(mainFont);
		adminModify2 = new JTextField(30) {public void setBorder(Border border) {} };	adminModify2.setFont(mainFont);
		adminModifyCount = new JTextField(30) {public void setBorder(Border border) {} };	adminModifyCount.setFont(mainFont);
		adminModify3 = new JTextField(50) {public void setBorder(Border border) {} };	adminModify3.setFont(mainFont);
		adminModify3.addActionListener(this);
		adminModifyButton = new JButton("수정");			adminModifyButton.setFont(mainFont);
		adminModifyButton.addActionListener(this);
		adminModifyButton.setContentAreaFilled(false);
		adminModifyButton.setBorderPainted(false);
		
		modifyPanel.add(modifyEx);				modifyEx.setBounds(20, 40, 560, 30);
		modifyPanel.add(modifyEx2);				modifyEx2.setBounds(22, 70, 560, 30);
		modifyPanel.add(adminModifyLabel1);		adminModifyLabel1.setBounds(20, 120, 90, 30);
		modifyPanel.add(adminModifyLabel2);		adminModifyLabel2.setBounds(20, 157, 90, 30);
		modifyPanel.add(adminModifyLabelCount);	adminModifyLabelCount.setBounds(295, 157, 90, 30);		//카운트 레이블
		modifyPanel.add(adminModifyLabel3);		adminModifyLabel3.setBounds(20, 195, 90, 30);
		modifyPanel.add(adminModify1);			adminModify1.setBounds(130, 120, 350, 30);
		modifyPanel.add(adminModify2);			adminModify2.setBounds(130, 155, 120, 30);
		modifyPanel.add(adminModifyCount);		adminModifyCount.setBounds(360, 155, 120, 30);							//카운트 텍스트필드
		modifyPanel.add(adminModify3);			adminModify3.setBounds(130, 190, 350, 30);
		modifyPanel.add(adminModifyButton);		adminModifyButton.setBounds(500, 190, 80, 30);
		
				//삭제부분
		deleteEx = new JLabel("※  목록에서 삭제할 항목을 선택하고 삭제버튼을 누르시오.");	deleteEx.setFont(mainFont);
		adminDeleteButton = new JButton("삭제");			adminDeleteButton.setFont(mainFont);
		adminDeleteButton.addActionListener(this);
		adminDeleteButton.setContentAreaFilled(false);
		adminDeleteButton.setBorderPainted(false);
		
		deletePanel.add(deleteEx);				deleteEx.setBounds(20, 40, 560, 30);
		deletePanel.add(adminDeleteButton);		adminDeleteButton.setBounds(500, 95, 80, 30);
				//관리자 내부 패널 끝
		
		adminPanel.add(searchPanel);		searchPanel.setBounds(50, 32, 610, 150);
		adminPanel.add(insertPanel);		insertPanel.setBounds(50, 223, 610, 250);
		adminPanel.add(modifyPanel);		modifyPanel.setBounds(50, 514, 610, 250);
		adminPanel.add(deletePanel);		deletePanel.setBounds(50, 805, 610, 150);
		adminPanel.add(adminBookScroll);	adminBookScroll.setBounds(700, 30, 1150, 945);

		//안내도 패널
		//전체검색결과 패널
		mapPanel = new JPanel();
		mapPanel.setBackground(new Color(237,211,74));
		mapPanel.setLayout(null);
		
		ImageIcon mapImage = new ImageIcon("C:\\Users\\Jiwoo Kim\\Documents\\도서검색대(삭제금지)\\체셔지도.png");//경로지정
		map = new JLabel(mapImage);
		
		mapBackButton = new JButton("뒤로 가기");		mapBackButton.setFont(backFont);
		mapBackButton.addActionListener(this);
		mapBackButton.setBorderPainted(false);
		mapBackButton.setContentAreaFilled(false);
		
		mapPanel.add(map);					map.setBounds(420, 70, 1100, 900);
		mapPanel.add(mapBackButton);		mapBackButton.setBounds(1630, 930, 180, 40);

		
		
		//카드레이아웃으로 패널 추가
		add(mainPanel, "MAIN");
		add(resultPanel, "RESULT");
		add(allResultPanel, "ALLRESULT");
		add(adminPanel, "ADMIN");
		add(mapPanel, "MAP");
		
		
		
		setUndecorated(true);
		setLocation(0,0);
		setSize(1940, 1080);
		setVisible(true);
	}
	
	
	public void createJTable() {
		ArrayList<String[]> cont = dbhelper.printBook();
		contents = new String[cont.size()][5];
		for(int i = 0; i < cont.size(); i++) {
			for(int j = 0; j < 5; j++) {
				contents[i][j] = cont.get(i)[j];
			}
		}
		tableRowCount = contents.length;
		
		mod = new DefaultTableModel(contents, header) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		Font tableHeaderFont = new Font("배달의민족 도현", Font.PLAIN, 25);
		bookTable = new JTable(mod);
		bookTable.setFont(mainFont);
		JTableHeader tableHeader = bookTable.getTableHeader();
		tableHeader.setFont(tableHeaderFont);

		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 가운데 정렬

		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = bookTable.getColumnModel() ; // 정렬할 테이블의 컬럼모델을 가져옴
	
		tcm.getColumn(2).setCellRenderer(dtcr);
		tcm.getColumn(3).setCellRenderer(dtcr);
		//가운데 정렬 끝

		
		bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		bookTable.getColumnModel().getColumn(0).setPreferredWidth(100);	//셀 너비
		bookTable.getColumnModel().getColumn(1).setPreferredWidth(700);
		bookTable.getColumnModel().getColumn(2).setPreferredWidth(132);
		bookTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		bookTable.getColumnModel().getColumn(4).setPreferredWidth(300);
		
		bookTable.setRowHeight(40);			//셀 높이
		
		bookScroll= new JScrollPane(bookTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	}
	
	public void createAdminTable() {
		ArrayList<String[]> cont = dbhelper.printBook();
		adminContents = new String[cont.size()][5];
		for(int i = 0; i < cont.size(); i++) {
			for(int j = 0; j < 5; j++) {
				adminContents[i][j] = cont.get(i)[j];
			}
		}
		adminTableRowCount = contents.length;
		
		adminMod = new DefaultTableModel(adminContents, adminHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		adminBookTable = new JTable(adminMod);
		adminBookTable.setFont(mainFont);
		JTableHeader tableHeader = adminBookTable.getTableHeader();
		tableHeader.setFont(mainFont);
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 가운데 정렬

		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = adminBookTable.getColumnModel() ; // 정렬할 테이블의 컬럼모델을 가져옴
	
		tcm.getColumn(2).setCellRenderer(dtcr);
		tcm.getColumn(3).setCellRenderer(dtcr);
		//가운데 정렬 끝
		
		adminBookTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		adminBookTable.getColumnModel().getColumn(0).setPreferredWidth(100);	//셀 너비
		adminBookTable.getColumnModel().getColumn(1).setPreferredWidth(600);
		adminBookTable.getColumnModel().getColumn(2).setPreferredWidth(132);
		adminBookTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		adminBookTable.getColumnModel().getColumn(4).setPreferredWidth(200);
		
		adminBookTable.setRowHeight(40);			//셀 높이
		
		adminBookScroll= new JScrollPane(adminBookTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
	}

	
	public void createMenu() {
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(237,211,74));
		menuBar.setBorderPainted(false);;
		JMenu menu = new JMenu("          ");
		Font menuFont = new Font("배달의민족 도현", Font.PLAIN, 20);
		menu.setFont(menuFont);
		
		menuBar.add(menu);
		
		
		adminLogin.setFont(menuFont);
		adminLogout.setFont(menuFont);
		menu.add(adminLogin);
		menu.add(adminLogout);
		
		adminLogin.addActionListener(this);
		adminLogout.addActionListener(this);
		
		menu.addSeparator();			//나중에 메뉴 많아지면 분리..
		//백업 메뉴
		menu.add(backupMenu);
		backupMenu.setFont(menuFont);
		backupMenu.addActionListener(this);
		
		menu.addSeparator();			//나중에 메뉴 많아지면 분리..
		menu.add(exitMenu);
		exitMenu.setFont(menuFont);
		exitMenu.addActionListener(this);
		
		
		this.setJMenuBar(menuBar);
		
	}
	
	public void refreshTable() {
		ArrayList<String[]> cont = dbhelper.printBook();
		contents = new String[cont.size()][5];
		for(int i = 0; i < cont.size(); i++) {
			for(int j = 0; j < 5; j++) {
				contents[i][j] = cont.get(i)[j];
			}
		}
		//System.out.println(Arrays.deepToString(contents));
		
		if(tableRowCount > 0) {
			for (int i = 0; i < tableRowCount; i++) {//테이블 지우기
				mod.removeRow(0);
			}
		}
		
		for (int i = 0; i < contents.length; i++) {//테이블에 추가
			mod.addRow(contents[i]);
		}
		tableRowCount = contents.length;
		//System.out.println(Arrays.deepToString(contents));
	}
	
	public void refreshAdminTable() {
		ArrayList<String[]> cont = dbhelper.printBook();
		adminContents = new String[cont.size()][5];
		for(int i = 0; i < cont.size(); i++) {
			for(int j = 0; j < 5; j++) {
				adminContents[i][j] = cont.get(i)[j];
			}
		}
		//System.out.println(Arrays.deepToString(contents));
		
		if(adminTableRowCount > 0) {
			for (int i = 0; i < adminTableRowCount; i++) {//테이블 지우기
				adminMod.removeRow(0);
			}
		}
		
		for (int i = 0; i < adminContents.length; i++) {//테이블에 추가
			adminMod.addRow(adminContents[i]);
		}
		adminTableRowCount = adminContents.length;
		//System.out.println(Arrays.deepToString(adminContents));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == adminLogin) {	//메뉴에서 관리자 로그인 버튼
			String adminPassword = "";
			adminPassword = JOptionPane.showInputDialog(getParent(), "관리자 암호 입력", "암호 입력");

			if (adminPassword.equals(ADMINPASSWORD)) {
				JOptionPane.showMessageDialog(getParent(), "암호 일치!!");
				cardLayout.show(getContentPane(), "ADMIN");	//관리자 화면으로 넘어감
			}else {
				JOptionPane.showMessageDialog(getParent(), "암호가 일치하지 않습니다.", "로그인 실패", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if(e.getSource() == adminLogout) {
			refreshTable();
			cardLayout.show(getContentPane(), "MAIN");
		}
		else if(e.getSource() == backupMenu) {	//엑셀로 백업하는 부분
			String backupPassword = "";
			backupPassword = JOptionPane.showInputDialog(getParent(), "관리자 암호 입력", "암호 입력");
			
			if (backupPassword.equals(ADMINPASSWORD)) {
				JOptionPane.showMessageDialog(getParent(), "암호 일치!!");
				backupChooser = new JFileChooser("c:/");	//기본 위치 설정
            	backupChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("폴더", ".");
				backupChooser.setFileFilter(filter);

				int returnVal = backupChooser.showOpenDialog(null);
            	if(returnVal == JFileChooser.APPROVE_OPTION) {
					String filePath = backupChooser.getSelectedFile().getAbsolutePath();
					//여기에 엑셀파일(파일이름은 filePath+현재날짜+.xls)로 저장하는 구문, 책장위치 빼고 전부다 앞에 " " 없애야 함, 비고부분은 " "이면 ""를 넣게하고 " " 아니면 맨앞에 " " 빼고 넣게 해야함
					ArrayList<String[]> cont = dbhelper.printBook();
					contents = new String[cont.size()][5];
					for(int i = 0; i < cont.size(); i++) {
						for(int j = 0; j < 5; j++) {
							contents[i][j] = cont.get(i)[j].trim();
						}
					}

					try {
						Date now = new Date();
						SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd-HH-mm");
						String filename = filePath + "\\" + format.format(now) + "도서목록.xls";
						WritableWorkbook myWorkbook;
						myWorkbook = Workbook.createWorkbook(new File(filename));//파일이름 정해서 생성
						WritableSheet mySheet = myWorkbook.createSheet("booklist", 0);//첫번째 시트, "booklist"라는 이름
						WritableCellFormat numberFormat = new WritableCellFormat(); //책장번호
						WritableCellFormat nameFormat = new WritableCellFormat();   //책이름
						WritableCellFormat countFormat = new WritableCellFormat();  //권 수
						WritableCellFormat memoFormat = new WritableCellFormat();   //비고

						//번호 레이블 셀 포맷 구성
						numberFormat.setAlignment(Alignment.CENTRE);	//셀 가운데 정렬
						numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE);    //셀 수직가운데정렬
						numberFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);  //보더와 보더라인스타일
						//numberFormat.setBackground(Colour.ICE_BLUE);    //배경색

						nameFormat.setAlignment(Alignment.GENERAL);
						nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
						nameFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
						//nameFormat.setBackground(Colour.ICE_BLUE);

						countFormat.setAlignment(Alignment.CENTRE);
						countFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
						countFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
						//countFormat.setBackground(Colour.ICE_BLUE);

						memoFormat.setAlignment(Alignment.CENTRE);
						memoFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
						memoFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
						//memoFormat.setBackground(Colour.ICE_BLUE);
							
						    
						

						//컬럼 너비 설정
						mySheet.setColumnView(0,10); //첫번째 컬럼 너비 설정
						mySheet.setColumnView(1,30);
						mySheet.setColumnView(2,10);
						mySheet.setColumnView(3,30);

						//라벨을 이용하여 해당 셀에 정보 넣기 시작
						Label numberLabel = new Label(0, 0, "책장번호", numberFormat);  
						mySheet.addCell(numberLabel);   //시트에 삽입
						Label nameLabel = new Label(1, 0, "책 제목", nameFormat);  
						mySheet.addCell(nameLabel);   //시트에 삽입
						Label countLabel = new Label(2, 0, "권 수", countFormat);  
						mySheet.addCell(countLabel);   //시트에 삽입
						Label memoLabel = new Label(3, 0, "비고", memoFormat);  
						mySheet.addCell(memoLabel);   //시트에 삽입

						for(int i = 1; i < contents.length; i++){
						    Label numberLabels = new Label(0, i, contents[i][2], numberFormat);
						    mySheet.addCell(numberLabels);
						    
						    Label nameLabels = new Label(1, i, contents[i][1], nameFormat);
						    mySheet.addCell(nameLabels);
						    
						    Label countLabels = new Label(2, i, contents[i][3], countFormat);
						    mySheet.addCell(countLabels);
						    
						    Label memoLabels = new Label(3, i, contents[i][4], memoFormat);
						    mySheet.addCell(memoLabels);
						}

						myWorkbook.write(); //준비된 정보를 엑셀 포맷에 맞게 작성
						myWorkbook.close(); //처리 후 메모리에서 해제
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					

					//여기까지 엑셀파일 백업부분
					JOptionPane.showMessageDialog(getParent(), filePath + "에 목록 백업 완료!");
					System.out.println(filePath);
            	}
			}else {
				JOptionPane.showMessageDialog(getParent(), "암호가 일치하지 않습니다.", "거부", JOptionPane.WARNING_MESSAGE);
			}	
		}
		else if(e.getSource() == exitMenu) {
			//this.setVisible(false);	//종료버튼
			System.exit(0);
		}
		else if ((e.getSource() == searchButton) || (e.getSource() == searchTextField)) {	//검색 버튼
			initIndex = 0;
			String searchText = searchTextField.getText();
			searchText = searchText.replaceAll(" ", "");	//공백 삭제
			if(searchText.equals("")) {
				JOptionPane.showMessageDialog(getParent(), "검색어를 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
				searchTextField.setFocusable(true);
				searchTextField.requestFocus();//여기이이
			} else {
				searchTextField.setText(null);
				ArrayList<String[]> cont = dbhelper.printSelectedBook(searchText);
				resultContents = new String[cont.size()][5];
				for(int i = 0; i < cont.size(); i++) {
					for(int j = 0; j < 5; j++) {
							resultContents[i][j] = cont.get(i)[j];
						}
				}
				System.out.println("전체 길이는 " + resultContents.length);
				if (resultContents.length == 0) {
					JOptionPane.showMessageDialog(getParent(), "  검색된 책이 없습니다.", "메시지", JOptionPane.CLOSED_OPTION);
					searchTextField.setFocusable(true);
					searchTextField.requestFocus();//여기이이
				}
				else {
					searchTextField.setFocusable(false);	//여기도오
					searchTextField.requestFocus(false);
					clickable = resultContents.length / 7;
					spareLabel = resultContents.length % 7;
					isFirstPage = true;
					if(clickable == 0) {
						isLastPage = true;
						switch(spareLabel) {
						case 0:{
							resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]);	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
							resultLabel2_1.setText(resultContents[1][1]);	resultLabel2_2.setText(resultContents[1][2]);	resultLabel2_3.setText(resultContents[1][4]);	resultLabel2_4.setText("1 ~" + resultContents[1][3] + " 권");
							resultLabel3_1.setText(resultContents[2][1]);	resultLabel3_2.setText(resultContents[2][2]);	resultLabel3_3.setText(resultContents[2][4]);	resultLabel3_4.setText("1 ~" + resultContents[2][3] + " 권");
							resultLabel4_1.setText(resultContents[3][1]);	resultLabel4_2.setText(resultContents[3][2]);	resultLabel4_3.setText(resultContents[3][4]);	resultLabel4_4.setText("1 ~" + resultContents[3][3] + " 권");
							resultLabel5_1.setText(resultContents[4][1]);	resultLabel5_2.setText(resultContents[4][2]);	resultLabel5_3.setText(resultContents[4][4]);	resultLabel5_4.setText("1 ~" + resultContents[4][3] + " 권");
							resultLabel6_1.setText(resultContents[5][1]);	resultLabel6_2.setText(resultContents[5][2]);	resultLabel6_3.setText(resultContents[5][4]);	resultLabel6_4.setText("1 ~" + resultContents[5][3] + " 권");
							resultLabel7_1.setText(resultContents[6][1]);	resultLabel7_2.setText(resultContents[6][2]);	resultLabel7_3.setText(resultContents[6][4]);	resultLabel7_4.setText("1 ~" + resultContents[6][3] + " 권");
							break;
							}
						case 1:{
							resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]);	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
							resultLabel2_1.setText(null);	resultLabel2_2.setText(null);	resultLabel2_3.setText(null);	resultLabel2_4.setText(null);
							resultLabel3_1.setText(null);	resultLabel3_2.setText(null);	resultLabel3_3.setText(null);	resultLabel3_4.setText(null);
							resultLabel4_1.setText(null);	resultLabel4_2.setText(null);	resultLabel4_3.setText(null);	resultLabel4_4.setText(null);
							resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
							resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
							resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
							break;
							}
						case 2:{
							resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]); 	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
							resultLabel2_1.setText(resultContents[1][1]);	resultLabel2_2.setText(resultContents[1][2]);	resultLabel2_3.setText(resultContents[1][4]);	resultLabel2_4.setText("1 ~" + resultContents[1][3] + " 권");
							resultLabel3_1.setText(null);	resultLabel3_2.setText(null);	resultLabel3_3.setText(null);	resultLabel3_4.setText(null);
							resultLabel4_1.setText(null);	resultLabel4_2.setText(null);	resultLabel4_3.setText(null);	resultLabel4_4.setText(null);
							resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
							resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
							resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
							break;
							}
						case 3:{
							resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]);	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
							resultLabel2_1.setText(resultContents[1][1]);	resultLabel2_2.setText(resultContents[1][2]);	resultLabel2_3.setText(resultContents[1][4]);	resultLabel2_4.setText("1 ~" + resultContents[1][3] + " 권");
							resultLabel3_1.setText(resultContents[2][1]);	resultLabel3_2.setText(resultContents[2][2]);	resultLabel3_3.setText(resultContents[2][4]);	resultLabel3_4.setText("1 ~" + resultContents[2][3] + " 권");
							resultLabel4_1.setText(null);	resultLabel4_2.setText(null);	resultLabel4_3.setText(null);	resultLabel4_4.setText(null);
							resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
							resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
							resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
							break;
							}
						case 4:{
							resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]);	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
							resultLabel2_1.setText(resultContents[1][1]);	resultLabel2_2.setText(resultContents[1][2]);	resultLabel2_3.setText(resultContents[1][4]);	resultLabel2_4.setText("1 ~" + resultContents[1][3] + " 권");
							resultLabel3_1.setText(resultContents[2][1]);	resultLabel3_2.setText(resultContents[2][2]);	resultLabel3_3.setText(resultContents[2][4]);	resultLabel3_4.setText("1 ~" + resultContents[2][3] + " 권");
							resultLabel4_1.setText(resultContents[3][1]);	resultLabel4_2.setText(resultContents[3][2]);	resultLabel4_3.setText(resultContents[3][4]);	resultLabel4_4.setText("1 ~" + resultContents[3][3] + " 권");
							resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
							resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
							resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
							break;
							}
						case 5:{
							resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]);	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
							resultLabel2_1.setText(resultContents[1][1]);	resultLabel2_2.setText(resultContents[1][2]);	resultLabel2_3.setText(resultContents[1][4]);	resultLabel2_4.setText("1 ~" + resultContents[1][3] + " 권");
							resultLabel3_1.setText(resultContents[2][1]);	resultLabel3_2.setText(resultContents[2][2]);	resultLabel3_3.setText(resultContents[2][4]);	resultLabel3_4.setText("1 ~" + resultContents[2][3] + " 권");
							resultLabel4_1.setText(resultContents[3][1]);	resultLabel4_2.setText(resultContents[3][2]);	resultLabel4_3.setText(resultContents[3][4]);	resultLabel4_4.setText("1 ~" + resultContents[3][3] + " 권");
							resultLabel5_1.setText(resultContents[4][1]);	resultLabel5_2.setText(resultContents[4][2]);	resultLabel5_3.setText(resultContents[4][4]);	resultLabel5_4.setText("1 ~" + resultContents[4][3] + " 권");
							resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
							resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
							break;
							}
						case 6:{
							resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]);	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
							resultLabel2_1.setText(resultContents[1][1]);	resultLabel2_2.setText(resultContents[1][2]);	resultLabel2_3.setText(resultContents[1][4]);	resultLabel2_4.setText("1 ~" + resultContents[1][3] + " 권");
							resultLabel3_1.setText(resultContents[2][1]);	resultLabel3_2.setText(resultContents[2][2]);	resultLabel3_3.setText(resultContents[2][4]);	resultLabel3_4.setText("1 ~" + resultContents[2][3] + " 권");
							resultLabel4_1.setText(resultContents[3][1]);	resultLabel4_2.setText(resultContents[3][2]);	resultLabel4_3.setText(resultContents[3][4]);	resultLabel4_4.setText("1 ~" + resultContents[3][3] + " 권");
							resultLabel5_1.setText(resultContents[4][1]);	resultLabel5_2.setText(resultContents[4][2]);	resultLabel5_3.setText(resultContents[4][4]);	resultLabel5_4.setText("1 ~" + resultContents[4][3] + " 권");
							resultLabel6_1.setText(resultContents[5][1]);	resultLabel6_2.setText(resultContents[5][2]);	resultLabel6_3.setText(resultContents[5][4]);	resultLabel6_4.setText("1 ~" + resultContents[5][3] + " 권");
							resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
							break;
							}
						}
					}
					else {
						resultLabel1_1.setText(resultContents[0][1]);	resultLabel1_2.setText(resultContents[0][2]);	resultLabel1_3.setText(resultContents[0][4]);	resultLabel1_4.setText("1 ~" + resultContents[0][3] + " 권");
						resultLabel2_1.setText(resultContents[1][1]);	resultLabel2_2.setText(resultContents[1][2]);	resultLabel2_3.setText(resultContents[1][4]);	resultLabel2_4.setText("1 ~" + resultContents[1][3] + " 권");
						resultLabel3_1.setText(resultContents[2][1]);	resultLabel3_2.setText(resultContents[2][2]);	resultLabel3_3.setText(resultContents[2][4]);	resultLabel3_4.setText("1 ~" + resultContents[2][3] + " 권");
						resultLabel4_1.setText(resultContents[3][1]);	resultLabel4_2.setText(resultContents[3][2]);	resultLabel4_3.setText(resultContents[3][4]);	resultLabel4_4.setText("1 ~" + resultContents[3][3] + " 권");
						resultLabel5_1.setText(resultContents[4][1]);	resultLabel5_2.setText(resultContents[4][2]);	resultLabel5_3.setText(resultContents[4][4]);	resultLabel5_4.setText("1 ~" + resultContents[4][3] + " 권");
						resultLabel6_1.setText(resultContents[5][1]);	resultLabel6_2.setText(resultContents[5][2]);	resultLabel6_3.setText(resultContents[5][4]);	resultLabel6_4.setText("1 ~" + resultContents[5][3] + " 권");
						resultLabel7_1.setText(resultContents[6][1]);	resultLabel7_2.setText(resultContents[6][2]);	resultLabel7_3.setText(resultContents[6][4]);	resultLabel7_4.setText("1 ~" + resultContents[6][3] + " 권");
						initIndex += 7;
					}
				
					//System.out.println(Arrays.deepToString(contents));
					cardLayout.show(getContentPane(), "RESULT");
				}
			}
			
		}else if(e.getSource() == showMapButton) {
			cardLayout.show(getContentPane(), "MAP");
		}else if (e.getSource() == mapBackButton) {
			cardLayout.show(getContentPane(), "RESULT");
		}else if (e.getSource() == printAllButton) {
			//searchTextField.setFocusable(false);	//여기도오
			//searchTextField.requestFocus(false);
			
			searchTextField.setText(null);
			ArrayList<String[]> cont = dbhelper.printBook();
			contents = new String[cont.size()][5];
			for(int i = 0; i < cont.size(); i++) {
				for(int j = 0; j < 5; j++) {
					contents[i][j] = cont.get(i)[j];
				}
			}
			//System.out.println(Arrays.deepToString(contents));
			
			if(tableRowCount > 0) {
				for (int i = 0; i < tableRowCount; i++) {//테이블 지우기
					mod.removeRow(0);
				}
			}
			
			for (int i = 0; i < contents.length; i++) {//테이블에 추가
				mod.addRow(contents[i]);
			}
			tableRowCount = contents.length;
			//System.out.println(Arrays.deepToString(contents));
			cardLayout.show(getContentPane(), "ALLRESULT");
		}
		else if (e.getSource() == nextButton) {
			if((initIndex > 0) && ((initIndex + 7) < resultContents.length)) {		//버튼을 눌러도 되는지, 버튼을 눌러도 된다면 불러옴, 다음장 있음
				resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
				resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
				resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
				resultLabel4_1.setText(resultContents[initIndex+3][1]);	resultLabel4_2.setText(resultContents[initIndex+3][2]);	resultLabel4_3.setText(resultContents[initIndex+3][4]);	resultLabel4_4.setText("1 ~" + resultContents[initIndex+3][3] + " 권");
				resultLabel5_1.setText(resultContents[initIndex+4][1]);	resultLabel5_2.setText(resultContents[initIndex+4][2]);	resultLabel5_3.setText(resultContents[initIndex+4][4]);	resultLabel5_4.setText("1 ~" + resultContents[initIndex+4][3] + " 권");
				resultLabel6_1.setText(resultContents[initIndex+5][1]);	resultLabel6_2.setText(resultContents[initIndex+5][2]);	resultLabel6_3.setText(resultContents[initIndex+5][4]);	resultLabel6_4.setText("1 ~" + resultContents[initIndex+5][3] + " 권");
				resultLabel7_1.setText(resultContents[initIndex+6][1]);	resultLabel7_2.setText(resultContents[initIndex+6][2]);	resultLabel7_3.setText(resultContents[initIndex+6][4]);	resultLabel7_4.setText("1 ~" + resultContents[initIndex+6][3] + " 권");
				initIndex += 7;
				isFirstPage = false;
			} 
			else if((initIndex > 0) && ((initIndex + 7) == resultContents.length)) {
				resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
				resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
				resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
				resultLabel4_1.setText(resultContents[initIndex+3][1]);	resultLabel4_2.setText(resultContents[initIndex+3][2]);	resultLabel4_3.setText(resultContents[initIndex+3][4]);	resultLabel4_4.setText("1 ~" + resultContents[initIndex+3][3] + " 권");
				resultLabel5_1.setText(resultContents[initIndex+4][1]);	resultLabel5_2.setText(resultContents[initIndex+4][2]);	resultLabel5_3.setText(resultContents[initIndex+4][4]);	resultLabel5_4.setText("1 ~" + resultContents[initIndex+4][3] + " 권");
				resultLabel6_1.setText(resultContents[initIndex+5][1]);	resultLabel6_2.setText(resultContents[initIndex+5][2]);	resultLabel6_3.setText(resultContents[initIndex+5][4]);	resultLabel6_4.setText("1 ~" + resultContents[initIndex+5][3] + " 권");
				resultLabel7_1.setText(resultContents[initIndex+6][1]);	resultLabel7_2.setText(resultContents[initIndex+6][2]);	resultLabel7_3.setText(resultContents[initIndex+6][4]);	resultLabel7_4.setText("1 ~" + resultContents[initIndex+6][3] + " 권");
				isLastPage = true;
				isFirstPage = false;
			}
			else if((initIndex > 0) && ((initIndex + 7) > resultContents.length)) {
				switch(spareLabel) {
				case 1:{
					resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
					resultLabel2_1.setText(null);	resultLabel2_2.setText(null);	resultLabel2_3.setText(null);	resultLabel2_4.setText(null);
					resultLabel3_1.setText(null);	resultLabel3_2.setText(null);	resultLabel3_3.setText(null);	resultLabel3_4.setText(null);
					resultLabel4_1.setText(null);	resultLabel4_2.setText(null);	resultLabel4_3.setText(null);	resultLabel4_4.setText(null);
					resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
					resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
					resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
					break;
					}
				case 2:{
					resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
					resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
					resultLabel3_1.setText(null);	resultLabel3_2.setText(null);	resultLabel3_3.setText(null);	resultLabel3_4.setText(null);
					resultLabel4_1.setText(null);	resultLabel4_2.setText(null);	resultLabel4_3.setText(null);	resultLabel4_4.setText(null);
					resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
					resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
					resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
					break;
					}
				case 3:{
					resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
					resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
					resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
					resultLabel4_1.setText(null);	resultLabel4_2.setText(null);	resultLabel4_3.setText(null);	resultLabel4_4.setText(null);
					resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
					resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
					resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
					break;
					}
				case 4:{
					resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
					resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
					resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
					resultLabel4_1.setText(resultContents[initIndex+3][1]);	resultLabel4_2.setText(resultContents[initIndex+3][2]);	resultLabel4_3.setText(resultContents[initIndex+3][4]);	resultLabel4_4.setText("1 ~" + resultContents[initIndex+3][3] + " 권");
					resultLabel5_1.setText(null);	resultLabel5_2.setText(null);	resultLabel5_3.setText(null);	resultLabel5_4.setText(null);
					resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
					resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
					break;
					}
				case 5:{
					resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
					resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
					resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
					resultLabel4_1.setText(resultContents[initIndex+3][1]);	resultLabel4_2.setText(resultContents[initIndex+3][2]);	resultLabel4_3.setText(resultContents[initIndex+3][4]);	resultLabel4_4.setText("1 ~" + resultContents[initIndex+3][3] + " 권");
					resultLabel5_1.setText(resultContents[initIndex+4][1]);	resultLabel5_2.setText(resultContents[initIndex+4][2]);	resultLabel5_3.setText(resultContents[initIndex+4][4]);	resultLabel5_4.setText("1 ~" + resultContents[initIndex+4][3] + " 권");
					resultLabel6_1.setText(null);	resultLabel6_2.setText(null);	resultLabel6_3.setText(null);	resultLabel6_4.setText(null);
					resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
					break;
					}
				case 6:{
					resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
					resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
					resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
					resultLabel4_1.setText(resultContents[initIndex+3][1]);	resultLabel4_2.setText(resultContents[initIndex+3][2]);	resultLabel4_3.setText(resultContents[initIndex+3][4]);	resultLabel4_4.setText("1 ~" + resultContents[initIndex+3][3] + " 권");
					resultLabel5_1.setText(resultContents[initIndex+4][1]);	resultLabel5_2.setText(resultContents[initIndex+4][2]);	resultLabel5_3.setText(resultContents[initIndex+4][4]);	resultLabel5_4.setText("1 ~" + resultContents[initIndex+4][3] + " 권");
					resultLabel6_1.setText(resultContents[initIndex+5][1]);	resultLabel6_2.setText(resultContents[initIndex+5][2]);	resultLabel6_3.setText(resultContents[initIndex+5][4]);	resultLabel6_4.setText("1 ~" + resultContents[initIndex+5][3] + " 권");
					resultLabel7_1.setText(null);	resultLabel7_2.setText(null);	resultLabel7_3.setText(null);	resultLabel7_4.setText(null);
					break;
					}
				}
				isLastPage = true;
				isFirstPage = false;
			}
			else {
						//아무것도 안함
			}
		}
		else if (e.getSource() == lastButton) {
			if(isFirstPage) {
				//아무것도 안함
			}
			else if((isLastPage) && (!isFirstPage)) {	//마지막 페이지 => initIndex 그대로임
				initIndex -= 7;
				if (initIndex == 0) {
					isFirstPage = true;
				}
				resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
				resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
				resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
				resultLabel4_1.setText(resultContents[initIndex+3][1]);	resultLabel4_2.setText(resultContents[initIndex+3][2]);	resultLabel4_3.setText(resultContents[initIndex+3][4]);	resultLabel4_4.setText("1 ~" + resultContents[initIndex+3][3] + " 권");
				resultLabel5_1.setText(resultContents[initIndex+4][1]);	resultLabel5_2.setText(resultContents[initIndex+4][2]);	resultLabel5_3.setText(resultContents[initIndex+4][4]);	resultLabel5_4.setText("1 ~" + resultContents[initIndex+4][3] + " 권");
				resultLabel6_1.setText(resultContents[initIndex+5][1]);	resultLabel6_2.setText(resultContents[initIndex+5][2]);	resultLabel6_3.setText(resultContents[initIndex+5][4]);	resultLabel6_4.setText("1 ~" + resultContents[initIndex+5][3] + " 권");
				resultLabel7_1.setText(resultContents[initIndex+6][1]);	resultLabel7_2.setText(resultContents[initIndex+6][2]);	resultLabel7_3.setText(resultContents[initIndex+6][4]);	resultLabel7_4.setText("1 ~" + resultContents[initIndex+6][3] + " 권");
				isLastPage = false;
				initIndex += 7;
			}
			else if((!isLastPage) && (!isFirstPage)) {	//마지막 페이지 아닌 경우 => initIndex에 +5 되어있음
				initIndex -= 14;
				if (initIndex == 0) {
					isFirstPage = true;
				}
				resultLabel1_1.setText(resultContents[initIndex][1]);	resultLabel1_2.setText(resultContents[initIndex][2]);	resultLabel1_3.setText(resultContents[initIndex][4]);	resultLabel1_4.setText("1 ~" + resultContents[initIndex][3] + " 권");
				resultLabel2_1.setText(resultContents[initIndex+1][1]);	resultLabel2_2.setText(resultContents[initIndex+1][2]);	resultLabel2_3.setText(resultContents[initIndex+1][4]);	resultLabel2_4.setText("1 ~" + resultContents[initIndex+1][3] + " 권");
				resultLabel3_1.setText(resultContents[initIndex+2][1]);	resultLabel3_2.setText(resultContents[initIndex+2][2]);	resultLabel3_3.setText(resultContents[initIndex+2][4]);	resultLabel3_4.setText("1 ~" + resultContents[initIndex+2][3] + " 권");
				resultLabel4_1.setText(resultContents[initIndex+3][1]);	resultLabel4_2.setText(resultContents[initIndex+3][2]);	resultLabel4_3.setText(resultContents[initIndex+3][4]);	resultLabel4_4.setText("1 ~" + resultContents[initIndex+3][3] + " 권");
				resultLabel5_1.setText(resultContents[initIndex+4][1]);	resultLabel5_2.setText(resultContents[initIndex+4][2]);	resultLabel5_3.setText(resultContents[initIndex+4][4]);	resultLabel5_4.setText("1 ~" + resultContents[initIndex+4][3] + " 권");
				resultLabel6_1.setText(resultContents[initIndex+5][1]);	resultLabel6_2.setText(resultContents[initIndex+5][2]);	resultLabel6_3.setText(resultContents[initIndex+5][4]);	resultLabel6_4.setText("1 ~" + resultContents[initIndex+5][3] + " 권");
				resultLabel7_1.setText(resultContents[initIndex+6][1]);	resultLabel7_2.setText(resultContents[initIndex+6][2]);	resultLabel7_3.setText(resultContents[initIndex+6][4]);	resultLabel7_4.setText("1 ~" + resultContents[initIndex+6][3] + " 권");
				isLastPage = false;
				initIndex += 7;
			}
			else {
				System.out.println("lastButton 에서 오류발생함. 확인하세요");//??? 오류일거임
			}
		}
		else if (e.getSource() == backButton) {
			clickable = 0;
			initIndex = 0;
			spareLabel = 0;
			cardLayout.show(getContentPane(), "MAIN");
			searchTextField.setFocusable(true);
			searchTextField.requestFocus();
		}
		else if (e.getSource() == allBackButton) {
			cardLayout.show(getContentPane(), "MAIN");
			searchTextField.setFocusable(true);
			searchTextField.requestFocus();
		}
		//여기부터 관리자메뉴에서의 이벤트
		else if ((e.getSource() == adminSearchButton) || (e.getSource() == adminSearch1)) {	//관리자-검색
			String searchText = adminSearch1.getText();
			searchText = searchText.replaceAll(" ", "");	//공백 삭제
			adminSearch1.setText(null);
			ArrayList<String[]> cont = dbhelper.printSelectedBook(searchText);
			adminContents = new String[cont.size()][5];
			for(int i = 0; i < cont.size(); i++) {
				for(int j = 0; j < 5; j++) {
					adminContents[i][j] = cont.get(i)[j];
				}
			}
			//System.out.println(Arrays.deepToString(contents));
			
			if(adminTableRowCount > 0) {
				for (int i = 0; i < adminTableRowCount; i++) {//테이블 지우기
					adminMod.removeRow(0);
				}
			}
			
			for (int i = 0; i < adminContents.length; i++) {//테이블에 추가
				adminMod.addRow(adminContents[i]);
			}
			adminTableRowCount = adminContents.length;
			//System.out.println(Arrays.deepToString(adminContents));
		}
		else if ((e.getSource() == adminInsertButton) || (e.getSource() == adminInsert3)) {	//관리자-추가
			if ((adminInsert1.getText().equals("")) || (adminInsert2.getText().equals("")) || (adminInsertCount.getText().equals(""))) {//입력 텍스트필드 공백 검사
				JOptionPane.showMessageDialog(getParent(), "'도서 제목', '책장 위치', '권 수'를 모두 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
			}
			else {	//DB에 추가하는 부분
				String searchText = adminInsert1.getText();
				searchText = searchText.replaceAll(" ", "");	//공백 삭제
				ArrayList<String[]> cont = dbhelper.printSelectedBook(searchText);
				adminCheckContents = new String[cont.size()][5];
				for(int i = 0; i < cont.size(); i++) {
					for(int j = 0; j < 5; j++) {
						adminCheckContents[i][j] = cont.get(i)[j];
					}
				}
				if(adminCheckContents.length != 0) {
					JOptionPane.showMessageDialog(getParent(), "같은 이름의 도서가 있습니다.", "오류", JOptionPane.WARNING_MESSAGE);
				} else {
					String name = adminInsert1.getText().replaceAll(" ", "");	//공백 삭제
					if(!adminInsertCount.getText().matches("^[0-9]*$")) {
						JOptionPane.showMessageDialog(getParent(), "[권 수] 항목에 숫자만 입력하세요.(공백X)", "오류", JOptionPane.WARNING_MESSAGE);
					}
					else {
						dbhelper.insertBook(name, adminInsert2.getText().trim(), Integer.parseInt(adminInsertCount.getText()), adminInsert3.getText().trim());
						adminInsert1.setText(null);
						adminInsert2.setText(null);
						adminInsert3.setText(null);
						adminInsertCount.setText(null);
						refreshAdminTable();
						JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 추가되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
					}	
				}
				
			}
		}
		else if ((e.getSource() == adminModifyButton) || (e.getSource() == adminModify3)) {	//관리자-수정
			//DB에서 수정하는 부분
			//JTabel에서 선택된 항목이 없는지 검사하는 부분
			if (adminBookTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(getParent(), "선택된 항목이 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
				return;
				}
			else {
				int mdfResult = JOptionPane.showConfirmDialog(getParent(), "도서를 수정하시겠습니다?", "수정", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(mdfResult == JOptionPane.YES_OPTION) {
					String selectedName = adminContents[adminBookTable.getSelectedRow()][1];
					selectedName = selectedName.replaceAll(" ", "");	//공백 삭제(표에서 왼쪽에 띄어보이기위해서 앞에 공백 넣었기 때문)
					int mdfID = dbhelper.selectBookByName(selectedName);
					String name = adminModify1.getText().replaceAll(" ", "");	//공백 삭제
					if((adminModifyCount.getText().replaceAll(" ", "").equals("")) && (!adminModifyCount.getText().matches("^[0-9]*$"))) {
						JOptionPane.showMessageDialog(getParent(), "[권 수] 항목에 숫자만 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
					}
					else {
						if((name.equals(""))&&(adminModify2.getText().replaceAll(" ", "").equals(""))&&(adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_0001(mdfID, name, adminModify2.getText(), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((name.equals(""))&&(adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_0010(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((name.equals(""))&&(adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_0011(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_0100(mdfID, name, adminModify2.getText(), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_0101(mdfID, name, adminModify2.getText(), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_0110(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_0111(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(adminModify2.getText().replaceAll(" ", "").equals(""))&&(adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1000(mdfID, name, adminModify2.getText(), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(adminModify2.getText().replaceAll(" ", "").equals(""))&&(adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1001(mdfID, name, adminModify2.getText(), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1010(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1011(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1100(mdfID, name, adminModify2.getText(), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1101(mdfID, name, adminModify2.getText(), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1110(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						} else if((!name.equals(""))&&(!adminModify2.getText().replaceAll(" ", "").equals(""))&&(!adminModifyCount.getText().replaceAll(" ", "").equals(""))&&(!adminModify3.getText().equals(""))) {
							dbhelper.modifyBook_1111(mdfID, name, adminModify2.getText(), Integer.parseInt(adminModifyCount.getText()), adminModify3.getText());
							adminModify1.setText(null);
							adminModify2.setText(null);
							adminModify3.setText(null);
							adminModifyCount.setText(null);
							refreshAdminTable();
							JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 수정되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
						}
						
					}
				}else if(mdfResult == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(getParent(), "수정이 취소되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		else if (e.getSource() == adminDeleteButton) {
			if (adminBookTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(getParent(), "선택된 항목이 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
				return;
				}
			else {	//검사 이후 else안에 DB에서 삭제하는 부분
				int dltResult = JOptionPane.showConfirmDialog(getParent(), "도서를 삭제하시겠습니다?", "삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(dltResult == JOptionPane.YES_OPTION) {
					String selectedName = adminContents[adminBookTable.getSelectedRow()][1];
					selectedName = selectedName.replaceAll(" ", "");	//공백 삭제(표에서 왼쪽에 띄어보이기위해서 앞에 공백 넣었기 때문)
					int mdfID = dbhelper.selectBookByName(selectedName);
					dbhelper.deleteBook(mdfID);
					refreshAdminTable();
					JOptionPane.showMessageDialog(getParent(), "도서가 성공적으로 삭제되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
				}else if(dltResult == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(getParent(), "삭제가 취소되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
				}
			} 	
		}
	}
	
	public static void main(String[] args) {
		new BookSearch();
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			clickable = 0;
			initIndex = 0;
			spareLabel = 0;
			cardLayout.show(getContentPane(), "MAIN");
			searchTextField.setFocusable(true);
			searchTextField.requestFocus();
		}
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
