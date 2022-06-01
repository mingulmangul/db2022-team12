import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// 로그인 및 회원가입
public class AuthenticationPanel extends JPanel {

	private JDialog signUpDialog;
	private JPanel authPanel;
	private JButton signInBtn, signUpBtn, submitBtn;
	private JTextField idField, nameField, phoneField, emailField, addressField;
	private JPasswordField pwField;
	private JLabel noticeLabel, idLabel, pwLabel, nameLabel, phoneLabel, emailLabel, addressLabel;

	// 유저 정보 검색 쿼리
	private static String SIGNIN_QUERY = "SELECT pw FROM member WHERE id = ?";
	
	// 유저 정보 삽입 쿼리
	// id, pw, name, phone, email, address
	private static String SIGNUP_QUERY = "INSERT INTO member VALUES (?, ?, ?, ?, ?, ?)";

	public AuthenticationPanel() {
		authPanel = new JPanel();
		JPanel signInPanel = new JPanel(new GridLayout(2, 1));
		JPanel signInTextFieldPanel = new JPanel(new GridLayout(2, 3));

		noticeLabel = new JLabel();
		signInPanel.add(noticeLabel);
		
		idLabel = new JLabel("ID: ");
		idField = new JTextField();
		pwLabel = new JLabel("Password: ");
		pwField = new JPasswordField();

		signInTextFieldPanel.add(idLabel);
		signInTextFieldPanel.add(idField);
		signInTextFieldPanel.add(pwLabel);
		signInTextFieldPanel.add(pwField);
		signInPanel.add(signInTextFieldPanel);
		
		signInBtn = new JButton("로그인");
		signInBtn.addActionListener(new signInBtnListener());
		signUpBtn = new JButton("회원가입");
		signUpBtn.addActionListener(new signUpBtnListener());

		authPanel.add(signInPanel);
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
						// 비밀번호 검증
						String stored_pw = res.getString("pw");
						if (!pw.equals(stored_pw)) {
							noticeLabel.setText("잘못된 비밀번호입니다");
						} else {
							// 로그인 성공
							authPanel.setVisible(false);
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

}