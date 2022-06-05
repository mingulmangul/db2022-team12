package DB2022Team12;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


//<작성 리뷰 관리> 버튼에 대한 리스너
//작성 리뷰 정보와 삭제 동의 dialog 생성
class reviewBtnListener implements ActionListener {
	
	private JDialog myreviewDialog;
	private JLabel myreviewLabel, ReviewinfoLabel;
	private JButton deleRBtn, closeRBtn;
	private JPanel BtnRPanel;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		myreviewDialog = new JDialog();
		myreviewDialog.setSize(400, 250);
		myreviewDialog.setTitle("작성 리뷰 관리");
		myreviewDialog.setLayout(new GridLayout(3, 1, 10, 20));
		
		myreviewLabel = new JLabel(User.getName() + "님의 REVIEW", SwingConstants.CENTER);
		myreviewLabel.setFont(new Font("고딕", Font.BOLD, 20));
		myreviewDialog.add(myreviewLabel);
		
		List<Object> Review = new ArrayList<Object>();
		
		try (Connection conn = new ConnectionClass().getConnection();
				Statement stmt = conn.createStatement();) {
				
			String REVIEWSEARCH_QUERY = "SELECT * FROM review WHERE member_id = '" + User.getId() + "'";
			
			boolean Rresults = stmt.execute(REVIEWSEARCH_QUERY);
			
	        do {
	            if (Rresults) {
	                ResultSet res = stmt.getResultSet();

	                while (res.next()) {
	                	Review.add(res.getInt("id") + ", " + res.getString("musical_title") + ", " + res.getInt("rate") + ", " + res.getString("written_at"));
	                }
	            }
	            Rresults = stmt.getMoreResults();
	        } while (Rresults);
	        
	        String Rstr = "";
	        for(int i = 0; i < Review.size(); i++) {
	        	Rstr += Review.get(i) + "<br />";
	        }
	        ReviewinfoLabel = new JLabel("<html><body style='text-align:center;'>" + Rstr +"</body></html>");
	        myreviewDialog.add(ReviewinfoLabel);
			
			BtnRPanel = new JPanel(new GridLayout(1, 2, 1, 10));
			deleRBtn = new JButton("리뷰 전체 삭제");
			deleRBtn.addActionListener(new delRcheckListener());
			closeRBtn = new JButton("CLOSE");
			closeRBtn.addActionListener(new deleRDlgListener());
			BtnRPanel.add(deleRBtn);
			BtnRPanel.add(closeRBtn);

			myreviewDialog.add(BtnRPanel);
			
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
