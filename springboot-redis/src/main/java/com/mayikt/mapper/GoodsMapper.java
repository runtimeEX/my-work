package com.mayikt.mapper;

import com.mayikt.entity.Goods;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsMapper {
    @Select("select * from goods")
    List<Goods> findAll();
}
