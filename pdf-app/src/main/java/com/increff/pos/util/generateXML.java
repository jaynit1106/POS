package com.increff.pos.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.increff.pos.model.OrderItemData;
public class generateXML {

    public static void createXml(List<OrderItemData> list) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("invoice-record");
        doc.appendChild(rootElement);

        Element array = doc.createElement("line-item-records");
        rootElement.appendChild(array);
        int orderid = list.get(0).getOrderId();
        int id=1;
        double totalCost = 0;
        for(OrderItemData data : list) {
            Element item = doc.createElement("line-item-record");
            array.appendChild(item);

            Element sid = doc.createElement("id");
            sid.appendChild(doc.createTextNode(String.valueOf(id)));
            item.appendChild(sid);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(data.getName()));
            item.appendChild(name);

            Element barcode = doc.createElement("barcode");
            barcode.appendChild(doc.createTextNode(data.getBarcode()));
            item.appendChild(barcode);

            Element quantity = doc.createElement("quantity");
            quantity.appendChild(doc.createTextNode(String.valueOf(data.getQuantity())));
            item.appendChild(quantity);

            Element price = doc.createElement("price");
            price.appendChild(doc.createTextNode(String.valueOf(data.getSellingPrice())));
            item.appendChild(price);

            Element total = doc.createElement("total");
            total.appendChild(doc.createTextNode(String.format("%.1f",data.getQuantity()*data.getSellingPrice())));
            item.appendChild(total);

            totalCost+=data.getQuantity()*data.getSellingPrice();
            id++;
        }

        Element costs = doc.createElement("totalCost");
        costs.appendChild(doc.createTextNode(String.valueOf(totalCost)));
        rootElement.appendChild(costs);


        Element orderId = doc.createElement("orderId");
        orderId.appendChild(doc.createTextNode(String.valueOf(orderid)));
        rootElement.appendChild(orderId);

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss z");
        Date date = new Date();
        formatDate.setTimeZone(TimeZone.getTimeZone("IST"));

        Element dates = doc.createElement("dates");
        dates.appendChild(doc.createTextNode(formatDate.format(date).substring(0,10)));
        rootElement.appendChild(dates);

        Element times = doc.createElement("times");
        times.appendChild(doc.createTextNode(formatDate.format(date).substring(11,20)));
        rootElement.appendChild(times);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        String path = new File("./src/main/resources/com/increff/pos/invoiceTemplate "+String.valueOf(list.get(0).getOrderId())+".xml").getAbsolutePath();
        StreamResult result = new StreamResult(new File(path).toURI()
                .getPath());
        transformer.transform(source, result);
        System.out.println("File saved!");

    }

}
