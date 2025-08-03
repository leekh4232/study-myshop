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
    @Select("SELECT id, name, price, discount, summary, image_url FROM products "
            + "ORDER BY id DESC"     // 정렬 기준: 등록 역순
            + " LIMIT 0, 12"         // 최근 12개 상품만 조회(임시)
            )
    @Results(id = "resultMap")
    public List<Product> getProducts(Product input);
}