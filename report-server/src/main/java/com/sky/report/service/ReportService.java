package com.sky.report.service;

import com.sky.report.domain.vo.OrderReportVO;
import com.sky.report.domain.vo.SalesReportVO;
import com.sky.report.domain.vo.TurnoverReportVO;
import com.sky.report.domain.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    SalesReportVO Sales(LocalDate begin, LocalDate end);

    void export(LocalDate begin, LocalDate end, HttpServletResponse response);
}