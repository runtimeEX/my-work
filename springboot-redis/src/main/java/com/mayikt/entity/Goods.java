package com.mayikt.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Goods implements Serializable {
    private Long id;

    private String goods_Name;

    private String goods_Title;

    private String goods_Img;

    private BigDecimal goods_Price;

    private Integer goods_Stock;

    private Date create_Date;

    private Date update_Date;

    private String goods_Detail;
}
