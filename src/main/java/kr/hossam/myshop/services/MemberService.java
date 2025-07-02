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
}