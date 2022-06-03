-- DB2022Team12

CREATE DATABASE DB2022Team12;

USE DB2022Team12;

-- 첫 문자 대문자, 함수, 변수형과 겹치는 이름들은 앞에 테이블명_소문자 이름 했음. 아이디 패스워드만 전부 대문자

CREATE TABLE Member(
    ID VARCHAR(20),
    PW VARCHAR(20) NOT NULL,
    Member_name VARCHAR(20) NOT NULL,
    Birthday DATE NOT NULL,
    Phone char(13) NOT NULL,
    Email VARCHAR(20) NOT NULL,
    Member_address varchar(100),
    PRIMARY KEY(ID)
);

CREATE TABLE Theater(
    Theater_name VARCHAR(20),
    Theater_address VARCHAR(100) NOT NULL,
    Phone CHAR(12) NOT NULL,
    Size INT NOT NULL,
    PRIMARY KEY(Theater_name)
);

CREATE TABLE Musical(
    ID INT NOT NULL,
    Title VARCHAR(20) NOT NULL,
    Musical_date DATE NOT NULL,
    Musical_time TIME NOT NULL,
    Theater_name VARCHAR(20) NOT NULL,
    Remain_seat INT,
    Price INT NOT NULL,
    Summary VARCHAR(100),
    Musical_cast VARCHAR(100),
    PRIMARY KEY(ID),
    FOREIGN KEY(Theater_name) REFERENCES Theater(Theater_name)
);

CREATE TABLE Ticket(
    ID INT,
    Musical_id INT NOT NULL,
    Member_id VARCHAR(20) NOT NULL,
    Musical_date DATE NOT NULL,
    Musical_time TIME NOT NULL,
    Seat_row TINYINT NOT NULL,
    Seat_col TINYINT NOT NULL,
    Theater_name VARCHAR(20) NOT NULL,
    Order_date DATE NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(Musical_id) REFERENCES Musical(ID),
    FOREIGN KEY(Member_id) REFERENCES Member(ID),
    FOREIGN KEY(Theater_name) REFERENCES Theater(Theater_name)
);

CREATE TABLE Review(
    ID INT,
    Musical_id INT NOT NULL,
    Rate TINYINT NOT NULL,
    Written_at DATE NOT NULL,
    Member_id VARCHAR(20) NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(Musical_id) REFERENCES Musical(ID),
    FOREIGN KEY(Member_id) REFERENCES Member(ID)
);

-- insert Member
-- INSERT INTO Member values(ID, PW, Member_name, Birthday, Phone, Email);
INSERT INTO Member values('ga2059', '710hyeon', '권가현', '1989-08-24', '010-8975-3244', 'ga2059@naver.com','');
INSERT INTO Member values('min1905', 'ah710', '권민아', '1993-03-20', '010-6556-8536', 'min1905@naver.com','서울특별시 서대문구 이화여대길 52');
INSERT INTO Member values('19soo10', 'min710', '김수민', '1972-11-23', '010-8451-2994', '19soo10@gmail.com','');
INSERT INTO Member values('1684yoon', '150ah', '김윤아', '2001-09-16', '010-6987-3257', '1684yoon@gmail.com','서울특별시 서대문구 대현동 11-1');
INSERT INTO Member values('liu1771', '710siqi', '유은기', '2003-02-09', '010-2348-6582', 'liu1771@gmail.com','');

-- insert Theater
-- INSERT INTO Theater values(Theater_name, Theater_address, Phone, Size);
INSERT INTO Theater values('충무아트센터', '서울 중구 퇴계로 387', '02-2230-6600', 1250);
INSERT INTO Theater values('드림아트센터', '서울특별시 종로구 동숭길 123 드림아트센터', '02-3481-8843', 210);
INSERT INTO Theater values('예술의전당', '서울 서초구 남부순환로 2406', '02-5800-1300', 2505);
INSERT INTO Theater values('블루스퀘어', '서울 용산구 이태원로 294', '02-1544-1591', 1766);
INSERT INTO Theater values('샤롯데씨어터', '서울 송파구 올림픽로 240', '02-1644-0078', 1241);

-- insert Musical
-- INSERT INTO Musical values(ID, Title, Musical_date, Musical_time, Theater_name, Remain_seat, Price, Summary, Musical_cast);
INSERT INTO Musical values(1, '킹키부츠', '2022-07-01', '14:00:00', '충무아트센터', 10, 160000, '폐업 위기에 처한 구두 공장을 물려 받게 된 찰리는 생각도 스타일도 전혀 다른 아름답고 유쾌한 남자 롤라를 만나 새로운 영감을 얻고 도전을 시작한다.', '이석훈, 최재림, 김지우');
INSERT INTO Musical values(2, '킹키부츠', '2022-07-02', '19:00:00', '충무아트센터', 5, 160000, '폐업 위기에 처한 구두 공장을 물려 받게 된 찰리는 생각도 스타일도 전혀 다른 아름답고 유쾌한 남자 롤라를 만나 새로운 영감을 얻고 도전을 시작한다.', '김성규, 강홍석, 김환희');
INSERT INTO Musical values(3, '비더슈탄트', '2022-06-30', '20:00:00', '드림아트센터', 15, 66000, '1938년 독일, 엘리트 스포츠 학교에 입학한 펜싱부 소년들의 권력에 대한 저항과 우정을 그리며, 생각없이 시대에 순응하며 사는 위험을 경고하며, 반항의 진정한 가치를 돌아본다.', '최석진, 김바다, 김이담');
INSERT INTO Musical values(4, '비더슈탄트', '2022-07-01', '19:00:00', '드림아트센터', 7, 66000, '1938년 독일, 엘리트 스포츠 학교에 입학한 펜싱부 소년들의 권력에 대한 저항과 우정을 그리며, 생각없이 시대에 순응하며 사는 위험을 경고하며, 반항의 진정한 가치를 돌아본다.', '안지환, 김바다, 김도현');
INSERT INTO Musical values(5, '데스노트', '2022-07-02', '14:00:00', '예술의전당', 15, 130000, '법과 정의에 대해 고민하던 천재 고등학생 야가미 라이토는 어느날 우연히 데스노트를 줍게 된다. 노트의 원래 주인인 사신들이 지켜보는 가운데 라이토는 세상의 신이 되고자한다.', '홍광호, 김준수, 김선영');
INSERT INTO Musical values(6, '데스노트', '2022-07-03', '14:00:00', '예술의전당', 9, 130000, '법과 정의에 대해 고민하던 천재 고등학생 야가미 라이토는 어느날 우연히 데스노트를 줍게 된다. 노트의 원래 주인인 사신들이 지켜보는 가운데 라이토는 세상의 신이 되고자한다.', '고은성, 김성철, 장은아');
INSERT INTO Musical values(7, '아이다', '2022-06-15', '14:00:00', '블루스퀘어', 22, 120000, '고대 왕국의 여왕이었던 암네리스가 이집트와 그 이웃 나라였던 누비아 사이의 전쟁이 최고조에 달했던 시대의 투쟁과 그 안에서 꽃피었던 사랑 이야기로 관객들을 초대한다.', '윤공주, 김우형, 아이비');
INSERT INTO Musical values(8, '아이다', '2022-06-15', '20:00:00', '블루스퀘어', 4, 120000, '고대 왕국의 여왕이었던 암네리스가 이집트와 그 이웃 나라였던 누비아 사이의 전쟁이 최고조에 달했던 시대의 투쟁과 그 안에서 꽃피었던 사랑 이야기로 관객들을 초대한다.', '윤공주, 최재림, 민경아');
INSERT INTO Musical values(9, '마타하리', '2022-06-14', '19:00:00', '샤롯데씨어터', 17, 90000, '파리 박물관에서 희대의 스파이, 마타하리의 머리가 공개될 예정이었다. 하지만 그녀의 머리는 흔적없이 사라졌고, 한 노인이 알 수 없는 이야기를 한다. 그녀는 어떤 사람이었을까?', '옥주현, 이홍기, 최민철');
INSERT INTO Musical values(10, '마타하리', '2022-06-15', '19:00:00', '샤롯데씨어터', 8, 90000, '파리 박물관에서 희대의 스파이, 마타하리의 머리가 공개될 예정이었다. 하지만 그녀의 머리는 흔적없이 사라졌고, 한 노인이 알 수 없는 이야기를 한다. 그녀는 어떤 사람이었을까?', '솔라, 이창섭, 김바울');

-- insert Ticket
-- INSERT INTO Ticket values(ID, Musical_id, Member_id, Musical_date, Musical_time, Seat_row, Seat_col, Theater_name, Order_date);
-- 티켓 아이디는 뮤지컬 아이디 + 순서 숫자
INSERT INTO Ticket values(21, 2, 'ga2059', '2022-07-02', '19:00:00', 3, 5, '충무아트센터', '2022-05-28');
INSERT INTO Ticket values(31, 3, '19soo10', '2022-06-30', '20:00:00', 8, 12, '드림아트센터', '2022-05-31');
INSERT INTO Ticket values(51, 5, '19soo10', '2022-07-02', '14:00:00', 2, 9, '예술의전당', '2022-06-02');
INSERT INTO Ticket values(71, 7, '1684yoon', '2022-06-15', '14:00:00', 10, 16, '블루스퀘어', '2022-06-02');
INSERT INTO Ticket values(72, 7, 'liu1771', '2022-06-15', '14:00:00', 16, 4, '블루스퀘어', '2022-06-04');

-- insert Review
-- INSERT INTO Review values(ID, Musical_id, Rate, Written_at, Member_id);
-- 리뷰 아이디는 뮤지컬 아이디 + 순서 숫자
-- 킹키부츠=3.5 비더슈단트=3.5 데스노트=0 아이다=4 마타하리=3
INSERT INTO Review values(21, 2, 4, '2022-05-21', 'ga2059');
INSERT INTO Review values(22, 2, 3, '2022-05-26', 'min1905');
INSERT INTO Review values(41, 4, 5, '2022-06-09', '19soo10');
INSERT INTO Review values(42, 4, 2, '2022-06-12', 'min1905');
INSERT INTO Review values(81, 8, 4, '2022-05-26', '1684yoon');
INSERT INTO Review values(101, 10, 3, '2022-05-16', '1684yoon');


-- view
-- 1. 뮤지컬 이름, 해당 뮤지컬의 평균 별점
CREATE VIEW Ratings AS 
SELECT Musical.Title, avg(Review.Rate) as Rate
FROM Review, Musical
GROUP BY Musical.Title
HAVING Musical.ID = Review.Musical_id;

SELECT Musical.Title, avg(Review.Rate) as Rate
FROM Review, Musical
WHERE Musical.ID = Review.Musical_id
GROUP BY Musical.Title;

-- 2. 가격대, 뮤지컬 이름 10만원 이하, ~15만원, 그 이상
CREATE VIEW 10만원이하 AS
SELECT Title
FROM Musical
WHERE Price <= 100000;

CREATE VIEW 10만원부터_15만원 AS
SELECT Title
FROM Musical
WHERE Price between 100000 and 150000;

CREATE VIEW 15만원이상 AS
SELECT Title
FROM Musical
WHERE Price > 150000;