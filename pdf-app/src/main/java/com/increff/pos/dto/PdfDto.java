package com.increff.pos.dto;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.util.generatePdf;
import com.increff.pos.util.generateXML;
import org.springframework.stereotype.Component;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.List;

@Component
public class PdfDto {

    public String getBase64(List<OrderItemData> data) throws ParserConfigurationException, TransformerException {
        generateXML.createXml(data);
        return generatePdf.createPdf(String.valueOf(data.get(0).getOrderId()));
    }
}
