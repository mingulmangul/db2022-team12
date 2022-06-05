package DB2022Team12;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import DB2022Team12.checkTicketListener.TerrorC;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.delTcheckListener;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.deleSDlgListener;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.deleTItem;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.delTcheckListener.deleEDlgListener;


//<작성 리뷰 관리> 버튼에 대한 리스너
//작성 리뷰 정보와 삭제 동의 dialog 생성
class reviewBtnListener implements ActionListener {
	
	private JDialog myreviewDialog;
	private JLabel myreviewLabel, ReviewinfoLabel;
	private JButton deleRBtn, closeRBtn;
	private JPanel BtnRPanel;
	
	List<UserReview> Review; // 유저의 리뷰 정보를 저장할 UserReview 객체 리스트
	int myReviewNum = 0;  // 유저의 리뷰 개수
	int[] deleRId; // 삭제할 리뷰 id 배열
	
	@Override
	public void actionPerformed(ActionEvent e) {
		myreviewDialog = new JDialog();
		myreviewDialog.setSize(400, 250);
		myreviewDialog.setTitle("작성 리뷰 관리");
		myreviewDialog.setLayout(new GridLayout(3, 1, 10, 20));
		
		myreviewLabel = new JLabel(User.getName() + "님의 REVIEW", SwingConstants.CENTER);
		myreviewLabel.setFont(new Font("고딕", Font.BOLD, 20));
		myreviewDialog.add(myreviewLabel);
		
		Review = new ArrayList<UserReview>();
		myReviewNum = 0;
		
		try (Connection conn = new ConnectionClass().getConnection();
				Statement stmt = conn.createStatement();) {
				
			String REVIEWSEARCH_QUERY = "SELECT id, musical_title, rate, written_at FROM db2022_review WHERE member_id = '" + User.getId() + "'";
			
	        boolean Rresults = stmt.execute(REVIEWSEARCH_QUERY);
	        ResultSet res = stmt.getResultSet();
	        int Rerror = 0;
			
	        do {
	            if (Rresults) {
	                while (res.next()) {
	                	Rerror++;
	                	UserReview review = new UserReview();
	                	review.setID(res.getInt("id"));
	                	review.setTitle(res.getString("musical_title"));
	                	review.setRate(res.getInt("rate"));
	                	review.setTime(res.getString("written_at"));
	                	Review.add(review);
	                }
	            }
	            Rresults = stmt.getMoreResults();
	        } while (Rresults);
	        
	        if(Rerror != 0) {
	        	
		        String Rstr = "";
		        for(int i = 0; i < Review.size(); i++) {
		        	Rstr += "NO." + (i+1) + " --- 리뷰 id : " + Review.get(i).getID() + ",  공연 제목 : " + Review.get(i).getTitle() + ",  평점 : " + Review.get(i).getRate() + ",  리뷰 작성 날짜 : " + Review.get(i).getTime() + "<br />";
		        	myReviewNum++;
		        }
		        ReviewinfoLabel = new JLabel("<html><body style='text-align:center;'>" + Rstr +"</body></html>");
				
				BtnRPanel = new JPanel(new GridLayout(1, 2, 1, 10));
				deleRBtn = new JButton("리뷰 삭제");
				deleRBtn.addActionListener(new selectRListener());
				closeRBtn = new JButton("CLOSE");
				closeRBtn.addActionListener(new deleRDlgListener());
				BtnRPanel.add(deleRBtn);
				BtnRPanel.add(closeRBtn);
	        	
	        }else {
	        	ReviewinfoLabel = new JLabel("작성한 리뷰가 없습니다.");
	        	
	        	BtnRPanel = new JPanel();
				closeRBtn = new JButton("CLOSE");
				closeRBtn.addActionListener(new deleRDlgListener());
				BtnRPanel.add(closeRBtn);
	        }
	        
			myreviewDialog.add(ReviewinfoLabel);
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
	
	// 체크박스를 하나도 체크하지 않을 경우를 확인하기 위한 클래스
	class RerrorC {
		static int num = 0;
	}
	
	// 작성 리뷰 관리 dialog에서 <리뷰 삭제> 버튼에 대한 리스너
	// 작성한 정보와 리뷰 삭제 동의 dialog 생성
	class selectRListener implements ActionListener {
		private JButton deleRBtn, closeRBtn;
		private JPanel reserveRPanel, selectRPanel;
		private JDialog selectRDialog;
		private JLabel explainRLabel;
		JCheckBox[] ck = new JCheckBox[myReviewNum];
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			myreviewDialog.dispose();
			
			selectRDialog = new JDialog();
			selectRDialog.setSize(200, 150);
			selectRDialog.setTitle("삭제 리뷰 선택");
			selectRDialog.setLayout(new GridLayout(3, 1, 10, 5));
				
			explainRLabel = new JLabel("삭제할 리뷰를 선택하세요.");
			selectRDialog.add(explainRLabel);
				
			reserveRPanel = new JPanel();
			
			RerrorC.num = 0;
				
			for (int i = 0; i < myReviewNum; i++){
				ck[i] = new JCheckBox("NO." + (i+1) + " --- 리뷰 id : " + Review.get(i).getID() + ",  공연 제목 : " + Review.get(i).getTitle() + ",  평점 : " + Review.get(i).getRate() + ",  리뷰 작성 날짜 : " + Review.get(i).getTime(),false);
				ck[i].addItemListener(new deleRItem());
				reserveRPanel.add(ck[i]);
			}				
			selectRDialog.add(reserveRPanel);
				
			selectRPanel = new JPanel(new GridLayout(1, 2, 1, 5));
			deleRBtn = new JButton("리뷰 삭제 동의");
			deleRBtn.addActionListener(new delRcheckListener());
			selectRPanel.add(deleRBtn);
			
			closeRBtn = new JButton("CLOSE");
			closeRBtn.addActionListener(new deleCDlgListener());
			selectRPanel.add(closeRBtn);
				
			selectRDialog.add(selectRPanel);
				
			selectRDialog.setVisible(true);
		}
		
		// 삭제 리뷰 선택 dialog에서 <CLOSE> 버튼에 대한 리스너
		// 삭제 리뷰 선택 dialog 닫기
		class deleCDlgListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectRDialog.dispose();
			}
		}
		
		// 삭제 리뷰 선택 체크박스에 대한 리스너
		class deleRItem implements ItemListener {
			public void itemStateChanged(ItemEvent e) {
				deleRId = new int[myReviewNum];
				
				for (int i = 0; i < myReviewNum; i++){
					if(ck[i].isSelected()) {
						deleRId[i] = Review.get(i).getID();
					}else {
						deleRId[i] = 0;
					}
					RerrorC.num = 0;
					for (int j = 0; j < myReviewNum; j++){
						RerrorC.num += deleRId[j];
					}
				}
			}
			
		}	
		
		// <리뷰 삭제 동의> 버튼에 대한 리스너
		// 선택된 리뷰 정보를 DB에서 삭제, 리뷰 삭제 완료 dialog 생성
		class delRcheckListener implements ActionListener {
			private JLabel XRLabel, msgRLabel;
			private JButton XRBtn, checkRBtn;
			private JDialog delRcheckDialog;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(RerrorC.num == 0) {
					delRcheckDialog = new JDialog();
					delRcheckDialog.setSize(120, 100);
					delRcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
					
					XRLabel = new JLabel("하나 이상의 체크박스를 선택하세요.");
					delRcheckDialog.add(XRLabel);
					
					XRBtn = new JButton("OK");
					XRBtn.addActionListener(new deleBDlgListener());
					delRcheckDialog.add(XRBtn);
					
					delRcheckDialog.setVisible(true);
					
					selectRDialog.dispose();
					
				}else {
					delRcheckDialog = new JDialog();
					delRcheckDialog.setSize(250, 100);
					delRcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
					
					for (int i = 0; i < myReviewNum; i++){
						if(deleRId[i] != 0) {
							try (	Connection conn = new ConnectionClass().getConnection();
									Statement preStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
								ResultSet res = preStmt.executeQuery("SELECT * FROM db2022_Review WHERE id = '" + deleRId[i] + "'");
								
								// 해당 리뷰 id의 리뷰가 존재하는지 확인
								if (res.next()) {
									preStmt.executeUpdate("DELETE FROM db2022_Review WHERE id = '" + deleRId[i] + "'");
									msgRLabel = new JLabel("리뷰 삭제 완료", SwingConstants.CENTER);
								} else {
									msgRLabel = new JLabel("ERROR", SwingConstants.CENTER);
								}	
							}catch (SQLException sqle) {
								System.out.println(sqle);
							}
						}
					}
					
					delRcheckDialog.add(msgRLabel);
					
					checkRBtn = new JButton("OK");
					checkRBtn.addActionListener(new deleODlgListener());
					delRcheckDialog.add(checkRBtn);
					
					delRcheckDialog.setVisible(true);
					
					selectRDialog.dispose();
				}
			}
			
			// 체크박스 선택이 없음을 알리는 dialog에서 <OK> 버튼에 대한 리스너
			// 체크박스 선택이 없음을 알리는 dialog 닫기
			class deleBDlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delRcheckDialog.dispose();
				}
			}
			
			// 리뷰 삭제 완료 dialog에서 <OK> 버튼에 대한 리스너
			// 리뷰 삭제 완료 dialog 닫기
			class deleODlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delRcheckDialog.dispose();
				}
			}
		}
	}

}




