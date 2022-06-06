package DB2022Team12;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

/**
 * 예매 티켓 조회 버튼에 대한 리스너<br>
 * 예매 티켓 정보 DB에서 검색<br>
 * 티켓 정보를 보여주는 예매 티켓 조회 dialog 생성
 * 
 * @author sonab
 * 
 */

class checkTicketListener implements ActionListener {

	private JLabel myInfoLabel, myTicketLabel;
	private JButton cancelTicketBtn, closeBtn;
	private JPanel BtnPanel;
	private JDialog checkTicketDialog;

	List<UserTicket> Ticket; // 사용자의 티켓 정보를 저장할 UserTicket 객체 리스트
	int myTicketNum = 0; // 사용자 티켓 개수
	int[] deleTId; // 삭제할 티켓 id 배열

	@Override
	public void actionPerformed(ActionEvent e) {
		// 예매 티켓 조회 Dialog 레이아웃 설정
		checkTicketDialog = new JDialog();
		checkTicketDialog.setSize(450, 250);
		checkTicketDialog.setTitle("예매 티켓 조회");
		checkTicketDialog.setLayout(new BorderLayout());

		// 사용자 이름 출력 Label
		myInfoLabel = new JLabel(User.getName() + "님의 TICKET", SwingConstants.CENTER);
		myInfoLabel.setFont(new Font("고딕", Font.BOLD, 20));
		checkTicketDialog.add(myInfoLabel, BorderLayout.NORTH);

		Ticket = new ArrayList<UserTicket>(); // 사용자의 티켓 정보를 저장할 UserTicket 객체 ArrayList
		myTicketNum = 0; // 사용자 티켓 개수 초기화

		/**
		 * 데이터베이스 연결
		 */
		try (Connection conn = new ConnectionClass().getConnection(); Statement stmt = conn.createStatement();) {

			/**
			 * ticket SELECT를 위한 쿼리 생성 및 UserTicket 객체 ArrayList를 이용해 정보 저장
			 */
			String TICKETSEARCH_QUERY = "SELECT id, musical_title, order_date FROM db2022_ticket WHERE member_id = '"
					+ User.getId() + "'";

			boolean Tresults = stmt.execute(TICKETSEARCH_QUERY);
			ResultSet res = stmt.getResultSet();
			int Terror = 0;

			do {
				if (Tresults) {
					while (res.next()) {
						Terror++;
						UserTicket ticket = new UserTicket();
						ticket.setID(res.getInt("id"));
						ticket.setTitle(res.getString("musical_title"));
						ticket.setOrderDate(res.getString("order_date"));
						Ticket.add(ticket);
					}
				}
				Tresults = stmt.getMoreResults();
			} while (Tresults);

			if (Terror != 0) { // 예매한 티켓이 있을 경우

				// 사용자 티켓 정보 제시 Label
				String Tstr = "";
				for (int i = 0; i < Ticket.size(); i++) {
					Tstr += "NO." + (i + 1) + " --- 티켓 id : " + Ticket.get(i).getID() + ",  공연 제목 : "
							+ Ticket.get(i).getTitle() + ",  티켓 예매 날짜 : " + Ticket.get(i).getOrderDate() + "<br />";
					myTicketNum++;
				}
				myTicketLabel = new JLabel("<html><body style='text-align:center;'>" + Tstr + "</body></html>",
						SwingConstants.CENTER);

				// <예매 취소>, <CLOSE> 버튼 Panel
				BtnPanel = new JPanel(new GridLayout(1, 2, 1, 1));
				cancelTicketBtn = new JButton("예매 취소");
				cancelTicketBtn.addActionListener(new cancelTicketBtnListener());
				closeBtn = new JButton("CLOSE");
				closeBtn.addActionListener(new delePDlgListener());
				BtnPanel.add(cancelTicketBtn);
				BtnPanel.add(closeBtn);

			} else { // 예매한 티켓이 없을 경우

				// 사용자 티켓 정보 제시 Label
				myTicketLabel = new JLabel("예매한 티켓이 없습니다.", SwingConstants.CENTER);

				// <CLOSE> 버튼 Panel
				BtnPanel = new JPanel();
				closeBtn = new JButton("CLOSE");
				closeBtn.addActionListener(new delePDlgListener());
				BtnPanel.add(closeBtn);
			}

			checkTicketDialog.add(myTicketLabel, BorderLayout.CENTER);
			checkTicketDialog.add(BtnPanel, BorderLayout.SOUTH);

		} catch (SQLException sqle) {
			System.out.println(sqle);
		}

		checkTicketDialog.setVisible(true);
	}

	/**
	 * 예매 티켓 조회 Dialog에서 CLOSE 버튼에 대한 리스너<br>
	 * 예매 티켓 조회 Dialog 닫기
	 */
	class delePDlgListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			checkTicketDialog.dispose();
		}
	}

	/**
	 * 체크박스를 하나도 체크하지 않을 경우를 확인하기 위한 클래스<br>
	 * 체크박스를 하나도 선택하지 않았을 경우 num == 0, 적어도 하나 선택한 경우 num != 0
	 */
	class TerrorC {
		static int num = 0;
	}

	/**
	 * 예매 티켓 조회 Dialog에서 예매 취소 버튼에 대한 리스너<br>
	 * 예매 티켓 조회 Dialog 닫기<br>
	 * 예매 취소 티켓 선택 Dialog 생성
	 */

	class cancelTicketBtnListener implements ActionListener {

		private JButton deleTBtn, closeTBtn;
		private JPanel reserveTPanel, selectTPanel;
		private JDialog deleTAgreeDialog;
		private JLabel explainTLabel;
		JCheckBox[] chk = new JCheckBox[myTicketNum];

		@Override
		public void actionPerformed(ActionEvent e) {

			/**
			 * 예매 티켓 조회 Dialog 닫기
			 */
			checkTicketDialog.dispose();

			// 예매 취소 티켓 선택 Dialog 레이아웃 설정
			deleTAgreeDialog = new JDialog();
			deleTAgreeDialog.setSize(450, 250);
			deleTAgreeDialog.setTitle("예매 취소 티켓 선택");
			deleTAgreeDialog.setLayout(new BorderLayout());

			// 예매 취소 티켓 선택 안내 Label
			explainTLabel = new JLabel("예매를 취소할 티켓을 선택하세요.", SwingConstants.CENTER);
			explainTLabel.setFont(new Font("고딕", Font.BOLD, 15));
			deleTAgreeDialog.add(explainTLabel, BorderLayout.NORTH);

			// 예매를 취소할 티켓 선택 체크박스 Panel
			reserveTPanel = new JPanel();
			TerrorC.num = 0; // 체크박스 확인용 변수 초기화

			/**
			 * 체크박스 생성
			 */
			for (int i = 0; i < myTicketNum; i++) {
				chk[i] = new JCheckBox("NO." + (i + 1) + " --- 티켓 id : " + Ticket.get(i).getID() + ",  공연 제목 : "
						+ Ticket.get(i).getTitle() + ",  티켓 예매 날짜 : " + Ticket.get(i).getOrderDate(), false);
				chk[i].addItemListener(new deleTItem());
				reserveTPanel.add(chk[i]);
			}
			deleTAgreeDialog.add(reserveTPanel, BorderLayout.CENTER);

			// <예매 취소 동의>, <CLOSE> 버튼 Panel
			selectTPanel = new JPanel(new GridLayout(1, 2, 1, 1));
			deleTBtn = new JButton("예매 취소 동의");
			deleTBtn.addActionListener(new delTcheckListener());
			selectTPanel.add(deleTBtn);
			closeTBtn = new JButton("CLOSE");
			closeTBtn.addActionListener(new deleSDlgListener());
			selectTPanel.add(closeTBtn);

			deleTAgreeDialog.add(selectTPanel, BorderLayout.SOUTH);

			deleTAgreeDialog.setVisible(true);
		}

		/**
		 * 예매 취소 티켓 선택 Dialog에서 CLOSE 버튼에 대한 리스너<br>
		 * 예매 취소 티켓 선택 Dialog 닫기
		 */

		class deleSDlgListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleTAgreeDialog.dispose();
			}
		}

		/**
		 * 예매 취소 티켓 선택 체크박스에 대한 리스너
		 * 
		 * @author sonab
		 *
		 */
		class deleTItem implements ItemListener {
			public void itemStateChanged(ItemEvent e) {

				/**
				 * ticket DELETE에 사용할 티켓 id를 deleTId 배열에 저장
				 */
				deleTId = new int[myTicketNum];
				for (int i = 0; i < myTicketNum; i++) {
					if (chk[i].isSelected()) {
						deleTId[i] = Ticket.get(i).getID();
					} else {
						deleTId[i] = 0;
					}

					TerrorC.num = 0; // 체크박스 확인용 변수 초기화
					// 체크박스를 하나도 선택하지 않았을 경우 TerrorC.num == 0, 적어도 하나 선택한 경우 TerrorC.num != 0
					for (int j = 0; j < myTicketNum; j++) {
						TerrorC.num += deleTId[j];
					}
				}

			}
		}

		/**
		 * 예매 취소 티켓 선택 Dialog에서 예매 취소 동의 버튼에 대한 리스너<br>
		 * 예매 취소 티켓 선택 Dialog 닫기<br>
		 * 선택된 예매 티켓을 DB에서 삭제<br>
		 * 예매 취소 완료 Dialog 생성
		 */

		class delTcheckListener implements ActionListener {

			private JLabel XTLabel, msgTLabel;
			private JButton XTBtn, checkTBtn;
			private JDialog delTcheckDialog;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (TerrorC.num == 0) { // 체크박스를 하나도 선택하지 않았을 경우

					// 안내 Dialog 레이아웃 설정
					delTcheckDialog = new JDialog();
					delTcheckDialog.setSize(250, 120);
					delTcheckDialog.setLayout(new BorderLayout());

					XTLabel = new JLabel("하나 이상의 체크박스를 선택하세요.", SwingConstants.CENTER);
					delTcheckDialog.add(XTLabel, BorderLayout.CENTER);

					XTBtn = new JButton("OK");
					XTBtn.addActionListener(new deleEDlgListener());
					delTcheckDialog.add(XTBtn, BorderLayout.SOUTH);

					delTcheckDialog.setVisible(true);

					deleTAgreeDialog.dispose();

				} else { // 체크박스를 적어도 하나 선택한 경우

					// 삭제 완료 Dialog 레이아웃 설정
					delTcheckDialog = new JDialog();
					delTcheckDialog.setSize(120, 100);
					delTcheckDialog.setLayout(new BorderLayout());

					/**
					 * ticket DELETE
					 */
					for (int i = 0; i < myTicketNum; i++) {
						if (deleTId[i] != 0) { // 체크박스에서 선택한 티켓만 삭제

							/**
							 * 데이터베이스 연결
							 */
							try (Connection conn = new ConnectionClass().getConnection();
									Statement preStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
											ResultSet.CONCUR_UPDATABLE)) {
								ResultSet res = preStmt
										.executeQuery("SELECT * FROM db2022_ticket WHERE id = '" + deleTId[i] + "'");

								// 해당 티켓 id의 티켓이 존재하는지 확인
								if (res.next()) { // 해당 티켓 id의 티켓이 존재
									// 삭제 성공
									/**
									 * ticket DELETE 쿼리 실행
									 */
									preStmt.executeUpdate("DELETE FROM db2022_ticket WHERE id = '" + deleTId[i] + "'");
									msgTLabel = new JLabel("예매 취소 완료", SwingConstants.CENTER);
								} else { // 해당 티켓 id의 티켓이 존재하지 않음
									msgTLabel = new JLabel("ERROR", SwingConstants.CENTER);
								}
							} catch (SQLException sqle) {
								System.out.println(sqle);
							}
						}
					}

					delTcheckDialog.add(msgTLabel, BorderLayout.CENTER);

					checkTBtn = new JButton("OK");
					checkTBtn.addActionListener(new deleTDlgListener());
					delTcheckDialog.add(checkTBtn, BorderLayout.SOUTH);

					delTcheckDialog.setVisible(true);

					deleTAgreeDialog.dispose();
				}

			}

			/**
			 * 체크박스 선택이 없음을 알리는 Dialog에서 OK 버튼에 대한 리스너<br>
			 * 체크박스 선택이 없음을 알리는 Dialog 닫기
			 */
			class deleEDlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delTcheckDialog.dispose();
				}
			}

			/**
			 * 예매 취소 완료 dialog에서 OK 버튼에 대한 리스너<br>
			 * 예매 취소 완료 dialog 닫기
			 */

			class deleTDlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delTcheckDialog.dispose();
				}
			}

		}

	}
}
