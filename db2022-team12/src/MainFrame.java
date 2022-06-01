import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		setTitle("프로그램");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);

		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());

		AuthenticationPanel authPanel = new AuthenticationPanel();

		contentPane.add(authPanel);

		setVisible(true);
	}
}
