package DB2022Team12;
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

class TicketPanel extends JPanel {

	private JDialog ticketDialog;
	private JButton ticketDialogBtn, bookBtn;
	private JLabel noticeLabel, musicalLabel1, musicalLabel2, dateTimeLabel, theaterLabel1, 
					theaterLabel2, priceLabel1, priceLabel2;
	private JComboBox<String> dateSelector, timeSelector;

	private String musical;


	public TicketPanel(String musical) {
		this.musical = musical;
		ticketDialogBtn = new JButton("티켓 구매");
		ticketDialogBtn.addActionListener(new TicketDialogCreater());
		this.add(ticketDialogBtn);
	}

	private class TicketDialogCreater implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 예매하려는 뮤지컬 정보 가져오기
//			getMusicalInfo();
			new Musical(musical);

			// 티켓 예매 다이얼로그 생성
			ticketDialog = new JDialog();
			ticketDialog.setTitle("뮤지컬 등록");
			ticketDialog.setSize(300, 300);
			ticketDialog.setLayout(new GridLayout(3, 1));

			JPanel noticePanel = new JPanel();
			JPanel inputPanel = new JPanel(new GridLayout(7, 2));
			JPanel btnPanel = new JPanel();
			JPanel dateTimePanel = new JPanel();

			noticeLabel = new JLabel("예매 티켓의 정보를 입력해주세요");
			noticePanel.add(noticeLabel);

			musicalLabel1 = new JLabel("공연 제목");
			musicalLabel2 = new JLabel(musical);
			dateTimeLabel = new JLabel("날짜 및 시간");
			dateSelector = new JComboBox<String>();
			timeSelector = new JComboBox<String>();
			theaterLabel1 = new JLabel("극장");
			theaterLabel2 = new JLabel("충무로아트센터");
			priceLabel1 = new JLabel("예매가");
			priceLabel2 = new JLabel("100,000원");
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
			
			bookBtn = new JButton("예매하기");
			bookBtn.addActionListener(null);
			btnPanel.add(bookBtn);
			
			ticketDialog.add(noticePanel);
			ticketDialog.add(inputPanel);
			ticketDialog.add(btnPanel);
			ticketDialog.setVisible(true);
		}

	}
}
