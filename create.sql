/*
    Database 생성하기
*/
CREATE DATABASE DB2022Team12;
USE DB2022Team12;

/*
    Table 생성하기
*/
/*
    유저 정보 테이블
    id: 유저 아이디 (PK)
    pw: 유저 비밀번호
    name: 유저 이름
    phone: 유저 전화번호
    email: 유저 이메일 주소
*/
CREATE TABLE member(
    id VARCHAR(20) PRIMARY KEY,
    pw VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    phone char(13) NOT NULL,
    email VARCHAR(20) NOT NULL,
    address varchar(100)
);

/*
    극장 정보 테이블
    name: 극장 이름 (PK)
    address: 극장 주소
    phone: 극장 전화번호
    size: 극장 규모 (좌석 수)
    극장 이름(name)으로 인덱스 추가
*/
CREATE TABLE theater(
    name VARCHAR(20) PRIMARY KEY,
    address VARCHAR(100) NOT NULL,
    phone CHAR(12) NOT NULL,
    size INT NOT NULL,
    INDEX theater_name_idx(name) 
);

/*
    공연 정보 테이블
    title: 공연 제목 (PK)
    theater_name: 상영 극장 (theater 테이블 참조)
    price: 티켓 예매가
    remain_seat: 남은 좌석 수
    summary: 공연 줄거리
    공연 제목(title)에 인덱스 추가
*/
CREATE TABLE musical(
    title VARCHAR(20) PRIMARY KEY,
    theater_name VARCHAR(20) NOT NULL,
    price INT NOT NULL,
    remain_seat INT NOT NULL,
    summary VARCHAR(100),
    FOREIGN KEY (theater_name) REFERENCES theater(name),
    INDEX musical_title_idx(title)
);

/*
    공연 날짜 정보 테이블
    id: 날짜 정보 식별자 (PK)
    title: 공연 제목 (musical 테이블 참조)
    date: 공연 날짜
    time: 공연 시각
    (title, date, time)은 중복될 수 없음 (세 컬럼 중 하나라도 달라야 함)
*/
CREATE TABLE musical_date(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(20),
    date DATE,
    time TIME,
    FOREIGN KEY (title) REFERENCES musical(title),
    UNIQUE (title, date, time)
);

/*
    예매 티켓 정보 테이블
    id: 예매 티켓 식별자 (PK)
    musical_title: 공연 제목 (musical 테이블 참조)
    musical_date: 공연 날짜 (musical_date 테이블 참조)
    member_id: 예매한 유저 (member 테이블 참조)
    order_date: 티켓 예매 날짜
    예매한 유저(member_id)에 인덱스 추가
*/
CREATE TABLE ticket(
    id INT AUTO_INCREMENT PRIMARY KEY,
    musical_title VARCHAR(20) NOT NULL,
    musical_date INT NOT NULL,
    member_id VARCHAR(20) NOT NULL,
    order_date DATE NOT NULL,
    FOREIGN KEY (musical_title) REFERENCES musical(title),
    FOREIGN KEY (musical_date) REFERENCES musical_date(id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    INDEX ticket_member_idx(member_id)
);

/*
    리뷰 정보 테이블
    id: 리뷰 식별자 (PK)
    musical_title: 공연 제목 (musical 테이블 참조)
    member_id: 리뷰 작성 유저 (member 테이블 참조)
    rate: 평점
    written_at: 리뷰 작성 날짜
    리뷰 작성 유저(member_id)에 인덱스 추가
*/
CREATE TABLE review(
    id INT AUTO_INCREMENT PRIMARY KEY,
    musical_title VARCHAR(20) NOT NULL,
    member_id VARCHAR(20) NOT NULL,
    rate TINYINT NOT NULL,
    written_at DATE NOT NULL,
    FOREIGN KEY (musical_title) REFERENCES musical(title),
    FOREIGN KEY (member_id) REFERENCES member(id),
    INDEX review_member_idx(member_id)
);

/*
    tuple 삽입하기
*/
-- insert Member
-- INSERT INTO Member values(id, pw, name, phone, email, address);
INSERT INTO member VALUES
    ('ga2059', '710hyeon', '권가현', '010-8975-3244', 'ga2059@naver.com', NULL),
    ('min1905', 'ah710', '권민아', '010-6556-8536', 'min1905@naver.com','서울특별시 서대문구 이화여대길 52'),
    ('19soo10', 'min710', '김수민', '010-8451-2994', '19soo10@gmail.com',NULL),
    ('1684yoon', '150ah', '김윤아',  '010-6987-3257', '1684yoon@gmail.com','서울특별시 서대문구 대현동 11-1'),
    ('liu1771', '710siqi', '유은기', '010-2348-6582', 'liu1771@gmail.com',NULL);

-- insert Theater
-- INSERT INTO Theater values(Name, Address, Phone, Size);
INSERT INTO theater VALUES
    ('충무아트센터', '서울 중구 퇴계로 387', '02-2230-6600', 1250),
    ('드림아트센터', '서울특별시 종로구 동숭길 123 드림아트센터', '02-3481-8843', 210),
    ('예술의전당', '서울 서초구 남부순환로 2406', '02-5800-1300', 2505),
    ('블루스퀘어', '서울 용산구 이태원로 294', '02-1544-1591', 1766),
    ('샤롯데씨어터', '서울 송파구 올림픽로 240', '02-1644-0078', 1241);

-- insert Musical
-- INSERT INTO Musical values(Title, Theater_name, Price, Remain_seat, Summary);
INSERT INTO musical VALUES
    ('킹키부츠', '충무아트센터', 160000, 1250, '폐업 위기에 처한 구두 공장을 물려 받게 된 찰리는 생각도 스타일도 전혀 다른 아름답고 유쾌한 남자 롤라를 만나 새로운 영감을 얻고 도전을 시작한다.'),
    ('비더슈탄트', '드림아트센터', 66000, 210, '1938년 독일, 엘리트 스포츠 학교에 입학한 펜싱부 소년들의 권력에 대한 저항과 우정을 그리며, 생각없이 시대에 순응하며 사는 위험을 경고하며, 반항의 진정한 가치를 돌아본다.'),
    ('데스노트', '예술의전당', 130000, 2505, '법과 정의에 대해 고민하던 천재 고등학생 야가미 라이토는 어느날 우연히 데스노트를 줍게 된다. 노트의 원래 주인인 사신들이 지켜보는 가운데 라이토는 세상의 신이 되고자한다.'),
    ('아이다', '블루스퀘어', 120000, 1766, '고대 왕국의 여왕이었던 암네리스가 이집트와 그 이웃 나라였던 누비아 사이의 전쟁이 최고조에 달했던 시대의 투쟁과 그 안에서 꽃피었던 사랑 이야기로 관객들을 초대한다.'),
    ('마타하리', '샤롯데씨어터', 90000, 1241, '파리 박물관에서 희대의 스파이, 마타하리의 머리가 공개될 예정이었다. 하지만 그녀의 머리는 흔적없이 사라졌고, 한 노인이 알 수 없는 이야기를 한다. 그녀는 어떤 사람이었을까?');

-- insert Musical_date
-- INSERT INTO Musical_date values(title, date, time);
INSERT INTO musical_date(title, date, time) VALUES
    ('킹키부츠', '2022-07-01', '14:00:00'),
    ('킹키부츠', '2022-07-02', '19:00:00'),
    ('비더슈탄트', '2022-06-30', '20:00:00'),
    ('비더슈탄트', '2022-07-01', '19:00:00'),
    ('데스노트', '2022-07-02', '14:00:00'),
    ('데스노트', '2022-07-03', '14:00:00'),
    ('아이다', '2022-06-15', '14:00:00'),
    ('아이다', '2022-06-15', '20:00:00'),
    ('마타하리', '2022-06-14', '19:00:00'),
    ('마타하리', '2022-06-15', '19:00:00');

-- insert Ticket
-- INSERT INTO Ticket values(musical_title, musical_date, member_id, order_date);
INSERT INTO ticket(musical_title, musical_date, member_id, order_date) VALUES
    ('킹키부츠', 1, 'ga2059', '2022-05-28'),
    ('킹키부츠', 2, '19soo10', '2022-05-31'),
    ('데스노트', 5, '19soo10', '2022-06-02'),
    ('아이다', 7, '1684yoon', '2022-06-02'),
    ('아이다', 7, 'liu1771', '2022-06-04');

-- insert Review
-- INSERT INTO Review values(musical_title, member_id, rate, written_at);
INSERT INTO review(musical_title, member_id, rate, written_at) VALUES
    ('킹키부츠', 'ga2059', 4, '2022-07-02'),
    ('킹키부츠', 'min1905', 3, '2022-07-02'),
    ('비더슈탄트', '19soo10', 5, '2022-06-30'),
    ('비더슈탄트', 'min1905', 2, '2022-07-02'),
    ('아이다', '1684yoon', 4, '2022-06-17'),
    ('마타하리', '1684yoon',  3, '2022-06-17');

/*
    view 생성하기
*/
-- 1. 각 뮤지컬의 평균 별점
CREATE VIEW avg_rate AS
SELECT musical.title, avg(review.rate) as score
FROM musical, review
WHERE musical.title = review.musical_title
GROUP BY musical.title;

-- 2. 가격대 별 뮤지컬 이름
-- 티켓 예매가가 10만원 미만인 뮤지컬 목록
CREATE VIEW under_10 AS
SELECT DISTINCT title
FROM musical
WHERE price < 100000;

-- 티켓 예매가가 10만원 ~ 15만원인 뮤지컬 목록
CREATE VIEW between10_15 AS
SELECT DISTINCT title
FROM musical
WHERE price BETWEEN 100000 AND 150000;

-- 티켓 예매가가 15만원 초과인 뮤지컬 목록
CREATE VIEW over_15 AS
SELECT DISTINCT title
FROM musical
WHERE price > 150000;
