package com.rabbit.seckill.mapper;

import com.rabbit.seckill.model.OrderRecord;
import org.apache.ibatis.annotations.Param;

public interface OrderRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRecord record);

    int insertSelective(OrderRecord record);

    OrderRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRecord record);

    int updateByPrimaryKey(OrderRecord record);

    int updateByToken(@Param("tokenNo") String token , @Param("statu") String statu);
}