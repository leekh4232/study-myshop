package kr.hossam.myshop.mappers;

import kr.hossam.myshop.models.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    /**
     * 검색 조건과 일치하는 회원 수를 조회한다.
     *
     * @param input - 회원 정보
     * @return int - 검색 조건에 일치하는 회원 수
     */
    @Select("<script>"
            + "SELECT COUNT(*) FROM members"
            + "<where>"
            + "<if test='userId != null'>user_id = #{userId}</if>"
            + "<if test='email != null'>email = #{email}</if>"
            // 회원정보 수정시 내 정보는 제외하고 중복검사 수행
            + "<if test='id != 0'>AND id != #{id}</if>"
            + "</where>"
            + "</script>")
    public int selectCount(Member input);

    /**
     * 회원 정보를 등록한다.
     * 회원가입시 입력한 비밀번호는 MD5 해시로 암호화하여 저장한다.
     *
     * @param input - 회원 정보
     * @return int - 등록된 회원의 일련번호(PK)
     */
    @Insert("INSERT INTO members ("
            + "user_id, user_pw, user_name, email, phone, birthday, gender, postcode, addr1, addr2,"
            + "photo, is_out, is_admin, login_date, reg_date, edit_date"
            + ") VALUES ("
            + "#{userId}, MD5(#{userPw}), #{userName}, #{email}, #{phone}, #{birthday}, #{gender}, #{postcode},"
            + "#{addr1}, #{addr2}, #{photo}, 'N', 'N', NULL, NOW(), NOW()"
            + ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insert(Member input);

    /**
     * 회원 정보를 조회한다.
     * 회원의 일련번호를 기준으로 회원 정보를 조회한다.
     *
     * @param input - 회원 정보
     * @return Member - 조회된 회원 정보
     */
    @Select("SELECT "
            + "id, user_id, user_pw, user_name, email, phone, birthday, gender, "
            + "postcode, addr1, addr2, photo, is_out, is_admin, login_date, reg_date, edit_date "
            + "FROM members "
            + "WHERE id = #{id}")
    // 조회 결과를 resultMap이라는 이름의 Member 객체로 매핑한다.
    // application.properties에 정의된 옵션에 따라 자동 맵핑된다.
    @Results(id = "resultMap")
    public Member selectItem(Member input);


    /**
     * 아이디와 비밀번호가 일치하는 회원 정보를 조회한다.
     * 로그인 시 사용되며, 비밀번호는 MD5 해시로 암호화되어 비교된다.
     *
     * @param input - 회원 정보
     * @return Member - 조회된 회원 정보를 포함하는 객체
     */
    @Select("SELECT "
            + "id, user_id, user_pw, user_name, email, phone, birthday, gender, "
            + "postcode, addr1, addr2, photo, is_out, is_admin, login_date, reg_date, edit_date "
            + "FROM members "
            + "WHERE user_id = #{userId} AND user_pw = MD5(#{userPw}) AND is_out='N'")
    @ResultMap("resultMap")
    public Member login(Member input);

    /**
     * 회원의 마지막 로그인 날짜를 현재 시간으로 업데이트한다.
     * 로그인 성공 시 호출되어 로그인 시간을 갱신한다.
     *
     * @param input - 회원 정보
     * @return int - 수정된 행의 수
     */
    @Update("UPDATE members SET login_date=NOW() WHERE id = #{id} AND is_out='N'")
    public int updateLoginDate(Member input);

    /**
     * 이름과 이메일 주소가 일치하는 회원 아이디를 검색한다.
     * @param input - 회원 정보
     * @return Member - 조회된 회원 아이디를 포함하는 객체
     */
    @Select("SELECT user_id FROM members "
            + "WHERE user_name = #{userName} AND email = #{email} AND is_out='N'")
    @ResultMap("resultMap")
    public Member findId(Member input);

    /**
     * 아이디와 이메일 주소가 일치하는 회원의 비밀번호를 초기화한다.
     *
     * @param input - 회원 정보
     * @return int - 수정된 행의 수
     */
    @Update("UPDATE members SET user_pw = MD5(#{userPw}) "
            + "WHERE user_id = #{userId} AND email = #{email} AND is_out='N'")
    public int resetPw(Member input);

    /**
     * 회원 탈퇴를 처리한다.
     * 입력한 비밀번호가 일치하는 경우에만 탈퇴 처리를 수행하며, is_out 값을 'Y'로 변경하고 edit_date를 현재 시간으로 설정한다.
     *
     * @param input - 회원 정보
     * @return int - 수정된 행의 수
     */
    @Update("UPDATE members "
            + "SET is_out='Y', edit_date=NOW() "
            + "WHERE id = #{id} AND user_pw = MD5(#{userPw}) AND is_out = 'N'")
    public int out(Member input);

    /**
     * 탈퇴한 회원의 프로필 사진을 조회한다.
     * 탈퇴한 회원 중에서 edit_date가 현재 시간으로부터 1분 이전인 회원의 사진만 조회한다.
     * 실제 개발시에는 1분 이전 회원이 아닌 3개월 이전의 회원을 조회해야 함(사이트 정책마다 다를 수 있음)
     *
     * @return List<Member> - 탈퇴한 회원의 프로필 사진 목록
     */
    @Select("SELECT photo FROM members "
            + "WHERE is_out='Y' AND  "
            + "edit_date < DATE_ADD(NOW(), interval -1 minute) AND "
            + "photo IS NOT NULL")
    @ResultMap("resultMap")
    public List<Member> selectOutMembersPhoto();

    /**
     * 탈퇴한 회원 정보를 삭제한다.
     * 탈퇴한 회원 중에서 edit_date가 현재 시간으로부터 1분 이전인 회원의 정보를 삭제한다.
     * 실제 개발시에는 1분 이전 회원이 아닌 3개월 이전의 회원을 조회해야 함(사이트 정책마다 다를 수 있음)
     *
     * @return int - 삭제된 행의 수
     */
    @Delete("DELETE FROM members "
            + "WHERE is_out='Y' AND  "
            + "edit_date < DATE_ADD(NOW(), interval -1 minute)")
    public int deleteOutMembers();

    /**
     * 회원 정보를 수정한다.
     * 회원정보 수정시 입력한 비밀번호는 MD5 해시로 암호화하여 저장한다.
     * 신규 비밀번호가 입력된 경우에만 비밀번호를 수정한다.
     *
     * @param input - 회원 정보
     * @return int - 수정된 행의 수
     */
    @Update("<script>"
            + "UPDATE members SET "
            // 아이디는 수정하지 않는다.
            + "user_name = #{userName},"
            // 신규 비밀번호가 입력 된 경우만 UPDATE절에 추가함
            + "<if test='newUserPw != null and newUserPw != \"\"'>user_pw = MD5(#{newUserPw}),</if>"
            + "email = #{email},"
            + "phone = #{phone},"
            + "birthday = #{birthday},"
            + "gender = #{gender},"
            + "postcode = #{postcode},"
            + "addr1 = #{addr1},"
            + "addr2 = #{addr2},"
            + "photo = #{photo},"
            + "edit_date = NOW()"
            // 세션의 일련번호와 입력한 비밀번호가 일치할 경우만 수정
            + "WHERE id = #{id} AND user_pw = MD5(#{userPw})"
            + "</script>")
    public int update(Member input);
}