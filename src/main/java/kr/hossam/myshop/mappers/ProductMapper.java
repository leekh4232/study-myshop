/**
 *  /src/main/java/kr/hossam/myshop/mappers/ProductMapper.java
 */
package kr.hossam.myshop.mappers;

import kr.hossam.myshop.models.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("<script>"
            + "SELECT id, name, price, discount, summary, image_url FROM products "
            + "<where>"
            + "<if test='name != null and name != \"\"'> name LIKE concat('%', #{name}, '%')</if>"
            + "</where>"
            + "ORDER BY id DESC"     // 정렬 기준: 등록 역순
            + "<if test='listCount > 0'>LIMIT #{offset}, #{listCount}</if>"
            + "</script>")
    @Results(id = "getProducts")
    public List<Product> getProducts(Product input);

    @Select("<script>"
            + "SELECT COUNT(*) FROM products "
            + "<where>"
            + "<if test='name != null and name != \"\"'> name LIKE concat('%', #{name}, '%')</if>"
            + "</where>"
            + "</script>")
    @Results(id = "getProductCount")
    public int getProductCount(Product input);

    @Select("<script>"
            + "SELECT p.id,  p.`name`, p.price, p.discount, p.summary, p.image_url, "
            + "p.product_url, p.reg_date, p.edit_date, "
            + "c.id AS category_id, c.`name` AS category_name "
            + "FROM products AS p "
            + "INNER JOIN product_categories AS pc ON p.id = pc.product_id "
            + "INNER JOIN categories AS c ON pc.category_id = c.id "
            + "<where>"
            + "<if test='categoryId > 0'> AND pc.category_id = #{categoryId} </if>"
            + "<if test='name != null and name != \"\"'> AND p.name LIKE concat('%', #{name}, '%')</if>"
            + "</where>"
            + "ORDER BY id DESC "     // 정렬 기준: 등록 역순
            + "<if test='listCount > 0'>LIMIT #{offset}, #{listCount}</if>"
            + "</script>")
    @Results(id = "getProductsByCategory")
    public List<Product> getProductsByCategory(Product input);

    @Select("<script>"
            + "SELECT COUNT(*) "
            + "FROM products AS p "
            + "INNER JOIN product_categories AS pc ON p.id = pc.product_id "
            + "INNER JOIN categories AS c ON pc.category_id = c.id "
            + "<where>"
            + "<if test='categoryId > 0'> AND pc.category_id = #{categoryId} </if>"
            + "<if test='name != null and name != \"\"'> AND p.name LIKE concat('%', #{name}, '%')</if>"
            + "</where>"
            + "</script>")
    @Results(id = "getProductCountByCategory")
    public int getProductCountByCategory(Product input);
}