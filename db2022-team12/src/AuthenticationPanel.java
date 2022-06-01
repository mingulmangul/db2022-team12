import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AuthenticationPanel extends JPanel {

	private JButton signInBtn, signUpBtn, submitBtn;
	private JTextField idField, nameField, phoneField, emailField, addressField;
	private JPasswordField pwField;
	private JLabel noticeLabel, idLabel, pwLabel, nameLabel, phoneLabel, emailLabel, addressLabel;

	private boolean isValid;

	// id, pw, name, phone, email, address
	private static String SIGNUP_QUERY = "INSERT INTO member VALUES (?, ?, ?, ?, ?, ?)";

	public AuthenticationPanel() {
		JPanel signInTextFieldPanel = new JPanel(new GridLayout(2, 3));

		idLabel = new JLabel("ID: ");
		idField = new JTextField();
		pwLabel = new JLabel("Password: ");
		pwField = new JPasswordField();

		signInTextFieldPanel.add(idLabel);
		signInTextFieldPanel.add(idField);
		signInTextFieldPanel.add(pwLabel);
		signInTextFieldPanel.add(pwField);

		signInBtn = new JButton("로그인");
		signInBtn.addActionListener(new signInBtnListener());
		signUpBtn = new JButton("회원가입");
		signUpBtn.addActionListener(new signUpBtnListener());

		this.add(signInTextFieldPanel);
		this.add(signInBtn);
		this.add(signUpBtn);
	}

	private class signInBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

		}

	}

	private class signUpBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog d = new JDialog();
			d.setSize(300, 400);
			d.setLayout(new GridLayout(3, 1));

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

			d.add(topPanel);
			d.add(midPanel);
			d.add(botPanel);
			d.setVisible(true);
		}

	}

	private class submitBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String id = idField.getText().strip();
			String pw = String.valueOf(pwField.getPassword()).strip();
			if (id.isEmpty()) {
				noticeLabel.setText("id is null");
			} else if (pw.isEmpty()) {
				noticeLabel.setText("pw is null");
			} else {
				String name = nameField.getText().strip();
				String phone = phoneField.getText().strip();
				String email = emailField.getText().strip();
				String address = addressField.getText().strip();

				try (Connection conn = new ConnectionClass().getConnection();
						PreparedStatement preStmt = conn.prepareStatement(SIGNUP_QUERY);) {
					preStmt.setString(1, id);
					preStmt.setString(2, pw);
					preStmt.setString(3, name);
					preStmt.setString(4, phone);
					preStmt.setString(5, email);
					preStmt.setString(6, address);
					preStmt.executeUpdate();
				} catch (SQLException sqle) {
					System.out.println(sqle);
				}
			}
		}

	}

}