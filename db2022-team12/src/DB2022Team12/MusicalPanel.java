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
	
	private JPanel searchPanel, listPanel;
	private JLabel searchLabel, resultLabel;
	private JLabel titleLabel, dateLabel, timeLabel, theaterLabel, summaryLabel;
	private JButton searchBtn, buyBtn;
	private JTextField searchTextField;
	private JList<String> musicalList;
	
	private JDialog infoDialog;
	
	public Vector<String> getMusicals(String query){
		Connection conn;
		Vector<String> musicals = new Vector<String>();
		
		try {
			conn = new ConnectionClass().getConnection();
			PreparedStatement preStmt = conn.prepareStatement(query);
			ResultSet res = preStmt.executeQuery();
			
			// Fetch each row from the result set
			while(res.next()) {
			  String title = res.getString("Title");
			  String summary = res.getString("Summary");

			  musicals.add(title + "    " + summary + "		보러가기 ");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return musicals;
	}
	// title search
	public Vector<String> getMusicals(String query, String searchkey){
		Connection conn;
		Vector<String> musicals = new Vector<String>();
		
		try {
			conn = new ConnectionClass().getConnection();
			PreparedStatement preStmt = conn.prepareStatement(query);
			preStmt.setString(1,searchkey);
			ResultSet res = preStmt.executeQuery();
			
			// Fetch each row from the result set
			while(res.next()) {
			  String title = res.getString("Title");
			  String summary = res.getString("Summary");

			  musicals.add(title + "    " + summary + "		보러가기 ");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return musicals;
	}
	
	public MusicalPanel() {
		
		this.setLayout(new BorderLayout(10,10));
		
		searchPanel = new JPanel();
		//searchPanel.setSize(1200, 50);
		this.add(searchPanel, BorderLayout.NORTH);
		
		listPanel = new JPanel();
		//listPanel.setSize(1200, 250);
		this.add(listPanel, BorderLayout.SOUTH);
		
		searchLabel = new JLabel(" 뮤지컬 조회 : ");
		searchPanel.add(searchLabel);
		
		searchTextField = new JTextField(20);
		searchPanel.add(searchTextField);
		
		searchBtn = new JButton("Search");
		searchPanel.add(searchBtn);
		searchBtn.addActionListener(new searchBtnListner());
		
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
				// musicalList.setListData(getMusicals(SEARCH_MUSICAL_QUERY));
				
				// 모든 필드가 유효하다면 데이터베이스 연결
				try (Connection conn = new ConnectionClass().getConnection();
						PreparedStatement preStmt = conn.prepareStatement(SEARCH_MUSICAL_QUERY);) {
					
					musicalList.setListData(getMusicals(SEARCH_MUSICAL_QUERY,title));
					
					/*
					// 해당 제목의 뮤지컬이 존재하는지 확인
					if (!res.next()) {
						resultLabel = new JLabel("입력하신 제목의 뮤지컬이 없습니다.");
						listPanel.add(resultLabel);
						resultLabel.setVisible(true);
					} 
					else {
						//musicalList = new JList<String>(getMusicals(SEARCH_MUSICAL_QUERY));
						musicalList.setListData(getMusicals(SEARCH_MUSICAL_QUERY));
						//listPanel.add(musicalList);
						//listPanel.add(new JScrollPane(musicalList));
						
					}
					*/
				} catch (SQLException sqle) {
					System.out.println(sqle);
				}
				
			}
		}
	}
	
	// JList click listener
	MouseListener mouseListener = new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
	        if (e.getClickCount() == 2) {
    			infoDialog = new JDialog();
    			infoDialog.setSize(300, 400);
    			infoDialog.setLayout(new GridLayout(3, 1));

    			JPanel topPanel = new JPanel(new GridLayout(5, 1));
    			JPanel midPanel = new JPanel();
    			JPanel botPanel = new JPanel();

    			titleLabel = new JLabel("  제목  "); // + Musical.title
    			dateLabel = new JLabel("  날짜  ");
    			timeLabel = new JLabel("  시간  ");
    			theaterLabel = new JLabel("  극장  ");
    			summaryLabel = new JLabel("  줄거리  ");
    			topPanel.add(titleLabel);
    			topPanel.add(dateLabel);
    			topPanel.add(timeLabel);
    			topPanel.add(theaterLabel);
    			topPanel.add(summaryLabel);

    			//리뷰 추가
    			
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
			/*
				//티켓 예매로 넘
				try (Connection conn = new ConnectionClass().getConnection();
						PreparedStatement preStmt = conn.prepareStatement(SIGNUP_QUERY);) {
					// Member INSERT를 위한 쿼리 생성 및 커밋
					preStmt.setString(1, id);
					preStmt.setString(2, pw);
					preStmt.setString(3, name);
					preStmt.setString(4, phone);
					preStmt.setString(5, email);
					preStmt.setString(6, address);
					preStmt.executeUpdate();
				} catch (SQLException sqle) {
					int error = sqle.getErrorCode();
					// ID(Primary Key) 중복 에러 처리
					if (error == ConnectionClass.ER_DUP_ENTRY || error == ConnectionClass.ER_DUP_KEY)
						noticeLabel.setText("중복된 아이디입니다");

					System.out.println(sqle);
				}
				*/
				// 회원가입 dialog 닫기
				infoDialog.dispose();
			}
	}
}