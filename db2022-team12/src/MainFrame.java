import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		setTitle("���α׷�");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(5, 1));

		AuthenticationPanel authPanel = new AuthenticationPanel();
		TicketPanel ticketPanel = new TicketPanel("ŷŰ����");

		contentPane.add(authPanel);
		contentPane.add(ticketPanel);

		setVisible(true);
	}
}
