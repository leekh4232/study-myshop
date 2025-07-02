# 데이터베이스가 없다면 생성
CREATE DATABASE IF NOT EXISTS `myshop` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

# 사용자 계정 생성하기
# --> create user '아이디'@'접근허용호스트' identified by '비밀번호';
# 일반적으로 프로그램이름 == 데이터베이스이름 == 사용자이름 으로 통일한다.
create user 'myshop'@'localhost' identified by '1234';

# 생성된 사용자에 대한 권한 부여
# --> grant all privileges on 데이터베이스이름.* to '아이디'@'접근허용호스트';
grant all privileges on myshop.* to 'myshop'@'localhost';