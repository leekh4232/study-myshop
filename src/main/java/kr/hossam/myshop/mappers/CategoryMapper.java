package kr.hossam.myshop.mappers;

import kr.hossam.myshop.models.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT id, name, reg_date, edit_date FROM categories ORDER BY id ASC")
    @Results(id = "resultMap")
    public List<Category> getAllCategories();

}