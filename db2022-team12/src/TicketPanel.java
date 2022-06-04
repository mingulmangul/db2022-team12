import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

public class TicketPanel extends JPanel {

	private JDialog ticketDialog;
	private JButton ticketDialogBtn, bookBtn;
	private JLabel noticeLabel, musicalLabel1, musicalLabel2, dateTimeLabel, theaterLabel1, 
					theaterLabel2, priceLabel1, priceLabel2;
	private JComboBox<String> dateSelector, timeSelector;

	private String musical;

	// ������ ������ �������� ����
	private final String GET_MUSICAL_QUERY = "SELECT * FROM musical WHERE title = ?";

	public TicketPanel(String musical) {
		this.musical = musical;
		ticketDialogBtn = new JButton("Ƽ�� ����");
		ticketDialogBtn.addActionListener(new TicketDialogCreater());
		this.add(ticketDialogBtn);
	}

	private class TicketDialogCreater implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// �����Ϸ��� ������ ���� ��������

			// Ƽ�� ���� ���̾�α� ����
			ticketDialog = new JDialog();
			ticketDialog.setTitle("������ ���");
			ticketDialog.setSize(300, 300);
			ticketDialog.setLayout(new GridLayout(3, 1));

			JPanel noticePanel = new JPanel();
			JPanel inputPanel = new JPanel(new GridLayout(7, 2));
			JPanel btnPanel = new JPanel();
			JPanel dateTimePanel = new JPanel();

			noticeLabel = new JLabel("���� Ƽ���� ������ �Է����ּ���");
			noticePanel.add(noticeLabel);

			musicalLabel1 = new JLabel("���� ����");
			musicalLabel2 = new JLabel(musical);
			dateTimeLabel = new JLabel("��¥ �� �ð�");
			dateSelector = new JComboBox<String>();
			timeSelector = new JComboBox<String>();
			theaterLabel1 = new JLabel("����");
			theaterLabel2 = new JLabel("�湫�ξ�Ʈ����");
			priceLabel1 = new JLabel("���Ű�");
			priceLabel2 = new JLabel("100,000��");
			inputPanel.add(musicalLabel1);
			inputPanel.add(musicalLabel2);
			inputPanel.add(dateTimeLabel);
			dateTimePanel.add(dateSelector);
			dateTimePanel.add(timeSelector);
			inputPanel.add(dateTimePanel);
			inputPanel.add(theaterLabel1);
			inputPanel.add(theaterLabel2);
			inputPanel.add(priceLabel1);
			inputPanel.add(priceLabel2);
			
			bookBtn = new JButton("�����ϱ�");
			bookBtn.addActionListener(null);
			btnPanel.add(bookBtn);
			
			ticketDialog.add(noticePanel);
			ticketDialog.add(inputPanel);
			ticketDialog.add(btnPanel);
			ticketDialog.setVisible(true);
		}

	}
}