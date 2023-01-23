package com.increff.pos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

public class generatePdf {
	public static void createPdf() {
		try {
			File xmlfile = new File("C:\\Increff Project\\POS\\src\\main\\resources\\com\\increff\\pos\\template.xml");
			File xslfile = new File("C:\\Increff Project\\POS\\src\\main\\resources\\com\\increff\\pos\\template.xsl");
			File pdfDir = new File("C:\\Increff Project\\POS\\src\\main\\resources\\com\\increff\\pos\\pdf");
			pdfDir.mkdirs();
			File pdfFile = new File(pdfDir,"salesReport.pdf");
			final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
			OutputStream out = new FileOutputStream(pdfFile);
			out = new java.io.BufferedOutputStream(out);
			
			try {
				Fop fop;
				fop = fopFactory.newFop(MimeConstants.MIME_PDF,foUserAgent,out);
				
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(new StreamSource(xslfile));
				
				Source src = new StreamSource(xmlfile);
				
				Result res = new SAXResult(fop.getDefaultHandler());
				
				transformer.transform(src, res);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				out.close();
			}
		} catch (Exception e) {
			
			
			// TODO: handle exception
		}
	}

}
