package com.fssy.shareholder.management.tools.common;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QinHui
 * @title: YearMonthNumberTool
 * @description: 年月编号工具
 * @date 2021/10/20
 */
@Component
public class YearMonthNumberTool
{

    /**
     * 根据年月生成编号
     *
     * @param prefix 编号的前缀，如采购P，供应商发货GYFH，收货SH
     * @param serial 当前序列号
     * @return
     */
    public Map<String, Object> generateBillNumberByYearMonth(String prefix, Integer serial)
    {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Map<String, Object> result = new HashMap<>();
        Integer year = Integer.parseInt(date.substring(0, 4));
        Integer month = Integer.parseInt(date.substring(4, 6));
        Integer day = Integer.parseInt(date.substring(6, 8));
        String billNumber = prefix + date + String.format("%05d", serial);
        result.put("year", year);
        result.put("month", month);
        result.put("day", day);
        result.put("serial", serial);
        result.put("billNumber", billNumber);

        return result;

    }


}
