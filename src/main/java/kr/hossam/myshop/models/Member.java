package kr.hossam.myshop.models;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// 세션에 저장할 클래스 타입은 Serializable 인터페이스를 상속해야 한다.
// Serializable : "객체직렬화"
@Data
public class Member implements Serializable {
    private int id; // 일련번호
    private String userId; // 아이디
    private String userPw; // 비밀번호(암호화저장)
    private String userName; // 회원이름
    private String email; // 이메일
    private String phone; // 연락처
    private String birthday; // 생년월일
    private String gender; // 성별(M=남자,F=여자)
    private String postcode; // 우편번호
    private String addr1; // 검색된 주소
    private String addr2; // 나머지 주소
    private String photo; // 프로필사진 정보{json=UploadItem}
    private String isOut; // 탈퇴여부(Y/N)
    private String isAdmin; // 관리자 여부(Y/N)
    private String loginDate; // 마지막 로그인 일시
    private String regDate; // 등록일시
    private String editDate; // 변경일시

    private String newUserPw;   // 회원정보 수정에서 사용할 신규 비밀번호

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}