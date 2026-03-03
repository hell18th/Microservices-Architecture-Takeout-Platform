package com.sky.report.service.impl;

import com.sky.api.client.OrderClient;
import com.sky.api.client.UserClient;
import com.sky.api.dto.SalesDTO;
import com.sky.report.domain.vo.*;
import com.sky.report.service.ReportService;
import com.sky.report.service.WorkSpaceService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 根据起始和结束日期生成日期列表
     */
    private List<LocalDate> generateDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();
        while (!begin.isAfter(end)) {
            list.add(begin);
            begin = begin.plusDays(1);
        }
        return list;
    }

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 构造日期集合
        List<LocalDate> list = generateDateList(begin, end);
        String dateList = String.join(",", list.stream().map(LocalDate::toString).toList());
        // 查询每天的营业额
        List<Double> doubleList = list.stream().map(localDate -> {
            LocalDateTime dayBegin = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(localDate, LocalTime.MAX);
            Double turnover = orderClient.getTurnover(dayBegin, dayEnd).getData();
//            Double turnover = orderMapper.selectTurnoverByDate(dayBegin, dayEnd, Orders.COMPLETED);
            return turnover == null ? 0.0 : turnover;
        }).toList();
        String turnoverList = String.join(",", doubleList.stream().map(String::valueOf).toList());
        return new TurnoverReportVO(dateList, turnoverList);
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        // 构造日期集合
        List<LocalDate> list = generateDateList(begin, end);
        String dateList = String.join(",", list.stream().map(LocalDate::toString).toList());
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate localDate : list) {
            LocalDateTime dayBegin = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(localDate, LocalTime.MAX);
            totalUserList.add(userClient.selectByDate(null, dayEnd).getData());
            newUserList.add(userClient.selectByDate(dayBegin, dayEnd).getData());
        }
        String totalUserListStr = String.join(",", totalUserList.stream().map(String::valueOf).toList());
        String newUserListStr = String.join(",", newUserList.stream().map(String::valueOf).toList());
        return new UserReportVO(dateList, totalUserListStr, newUserListStr);
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        // 构造日期集合
        List<LocalDate> list = generateDateList(begin, end);
        List<Integer> totalOrderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate localDate : list) {
            LocalDateTime dayBegin = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(localDate, LocalTime.MAX);
            Integer totalOrderCount = orderClient.countAll(dayBegin, dayEnd).getData();
            Integer validOrderCount = orderClient.countCOMPLETED(dayBegin, dayEnd).getData();
            totalOrderCountList.add(totalOrderCount);
            validOrderCountList.add(validOrderCount);
        }
        String dateList = String.join(",", list.stream().map(LocalDate::toString).toList());
        String totalOrderCountListStr = String.join(",", totalOrderCountList.stream().map(String::valueOf).toList());
        String validOrderCountListStr = String.join(",", validOrderCountList.stream().map(String::valueOf).toList());
        Integer totalOrderCount = totalOrderCountList.stream().reduce(0, Integer::sum);
        Integer validOrderCount = validOrderCountList.stream().reduce(0, Integer::sum);
        Double orderCompleteRate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount * 100;
        return new OrderReportVO(dateList, totalOrderCountListStr, validOrderCountListStr, orderCompleteRate, totalOrderCount, validOrderCount);
    }

    @Override
    public SalesReportVO Sales(LocalDate begin, LocalDate end) {
        LocalDateTime dayBegin = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime dayEnd = LocalDateTime.of(end, LocalTime.MAX);
//        List<SalesDTO> salesDTOList = orderDetailMapper.getSales(dayBegin, dayEnd, Orders.COMPLETED);
        List<SalesDTO> salesDTOList = orderClient.getSales(dayBegin, dayEnd).getData();
        List<String> nameList = salesDTOList.stream().map(SalesDTO::getName).toList();
        String nameListStr = String.join(",", nameList);
        List<Integer> numberList = salesDTOList.stream().map(SalesDTO::getNumber).toList();
        String numberListStr = String.join(",", numberList.stream().map(String::valueOf).toList());
        return new SalesReportVO(nameListStr, numberListStr);
    }

    @Override
    public void export(LocalDate begin, LocalDate end, HttpServletResponse response) {
        TodayDataVO todayData = workSpaceService.getTodayData(begin, end);
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx"); XSSFWorkbook sheets = inputStream != null ? new XSSFWorkbook(inputStream) : null) {
            if (sheets != null) {
                XSSFSheet sheet = sheets.getSheet("info");
                sheet.getRow(1).getCell(0).setCellValue("时间：" + begin + "至" + end);
                sheet.getRow(3).getCell(1).setCellValue("￥" + todayData.getTurnover());
                sheet.getRow(3).getCell(3).setCellValue(todayData.getOrderCompletionRate() + "%");
                sheet.getRow(3).getCell(5).setCellValue(todayData.getNewUsers());
                sheet.getRow(4).getCell(1).setCellValue(todayData.getValidOrderCount());
                sheet.getRow(4).getCell(3).setCellValue("￥" + todayData.getUnitPrice());
                for (int i = 0; !begin.isAfter(end); begin = begin.plusDays(1), i++) {
                    int rowNum = 7 + i;
                    TodayDataVO data = workSpaceService.getTodayData(begin, begin);
                    XSSFRow row = sheet.createRow(rowNum);
                    row.createCell(0).setCellValue(begin.toString());
                    row.createCell(1).setCellValue("￥" + data.getTurnover());
                    row.createCell(2).setCellValue(data.getValidOrderCount());
                    row.createCell(3).setCellValue(data.getOrderCompletionRate() + "%");
                    row.createCell(4).setCellValue("￥" + data.getUnitPrice());
                    row.createCell(5).setCellValue(data.getNewUsers());
                }
                sheets.write(response.getOutputStream());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}