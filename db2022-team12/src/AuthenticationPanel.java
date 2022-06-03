import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font; //추가
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants; //추가

//로그인 및 회원가입
public class AuthenticationPanel extends JPanel {

	private JDialog signUpDialog, signInDialog, checkTicketDialog, deleTAgreeDialog, myreviewDialog, delTcheckDialog, delRcheckDialog, deleErrorDialog;
	private JPanel authPanel, noticePanel, signInTextFieldPanel, personalPanel, MypagePanel, emptyPanel;
	private JButton signInBtn, signUpBtn, submitBtn, checkTicketBtn, reviewBtn;
	private JTextField idField, nameField, phoneField, emailField, addressField;
	private JPasswordField pwField;
	private JLabel noticeLabel, idLabel, pwLabel, nameLabel, phoneLabel, emailLabel, addressLabel, infoLabel;

	
	// 유저 정보 검색 쿼리
	private static String SIGNIN_QUERY = "SELECT pw, Member_name FROM member WHERE id = ?";
	
	// 유저 정보 삽입 쿼리
		// id, pw, name, phone, email, address
		private static String SIGNUP_QUERY = "INSERT INTO member VALUES (?, ?, ?, ?, ?, ?)";
	
	// 티켓 정보 검색 쿼리
		private static String TICKETSEARCH_QUERY = "SELECT Musical_id, Musical_date, Musical_time, Seat_row, Seat_col, Theater_name, Order_date FROM Ticket WHERE Member_id = ?";
	
	// 티켓 정보 삭제 쿼리
		//private static String DELETETICKET_QUERY = "DELETE FROM Ticket WHERE Member_id = ?";
	
	// 리뷰 정보 검색 쿼리
		private static String REVIEWSEARCH_QUERY = "SELECT Musical_id, Rate, Written_at FROM Review WHERE Member_id = ?";
				
	// 리뷰 정보 삭제 쿼리
		private static String DELETEREVIEW_QUERY = "DELETE FROM Review WHERE Member_id = ?";
	
	
	

	public AuthenticationPanel() {
		this.setLayout(new GridLayout(2, 1));
		
		authPanel = new JPanel();
		noticePanel = new JPanel();
		signInTextFieldPanel = new JPanel(new GridLayout(2, 3));

		noticeLabel = new JLabel();
		noticePanel.add(noticeLabel);
		this.add(noticePanel);

		idLabel = new JLabel("ID: ");
		idField = new JTextField();
		pwLabel = new JLabel("Password: ");
		pwField = new JPasswordField();

		signInTextFieldPanel.add(idLabel);
		signInTextFieldPanel.add(idField);
		signInTextFieldPanel.add(pwLabel);
		signInTextFieldPanel.add(pwField);

		signInBtn = new JButton("로그인");
		signInBtn.addActionListener(new signInBtnListener()); //(new signIpBtnListener()); 
		signUpBtn = new JButton("회원가입");
		signUpBtn.addActionListener(new signUpBtnListener());

		authPanel.add(signInTextFieldPanel);
		authPanel.add(signInBtn);
		authPanel.add(signUpBtn);
		this.add(authPanel);
	}

		// <로그인> 버튼에 대한 리스너
		// DB에 사용자로부터 받은 입력과 일치하는 데이터가 존재하는지 확인 후 로그인
	private class signInBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String id = idField.getText().strip();
			String pw = String.valueOf(pwField.getPassword()).strip();

			// 사용자 입력 유효성 검증
			if (id.isEmpty()) {
				noticeLabel.setText("아이디를 입력해주세요");
			} else if (pw.isEmpty()) {
				noticeLabel.setText("비밀번호를 입력해주세요");
			} else {

				// 모든 필드가 유효하다면 데이터베이스 연결
				try (Connection conn = new ConnectionClass().getConnection();
						PreparedStatement preStmt = conn.prepareStatement(SIGNIN_QUERY);) {
					// 유저 정보 검색을 위한 쿼리
					preStmt.setString(1, id);
					ResultSet res = preStmt.executeQuery();

					// 해당 아이디의 유저가 존재하는지 확인
					if (!res.next()) {
						noticeLabel.setText("존재하지 않는 아이디입니다");
					} else {
						// 유저 정보 저장
						String user_pw = res.getString("pw");
						if (!pw.equals(user_pw)) {
							noticeLabel.setText("잘못된 비밀번호입니다");
						} else {
							// 로그인 성공
							User.ID = id;
							User.NAME = res.getString("Member_name");

							// 로그인 및 회원가입 패널 숨기기
							authPanel.setVisible(false);
							noticeLabel.setText(User.NAME + "님 안녕하세요 :)");
												
							
							// <로그인> 버튼에 대한 리스너
							// 마이페이지 예매 티켓 조회, 예매 취소, 작성 리뷰 관리 버튼 선택 dialog 생성
							signInDialog = new JDialog();
							signInDialog.setSize(300, 350);
							signInDialog.setTitle("마이페이지");
							signInDialog.setLayout(new GridLayout(3, 1, 20, 5));
							
							emptyPanel = new JPanel();
							signInDialog.add(emptyPanel);
							
							personalPanel = new JPanel(new GridLayout(2, 1, 10, 10));
							MypagePanel = new JPanel(new GridLayout(2, 1, 10, 8));
							
							infoLabel = new JLabel(User.NAME + "님", SwingConstants.CENTER);
							infoLabel.setFont(new Font("고딕", Font.BOLD, 30));
							
							personalPanel.add(infoLabel);
							signInDialog.add(personalPanel);
							
							checkTicketBtn = new JButton("예매 티켓 조회");
							checkTicketBtn.addActionListener(new checkTicketListener());
							reviewBtn = new JButton("작성 리뷰 관리");
							reviewBtn.addActionListener(new reviewBtnListener());
							MypagePanel.add(checkTicketBtn);
							MypagePanel.add(reviewBtn);
							signInDialog.add(MypagePanel);
								
							signInDialog.setVisible(true);
						}
					}
				} catch (SQLException sqle) {
					System.out.println(sqle);
				}
			}
		}

	}

	// <회원가입> 버튼에 대한 리스너
	// 회원가입 Dialog 창 생성
	private class signUpBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			signUpDialog = new JDialog();
			signUpDialog.setSize(300, 400);
			signUpDialog.setLayout(new GridLayout(3, 1));

			JPanel topPanel = new JPanel();
			JPanel midPanel = new JPanel(new GridLayout(6, 2));
			JPanel botPanel = new JPanel();

			noticeLabel = new JLabel();
			topPanel.add(noticeLabel);

			idLabel = new JLabel("ID: ");
			idField = new JTextField();
			pwLabel = new JLabel("Password: ");
			pwField = new JPasswordField();
			nameLabel = new JLabel("이름: ");
			nameField = new JTextField("홍길동");
			phoneLabel = new JLabel("전화번호: ");
			phoneField = new JTextField("010-0000-0000");
			emailLabel = new JLabel("이메일: ");
			emailField = new JTextField("example@example.com");
			addressLabel = new JLabel("주소: ");
			addressField = new JTextField("서울시 서대문구 이화여대길");
			midPanel.add(idLabel);
			midPanel.add(idField);
			midPanel.add(pwLabel);
			midPanel.add(pwField);
			midPanel.add(nameLabel);
			midPanel.add(nameField);
			midPanel.add(phoneLabel);
			midPanel.add(phoneField);
			midPanel.add(emailLabel);
			midPanel.add(emailField);
			midPanel.add(addressLabel);
			midPanel.add(addressField);

			submitBtn = new JButton("가입하기");
			submitBtn.addActionListener(new submitBtnListener());
			botPanel.add(submitBtn);

			signUpDialog.add(topPanel);
			signUpDialog.add(midPanel);
			signUpDialog.add(botPanel);
			signUpDialog.setVisible(true);
		}

	}

	// <가입하기> 버튼 리스너
	// 사용자로부터 받은 입력으로 쿼리 생성 후 DB에 삽입
	private class submitBtnListener implements ActionListener {

		// 유효한 전화번호인지 검증
		private boolean isValidPhone(String phone) {
			String phonePattern = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
			return Pattern.matches(phonePattern, phone);
		}

		// 유효한 이메일 주소인지 검증
		private boolean isValidEmail(String email) {
			String emailPattern = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$";
			return Pattern.matches(emailPattern, email);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String id = idField.getText().strip();
			String pw = String.valueOf(pwField.getPassword()).strip();
			String name = nameField.getText().strip();
			String phone = phoneField.getText().strip();
			String email = emailField.getText().strip();
			String address = addressField.getText().strip();

			// 사용자가 입력한 각 필드의 내용에 대한 유효성 검증
			if (id.isEmpty()) {
				noticeLabel.setText("아이디를 입력해주세요");
			} else if (pw.isEmpty()) {
				noticeLabel.setText("비밀번호를 입력해주세요");
			} else if (name.isEmpty()) {
				noticeLabel.setText("이름을 입력해주세요");
			} else if (!this.isValidPhone(phone)) {
				noticeLabel.setText("잘못된 전화번호입니다");
			} else if (!this.isValidEmail(email)) {
				noticeLabel.setText("잘못된 이메일 주소입니다");
			} else {

				// 모든 필드가 유효하다면 데이터베이스 연결
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

				// 회원가입 dialog 닫기
				signUpDialog.dispose();
			}
		}

	}
	
	// <예매 티켓 조회> 버튼에 대한 리스너
	// 예매 티켓 정보 DB에서 검색, 티켓 정보를 보여주는 dialog 생성
	private class checkTicketListener implements ActionListener {
		private JLabel myInfoLabel, myTicketLabel1, myTicketLabel2, myTicketLabel3, myTicketLabel4, myTicketLabel5, myTicketLabel6;
		private JButton cancelTicketBtn, closeBtn;
		private JPanel BtnPanel;
			
		@Override
		public void actionPerformed(ActionEvent e) {
			checkTicketDialog = new JDialog();
			checkTicketDialog.setSize(300, 600);
			checkTicketDialog.setLayout(new GridLayout(8, 1, 10, 1));
				
			try (Connection conn = new ConnectionClass().getConnection();
					PreparedStatement preStmt = conn.prepareStatement(TICKETSEARCH_QUERY)) {
					
				// 유저 티켓 검색을 위한 쿼리
				preStmt.setString(1, User.ID);
				ResultSet res = preStmt.executeQuery();
					
				// 해당 아이디의 티켓이 존재하는지 확인
				if (!res.next()) {
					noticeLabel.setText("예매한 티켓이 없습니다.");
				} else {
					// 유저 정보 저장
					UserTicket.musical_id = res.getInt("Musical_id");
					UserTicket.Mdate = res.getString("Musical_date");
					UserTicket.Mtime = res.getString("Musical_time");
					UserTicket.row = res.getInt("Seat_row");
					UserTicket.col = res.getInt("Seat_col");
					UserTicket.theater = res.getString("Theater_name");
					UserTicket.Odate = res.getString("Order_date"); 
				}
			} catch (SQLException sqle) {
				System.out.println(sqle);
			}
			
			 switch(UserTicket.musical_id){
		        case 1 : 
		        	UserTicket.musical_name = "킹키부츠"; 
		            break;
		        case 2 : 
		        	UserTicket.musical_name = "킹키부츠"; 
		            break;  
		        case 3 : 
		        	UserTicket.musical_name = "비더슈탄트"; 
		            break;
		        case 4 : 
		        	UserTicket.musical_name = "비더슈탄트"; 
		            break;
		        case 5 : 
		        	UserTicket.musical_name = "데스노트"; 
		            break;
		        case 6 : 
		        	UserTicket.musical_name = "데스노트"; 
		            break;
		        case 7 : 
		        	UserTicket.musical_name = "아이다"; 
		            break;
		        case 8 : 
		        	UserTicket.musical_name = "아이다"; 
		            break;
		        case 9 : 
		        	UserTicket.musical_name = "마타하리"; 
		            break;
		        case 10 : 
		        	UserTicket.musical_name = "마타하리"; 
		            break;
		        default :    
		        	UserTicket.musical_name = "---";
		    }
			
			myInfoLabel = new JLabel(User.NAME + "님의 TICKET", SwingConstants.CENTER);
			myInfoLabel.setFont(new Font("고딕", Font.BOLD, 20));
			checkTicketDialog.add(myInfoLabel);
			
			myTicketLabel1 = new JLabel("제목 : " + UserTicket.musical_name);
			myTicketLabel2 = new JLabel("공연일자 : " + UserTicket.Mdate);
			myTicketLabel3 = new JLabel("공연 시작 시간 : " + UserTicket.Mtime);
			myTicketLabel4 = new JLabel("좌석 : " + UserTicket.row + "행 " + UserTicket.col + "열");
			myTicketLabel5 = new JLabel("장소 : " + UserTicket.theater);
			myTicketLabel6 = new JLabel("예매 일자 : " + UserTicket.Odate);
			
			checkTicketDialog.add(myTicketLabel1);
			checkTicketDialog.add(myTicketLabel2);
			checkTicketDialog.add(myTicketLabel3);
			checkTicketDialog.add(myTicketLabel4);
			checkTicketDialog.add(myTicketLabel5);
			checkTicketDialog.add(myTicketLabel6);
			
			BtnPanel = new JPanel(new GridLayout(1, 2, 1, 10));
			cancelTicketBtn = new JButton("예매 취소");
			cancelTicketBtn.addActionListener(new cancelTicketBtnListener());
			closeBtn = new JButton("CLOSE");
			closeBtn.addActionListener(new delePDlgListener());
			BtnPanel.add(cancelTicketBtn);
			BtnPanel.add(closeBtn);
			
			checkTicketDialog.add(BtnPanel);
			
			checkTicketDialog.setVisible(true);
		}
	}

	// 예매 티켓 조회 dialog에서 <CLOSE> 버튼에 대한 리스너
	// 예매 티켓 조회 dialog 닫기
	private class delePDlgListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			checkTicketDialog.dispose();
		}
	}
	
	
	// <예매 취소> 버튼에 대한 리스너
	// 예매한 티켓 정보와 예매 취소 동의 dialog 생성
	private class cancelTicketBtnListener implements ActionListener {
		private JLabel errorMsgLabel, deleTicketInfoLabel1, deleTicketInfoLabel2, deleTicketInfoLabel3, deleTicketInfoLabel4;
		private JButton deleTBtn;
		private JPanel reserveTicketPanel;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(UserTicket.musical_name == "---") {
				deleErrorDialog = new JDialog();
				deleErrorDialog.setSize(300, 50);
				
				errorMsgLabel = new JLabel("취소할 티켓이 없습니다.");
				deleErrorDialog.add(errorMsgLabel);
				deleErrorDialog.setVisible(true);
				
			}else {
				deleTAgreeDialog = new JDialog();
				deleTAgreeDialog.setSize(300, 200);
				deleTAgreeDialog.setLayout(new GridLayout(2, 1, 10, 30));
				
				reserveTicketPanel = new JPanel(new GridLayout(4, 1, 10, 1));
				
				deleTicketInfoLabel1 = new JLabel("제목 : " + UserTicket.musical_name);
				deleTicketInfoLabel2 = new JLabel("공연일자 : " + UserTicket.Mdate);
				deleTicketInfoLabel3 = new JLabel("공연 시작 시간 : " + UserTicket.Mtime);
				deleTicketInfoLabel4 = new JLabel("좌석 : " + UserTicket.row + "행 " + UserTicket.col + "열");
				reserveTicketPanel.add(deleTicketInfoLabel1);
				reserveTicketPanel.add(deleTicketInfoLabel2);
				reserveTicketPanel.add(deleTicketInfoLabel3);
				reserveTicketPanel.add(deleTicketInfoLabel4);
				
				deleTAgreeDialog.add(reserveTicketPanel);
				
				deleTBtn = new JButton("예매 취소 동의");
				deleTBtn.addActionListener(new delTcheckListener());
				deleTAgreeDialog.add(deleTBtn);
				
				deleTAgreeDialog.setVisible(true);
			}
		
		}

	}
	
	// <예매 취소 동의> 버튼에 대한 리스너
	// 예매 티켓 정보 DB에서 삭제, 예매 취소 완료 dialog 생성
	private class delTcheckListener implements ActionListener {
		private JLabel msgTLabel;
		private JButton checkTBtn;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			delTcheckDialog = new JDialog();
			delTcheckDialog.setSize(120, 100);
			delTcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
			
			try (Connection conn = new ConnectionClass().getConnection();
					Statement Stmt = conn.createStatement(); ){
				Stmt.executeUpdate("DELETE FROM Ticket WHERE Member_id = " + User.ID);
				
			} catch (SQLException sqle) {
				System.out.println(sqle);
			}
			msgTLabel = new JLabel("예매 취소 완료", SwingConstants.CENTER);
			delTcheckDialog.add(msgTLabel);
			
			checkTBtn = new JButton("OK");
			checkTBtn.addActionListener(new deleTDlgListener());
			delTcheckDialog.add(checkTBtn);
			
			delTcheckDialog.setVisible(true);
			
			//예매 취소 dialog 닫기
			deleTAgreeDialog.dispose();
		}
	}
	
		// 리뷰 삭제 완료 dialog에서 <OK> 버튼에 대한 리스너
		// 리뷰 삭제 완료 dialog 닫기
		private class deleTDlgListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				delTcheckDialog.dispose();
			}
		}
		
		
		// <작성 리뷰 관리> 버튼에 대한 리스너
		// 작성 리뷰 정보와 전체 삭제 동의 dialog 생성
		private class reviewBtnListener implements ActionListener {
			private JLabel myreviewLabel;
			private JButton deleRBtn;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				myreviewDialog = new JDialog();
				myreviewDialog.setSize(300, 100);
				myreviewDialog.setLayout(new GridLayout(2, 1, 10, 20));
				
				myreviewLabel = new JLabel(); //리뷰 정보 불러올 예정
				myreviewDialog.add(myreviewLabel);
				
				deleRBtn = new JButton("작성한 리뷰 전체 삭제");
				deleRBtn.addActionListener(new delRcheckListener());
				myreviewDialog.add(deleRBtn);
				
				myreviewDialog.setVisible(true);
			}

		}
	
	
	// <작성한 리뷰 전체 삭제> 버튼에 대한 리스너
		// 작성한 리뷰 DB에서 삭제, 삭제 완료 dialog 생성
		private class delRcheckListener implements ActionListener {
			private JLabel msgRLabel;
			private JButton checkRBtn;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				delRcheckDialog = new JDialog();
				delRcheckDialog.setSize(250, 100);
				delRcheckDialog.setLayout(new GridLayout(2, 1, 10, 10));
				
				msgRLabel = new JLabel("작성한 리뷰가 모두 삭제되었습니다.", SwingConstants.CENTER);
				delRcheckDialog.add(msgRLabel);
				
				checkRBtn = new JButton("OK");
				checkRBtn.addActionListener(new deleRDlgListener());
				delRcheckDialog.add(checkRBtn);
				
				delRcheckDialog.setVisible(true);
				
				//작성 리뷰 관리 dialog 닫기
				myreviewDialog.dispose();
			}
		}
		
		// 리뷰 삭제 완료 dialog에서 <OK> 버튼에 대한 리스너
		// 리뷰 삭제 완료 dialog 닫기
		private class deleRDlgListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				delRcheckDialog.dispose();
			}
		}
}
