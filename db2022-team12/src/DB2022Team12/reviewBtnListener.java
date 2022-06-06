package DB2022Team12;

import java.awt.BorderLayout;
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
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;

import DB2022Team12.checkTicketListener.TerrorC;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.delTcheckListener;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.deleSDlgListener;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.deleTItem;
import DB2022Team12.checkTicketListener.cancelTicketBtnListener.delTcheckListener.deleEDlgListener;

/**
 * 작성 리뷰 관리 버튼에 대한 리스너<br>
 * 작성 리뷰 정보 DB에서 검색<br>
 * 리뷰 정보를 보여주는 작성 리뷰 관리 dialog 생성
 * 
 * @author sonab
 * 
 */
class reviewBtnListener implements ActionListener {

	private JDialog myreviewDialog;
	private JLabel myreviewLabel, ReviewinfoLabel;
	private JButton deleRBtn, closeRBtn;
	private JPanel BtnRPanel;

	List<UserReview> Review;// 사용자의 리뷰 정보를 저장할 UserReview 객체 리스트
	int myReviewNum = 0;// 사용자 리뷰 개수
	int[] deleRId; // 삭제할 리뷰 id 배열

	@Override
	public void actionPerformed(ActionEvent e) {
		// 작성 리뷰 관리 Dialog 레이아웃 설정
		myreviewDialog = new JDialog();
		myreviewDialog.setSize(600, 300);
		myreviewDialog.setTitle("작성 리뷰 관리");
		myreviewDialog.setLayout(new BorderLayout());

		// 사용자 이름 출력 Label
		myreviewLabel = new JLabel(User.getName() + "님의 REVIEW", SwingConstants.CENTER);
		myreviewLabel.setFont(new Font("고딕", Font.BOLD, 20));
		myreviewDialog.add(myreviewLabel, BorderLayout.NORTH);

		Review = new ArrayList<UserReview>(); // 사용자의 리뷰 정보를 저장할 UserReview 객체 ArrayList
		myReviewNum = 0; // 사용자 리뷰 개수 초기화

		/**
		 * 데이터베이스 연결
		 */
		try (Connection conn = new ConnectionClass().getConnection(); Statement stmt = conn.createStatement();) {

			/**
			 * review SELECT를 위한 쿼리 생성 및 UserReview 객체 ArrayList를 이용해 정보 저장
			 */
			String REVIEWSEARCH_QUERY = "SELECT id, musical_title, rate, written_at FROM db2022_review WHERE member_id = '"
					+ User.getId() + "'";

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

			if (Rerror != 0) {// 작성한 리뷰가 있을 경우

				// 사용자 리뷰 정보 제시 Label
				String Rstr = "";
				for (int i = 0; i < Review.size(); i++) {
					Rstr += "NO." + (i + 1) + " --- 리뷰 id : " + Review.get(i).getID() + ",  공연 제목 : "
							+ Review.get(i).getTitle() + ",  평점 : " + Review.get(i).getRate() + ",  리뷰 작성 날짜 : "
							+ Review.get(i).getTime() + "<br />";
					myReviewNum++;
				}
				ReviewinfoLabel = new JLabel("<html><body style='text-align:center;'>" + Rstr + "</body></html>",
						SwingConstants.CENTER);

				// <리뷰 삭제>, <CLOSE> 버튼 Panel
				BtnRPanel = new JPanel(new GridLayout(1, 2, 1, 1));
				deleRBtn = new JButton("리뷰 삭제");
				deleRBtn.addActionListener(new selectRListener());
				closeRBtn = new JButton("CLOSE");
				closeRBtn.addActionListener(new deleRDlgListener());
				BtnRPanel.add(deleRBtn);
				BtnRPanel.add(closeRBtn);

			} else { // 작성한 리뷰가 없을 경우

				// 사용자 리뷰 정보 제시 Label
				ReviewinfoLabel = new JLabel("작성한 리뷰가 없습니다.", SwingConstants.CENTER);

				// <CLOSE> 버튼 Panel
				BtnRPanel = new JPanel();
				closeRBtn = new JButton("CLOSE");
				closeRBtn.addActionListener(new deleRDlgListener());
				BtnRPanel.add(closeRBtn);
			}

			myreviewDialog.add(ReviewinfoLabel, BorderLayout.CENTER);
			myreviewDialog.add(BtnRPanel, BorderLayout.SOUTH);

		} catch (SQLException sqle) {
			System.out.println(sqle);
		}

		myreviewDialog.setVisible(true);
	}

	/**
	 * 작성 리뷰 관리 Dialog에서 CLOSE 버튼에 대한 리스너<br>
	 * 작성 리뷰 관리 Dialog 닫기
	 */
	class deleRDlgListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			myreviewDialog.dispose();
		}
	}

	/**
	 * 체크박스를 하나도 체크하지 않을 경우를 확인하기 위한 클래스<br>
	 * 체크박스를 하나도 선택하지 않았을 경우 num == 0, 적어도 하나 선택한 경우 num != 0
	 */
	class RerrorC {
		static int num = 0;
	}

	/**
	 * 작성 리뷰 관리 Dialog에서 리뷰 삭제 버튼에 대한 리스너 작성 리뷰 관리 Dialog 닫기 삭제 리뷰 선택 Dialog 생성
	 */
	class selectRListener implements ActionListener {

		private JButton deleRBtn, closeRBtn;
		private JPanel reserveRPanel, selectRPanel;
		private JDialog selectRDialog;
		private JLabel explainRLabel;
		JCheckBox[] ck = new JCheckBox[myReviewNum];

		@Override
		public void actionPerformed(ActionEvent e) {

			/**
			 * 작성 리뷰 관리 Dialog 닫기
			 */
			myreviewDialog.dispose();

			// 삭제 리뷰 선택 Dialog 레이아웃 설정
			selectRDialog = new JDialog();
			selectRDialog.setSize(600, 500);
			selectRDialog.setTitle("삭제 리뷰 선택");
			selectRDialog.setLayout(new BorderLayout());

			// 삭제 리뷰 선택 안내 Label
			explainRLabel = new JLabel("삭제할 리뷰를 선택하세요.", SwingConstants.CENTER);
			explainRLabel.setFont(new Font("고딕", Font.BOLD, 15));
			selectRDialog.add(explainRLabel, BorderLayout.NORTH);

			// 삭제할 리뷰 선택 체크박스 Panel
			reserveRPanel = new JPanel();
			RerrorC.num = 0; // 체크박스 확인용 변수 초기화

			/**
			 * 체크박스 생성
			 */
			for (int i = 0; i < myReviewNum; i++) {
				ck[i] = new JCheckBox("NO." + (i + 1) + " --- 리뷰 id : " + Review.get(i).getID() + ",  공연 제목 : "
						+ Review.get(i).getTitle() + ",  평점 : " + Review.get(i).getRate() + ",  리뷰 작성 날짜 : "
						+ Review.get(i).getTime(), false);
				ck[i].addItemListener(new deleRItem());
				reserveRPanel.add(ck[i]);
			}
			selectRDialog.add(reserveRPanel, BorderLayout.CENTER);

			// <리뷰 삭제 동의>, <CLOSE> 버튼 Panel
			selectRPanel = new JPanel(new GridLayout(1, 2, 1, 1));
			deleRBtn = new JButton("리뷰 삭제 동의");
			deleRBtn.addActionListener(new delRcheckListener());
			selectRPanel.add(deleRBtn);
			closeRBtn = new JButton("CLOSE");
			closeRBtn.addActionListener(new deleCDlgListener());
			selectRPanel.add(closeRBtn);

			selectRDialog.add(selectRPanel, BorderLayout.SOUTH);

			selectRDialog.setVisible(true);
		}

		/**
		 * 삭제 리뷰 선택 Dialog에서 CLOSE 버튼에 대한 리스너<br>
		 * 삭제 리뷰 선택 Dialog 닫기
		 */
		class deleCDlgListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectRDialog.dispose();
			}
		}

		/**
		 * 삭제 리뷰 선택 체크박스에 대한 리스너
		 */
		class deleRItem implements ItemListener {
			public void itemStateChanged(ItemEvent e) {

				/**
				 * review DELETE에 사용할 리뷰 id를 deleRId 배열에 저장
				 */
				deleRId = new int[myReviewNum];
				for (int i = 0; i < myReviewNum; i++) {
					if (ck[i].isSelected()) {
						deleRId[i] = Review.get(i).getID();
					} else {
						deleRId[i] = 0;
					}

					RerrorC.num = 0; // 체크박스 확인용 변수 초기화
					// 체크박스를 하나도 선택하지 않았을 경우 RerrorC.num == 0, 적어도 하나 선택한 경우 RerrorC.num != 0
					for (int j = 0; j < myReviewNum; j++) {
						RerrorC.num += deleRId[j];
					}
				}
			}

		}

		/**
		 * 삭제 리뷰 선택 Dialog에서 리뷰 삭제 동의 버튼에 대한 리스너<br>
		 * 삭제 리뷰 선택 Dialog 닫기<br>
		 * 선택된 리뷰를 DB에서 삭제<br>
		 * 리뷰 삭제 완료 Dialog 생성
		 */
		class delRcheckListener implements ActionListener {

			private JLabel XRLabel, msgRLabel;
			private JButton XRBtn, checkRBtn;
			private JDialog delRcheckDialog;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (RerrorC.num == 0) { // 체크박스를 하나도 선택하지 않았을 경우

					// 안내 Dialog 레이아웃 설정
					delRcheckDialog = new JDialog();
					delRcheckDialog.setSize(250, 120);
					delRcheckDialog.setLayout(new BorderLayout());

					XRLabel = new JLabel("하나 이상의 체크박스를 선택하세요.", SwingConstants.CENTER);
					delRcheckDialog.add(XRLabel, BorderLayout.CENTER);

					XRBtn = new JButton("OK");
					XRBtn.addActionListener(new deleBDlgListener());
					delRcheckDialog.add(XRBtn, BorderLayout.SOUTH);

					delRcheckDialog.setVisible(true);

					selectRDialog.dispose();

				} else { // 체크박스를 적어도 하나 선택한 경우

					// 삭제 완료 Dialog 레이아웃 설정
					delRcheckDialog = new JDialog();
					delRcheckDialog.setSize(120, 100);
					delRcheckDialog.setLayout(new BorderLayout());

					/**
					 * review DELETE
					 */
					for (int i = 0; i < myReviewNum; i++) {
						if (deleRId[i] != 0) { // 체크박스에서 선택한 리뷰만 삭제

							/**
							 * 데이터베이스 연결
							 */
							try (Connection conn = new ConnectionClass().getConnection();
									Statement preStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
											ResultSet.CONCUR_UPDATABLE)) {
								ResultSet res = preStmt
										.executeQuery("SELECT * FROM db2022_Review WHERE id = '" + deleRId[i] + "'");

								// 해당 리뷰 id의 리뷰가 존재하는지 확인
								if (res.next()) { // 해당 리뷰 id의 리뷰가 존재

									// 삭제 성공
									/**
									 * review DELETE 쿼리 실행
									 */
									preStmt.executeUpdate("DELETE FROM db2022_Review WHERE id = '" + deleRId[i] + "'");
									msgRLabel = new JLabel("리뷰 삭제 완료", SwingConstants.CENTER);
								} else { // 해당 리뷰 id의 리뷰가 존재하지 않음
									msgRLabel = new JLabel("ERROR", SwingConstants.CENTER);
								}
							} catch (SQLException sqle) {
								System.out.println(sqle);
							}
						}
					}

					delRcheckDialog.add(msgRLabel, BorderLayout.CENTER);

					checkRBtn = new JButton("OK");
					checkRBtn.addActionListener(new deleODlgListener());
					delRcheckDialog.add(checkRBtn, BorderLayout.SOUTH);

					delRcheckDialog.setVisible(true);

					selectRDialog.dispose();
				}
			}

			/**
			 * 체크박스 선택이 없음을 알리는 Dialog에서 OK 버튼에 대한 리스너<br>
			 * 체크박스 선택이 없음을 알리는 Dialog 닫기
			 */
			class deleBDlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delRcheckDialog.dispose();
				}
			}

			/**
			 * 리뷰 삭제 완료 dialog에서 OK 버튼에 대한 리스너<br>
			 * 리뷰 삭제 완료 dialog 닫기
			 */
			class deleODlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delRcheckDialog.dispose();
				}
			}
		}
	}

}
