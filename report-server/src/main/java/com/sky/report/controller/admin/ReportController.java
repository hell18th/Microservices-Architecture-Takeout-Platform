package com.sky.report.controller.admin;

import com.sky.report.domain.vo.OrderReportVO;
import com.sky.report.domain.vo.SalesReportVO;
import com.sky.report.domain.vo.TurnoverReportVO;
import com.sky.report.domain.vo.UserReportVO;
import com.sky.report.service.ReportService;
import com.sky.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 数据统计
 */
@Slf4j
@RestController
@RequestMapping("/admin/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 查询营业额统计数据
     *
     * @param begin 开始日期，格式为yyyy-MM-dd
     * @param end   结束日期，格式为yyyy-MM-dd
     * @return Result<TurnoverReportVO> 包含营业额统计结果的响应对象
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询营业额数据：{} ~ {}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
    }

    /**
     * 查询用户统计数据
     * 根据指定的时间范围查询用户相关的统计信息
     *
     * @param begin 统计开始日期，格式为yyyy-MM-dd
     * @param end   统计结束日期，格式为yyyy-MM-dd
     * @return Result<UserReportVO> 包含用户统计报告数据的响应结果
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询用户数据：{} ~ {}", begin, end);
        UserReportVO userReportVO = reportService.userStatistics(begin, end);
        return Result.success(userReportVO);
    }

    /**
     * 查询订单统计信息
     * 根据指定的时间范围查询订单统计数据，并返回订单报表视图对象
     *
     * @param begin 统计开始日期，格式为yyyy-MM-dd
     * @param end   统计结束日期，格式为yyyy-MM-dd
     * @return Result<OrderReportVO> 包含订单统计结果的响应对象
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询订单数据：{} ~ {}", begin, end);
        OrderReportVO orderReportVO = reportService.ordersStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 查询销量报表数据
     * 根据指定的时间范围查询销量统计信息
     *
     * @param begin 开始日期，格式为yyyy-MM-dd
     * @param end   结束日期，格式为yyyy-MM-dd
     * @return Result<SalesReportVO> 包含销量报表数据的响应结果
     */
    @GetMapping("/Sales")
    public Result<SalesReportVO> Sales(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询销量数据：{} ~ {}", begin, end);
        SalesReportVO salesReportVO = reportService.Sales(begin, end);
        return Result.success(salesReportVO);
    }

    /**
     * 导出报表数据
     * 该方法接收开始日期和结束日期作为查询条件，将指定时间范围内的报表数据导出到HTTP响应中
     *
     * @param begin    开始日期，格式为yyyy-MM-dd，用于指定报表数据查询的时间范围起始点
     * @param end      结束日期，格式为yyyy-MM-dd，用于指定报表数据查询的时间范围结束点
     * @param response HTTP响应对象，用于将导出的报表数据写入响应流中
     */
    @GetMapping("/export")
    public void export(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end, HttpServletResponse response) {
        reportService.export(begin, end, response);
    }
}