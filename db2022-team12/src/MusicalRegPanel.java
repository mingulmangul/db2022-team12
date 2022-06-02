import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class MusicalRegPanel extends JPanel {

	JDialog musicalRegDialog;
	JButton musicalRegDialogBtn, submitBtn;
	JLabel noticeLabel, titleLabel, dateLabel, timeLabel, theaterLabel, priceLabel,
			summaryLabel;
	JTextField titleField, priceField, dateField, timeField;
	JTextArea summaryArea;
	JComboBox<String> theaterSelector;

	// 극장 정보(이름, 총 좌석 수)를 가져오는 쿼리
	private final String GET_THEATER_QUERY = "SELECT name, size FROM theater";

	public MusicalRegPanel() {
		musicalRegDialogBtn = new JButton("뮤지컬 등록");
		musicalRegDialogBtn.addActionListener(new musicalRegDialogCreater());
		this.add(musicalRegDialogBtn);
	}

	// 뮤지컬 등록 버튼 리스너: 뮤지컬 등록 다이얼로그를 생성
	private class musicalRegDialogCreater implements ActionListener {
		
		// DB로부터 극장 정보를 가져와 리턴함
		private Vector<String> getTheaterData() {
			Vector<String> theaters = new Vector<>();
			
			try (
				Connection conn = new ConnectionClass().getConnection();
				Statement stmt = conn.createStatement();
			){
				// 쿼리의 결과로 가져 온 극장 이름과 좌석 수 정보를 벡터에 추가
				ResultSet rs = stmt.executeQuery(GET_THEATER_QUERY);
				while (rs.next()) {
					String theater = rs.getString("name") + " (총 좌석 수: " + rs.getInt("size") + ")";
					theaters.add(theater);
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getErrorCode() + " : " + sqle);
			}
			return theaters;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// 뮤지컬 등록 다이얼로그 생성
			musicalRegDialog = new JDialog();
			musicalRegDialog.setTitle("뮤지컬 등록");
			musicalRegDialog.setSize(300, 300);
			musicalRegDialog.setLayout(new GridLayout(3, 1));

			JPanel noticePanel = new JPanel();
			JPanel inputPanel = new JPanel(new GridLayout(7, 2));
			JPanel btnPanel = new JPanel();

			noticeLabel = new JLabel("등록하려는 뮤지컬의 정보를 입력해주세요");
			noticePanel.add(noticeLabel);

			titleLabel = new JLabel("제목");
			titleField = new JTextField();
			theaterLabel = new JLabel("극장");
			JComboBox<String> theaterSelector = new JComboBox<>(this.getTheaterData());
			dateLabel = new JLabel("날짜");
			dateField = new JTextField();
			timeLabel = new JLabel("시간");
			timeField = new JTextField();
			priceLabel = new JLabel("가격");
			priceField = new JTextField();
			summaryLabel = new JLabel("줄거리");
			summaryArea = new JTextArea(10, 20);
			inputPanel.add(titleLabel);
			inputPanel.add(titleField);
			inputPanel.add(theaterLabel);
			inputPanel.add(theaterSelector);
			inputPanel.add(dateLabel);
			inputPanel.add(dateField);
			inputPanel.add(timeLabel);
			inputPanel.add(timeField);
			inputPanel.add(priceLabel);
			inputPanel.add(priceField);
			inputPanel.add(summaryLabel);
			inputPanel.add(summaryArea);

			submitBtn = new JButton("등록하기");
			submitBtn.addActionListener(null);
			btnPanel.add(submitBtn);

			musicalRegDialog.add(noticePanel);
			musicalRegDialog.add(inputPanel);
			musicalRegDialog.add(btnPanel);
			musicalRegDialog.setVisible(true);
		}

	}
}
