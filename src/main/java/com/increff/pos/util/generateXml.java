package com.increff.pos.util;

import java.io.File;
import java.util.List;

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

import com.increff.pos.model.SalesReportData;
public class generateXml {
	
	public static void createXml(List<SalesReportData> list) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("invoice-record");
		doc.appendChild(rootElement);

		Element orgname = doc.createElement("line-item-records");
		rootElement.appendChild(orgname);

		for(SalesReportData data : list) {
			Element branch = doc.createElement("line-item-record");
			orgname.appendChild(branch);
			
			Element brand = doc.createElement("description");
			brand.appendChild(doc.createTextNode(data.getBrand()));
			branch.appendChild(brand);
			
			Element category = doc.createElement("vat");
			category.appendChild(doc.createTextNode(data.getCategory()));
			branch.appendChild(category);
			
			Element revenue = doc.createElement("amount");
			revenue.appendChild(doc.createTextNode(String.valueOf(data.getRevenue())));
			branch.appendChild(revenue);
		}
		
		

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("C:\\Increff Project\\POS\\src\\main\\resources\\com\\increff\\pos\\template1.xml").toURI()
						.getPath());
		transformer.transform(source, result);
		System.out.println("File saved!");
	
	}

}
