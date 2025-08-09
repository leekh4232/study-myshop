/**
 *  /src/main/java/kr/hossam/myshop/mappers/ProductMapper.java
 */
package kr.hossam.myshop.mappers;

import kr.hossam.myshop.models.Product;
import kr.hossam.myshop.models.ProductOption;
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
            + "pc.id AS category_id "
            + "FROM products AS p "
            + "INNER JOIN product_categories AS pc ON p.id = pc.product_id "
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


    @Select("<script>"
            + "SELECT id, name, price, discount, summary, image_url, "
            + "delivery_info, md_comment, product_url, content, "
            + "reg_date, edit_date "
            + "FROM products "
            + "<where>"
            + "id = #{id}"
            + "</where>"
            + "</script>")
    @Results(id = "getProductById")
    public Product getProductById(Product input);

    @Select("<script>"
            + "SELECT name "
            + "FROM product_options "
            + "<where>"
            + "product_id = #{productId}"
            + "</where>"
            + "GROUP BY name "
            + "ORDER BY name ASC"
            + "</script>")
    @Results(id = "getProductOption")
    public String[] getProductOptionNames(ProductOption input);

    // 특정 상품 id에 대해 name이 특정 값인 상품 옵션 목록 조회
    @Select("<script>"
            + "SELECT id, product_id, name, value, reg_date, edit_date "
            + "FROM product_options "
            + "<where>"
            + "product_id = #{productId} "
            + "AND name = #{name}"
            + "</where>"
            + "ORDER BY value ASC"
            + "</script>")
    @Results(id = "getProductOptionsByName")
    public List<ProductOption> getProductOptionsByName(ProductOption input);
}