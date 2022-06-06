package DB2022Team12;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * 알림창을 생성하는 클래스
 * 
 * @author mingulmangul
 */
class NotificationClass {

	/**
	 * 알림창을 생성하는 메소드
	 * 
	 * @param title 알림창 제목
	 * @param msg   알림창에 표시할 메세지
	 */
	public static void createNotifDialog(String title, String msg) {
		// 알림창 레이아웃 설정
		JDialog notifDialog = new JDialog();
		notifDialog.setTitle(title);
		notifDialog.setSize(200, 100);
		notifDialog.setLayout(new GridLayout(2, 1));

		JLabel notifLabel = new JLabel(msg);
		notifDialog.add(notifLabel);
		JButton confirmBtn = new JButton("확인");
		// <확인> 버튼 누를 시 알림창 닫기
		confirmBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				notifDialog.dispose();
			}
		});
		notifDialog.add(confirmBtn);
		notifDialog.setVisible(true);
	}
}
