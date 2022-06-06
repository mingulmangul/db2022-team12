package DB2022Team12;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * 뮤지컬 예매 프로그램의 메인 프레임을 생성하는 클래스
 * 
 * @author mingulmangul
 */
class MainFrame extends JFrame {

	/**
	 * 메인 프레임 생성자<br>
	 * 메인 프레임의 레이아웃을 설정합니다.
	 */
	public MainFrame() {
		setTitle("프로그램");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(5, 1));

		AuthenticationPanel authPanel = new AuthenticationPanel();

		Musical musical = new Musical("최후진술");
		TicketPanel ticketPanel = new TicketPanel(musical);
		ReviewInsertionPanel reviewInsertionPanel = new ReviewInsertionPanel(musical);
		// MusicalPanel musical = new MusicalPanel();

		contentPane.add(authPanel);
		contentPane.add(ticketPanel);
		contentPane.add(reviewInsertionPanel);

		setVisible(true);
	}
}