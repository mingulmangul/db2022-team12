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


//<예매 티켓 조회> 버튼에 대한 리스너
//예매 티켓 정보 DB에서 검색, 티켓 정보를 보여주는 dialog 생성
class checkTicketListener implements ActionListener {
	
	private JLabel myInfoLabel, myTicketLabel;
	private JButton cancelTicketBtn, closeBtn;
	private JPanel BtnPanel;
	private JDialog checkTicketDialog;
	
	List<UserTicket> Ticket; // 유저의 티켓 정보를 저장할 UserTicket 객체 리스트
	int myTicketNum = 0; // 유저의 티켓 개수
	int[] deleTId; // 삭제할 티켓 id 배열
	
	@Override
	public void actionPerformed(ActionEvent e) {
		checkTicketDialog = new JDialog();
		checkTicketDialog.setSize(200, 300);
		checkTicketDialog.setTitle("예매 티켓 조회");
		checkTicketDialog.setLayout(new GridLayout(3, 1, 10, 1));
		
		myInfoLabel = new JLabel(User.getName() + "님의 TICKET", SwingConstants.CENTER);
		myInfoLabel.setFont(new Font("고딕", Font.BOLD, 20));
		checkTicketDialog.add(myInfoLabel);
		
		Ticket = new ArrayList<UserTicket>();
		myTicketNum = 0;
		
		try (Connection conn = new ConnectionClass().getConnection();
				Statement stmt = conn.createStatement();) {
			
			String TICKETSEARCH_QUERY = "SELECT id, musical_title, order_date FROM db2022_ticket WHERE member_id = '" + User.getId() + "'";

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

			if(Terror != 0) {
				
		        String Tstr = "";
		        for(int i = 0; i < Ticket.size(); i++) {
		        	Tstr += "NO." + (i+1) + " --- 티켓 id : " + Ticket.get(i).getID() + ",  공연 제목 : " + Ticket.get(i).getTitle() + ",  티켓 예매 날짜 : " + Ticket.get(i).getOrderDate() + "<br />";
		        	myTicketNum++;
		        }
				myTicketLabel = new JLabel("<html><body style='text-align:center;'>" + Tstr + "</body></html>");
	
				BtnPanel = new JPanel(new GridLayout(1, 2, 1, 10));
				cancelTicketBtn = new JButton("예매 취소");
				cancelTicketBtn.addActionListener(new cancelTicketBtnListener());
				closeBtn = new JButton("CLOSE");
				closeBtn.addActionListener(new delePDlgListener());
				BtnPanel.add(cancelTicketBtn);
				BtnPanel.add(closeBtn);
			}else {
				myTicketLabel = new JLabel("예매한 티켓이 없습니다.");

				BtnPanel = new JPanel();
				closeBtn = new JButton("CLOSE");
				closeBtn.addActionListener(new delePDlgListener());
				BtnPanel.add(closeBtn);
			}
			
			checkTicketDialog.add(myTicketLabel);
			checkTicketDialog.add(BtnPanel);
	
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
		
		checkTicketDialog.setVisible(true);
		
	}
	
	// 예매 티켓 조회 dialog에서 <CLOSE> 버튼에 대한 리스너
	// 예매 티켓 조회 dialog 닫기
	class delePDlgListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			checkTicketDialog.dispose();
		}
	}
	
	// 체크박스를 하나도 체크하지 않을 경우를 확인하기 위한 클래스
	class TerrorC {
		static int num = 0;
	}
	
	// 예매 티켓 조회 dialog에서 <예매 취소> 버튼에 대한 리스너
	// 예매한 티켓 정보와 예매 취소 동의 dialog 생성
	class cancelTicketBtnListener implements ActionListener {
		private JButton deleTBtn, closeTBtn;
		private JPanel reserveTPanel, selectTPanel;
		private JDialog deleTAgreeDialog;
		private JLabel explainTLabel;
		JCheckBox[] chk = new JCheckBox[myTicketNum];
				
		@Override
		public void actionPerformed(ActionEvent e) {
			
			checkTicketDialog.dispose();
			
			deleTAgreeDialog = new JDialog();
			deleTAgreeDialog.setSize(200, 150);
			deleTAgreeDialog.setTitle("예매 취소 티켓 선택");
			deleTAgreeDialog.setLayout(new GridLayout(3, 1, 10, 5));
				
			explainTLabel = new JLabel("예매를 취소할 티켓을 선택하세요.");
			deleTAgreeDialog.add(explainTLabel);
				
			reserveTPanel = new JPanel();
			
			TerrorC.num = 0;
				
			for (int i = 0; i < myTicketNum; i++){
				chk[i] = new JCheckBox("NO." + (i+1) + " --- 티켓 id : " + Ticket.get(i).getID() + ",  공연 제목 : " + Ticket.get(i).getTitle() + ",  티켓 예매 날짜 : " + Ticket.get(i).getOrderDate(),false);
				chk[i].addItemListener(new deleTItem());
				reserveTPanel.add(chk[i]);
			}				
			deleTAgreeDialog.add(reserveTPanel);
				
			selectTPanel = new JPanel(new GridLayout(1, 2, 1, 5));
			deleTBtn = new JButton("예매 취소 동의");
			deleTBtn.addActionListener(new delTcheckListener());
		
			
			selectTPanel.add(deleTBtn);
			
			closeTBtn = new JButton("CLOSE");
			closeTBtn.addActionListener(new deleSDlgListener());
			selectTPanel.add(closeTBtn);
				
			deleTAgreeDialog.add(selectTPanel);
				
			deleTAgreeDialog.setVisible(true);
		}		
		
		// 예매 취소 티켓 선택 dialog에서 <CLOSE> 버튼에 대한 리스너
		// 예매 취소 티켓 선택 dialog 닫기
		class deleSDlgListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleTAgreeDialog.dispose();
			}
		}
		
		// 예매 취소 티켓 선택 체크박스에 대한 리스너
		class deleTItem implements ItemListener {
			public void itemStateChanged(ItemEvent e) {
				deleTId = new int[myTicketNum];
				
				for (int i = 0; i < myTicketNum; i++){
					if(chk[i].isSelected()) {
						deleTId[i] = Ticket.get(i).getID();
					}else {
						deleTId[i] = 0;
					}
					TerrorC.num = 0;
					for (int j = 0; j < myTicketNum; j++){
						TerrorC.num += deleTId[j];
					}
				}
				
			}			
		}	
		
		// <예매 취소 동의> 버튼에 대한 리스너
		// 선택된 예매 티켓 정보를 DB에서 삭제, 예매 취소 완료 dialog 생성
		class delTcheckListener implements ActionListener {
			private JLabel XTLabel, msgTLabel;
			private JButton XTBtn, checkTBtn;	
			private JDialog delTcheckDialog;
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(TerrorC.num == 0) {
					delTcheckDialog = new JDialog();
					delTcheckDialog.setSize(120, 100);
					delTcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
					
					XTLabel = new JLabel("하나 이상의 체크박스를 선택하세요.");
					delTcheckDialog.add(XTLabel);
					
					XTBtn = new JButton("OK");
					XTBtn.addActionListener(new deleEDlgListener());
					delTcheckDialog.add(XTBtn);
					
					delTcheckDialog.setVisible(true);
					
					deleTAgreeDialog.dispose();
					
				}else {
					delTcheckDialog = new JDialog();
					delTcheckDialog.setSize(120, 100);
					delTcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
					
					for (int i = 0; i < myTicketNum; i++){
						if(deleTId[i] != 0) {
							try (	Connection conn = new ConnectionClass().getConnection();
									Statement preStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
								ResultSet res = preStmt.executeQuery("SELECT * FROM db2022_ticket WHERE id = '" + deleTId[i] + "'");
								
								// 해당 티켓 id의 티켓이 존재하는지 확인
								if (res.next()) {
									preStmt.executeUpdate("DELETE FROM db2022_ticket WHERE id = '" + deleTId[i] + "'");
									msgTLabel = new JLabel("예매 취소 완료", SwingConstants.CENTER);
								} else {
									msgTLabel = new JLabel("ERROR", SwingConstants.CENTER);
								}	
							}catch (SQLException sqle) {
								System.out.println(sqle);
							}
						}
					}
					delTcheckDialog.add(msgTLabel);
						
					checkTBtn = new JButton("OK");
					checkTBtn.addActionListener(new deleTDlgListener());
					delTcheckDialog.add(checkTBtn);
					
					delTcheckDialog.setVisible(true);
					
					deleTAgreeDialog.dispose();
				}
				
			}
			
			// 체크박스 선택이 없음을 알리는 dialog에서 <OK> 버튼에 대한 리스너
			// 체크박스 선택이 없음을 알리는 dialog 닫기
			class deleEDlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delTcheckDialog.dispose();
				}
			}
			
			// 티켓 예매 취소 완료 dialog에서 <OK> 버튼에 대한 리스너
			// 예매 취소 동의 dialog 닫기
			class deleTDlgListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent e) {
					delTcheckDialog.dispose();
				}
			}			
			
		}

	}
}


