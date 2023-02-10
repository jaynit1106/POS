package com.increff.pos.helper;

import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportHelper {
    @Autowired
    private static BrandService brandService;
    public static void salesReportsCheck(SalesReportForm salesReportForm) throws ApiException {
        Instant range = Instant.now().minus(Period.ofDays(365));
        Instant startDate = salesReportForm.getStartDate();
        Instant endDate = salesReportForm.getEndDate();

        int comparedValue = startDate.compareTo(endDate);
        if(comparedValue>0){throw new ApiException("Invalid Time Period");}

        comparedValue = range.compareTo(startDate);
        if(comparedValue>0){throw new ApiException("Only one year is allowed for the reports");}

        comparedValue = Instant.now().compareTo(endDate);
        if(comparedValue<-1){throw new ApiException("Invalid Time Period");}
    }


}
