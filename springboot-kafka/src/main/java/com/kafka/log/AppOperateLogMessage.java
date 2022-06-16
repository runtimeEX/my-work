package com.kafka.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppOperateLogMessage implements Serializable {

    public static final long serialVersionUID = 1L;


    private String appName;

    private String logTitle;

    private String operateContent;

    private String operateType;

    private Long operateUserId;

    private LocalDateTime operateDate;

    private String ipAddress;

    private String className;

    private String methodName;

    private String requestUrl;

    private String requestUri;

    private Set<String> permissions;

}