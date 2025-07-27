package kr.hossam.myshop.schedulers;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.hossam.myshop.helpers.FileHelper;
import kr.hossam.myshop.models.Member;
import kr.hossam.myshop.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 스케쥴러 샘플 클래스
 * 각 메서드가 정해진 스케쥴에 따라 자동 실행된다.
 *
 * 메인클래스 "프로그램명Application.java" 파일의 상단에 "@EnableScheduling"이 추가되어야 한다.
 */
@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class AccountScheduler {

    /** 업로드 된 파일이 저장될 경로 (application.properties로부터 읽어옴) */
    // --> import org.springframework.beans.factory.annotation.Value;
    @Value("${upload.dir}")
    private String uploadDir;

    /** 썸네일 이미지의 가로 크기 */
    @Value("${thumbnail.width}")
    private int thumbnailWidth;

    /** 썸네일 이미지의 세로 크기 */
    @Value("${thumbnail.height}")
    private int thumbnailHeight;

    /** 파일 처리 헬퍼 */
    private final FileHelper fileHelper;

    /** 회원 서비스 */
    private final MemberService memberService;

    //@Scheduled(cron = "0 0 4 * * ?") // 매일 오전 4시에 자동 실행
    @Scheduled(cron = "15/45 * * * * ?") // 매 분마다 15초에 실행
    //@Scheduled(cron = "0 0/30 * * * ?") // 30분마다 실행(0분,30분)
    public void processOutMembers() throws Exception {
        log.debug("탈퇴 회원 정리 시작");

        List<Member> outMembers = null;

        try {
            log.debug("탈퇴 회원 조회 및 삭제");
            outMembers = memberService.processOutMembers();
        } catch (Exception e) {
            log.error("탈퇴 회원 조회 및 삭제 실패", e);
            return;
        }

        if (outMembers == null || outMembers.size() == 0) {
            log.debug("탈퇴 대상 없음 :)");
            return;
        }

        for (int i=0; i<outMembers.size(); i++) {
            Member m = outMembers.get(i);
            fileHelper.deleteFile(m.getPhoto());
        }
    }
}