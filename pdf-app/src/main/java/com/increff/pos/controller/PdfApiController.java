package com.increff.pos.controller;

import com.increff.pos.dto.PdfDto;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.List;

@Api
@RestController
public class PdfApiController {
    @Autowired
    private final PdfDto pdfDto = new PdfDto();

    @ApiOperation("return base 64 string")
    @PostMapping(path = "/api/pdf")
    public String getBase64(@RequestBody List<OrderItemData> data) throws ParserConfigurationException, TransformerException {
        return pdfDto.getBase64(data);
    }
}
