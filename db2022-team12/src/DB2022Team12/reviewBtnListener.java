package DB2022Team12;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


//<작성 리뷰 관리> 버튼에 대한 리스너
//작성 리뷰 정보와 삭제 동의 dialog 생성
class reviewBtnListener implements ActionListener {
	
	private JDialog myreviewDialog;
	private JLabel myreviewLabel, ReviewinfoLabel1, ReviewinfoLabel2, ReviewinfoLabel3, ReviewinfoLabel4, ReviewinfoLabel5;
	private JButton deleRBtn, closeRBtn;
	private JPanel myreviewPanel, BtnRPanel;
	
	// 리뷰 정보 검색 쿼리
	private static String REVIEWSEARCH_QUERY = "SELECT id, musical_title, rate, written_at FROM Review WHERE member_id = ?";
	
	@Override
	public void actionPerformed(ActionEvent e) {
		myreviewDialog = new JDialog();
		myreviewDialog.setSize(400, 250);
		myreviewDialog.setTitle("작성 리뷰 관리");
		myreviewDialog.setLayout(new GridLayout(3, 1, 10, 20));
		
		myreviewLabel = new JLabel(User.getName() + "님의 REVIEW", SwingConstants.CENTER);
		myreviewLabel.setFont(new Font("고딕", Font.BOLD, 20));
		myreviewDialog.add(myreviewLabel);
		
		myreviewPanel = new JPanel();
		
		try (Connection conn = new ConnectionClass().getConnection();
				PreparedStatement preStmt = conn.prepareStatement(REVIEWSEARCH_QUERY)) {
				
			// 유저 티켓 검색을 위한 쿼리
			preStmt.setString(1, User.getId());
			ResultSet res = preStmt.executeQuery();
				
			// 해당 아이디의 리뷰가 존재하는지 확인
			if (!res.next()) {
				ReviewinfoLabel1 = new JLabel("작성한 리뷰가 없습니다.");
				myreviewPanel.add(ReviewinfoLabel1);

				myreviewDialog.add(myreviewPanel);
				
				BtnRPanel = new JPanel();
				closeRBtn = new JButton("CLOSE");
				closeRBtn.addActionListener(new deleRDlgListener());
				BtnRPanel.add(closeRBtn);

				myreviewDialog.add(BtnRPanel);
			} else {
				// 유저 리뷰 정보 저장
				// ID, Musical_id, Rate, Written_at
				// review_id. musical_id, musical_name, rate, written_at
				UserReview.ID = res.getInt("id");
				UserReview.title = res.getString("musical_title");
				UserReview.rate = res.getInt("rate");
				UserReview.time = res.getString("written_at");
			
			// ID, Musical_id, Rate, Written_at
			// review_id. musical_id, musical_name, rate, written_at
			ReviewinfoLabel2 = new JLabel("리뷰ID : " + UserReview.ID);
			ReviewinfoLabel3 = new JLabel("공연 제목 : " + UserReview.title);
			ReviewinfoLabel4 = new JLabel("평점 : " + UserReview.rate);
			ReviewinfoLabel5 = new JLabel("리뷰 작성 날짜 : " + UserReview.time);
			
			myreviewPanel.add(ReviewinfoLabel2);
			myreviewPanel.add(ReviewinfoLabel3);
			myreviewPanel.add(ReviewinfoLabel4);
			myreviewPanel.add(ReviewinfoLabel5);
			
			myreviewDialog.add(myreviewPanel);
			
			BtnRPanel = new JPanel();
			
			deleRBtn = new JButton("리뷰 전체 삭제");
			deleRBtn.addActionListener(new delRcheckListener());
			closeRBtn = new JButton("CLOSE");
			closeRBtn.addActionListener(new deleRDlgListener());
			
			BtnRPanel.add(deleRBtn);
			BtnRPanel.add(closeRBtn);

			myreviewDialog.add(BtnRPanel);
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
		
		myreviewDialog.setVisible(true);
	}
	
	// 작성 리뷰 관리 dialog에서 <CLOSE> 버튼에 대한 리스너
	// 작성 리뷰 관리 dialog 닫기
	class deleRDlgListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			myreviewDialog.dispose();
		}
	}
	
	// 작성 리뷰 관리 dialog에서 <리뷰 전체 삭제> 버튼에 대한 리스너
	// 선택한 리뷰를 DB에서 삭제, 삭제 완료 dialog 생성
	class delRcheckListener implements ActionListener {
		private JLabel msgRLabel;
		private JButton checkRBtn;
		private JDialog delRcheckDialog;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			delRcheckDialog = new JDialog();
			delRcheckDialog.setSize(250, 100);
			delRcheckDialog.setTitle("리뷰 삭제");
			delRcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
			
			try (	Connection conn = new ConnectionClass().getConnection();
					Statement preStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
				ResultSet res = preStmt.executeQuery("SELECT * FROM Review WHERE Member_id = '" + User.getId() + "'");
				
				// 해당 아이디의 티켓이 존재하는지 확인
				if (res.next()) {
					preStmt.executeUpdate("DELETE FROM Review WHERE Member_id = '" + User.getId() + "'");
					msgRLabel = new JLabel("작성한 리뷰가 모두 삭제되었습니다.", SwingConstants.CENTER);
					
				} else {
					msgRLabel = new JLabel("ERROR", SwingConstants.CENTER);
				}	
			}catch (SQLException sqle) {
				System.out.println(sqle);
			}
			
			delRcheckDialog.add(msgRLabel);
			
			checkRBtn = new JButton("OK");
			checkRBtn.addActionListener(new deleODlgListener());
			delRcheckDialog.add(checkRBtn);
			
			delRcheckDialog.setVisible(true);
			
			//작성 리뷰 관리 dialog 닫기
			myreviewDialog.dispose();
		}
		
		// 리뷰 삭제 dialog에서 <OK> 버튼에 대한 리스너
		// 리뷰 삭제 dialog 닫기
		class deleODlgListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				delRcheckDialog.dispose();
			}
		}
	}
}

