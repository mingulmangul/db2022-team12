package DB2022Team12;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class MypageBtnListener implements ActionListener{
	private JDialog signInDialog;
	private JPanel emptyPanel, personalPanel, MypagePanel;
	private JLabel infoLabel;
	private JButton checkTicketBtn, reviewBtn;
	
	@Override
	public void actionPerformed(ActionEvent e) {
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