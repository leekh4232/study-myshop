package kr.hossam.myshop.services.impl;

import kr.hossam.myshop.exceptions.AlreadyExistsException;
import kr.hossam.myshop.exceptions.ServiceNoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import kr.hossam.myshop.mappers.MemberMapper;
import kr.hossam.myshop.models.Member;
import kr.hossam.myshop.services.MemberService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // <-- 생성자 주입을 위한 어노테이션
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    public void isUniqueUserId(Member input) throws Exception {
        if (memberMapper.selectCount(input) > 0) {
            throw new AlreadyExistsException("사용할 수 없는 아이디 입니다.");
        }
    }

    @Override
    public void isUniqueEmail(Member input) throws Exception {
        if (memberMapper.selectCount(input) > 0) {
            throw new AlreadyExistsException("사용할 수 없는 이메일 입니다.");
        }
    }


    @Override
    public Member join(Member input) throws Exception {
        // 가입 과정에서 아이디와 이메일 중복 검사를 수행
        Member temp1 = new Member();
        temp1.setUserId(input.getUserId());
        this.isUniqueUserId(temp1);

        Member temp2 = new Member();
        temp2.setEmail(input.getEmail());
        this.isUniqueEmail(temp2);

        // 회원 가입 수행 후 결과 체크
        if (memberMapper.insert(input) == 0) {
            throw new ServiceNoResultException("저장된 Member 데이터가 없습니다.");
        }

        // 가입된 회원 정보를 반환
        return memberMapper.selectItem(input);
    }

    @Override
    public Member login(Member input) throws Exception {
        // 입력된 아이디와 비밀번호로 회원 정보 조회
        Member output = memberMapper.login(input);

        // 조회된 회원 정보가 없으면 예외 발생
        if (output == null) {
            throw new ServiceNoResultException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시 마지막 로그인 일시 업데이트
        memberMapper.updateLoginDate(output);

        return output;
    }

    @Override
    public Member findId(Member input) throws Exception {
        Member output = memberMapper.findId(input);

        if (output == null) {
            throw new Exception("조회된 아이디가 없습니다.");
        }

        return output;
    }

    @Override
    public void resetPw(Member input) throws Exception {
        if (memberMapper.resetPw(input) == 0) {
            throw new Exception("아이디와 이메일을 확인하세요.");
        }
    }
}
