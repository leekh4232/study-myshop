package kr.hossam.myshop.mappers;

import kr.hossam.myshop.models.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("<script>"
            + "SELECT id, name, parent_id, sort, reg_date, edit_date FROM categories "
            + "<where>"
            // parentId가 0이 아닌 경우에는 1depth 카테고리만 조회
            + "<if test='parentId == 0'>parent_id is null</if>"
            // parentId가 0이 아닌 경우에는 해당 parentId를 가진 카테고리만 조회
            + "<if test='parentId != 0'>parent_id = #{parentId}</if>"
            + "</where>"
            + "ORDER BY sort ASC"   // 정렬 기준: sort 컬럼 오름차순
            + "</script>")
    @Results(id = "resultMap")
    public List<Category> getAllCategories(Category input);
}