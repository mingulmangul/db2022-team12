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
CREATE TABLE db2022_member(
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
CREATE TABLE db2022_theater(
    name VARCHAR(20) PRIMARY KEY,
    address VARCHAR(100) NOT NULL,
    phone CHAR(12) NOT NULL,
    size INT NOT NULL,
    INDEX theater_name_idx(name) 
);

/*
    공연 정보 테이블
    title: 공연 제목 (PK)
    theater_name: 상영 극장 (db2022_theater 테이블 참조)
    price: 티켓 예매가
    summary: 공연 줄거리
    공연 제목(title)에 인덱스 추가
*/
CREATE TABLE db2022_musical(
    title CHAR(10) PRIMARY KEY,
    theater_name VARCHAR(20) NOT NULL,
    price INT NOT NULL,
    summary CHAR(100),
    FOREIGN KEY (theater_name) REFERENCES db2022_theater(name),
    INDEX musical_title_idx(title)
);

/*
    공연 회차 정보 테이블
    id: 공연 회차 정보 식별자 (PK)
    title: 공연 제목 (db2022_musical 테이블 참조)
    date: 공연 날짜
    time: 공연 시각
    remain_seat: 해당 회차의 남은 좌석 수
    (title, date, time)은 중복될 수 없음 (세 컬럼 중 하나라도 달라야 함)
*/
CREATE TABLE db2022_musical_schedule(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title CHAR(10) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    remain_seat INT NOT NULL,
    FOREIGN KEY (title) REFERENCES db2022_musical(title),
    UNIQUE (title, date, time)
);

/*
    예매 티켓 정보 테이블
    id: 예매 티켓 식별자 (PK)
    musical_title: 공연 제목 (db2022_musical 테이블 참조)
    musical_schedule: 공연 날짜 (musical_schedule 테이블 참조)
    member_id: 예매한 유저 (member 테이블 참조)
    order_date: 티켓 예매 날짜
    예매한 유저(member_id)에 인덱스 추가
*/
CREATE TABLE db2022_ticket(
    id INT AUTO_INCREMENT PRIMARY KEY,
    musical_title CHAR(10) NOT NULL,
    musical_schedule INT NOT NULL,
    member_id VARCHAR(20) NOT NULL,
    order_date DATE NOT NULL,
    FOREIGN KEY (musical_title) REFERENCES db2022_musical(title),
    FOREIGN KEY (musical_schedule) REFERENCES db2022_musical_schedule(id),
    FOREIGN KEY (member_id) REFERENCES db2022_member(id),
    INDEX ticket_member_idx(member_id)
);

/*
    리뷰 정보 테이블
    id: 리뷰 식별자 (PK)
    musical_title: 공연 제목 (db2022_musical 테이블 참조)
    member_id: 리뷰 작성 유저 (db2022_member 테이블 참조)
    rate: 평점
    written_at: 리뷰 작성 날짜
    (musical_title, member_id)는 중복될 수 없음 (=> 유저는 하나의 공연에 하나의 리뷰만 남길 수 있음)
    리뷰 작성 유저(member_id)에 인덱스 추가
*/
CREATE TABLE db2022_review(
    id INT AUTO_INCREMENT PRIMARY KEY,
    musical_title CHAR(10) NOT NULL,
    member_id VARCHAR(20) NOT NULL,
    rate TINYINT NOT NULL,
    written_at DATE NOT NULL,
    FOREIGN KEY (musical_title) REFERENCES db2022_musical(title),
    FOREIGN KEY (member_id) REFERENCES db2022_member(id),
    UNIQUE (musical_title, member_id),
    INDEX review_member_idx(member_id)
);

/*
    tuple 삽입하기
*/
-- insert db2022_member
-- INSERT INTO db2022_member values(id, pw, name, phone, email, address);
INSERT INTO db2022_member VALUES
    ('ga2059', '710hyeon', '권가현', '010-8975-3244', 'ga2059@naver.com', NULL),
    ('min1905', 'ah710', '권민아', '010-6556-8536', 'min1905@naver.com','서울특별시 서대문구 이화여대길 52'),
    ('19soo10', 'min710', '김수민', '010-8451-2994', '19soo10@gmail.com',NULL);

-- insert db2022_theater
-- INSERT INTO db2022_theater values(Name, Address, Phone, Size);
INSERT INTO db2022_theater VALUES
    ('충무아트센터', '서울 중구 퇴계로 387', '02-2230-6600', 1250),
    ('드림아트센터', '서울특별시 종로구 동숭길 123 드림아트센터', '02-3481-8843', 210),
    ('블루스퀘어', '서울 용산구 이태원로 294', '02-1544-1591', 1766),
    ('샤롯데씨어터', '서울 송파구 올림픽로 240', '02-1644-0078', 1241),
    ('예스24스테이지', '서울 종로구 대학로12길 21', '02-742-9637', 301);

-- insert db2022_musical
-- INSERT INTO db2022_musical values(Title, Theater_name, Price, Summary);
INSERT INTO db2022_musical VALUES
    ('킹키부츠', '충무아트센터', 160000, '폐업 위기에 처한 구두 공장을 물려 받게 된 찰리는 생각도 스타일도 전혀 다른 아름답고 유쾌한 남자 롤라를 만나 새로운 영감을 얻고 도전을 시작한다.'),
    ('비더슈탄트', '드림아트센터', 66000, '1938년 독일, 엘리트 스포츠 학교에 입학한 펜싱부 소년들의 권력에 대한 저항과 우정을 그리며, 생각없이 시대에 순응하며 사는 위험을 경고하며, 반항의 진정한 가치를 돌아본다.'),
    ('데스노트', '충무아트센터', 150000, '법과 정의에 대해 고민하던 천재 고등학생 야가미 라이토는 어느날 우연히 데스노트를 줍게 된다. 노트의 원래 주인인 사신들이 지켜보는 가운데 라이토는 세상의 신이 되고자한다.'),
    ('아이다', '블루스퀘어', 120000, '고대 왕국의 여왕이었던 암네리스가 이집트와 그 이웃 나라였던 누비아 사이의 전쟁이 최고조에 달했던 시대의 투쟁과 그 안에서 꽃피었던 사랑 이야기로 관객들을 초대한다.'),
    ('마타하리', '샤롯데씨어터', 90000, '파리 박물관에서 희대의 스파이, 마타하리의 머리가 공개될 예정이었다. 하지만 그녀의 머리는 흔적없이 사라졌고, 한 노인이 알 수 없는 이야기를 한다. 그녀는 어떤 사람이었을까?'),
    ('최후진술', '예스24스테이지', 66000, '1633년 갈릴레오 갈릴레이는 지동설을 지지한 이단이라는 명목으로 종교재판을 받게 된다. 그는 생의 마지막 여행길에 오르고 그곳에서 뜻밖의 인물, 윌리엄 셰익스피어를 만난다.'),
    ('미아파밀리아', '예스24스테이지', 66000, '1930년대 대공황 시대 뉴욕, 가난한 이들의 삶을 위로하는 아폴로니아 인앤바는 마피아에게 넘어가 내일이면 문을 닫아야 하고 마지막 공연 브루클린 브릿지의 전설을 준비한다.'),
    ('카파이즘', '드림아트센터', 60000, '로버트 카파는 사실 두 사람이었다. 사진을 현상하는 암실, 붉은색 조명이 어둡게 비치는 곳. 한 남자가 오래전 잃어버렸던 필름 케이스를 들고 들어온다.'),
    ('은하철도의밤', '드림아트센터', 66000, '앞을 못 보는 조반니는 학업과 아르바이트를 병행하며 고된 삶을 이어간다. 7년 만에 돌아온 은하수 축제의 날, 캄파넬라가 함께 축제에 가자고 제안하고, 조반니는 거절한다.'),
    ('맨오브라만차', '충무아트센터', 150000, '라만차에 살고 있는 알론조는 기사 이야기를 너무 많이 읽은 탓에 자신이 돈키호테라는 기사라고 착각하고 시종인 산초와 모험을 찾아 떠난다.');

-- insert db2022_musical_schedule
-- INSERT INTO db2022_musical_schedule values(title, date, time, remain_seat);
INSERT INTO db2022_musical_schedule(title, date, time, remain_seat) VALUES
    ('킹키부츠', '2022-08-01', '20:00:00', 649),
    ('킹키부츠', '2022-08-02', '20:00:00', 723),
    ('비더슈탄트', '2022-07-31', '14:00:00', 140),
    ('비더슈탄트', '2022-07-31', '19:00:00', 140),
    ('비더슈탄트', '2022-08-01', '20:00:00', 295),
    ('데스노트', '2022-07-02', '14:00:00', 25),
    ('데스노트', '2022-07-02', '19:00:00', 11),
    ('데스노트', '2022-07-03', '14:00:00', 7),
    ('데스노트', '2022-07-03', '19:00:00', 2),
    ('아이다', '2022-07-23', '14:00:00', 380),
    ('아이다', '2022-07-23', '20:00:00', 572),
    ('마타하리', '2022-06-17', '19:00:00', 48),
    ('마타하리', '2022-06-18', '14:00:00', 22),
    ('마타하리', '2022-06-18', '19:00:00', 3),
    ('최후진술', '2022-06-24', '20:00:00', 29),
    ('최후진술', '2022-06-25', '14:00:00', 0),
    ('최후진술', '2022-06-25', '19:00:00', 0),
    ('최후진술', '2022-06-26', '14:00:00', 7),
    ('최후진술', '2022-06-26', '19:00:00', 13),
    ('미아파밀리아', '2022-07-02', '14:00:00', 25),
    ('미아파밀리아', '2022-07-02', '19:00:00', 35),
    ('미아파밀리아', '2022-07-03', '14:00:00', 21),
    ('미아파밀리아', '2022-07-03', '19:00:00', 19),
    ('카파이즘', '2022-07-01', '20:00:00', 62),
    ('카파이즘', '2022-07-02', '14:00:00', 31),
    ('카파이즘', '2022-07-02', '19:00:00', 24),
    ('은하철도의밤', '2022-07-06', '20:00:00', 56),
    ('은하철도의밤', '2022-07-07', '20:00:00', 46),
    ('은하철도의밤', '2022-07-08', '20:00:00', 34),
    ('맨오브라만차', '2022-07-09', '14:00:00', 2),
    ('맨오브라만차', '2022-07-09', '19:00:00', 4),
    ('맨오브라만차', '2022-07-10', '14:00:00', 18),
    ('맨오브라만차', '2022-07-10', '19:00:00', 11);

-- insert db2022_ticket
-- INSERT INTO db2022_ticket values(musical_title, musical_schedule, member_id, order_date);
INSERT INTO db2022_ticket(musical_title, musical_schedule, member_id, order_date) VALUES
    ('킹키부츠', 1, 'ga2059', '2022-05-28'),
    ('킹키부츠', 2, '19soo10', '2022-05-31'),
    ('데스노트', 5, '19soo10', '2022-06-02');

-- insert db2022_review
-- INSERT INTO db2022_review values(musical_title, member_id, rate, written_at);
INSERT INTO db2022_review(musical_title, member_id, rate, written_at) VALUES
    ('킹키부츠', 'ga2059', 4, '2022-07-02'),
    ('킹키부츠', 'min1905', 3, '2022-07-02'),
    ('킹키부츠', '19soo10', 3, '2022-07-02'),
    ('비더슈탄트', 'ga2059', 4, '2022-07-02'),
    ('비더슈탄트', '19soo10', 5, '2022-06-30'),
    ('비더슈탄트', 'min1905', 4, '2022-07-02'),
    ('데스노트', 'ga2059', 3, '2022-07-02'),
    ('데스노트', '19soo10', 4, '2022-06-30'),
    ('데스노트', 'min1905', 5, '2022-07-02'),
    ('마타하리', 'ga2059', 2, '2022-07-02'),
    ('마타하리', '19soo10', 3, '2022-06-30'),
    ('마타하리', 'min1905', 5, '2022-07-02'),
    ('최후진술', 'ga2059', 5, '2022-07-02'),
    ('최후진술', '19soo10', 4, '2022-06-30'),
    ('최후진술', 'min1905', 5, '2022-07-02'),
    ('미아파밀리아', 'ga2059', 3, '2022-07-02'),
    ('미아파밀리아', '19soo10', 3, '2022-06-30'),
    ('미아파밀리아', 'min1905', 5, '2022-07-02'),
    ('카파이즘', 'ga2059', 5, '2022-07-02'),
    ('카파이즘', '19soo10', 3, '2022-06-30'),
    ('카파이즘', 'min1905', 5, '2022-07-02'),
    ('은하철도의밤', 'ga2059', 5, '2022-07-02'),
    ('은하철도의밤', '19soo10', 4, '2022-06-30'),
    ('은하철도의밤', 'min1905', 5, '2022-07-02'),
    ('맨오브라만차', 'ga2059', 3, '2022-07-02'),
    ('맨오브라만차', '19soo10', 4, '2022-06-30'),
    ('맨오브라만차', 'min1905', 3, '2022-07-02');

/*
    view 생성하기
*/
-- 1. 각 뮤지컬의 평균 별점
CREATE VIEW db2022_avg_rate AS
SELECT db2022_musical.title, avg(db2022_review.rate) as score
FROM db2022_musical, db2022_review
WHERE db2022_musical.title = db2022_review.musical_title
GROUP BY db2022_musical.title;

-- 2. 가격대 별 뮤지컬 이름
-- 티켓 예매가가 10만원 미만인 뮤지컬 목록
CREATE VIEW db2022_under_10 AS
SELECT DISTINCT title, summary
FROM db2022_musical
WHERE price < 100000;

-- 티켓 예매가가 10만원 ~ 15만원인 뮤지컬 목록
CREATE VIEW db2022_between10_15 AS
SELECT DISTINCT title, summary
FROM db2022_musical
WHERE price BETWEEN 100000 AND 150000;

-- 티켓 예매가가 15만원 초과인 뮤지컬 목록
CREATE VIEW db2022_over_15 AS
SELECT DISTINCT title, summary
FROM db2022_musical
WHERE price > 150000;
