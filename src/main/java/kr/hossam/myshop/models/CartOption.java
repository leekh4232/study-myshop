package kr.hossam.myshop.models;

import java.util.Map;
import lombok.Data;

@Data
public class CartOption {
    private long optionId;
    private int price;
    private int quantity;

    Map<String, String> optionValues;
}