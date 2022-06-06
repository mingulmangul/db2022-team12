package DB2022Team12;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



public class MusicalPanel extends JPanel{
    
    //뮤지컬 검색 쿼리
    private final String SEARCH_MUSICAL_QUERY = "SELECT Title, Summary FROM db2022_musical WHERE Title LIKE ?";
    //뮤지컬 정보 불러오기 쿼리
    private final String LOAD_MUSICAL_QUERY = "SELECT Title, Summary FROM db2022_musical";
    //가격 뷰
    private final String PRICE_VIEW_1 = "SELECT * FROM db2022_under_10";
    private final String PRICE_VIEW_2 = "SELECT * FROM db2022_between10_15";
    private final String PRICE_VIEW_3 = "SELECT * FROM db2022_over_15";
    
    //별점 뷰
    private final String SCORE_VIEW_4 = "SELECT title, summary FROM db2022_musical WHERE title in (SELECT title FROM db2022_avg_rate WHERE score >=4)";
    private final String SCORE_VIEW_3 = "SELECT title, summary FROM db2022_musical WHERE title in (SELECT title FROM db2022_avg_rate WHERE score >=3)";
    private final String SCORE_VIEW_2 = "SELECT title, summary FROM db2022_musical WHERE title in (SELECT title FROM db2022_avg_rate WHERE score >=2)";
    private final String SCORE_VIEW_1 = "SELECT title, summary FROM db2022_musical WHERE title in (SELECT title FROM db2022_avg_rate WHERE score >=1)";
    
    //가격 별 검색
    private final String PRICE1_MUSICAL_QUERY = "SELECT Title, Summary FROM db2022_under_10 WHERE Title LIKE ?";
    private final String PRICE2_MUSICAL_QUERY = "SELECT Title, Summary FROM db2022_between10_15 WHERE Title LIKE ?";
    private final String PRICE3_MUSICAL_QUERY = "SELECT Title, Summary FROM db2022_over_15 WHERE Title LIKE ?";

    //별점 별 검색
    private final String SCORE4_MUSICAL_QUERY = "SELECT title, summary FROM db2022_musical WHERE title LIKE ? and title in (SELECT title FROM db2022_avg_rate WHERE score >=4)";
    private final String SCORE3_MUSICAL_QUERY = "SELECT title, summary FROM db2022_musical WHERE title LIKE ? and title in (SELECT title FROM db2022_avg_rate WHERE score >=3)";
    private final String SCORE2_MUSICAL_QUERY = "SELECT title, summary FROM db2022_musical WHERE title LIKE ? and title in (SELECT title FROM db2022_avg_rate WHERE score >=2)";
    private final String SCORE1_MUSICAL_QUERY = "SELECT title, summary FROM db2022_musical WHERE title LIKE ? and title in (SELECT title FROM db2022_avg_rate WHERE score >=1)";
    
    //별점x가격 검색
    private final String PRICE1_SCORE_QUERY = "SELECT distinct musical.title, musical.summary FROM db2022_musical as musical, (SELECT price.title, price.summary FROM db2022_under_10 as price, db2022_avg_rate as review WHERE price.title = review.title and review.score>=?) as pricexscore WHERE musical.title = pricexscore.title and musical.title LIKE ?";
    private final String PRICE2_SCORE_QUERY = "SELECT distinct musical.title, musical.summary FROM db2022_musical as musical, (SELECT price.title, price.summary FROM db2022_between10_15 as price, db2022_avg_rate as review WHERE price.title = review.title and review.score>=?) as pricexscore WHERE musical.title = pricexscore.title and musical.title LIKE ?";
    private final String PRICE3_SCORE_QUERY = "SELECT distinct musical.title, musical.summary FROM db2022_musical as musical, (SELECT price.title, price.summary FROM db2022_over_15 as price, db2022_avg_rate as review WHERE price.title = review.title and review.score>=?) as pricexscore WHERE musical.title = pricexscore.title and musical.title LIKE ?";
    //별점x가격 
    private final String PRICE1_SCORE_VIEW = "SELECT distinct musical.title, musical.summary FROM db2022_musical as musical, (SELECT price.title, price.summary FROM db2022_under_10 as price, db2022_avg_rate as review WHERE price.title = review.title and review.score>=?) as pricexscore WHERE musical.title = pricexscore.title";
    private final String PRICE2_SCORE_VIEW = "SELECT distinct musical.title, musical.summary FROM db2022_musical as musical, (SELECT price.title, price.summary FROM db2022_between10_15 as price, db2022_avg_rate as review WHERE price.title = review.title and review.score>=?) as pricexscore WHERE musical.title = pricexscore.title";
    private final String PRICE3_SCORE_VIEW = "SELECT distinct musical.title, musical.summary FROM db2022_musical as musical, (SELECT price.title, price.summary FROM db2022_over_15 as price, db2022_avg_rate as review WHERE price.title = review.title and review.score>=?) as pricexscore WHERE musical.title = pricexscore.title";

    private JPanel searchPanel, listPanel;
    private JLabel searchLabel, fakeLabel1, fakeLabel2, fakeLabel3;
    private JLabel titleLabel,theaterLabel, priceLabel, summaryLabel, reviewLabel;
    private JButton searchBtn; 
    private JRadioButton under10Btn, bw1015Btn, over15Btn, allBtn;
    private JRadioButton over1radio, over2radio, over3radio, over4radio, allradio;
    private JTextField searchTextField;
    private JList<String> resultList;
    private JDialog infoDialog;
    
    
    public Vector<String> getMusicals(String query){
        Connection conn;
        Vector<String> musicalList = new Vector<String>();
        try {
            conn = new ConnectionClass().getConnection();
            PreparedStatement preStmt = conn.prepareStatement(query);
            ResultSet res = preStmt.executeQuery();
            // Fetch each row from the result set
            while(res.next()) {
              String title = res.getString("Title");
              String summary = res.getString("Summary");

              musicalList.add(title + "  " + summary + " 보러가기 ");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(musicalList.isEmpty()) {
            musicalList.add("검색하신 뮤지컬이 존재하지 않습니다.");
        }
        return musicalList;
    }
    
    // title search
    public Vector<String> getMusicals(String query, String searchkey){
        Connection conn;
        Vector<String> musicalList = new Vector<String>();
        try {
            conn = new ConnectionClass().getConnection();
            PreparedStatement preStmt = conn.prepareStatement(query);
            preStmt.setString(1,'%'+searchkey+'%');
            ResultSet res = preStmt.executeQuery();
            
            // Fetch each row from the result set
            while(res.next()) {
              String title = res.getString("Title");
              String summary = res.getString("Summary");

              musicalList.add(title + "  " + summary + " 보러가기 ");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(musicalList.isEmpty()) {
            musicalList.add("검색하신 뮤지컬이 존재하지 않습니다.");
        }
        return musicalList;
    }
    
    public Vector<String> getMusicals(String query, int reviewScore){
        Connection conn;
        Vector<String> musicalList = new Vector<String>();
        try {
            conn = new ConnectionClass().getConnection();
            PreparedStatement preStmt = conn.prepareStatement(query);
            String stringReviewScore = String.valueOf(reviewScore);
            preStmt.setString(1, stringReviewScore);
            ResultSet res = preStmt.executeQuery();
            // Fetch each row from the result set
            while(res.next()) {
              String title = res.getString("Title");
              String summary = res.getString("Summary");

              musicalList.add(title + "  " + summary + " 보러가기 ");
            } 
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(musicalList.isEmpty()) {
            musicalList.add("검색하신 뮤지컬이 존재하지 않습니다.");
        }
        return musicalList;
    }
    
    public Vector<String> getMusicals(String query, int reviewScore, String searchkey){
        Connection conn;
        Vector<String> musicalList = new Vector<String>();
        try {
            conn = new ConnectionClass().getConnection();
            PreparedStatement preStmt = conn.prepareStatement(query);
            String stringReviewScore = String.valueOf(reviewScore);
            preStmt.setString(1, stringReviewScore);
            preStmt.setString(2,'%'+searchkey+'%');
            ResultSet res = preStmt.executeQuery();
            // Fetch each row from the result set
            while(res.next()) {
              String title = res.getString("Title");
              String summary = res.getString("Summary");

              musicalList.add(title + "  " + summary + " 보러가기 ");
            } 
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(musicalList.isEmpty()) {
            musicalList.add("검색하신 뮤지컬이 존재하지 않습니다.");
        }
        return musicalList;
    }
    
    public MusicalPanel() {
        
        this.setLayout(new GridLayout(2,1));
    	//this.setLayout(new BorderLayout());
        
        searchPanel = new JPanel();
        this.add(searchPanel);
        
        listPanel = new JPanel();
        this.add(listPanel);
        
        searchPanel.setLayout(new GridLayout(3,5));
        
        fakeLabel1 = new JLabel("                ");
        searchPanel.add(fakeLabel1);
        
        searchLabel = new JLabel("뮤지컬 조회 : ");
        searchPanel.add(searchLabel);
        
        searchTextField = new JTextField(20);
        searchPanel.add(searchTextField);
        
        searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        searchBtn.addActionListener(new searchIntegratedListner());
      
        fakeLabel2 = new JLabel("                ");
        searchPanel.add(fakeLabel2);
        //----------------------------
        
        ButtonGroup priceGroup = new ButtonGroup();
        
        allBtn = new JRadioButton("전체", true);
        searchPanel.add(allBtn);
        allBtn.addActionListener(new radioListener());
        priceGroup.add(allBtn);
        
        under10Btn = new JRadioButton("~10만원");
        searchPanel.add(under10Btn);
        under10Btn.addActionListener(new radioListener());
        priceGroup.add(under10Btn);
        
        bw1015Btn = new JRadioButton("11만원~15만원");
        searchPanel.add(bw1015Btn);
        bw1015Btn.addActionListener(new radioListener());
        priceGroup.add(bw1015Btn);
        
        over15Btn = new JRadioButton("16만원~");
        searchPanel.add(over15Btn);
        over15Btn.addActionListener(new radioListener());
        priceGroup.add(over15Btn);
        
        
        fakeLabel3 = new JLabel("                ");
        searchPanel.add(fakeLabel3);
        
        //----------------------------
        ButtonGroup scoreGroup = new ButtonGroup();
        
        allradio = new JRadioButton("전체",true);
        searchPanel.add(allradio);
        allradio.addActionListener(new radioListener());
        scoreGroup.add(allradio);
        
        over4radio = new JRadioButton("★★★★ 이상");
        searchPanel.add(over4radio);
        over4radio.addActionListener(new radioListener());
        scoreGroup.add(over4radio);
        
        over3radio = new JRadioButton("★★★ 이상");
        searchPanel.add(over3radio);
        over3radio.addActionListener(new radioListener());
        scoreGroup.add(over3radio);
        
        over2radio = new JRadioButton("★★ 이상");
        searchPanel.add(over2radio);
        over2radio.addActionListener(new radioListener());
        scoreGroup.add(over2radio);
        
        over1radio = new JRadioButton("★ 이상");
        searchPanel.add(over1radio);
        over1radio.addActionListener(new radioListener());
        scoreGroup.add(over1radio);
        
        
        resultList = new JList<String>(getMusicals(LOAD_MUSICAL_QUERY));      
        resultList.addMouseListener(mouseListener);
        listPanel.add(resultList);
        listPanel.add(new JScrollPane(resultList));
        setVisible(true);
    }
    
    private class searchIntegratedListner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = searchTextField.getText().strip();
            searchTextField.setText("");
            
            // 사용자 입력 유효성 검증
            if (title.isEmpty()) {
                searchTextField.setText("뮤지컬 제목을 입력해주세요.");
            }
            else if(under10Btn.isSelected()){
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(PRICE1_MUSICAL_QUERY,title));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(PRICE1_SCORE_QUERY,4,title));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(PRICE1_SCORE_QUERY,3,title));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(PRICE1_SCORE_QUERY,2,title));
            	else
            		resultList.setListData(getMusicals(PRICE1_SCORE_QUERY,1,title));
            }
            else if(bw1015Btn.isSelected()){
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(PRICE2_MUSICAL_QUERY,title));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(PRICE2_SCORE_QUERY,4,title));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(PRICE2_SCORE_QUERY,3,title));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(PRICE2_SCORE_QUERY,2,title));
            	else
            		resultList.setListData(getMusicals(PRICE2_SCORE_QUERY,1,title));
            } 
            else if(over15Btn.isSelected()){
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(PRICE3_MUSICAL_QUERY,title));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(PRICE3_SCORE_QUERY,4,title));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(PRICE3_SCORE_QUERY,3,title));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(PRICE3_SCORE_QUERY,2,title));
            	else
            		resultList.setListData(getMusicals(PRICE3_SCORE_QUERY,1,title));
            }
            else {
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(SEARCH_MUSICAL_QUERY,title));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(SCORE4_MUSICAL_QUERY,title));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(SCORE3_MUSICAL_QUERY,title));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(SCORE2_MUSICAL_QUERY,title));
            	else
            		resultList.setListData(getMusicals(SCORE1_MUSICAL_QUERY,title));
            }
        }
    }
    
    private class radioListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	if(under10Btn.isSelected()){
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(PRICE_VIEW_1));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(PRICE1_SCORE_VIEW,4));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(PRICE1_SCORE_VIEW,3));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(PRICE1_SCORE_VIEW,2));
            	else
            		resultList.setListData(getMusicals(PRICE1_SCORE_VIEW,1));
            }
            else if(bw1015Btn.isSelected()){
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(PRICE_VIEW_2));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(PRICE2_SCORE_VIEW,4));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(PRICE2_SCORE_VIEW,3));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(PRICE2_SCORE_VIEW,2));
            	else
            		resultList.setListData(getMusicals(PRICE2_SCORE_VIEW,1));
            } 
            else if(over15Btn.isSelected()){
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(PRICE_VIEW_3));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(PRICE3_SCORE_VIEW,4));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(PRICE3_SCORE_VIEW,3));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(PRICE3_SCORE_VIEW,2));
            	else
            		resultList.setListData(getMusicals(PRICE3_SCORE_VIEW,1));
            }
            else {
            	if(allradio.isSelected())
            		resultList.setListData(getMusicals(LOAD_MUSICAL_QUERY));
            	else if(over4radio.isSelected())
            		resultList.setListData(getMusicals(SCORE_VIEW_4));
            	else if(over3radio.isSelected())
            		resultList.setListData(getMusicals(SCORE_VIEW_3));
            	else if(over2radio.isSelected())
            		resultList.setListData(getMusicals(SCORE_VIEW_2));
            	else
            		resultList.setListData(getMusicals(SCORE_VIEW_1));
            }
        }
    }
    
    // JList click listener
    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {

                //제목가져오기
                String selectedMusical = resultList.getSelectedValue();
                String [] text = selectedMusical.split(" ");
                String selectedtitle = text[0].strip();
                
                System.out.print(selectedtitle);
                
                infoDialog = new JDialog();
                infoDialog.setSize(500, 400);
                infoDialog.setLayout(new GridLayout(2, 1));

                JPanel topPanel = new JPanel(new GridLayout(5, 1));
                JPanel botPanel = new JPanel();
                
                Musical musical = new Musical(selectedtitle);


                titleLabel = new JLabel("   제목  :  " + musical.getTitle());
                theaterLabel = new JLabel("   극장  :  " + musical.getTheaterName());
                priceLabel = new JLabel("  가격  :  " + musical.getPrice());
                summaryLabel = new JLabel("   줄거리  :  "  + musical.getSummary());
                reviewLabel = new JLabel("   평균별점  :  ★" + musical.getScore());
            
                topPanel.add(titleLabel);           
                topPanel.add(theaterLabel);
                topPanel.add(priceLabel);
                topPanel.add(summaryLabel);
                topPanel.add(reviewLabel);
                
                TicketPanel ticketPanel = new TicketPanel(musical);
                botPanel.add(ticketPanel);

                infoDialog.add(topPanel);
                infoDialog.add(botPanel);
                infoDialog.setVisible(true);
            }
        }
        
    };
    
}
