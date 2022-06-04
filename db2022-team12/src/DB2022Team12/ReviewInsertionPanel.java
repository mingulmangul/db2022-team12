package DB2022Team12;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class ReviewInsertionPanel extends JPanel {
	
	private JPanel reviewPanel, scorePanel, btnPanel;
	private JRadioButton[] scoreBtn;
	private JButton submitBtn;
	private ButtonGroup btnGroup;
	
	// 별점(0~5)
	private final static String[] scoreList = {"★", "★★", "★★★", "★★★★", "★★★★★"};

	// 리뷰를 작성하려는 공연의 정보를 담은 Musical 객체
	private Musical musical;
	
	// 리뷰 작성 패널 레이아웃 설정
	// 리뷰를 작성하려는 공연의 정보를 담은 Musical 객체를 전달 받음
	public ReviewInsertionPanel(Musical musical) {
		this.musical = musical;
		reviewPanel = new JPanel();
		reviewPanel.setSize(300, 100);
		
		scorePanel = new JPanel();
		btnPanel = new JPanel();

		// 별점(0~5점)을 나타내는 라디오 버튼 생성
		scoreBtn = new JRadioButton[5];
		btnGroup = new ButtonGroup();
		for (int i=0; i<5; i++) {
			scoreBtn[i] = new JRadioButton(scoreList[i]);
			btnGroup.add(scoreBtn[i]);
			scorePanel.add(scoreBtn[i]);
		}
		
		submitBtn = new JButton("등록하기");
		submitBtn.addActionListener(null);
		btnPanel.add(submitBtn);
		
		reviewPanel.add(scorePanel);
		reviewPanel.add(btnPanel);
		this.add(reviewPanel);
	}
}
