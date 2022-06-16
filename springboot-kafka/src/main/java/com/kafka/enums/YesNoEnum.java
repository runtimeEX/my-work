package com.kafka.enums;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.enums
 * @Description: TODO
 * @date Date : 2021年05月28日 下午2:58
 */
public enum YesNoEnum {
    /**
     * 是
     */
    YES(1, "是"),
    /**
     * 否
     */
    NO(0, "否");

    private Integer code;
    private String value;

    YesNoEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static void main(String[] args) {
        List<Object> collect = Stream.of(YesNoEnum.values()).map(new Function<YesNoEnum, Object>() {
            @Override
            public Object apply(YesNoEnum yesNoEnum) {
                return yesNoEnum.getValue();
            }
        }).collect(Collectors.toList());
        System.out.println(collect);
    }
}
