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

		DB2022Team12.AuthenticationPanel authPanel = new AuthenticationPanel();
		DB2022Team12.TicketPanel ticketPanel = new TicketPanel(new Musical("아이다"));
<<<<<<< Updated upstream

		contentPane.add(authPanel);
		contentPane.add(ticketPanel);

=======
		DB2022Team12.MusicalPanel musicalPanel = new MusicalPanel();
		
		//contentPane.add(authPanel);
		//contentPane.add(ticketPanel);
		contentPane.add(musicalPanel);
		setSize(1100, 350);
>>>>>>> Stashed changes
		setVisible(true);
	}
}
