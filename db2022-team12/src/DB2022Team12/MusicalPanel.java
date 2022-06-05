package DB2022Team12;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



public class MusicalPanel extends JPanel{
	
	//뮤지컬 검색 쿼리
	private final String SEARCH_MUSICAL_QUERY = "SELECT Title, Summary FROM Musical WHERE Title = ?";
	//뮤지컬 정보 불러오기 쿼리
	private final String LOAD_MUSICAL_QUERY = "SELECT Title, Summary FROM Musical";
	//가격 뷰
	private final String PRICE_VIEW_1 = "SELECT * FROM under_10";
	private final String PRICE_VIEW_2 = "SELECT * FROM between10_15";
	private final String PRICE_VIEW_3 = "SELECT * FROM over_15";
	//가격 별 검색
	private final String PRICE1_MUSICAL_QUERY = "SELECT Title, Summary FROM under_10 WHERE Title = ?";
	private final String PRICE2_MUSICAL_QUERY = "SELECT Title, Summary FROM between10_15 WHERE Title = ?";
	private final String PRICE3_MUSICAL_QUERY = "SELECT Title, Summary FROM over_15 WHERE Title = ?";

	private JPanel searchPanel, listPanel;
	private JLabel searchLabel, resultLabel, fakeLabel;
	private JLabel titleLabel, dateLabel, timeLabel, theaterLabel, summaryLabel, reviewLabel;
	private JButton searchBtn, buyBtn; 
	private JRadioButton under10Btn, bw1015Btn, over15Btn, allBtn;
	private JTextField searchTextField;
	private JList<String> resultList;
	
	private JDialog infoDialog, buyDialog;
	
	public Vector<String> getMusicals(String query){
		Connection conn;
		Vector<String> musicalList = new Vector<String>();
		
		try {
			conn = new ConnectionClass().getConnection();
			PreparedStatement preStmt = conn.prepareStatement(query);
			ResultSet res = preStmt.executeQuery();
			
			// Fetch each row from the result set
			while(res.next()) {
			  String title = res.getString("Title");
			  String summary = res.getString("Summary");

			  musicalList.add(title + "  " + summary + " 보러가기 ");

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(musicalList.isEmpty()) {
			musicalList.add("검색하신 뮤지컬이 존재하지 않습니다.");
		}
		return musicalList;

	}
	// title search
	public Vector<String> getMusicals(String query, String searchkey){
		Connection conn;
		Vector<String> musicalList = new Vector<String>();

		
		try {
			conn = new ConnectionClass().getConnection();
			PreparedStatement preStmt = conn.prepareStatement(query);
			preStmt.setString(1,searchkey);
			ResultSet res = preStmt.executeQuery();
			
			// Fetch each row from the result set
			while(res.next()) {
			  String title = res.getString("Title");
			  String summary = res.getString("Summary");

			  musicalList.add(title + "  " + summary + " 보러가기 ");

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(musicalList.isEmpty()) {
			musicalList.add("검색하신 뮤지컬이 존재하지 않습니다.");
		}
		return musicalList;

	}
	
	public MusicalPanel() {
		
		this.setLayout(new GridLayout(2,1));
		
		searchPanel = new JPanel();
		//searchPanel.setSize(1100, 100);
		this.add(searchPanel);
		
		listPanel = new JPanel();
		//listPanel.setSize(1100, 250);
		this.add(listPanel);
		
		searchPanel.setLayout(new GridLayout(2,4));
		
		searchLabel = new JLabel("                                         뮤지컬 조회 : ");

		searchPanel.add(searchLabel);
		
		searchTextField = new JTextField(20);
		searchPanel.add(searchTextField);
		
		searchBtn = new JButton("Search");
		searchPanel.add(searchBtn);
		searchBtn.addActionListener(new searchBtnListner());
		
		fakeLabel = new JLabel("                ");
		searchPanel.add(fakeLabel);
		
		ButtonGroup group = new ButtonGroup();
		
		allBtn = new JRadioButton("전체");
		searchPanel.add(allBtn);
		allBtn.addActionListener(new priceBtnListner(LOAD_MUSICAL_QUERY));
		group.add(allBtn);
		
		under10Btn = new JRadioButton("~10만원");
		searchPanel.add(under10Btn);
		under10Btn.addActionListener(new priceBtnListner(PRICE_VIEW_1));
		group.add(under10Btn);
		
		bw1015Btn = new JRadioButton("10만원~15만원");
		searchPanel.add(bw1015Btn);
		bw1015Btn.addActionListener(new priceBtnListner(PRICE_VIEW_2));
		group.add(bw1015Btn);
		
		over15Btn = new JRadioButton("15만원~");
		searchPanel.add(over15Btn);
		over15Btn.addActionListener(new priceBtnListner(PRICE_VIEW_3));
		group.add(over15Btn);
		
		
		resultList = new JList<String>(getMusicals(LOAD_MUSICAL_QUERY));
		// musicalList = new JList<String>(getMusicals(LOAD_MUSICAL_QUERY));
		resultList.addMouseListener(mouseListener);
		listPanel.add(resultList);
		listPanel.add(new JScrollPane(resultList));
		
		setVisible(true);
	}
	

	// <검색> 버튼에 대한 리스너
	// DB에 사용자로부터 받은 입력과 일치하는 데이터가 존재하는지 확인 후 가져
	private class searchBtnListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String title = searchTextField.getText().strip();
			searchTextField.setText("");
			
			// 사용자 입력 유효성 검증
			if (title.isEmpty()) {
				searchTextField.setText("뮤지컬 제목을 입력해주세요.");
			} 
			else if(under10Btn.isSelected()){
				resultList.setListData(getMusicals(PRICE1_MUSICAL_QUERY,title));
			}
			else if(bw1015Btn.isSelected()){
				resultList.setListData(getMusicals(PRICE2_MUSICAL_QUERY,title));
			} 
			else if(over15Btn.isSelected()){
				resultList.setListData(getMusicals(PRICE3_MUSICAL_QUERY,title));
			} 
			else {
				resultList.setListData(getMusicals(SEARCH_MUSICAL_QUERY,title));
				
			}
		}
	}
	
	//가격 버튼
	private class priceBtnListner implements ActionListener {
		String view;
		
		priceBtnListner(String view){
			this.view = view;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			resultList.setListData(getMusicals(view));
		}
	}
	
	// JList click listener
	MouseListener mouseListener = new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
	        if (e.getClickCount() == 2) {

	        	//제목가져오기
	        	String selectedMusical = resultList.getSelectedValue();
	        	String [] text = selectedMusical.split(" ");
	        	String selectedtitle = text[0].strip();
	        	
	        	System.out.print(selectedtitle);
	        	
    			infoDialog = new JDialog();
    			infoDialog.setSize(500, 400);
    			infoDialog.setLayout(new GridLayout(3, 1));

    			JPanel topPanel = new JPanel(new GridLayout(4, 1));
    			JPanel midPanel = new JPanel();
    			JPanel botPanel = new JPanel();
    			
    			Musical musical = new Musical(selectedtitle);

    			titleLabel = new JLabel("  제목  " + musical.getTitle());
    			theaterLabel = new JLabel("  극장  " + musical.getTheaterName());
    			summaryLabel = new JLabel("  줄거리  "  + musical.getSummary());
    			//reviewLabel = new JLabel("  평균별점  " + musical.getAvgRate());
    			topPanel.add(titleLabel);			
    			topPanel.add(theaterLabel);
    			topPanel.add(summaryLabel);
    			//topPanel.add(reviewLabel);
    			
    			//buyBtn= new JButton("예매하기");
    			//buyBtn.addActionListener(new buyBtnListener());
    			//botPanel.add(buyBtn);
    			TicketPanel ticketPanel = new TicketPanel(musical);
    			midPanel.add(ticketPanel);

    			infoDialog.add(topPanel);
    			infoDialog.add(midPanel);
    			infoDialog.add(botPanel);
    			infoDialog.setVisible(true);
	        }
	    }
	    
	};
	
}