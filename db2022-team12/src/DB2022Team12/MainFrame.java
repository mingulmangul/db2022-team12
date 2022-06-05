package DB2022Team12;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

class MainFrame extends JFrame {

	public MainFrame() {
		setTitle("프로그램");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(5, 1));

		AuthenticationPanel authPanel = new AuthenticationPanel();
		
		Musical musical = new Musical("아이다");
		TicketPanel ticketPanel = new TicketPanel(musical);
		ReviewInsertionPanel reviewInsertionPanel = new ReviewInsertionPanel(musical);

		contentPane.add(authPanel);
		contentPane.add(ticketPanel);
		contentPane.add(reviewInsertionPanel);

		setVisible(true);
	}
}