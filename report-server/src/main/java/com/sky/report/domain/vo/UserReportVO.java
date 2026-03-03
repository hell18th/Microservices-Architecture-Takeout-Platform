package com.sky.report.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportVO {
    private String dateList;
    private String totalUserList;
    private String newUserList;
}