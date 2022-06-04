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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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
	
	private JPanel searchPanel, listPanel;
	private JLabel searchLabel, resultLabel;
	private JLabel titleLabel, dateLabel, timeLabel, theaterLabel, summaryLabel, reviewLabel;
	private JButton searchBtn, buyBtn, under10Btn, bw1015Btn, over15Btn;
	private JTextField searchTextField;
	private JList<String> musicalList, dateList, timeList;
	
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
		return musicalList;
	}
	
	public MusicalPanel() {
		
		this.setLayout(new BorderLayout(10,10));
		
		searchPanel = new JPanel();
		//searchPanel.setSize(1200, 50);
		this.add(searchPanel, BorderLayout.NORTH);
		
		listPanel = new JPanel();
		//listPanel.setSize(1200, 250);
		this.add(listPanel, BorderLayout.SOUTH);
		
		searchPanel.setLayout(new GridLayout(2,3));
		
		searchLabel = new JLabel("                                         뮤지컬 조회 : ");
		searchPanel.add(searchLabel);
		
		searchTextField = new JTextField(20);
		searchPanel.add(searchTextField);
		
		searchBtn = new JButton("Search");
		searchPanel.add(searchBtn);
		searchBtn.addActionListener(new searchBtnListner());
		
		under10Btn = new JButton("~10만원");
		searchPanel.add(under10Btn);
		under10Btn.addActionListener(new priceBtnListner(PRICE_VIEW_1));
		
		bw1015Btn = new JButton("10만원~15만원");
		searchPanel.add(bw1015Btn);
		under10Btn.addActionListener(new priceBtnListner(PRICE_VIEW_2));
		
		over15Btn = new JButton("15만원~");
		searchPanel.add(over15Btn);
		under10Btn.addActionListener(new priceBtnListner(PRICE_VIEW_3));
		
		musicalList = new JList<String>(getMusicals(LOAD_MUSICAL_QUERY));
		// musicalList = new JList<String>(getMusicals(LOAD_MUSICAL_QUERY));
		musicalList.addMouseListener(mouseListener);
		listPanel.add(musicalList);
		listPanel.add(new JScrollPane(musicalList));
		
		setVisible(true);
	}
	

	// <검색> 버튼에 대한 리스너
	// DB에 사용자로부터 받은 입력과 일치하는 데이터가 존재하는지 확인 후 가져
	private class searchBtnListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String title = searchTextField.getText().strip();
			// 사용자 입력 유효성 검증
			if (title.isEmpty()) {
				searchTextField.setText("뮤지컬 제목을 입력해주세요.");
			} else {
				
				musicalList.setListData(getMusicals(SEARCH_MUSICAL_QUERY,title));
				/*
				try (Connection conn = new ConnectionClass().getConnection();
						PreparedStatement preStmt = conn.prepareStatement(SEARCH_MUSICAL_QUERY);) {
					musicalList.setListData(getMusicals(SEARCH_MUSICAL_QUERY,title));
				} catch (SQLException sqle) {
					System.out.println(sqle);
				} */
				
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
			musicalList.setListData(getMusicals(view));
			/*
			try (Connection conn = new ConnectionClass().getConnection();
					PreparedStatement preStmt = conn.prepareStatement(view);) {
				musicalList.setListData(getMusicals(view));
			} catch (SQLException sqle) {
				System.out.println(sqle);
			}*/
		}
	}
	
	// JList click listener
	MouseListener mouseListener = new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
	        if (e.getClickCount() == 2) {
	        	
	        	//제목가져오기
	        	String selectedMusical = musicalList.getSelectedValue();
	        	String [] text = selectedMusical.split("-");
	        	String selectedtitle = text[0].strip();
	        	
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
    			reviewLabel = new JLabel("  평균별점  " + musical.getAvgRate());
    			topPanel.add(titleLabel);			
    			topPanel.add(theaterLabel);
    			topPanel.add(summaryLabel);
    			topPanel.add(reviewLabel);

    			
    			buyBtn= new JButton("예매하기");
    			buyBtn.addActionListener(new buyBtnListener());
    			botPanel.add(buyBtn);
    			

    			infoDialog.add(topPanel);
    			infoDialog.add(midPanel);
    			infoDialog.add(botPanel);
    			infoDialog.setVisible(true);
	        }
	    }
	};
	
	// <예매하> 버튼 리스너
	// 사용자로부터 받은 입력으로 쿼리 생성 후 DB에 삽입
	private class buyBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			buyDialog = new JDialog();
			buyDialog.setSize(500, 400);
			
			
			infoDialog.dispose();
		}
	}
}