package kr.hossam.myshop.services;

import java.util.List;
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

    /**
     * 회원 탈퇴를 처리한다.
     * 입력된 비밀번호가 일치하는 경우에만 탈퇴 처리를 수행하며,
     * is_out 값을 'Y'로 변경하고 edit_date를 현재 시간으로 설정한다.
     *
     * @param input - 회원 정보 (회원 ID, 비밀번호)
     * @throws Exception - 예외 발생 시
     */
    public void out(Member input) throws Exception;

    /**
     * 탈퇴한 회원들의 정보를 조회한다.
     * is_out 값이 'Y'인 회원들 중에서 탈퇴일시가 현재 시각을 기준으로 1분 이전인 회원들의 정보를 조회한다.
     * 실제 서비스 개발시에는 3개월에 해당하는 시간을 설정하는 등 사이트 정책에 따라 달라져야 한다.
     *
     * @return List<Member> - 탈퇴한 회원들의 정보 리스트
     * @throws Exception - 예외 발생 시
     */
    public List<Member> processOutMembers() throws Exception;
}