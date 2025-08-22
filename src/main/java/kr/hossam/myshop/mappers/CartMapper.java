package kr.hossam.myshop.mappers;

import kr.hossam.myshop.models.Cart;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CartMapper {

    @Insert("INSERT INTO carts (session_id, member_id, product_id, options) "
            + "VALUES (#{sessionId}, #{memberId}, #{productId}, #{optionJson})")
    public int insertCart(Cart cart) throws Exception;

    @Select("SELECT id, session_id, member_id, product_id, options, reg_date, edit_date FROM carts WHERE id=#{id}")
    @Results(id="selectCartItemById", value = {
            @Result(property = "optionJson", column = "options")
    })
    public Cart selectCartItemById(int id) throws Exception;
}