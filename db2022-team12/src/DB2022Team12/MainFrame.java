package DB2022Team12;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

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
		setTitle("뮤지컬 예매 프로그램");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1;
		gbc1.weighty = 0.1;
		gbc1.fill = GridBagConstraints.BOTH;

		AuthenticationPanel authPanel = new AuthenticationPanel();
		contentPane.add(authPanel, gbc1);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.weightx = 1;
		gbc2.weighty = 0.9;
		gbc2.fill = GridBagConstraints.BOTH;

		MusicalPanel musicalPanel = new MusicalPanel();
		contentPane.add(musicalPanel, gbc2);

		setVisible(true);
	}
}