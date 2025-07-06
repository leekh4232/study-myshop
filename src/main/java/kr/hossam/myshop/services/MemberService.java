package kr.hossam.myshop.services;

import kr.hossam.myshop.models.Member;

public interface MemberService {

    /**
     * 회원 아이디가 중복되는지 확인한다.
     * 중복된 경우 예외를 발생시킨다.
     *
     * @param input - 회원 정보
     * @throws Exception - 중복된 아이디가 있는 경우 예외 발생
     */
    public void isUniqueUserId(Member input) throws Exception;

    /**
     * 회원 이메일이 중복되는지 확인한다.
     * 중복된 경우 예외를 발생시킨다.
     *
     * @param input - 회원 정보
     * @throws Exception - 중복된 이메일이 있는 경우 예외 발생
     */
    public void isUniqueEmail(Member input) throws Exception;

    /**
     * 회원 가입을 처리한다.
     * 입력된 회원 정보를 바탕으로 회원 정보를 등록하고, 등록된 회원 정보를 반환한다.
     *
     * @param input - 회원 정보
     * @return Member - 등록된 회원 정보
     * @throws Exception - 예외 발생 시
     */
    public Member join(Member input) throws Exception;

    /**
     * 로그인을 수행한다.
     * 아이디와 비밀번호가 일치하는 회원의 정보를 조회하고, 조회 결과가 있을 경우 마지막 로그인 시각을 업데이트한다.
     *
     * @param input - 회원 정보
     * @return Member - 조회된 회원 정보
     * @throws Exception - 예외 발생 시
     */
    public Member login(Member input) throws Exception;

    /**
     * 입력된 정보(이름, 이메일)를 바탕으로 회원 아이디를 조회한다.
     *
     * @param input         - 회원 정보 (이름, 이메일)
     * @return
     * @throws Exception
     */
    public Member findId(Member input) throws Exception;

    /**
     * 입력된 회원 정보를 바탕으로 비밀번호를 재설정한다.
     * 비밀번호는 MD5 해시로 암호화하여 저장한다.
     *
     * @param input - 회원 정보 (아이디, 새 비밀번호)
     * @throws Exception
     */
    public void resetPw(Member input) throws Exception;
}