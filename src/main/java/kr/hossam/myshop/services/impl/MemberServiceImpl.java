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
@RequiredArgsConstructor    // <-- 생성자 주입을 위한 어노테이션
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
}