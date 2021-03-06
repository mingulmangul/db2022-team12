package DB2022Team12;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

/**
 * 로그인과 회원가입 기능을 수행하는 패널
 * 
 * @author mingulmangul
 */
class AuthenticationPanel extends JPanel {

	private JDialog signUpDialog;
	private JPanel authPanel, userPanel, signInTextFieldPanel, noticePanel;
	private JButton signInBtn, signUpBtn, submitBtn, MypageBtn;
	private JTextField idField, nameField, phoneField, emailField, addressField;
	private JPasswordField pwField;
	private JLabel noticeLabel, idLabel, pwLabel, nameLabel, phoneLabel1, phoneLabel2, emailLabel, addressLabel;

	/** 유저 정보 검색 쿼리 */
	private final String SIGNIN_QUERY = "SELECT pw, name FROM db2022_member WHERE id = ?";

	/** 유저 정보 삽입 쿼리 */
	private final String SIGNUP_QUERY = "INSERT INTO db2022_member VALUES (?, ?, ?, ?, ?, ?)";

	/**
	 * 로그인 &amp; 회원가입 패널 생성자<br>
	 * 패널의 레이아웃을 생성합니다.
	 */
	public AuthenticationPanel() {
		this.setLayout(new BorderLayout());

		userPanel = new JPanel(new BorderLayout());
		authPanel = new JPanel();
		signInTextFieldPanel = new JPanel(new GridLayout(2, 2));

		noticePanel = new JPanel();
		noticeLabel = new JLabel(" ");
		noticeLabel.setFont(new Font(null, Font.PLAIN, 14));
		noticePanel.add(noticeLabel);
		userPanel.add(noticePanel, JLabel.CENTER);
		this.add(userPanel, BorderLayout.NORTH);

		idLabel = new JLabel("ID: ");
		idField = new JTextField();
		idField.setPreferredSize(new Dimension(150, 25));
		pwLabel = new JLabel("Password: ");
		pwField = new JPasswordField();
		pwField.setPreferredSize(new Dimension(150, 25));

		signInTextFieldPanel.add(idLabel);
		signInTextFieldPanel.add(idField);
		signInTextFieldPanel.add(pwLabel);
		signInTextFieldPanel.add(pwField);

		signInBtn = new JButton("로그인");
		signInBtn.addActionListener(new SignInBtnListener());
		signInBtn.setPreferredSize(new Dimension(85, 50));
		signUpBtn = new JButton("회원가입");
		signUpBtn.addActionListener(new SignUpBtnListener());
		signUpBtn.setPreferredSize(new Dimension(85, 50));

		authPanel.add(signInTextFieldPanel);
		authPanel.add(signInBtn);
		authPanel.add(signUpBtn);
		this.add(authPanel);
	}

	/**
	 * [로그인] 버튼에 대한 리스너<br>
	 * DB에 사용자로부터 받은 입력과 일치하는 데이터가 존재하는지 확인 후 존재하면 로그인을 수행합니다.
	 * 
	 * @author mingulmangul
	 */
	private class SignInBtnListener implements ActionListener {

		/**
		 * [로그인] 버튼 클릭 시 수행되는 메소드<br>
		 * 사용자가 입력한 값을 가져와 유효성을 검증합니다. 그 후 DB에 입력과 일치하는 유저 정보가 존재하는 지 확인하고, 로그인을 수행합니다.
		 */
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
						String user_pw = res.getString("pw");
						// 비밀번호 비교
						if (!pw.equals(user_pw)) {
							noticeLabel.setText("잘못된 비밀번호입니다");
						} else {
							// 로그인 성공
							User.setId(id);
							User.setName(res.getString("name"));

							// 로그인 & 회원가입 패널 숨기기
							authPanel.setVisible(false);
							noticeLabel.setText(User.getName() + "님 안녕하세요 :)");

							// 정렬을 위한 빈 공간 추가하기
							JLabel emptyPanel = new JLabel();
							emptyPanel.setPreferredSize(new Dimension(110, 25));
							userPanel.add(emptyPanel, BorderLayout.WEST);

							// 마이페이지 버튼 추가하기
							MypageBtn = new JButton("마이페이지");
							MypageBtn.addActionListener(new MypageBtnListener());
							MypageBtn.setPreferredSize(new Dimension(110, 25));
							userPanel.add(MypageBtn, BorderLayout.EAST);
						}
					}
				} catch (SQLException sqle) {
					System.out.println(sqle);
				}
			}
		}

	}

	/**
	 * [회원가입] 버튼에 대한 리스너<br>
	 * 회원가입 Dialog 창을 생성합니다.
	 * 
	 * @author mingulmangul
	 */
	private class SignUpBtnListener implements ActionListener {

		/**
		 * [회원가입] 버튼 클릭 시 수행되는 메소드<br>
		 * 회원가입 Dialog 창을 생성하고, 레이아웃을 설정합니다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// 회원가입 Dialog 레이아웃 설정
			signUpDialog = new JDialog();
			signUpDialog.setTitle("회원가입");
			signUpDialog.setSize(400, 330);
			signUpDialog.setLayout(new BorderLayout(20, 20));
			signUpDialog.setLocationRelativeTo(null);

			JPanel noticePanel = new JPanel();
			JPanel inputPanel = new JPanel(new GridLayout(6, 2));
			JPanel btnPanel = new JPanel();

			noticeLabel = new JLabel("회원 정보를 입력해주세요");
			noticePanel.add(noticeLabel);

			idLabel = new JLabel("   ID:");
			idField = new JTextField();
			inputPanel.add(idLabel);
			inputPanel.add(idField);

			pwLabel = new JLabel("   Password:");
			pwField = new JPasswordField();
			inputPanel.add(pwLabel);
			inputPanel.add(pwField);

			nameLabel = new JLabel("   이름:");
			nameField = new JTextField();
			inputPanel.add(nameLabel);
			inputPanel.add(nameField);

			phoneLabel1 = new JLabel("   전화번호:");
			phoneLabel2 = new JLabel("   ex: 010-0000-0000");
			phoneLabel2.setFont(new Font(null, Font.ITALIC, 10));
			phoneLabel2.setForeground(Color.gray);
			JPanel phonePanel = new JPanel(new BorderLayout());
			phonePanel.add(phoneLabel1);
			phonePanel.add(phoneLabel2, BorderLayout.SOUTH);
			inputPanel.add(phonePanel);
			phoneField = new JTextField();
			inputPanel.add(phoneField);

			emailLabel = new JLabel("   이메일:");
			emailField = new JTextField();
			inputPanel.add(emailLabel);
			inputPanel.add(emailField);

			addressLabel = new JLabel("   주소:");
			addressField = new JTextField();
			inputPanel.add(addressLabel);
			inputPanel.add(addressField);

			submitBtn = new JButton("   가입하기");
			submitBtn.addActionListener(new SubmitBtnListener());
			btnPanel.add(submitBtn);

			signUpDialog.add(noticePanel, BorderLayout.NORTH);
			signUpDialog.add(inputPanel);
			signUpDialog.add(btnPanel, BorderLayout.SOUTH);
			signUpDialog.setVisible(true);
		}

	}

	/**
	 * [가입하기] 버튼에 대한 리스너<br>
	 * 사용자로부터 받은 입력으로 유저 정보 삽입 쿼리를 생성한 후 DB에 삽입합니다.
	 * 
	 * @author mingulmangul
	 */
	private class SubmitBtnListener implements ActionListener {

		/**
		 * 사용자로부터 입력 받은 전화번호가 유효한지 검증하는 메소드
		 * 
		 * @param phone 사용자로부터 입력 받은 전화번호
		 * @return 유효하다면 true, 아니면 false
		 */
		private boolean isValidPhone(String phone) {
			String phonePattern = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
			return Pattern.matches(phonePattern, phone);
		}

		/**
		 * 사용자로부터 입력 받은 이메일이 유효한지 검증하는 메소드
		 * 
		 * @param email 사용자로부터 입력 받은 이메일
		 * @return 유효하다면 true, 아니면 false
		 */
		private boolean isValidEmail(String email) {
			String emailPattern = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$";
			return Pattern.matches(emailPattern, email);
		}

		/**
		 * [가입하기] 버튼 클릭 시 수행되는 메소드<br>
		 * 사용자가 입력한 값을 가져와 유효성을 검증합니다. 그 후 유저 정보 삽입 쿼리를 생성한 후 DB에 삽입합니다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// 사용자 입력 가져오기
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