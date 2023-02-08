package com.increff.pos.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

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
    public static String createPdf(String name) {
        try {
            System.out.println(name);
            String xmlPath ="C:\\Increff Project\\POS\\pdf-app\\src\\main\\resources\\com\\increff\\pos\\invoiceTemplate "+name+".xml";
            String xslPath = "C:\\Increff Project\\POS\\pdf-app\\src\\main\\resources\\com\\increff\\pos\\invoiceTemplate.xsl";
            System.out.println(xslPath);
            File xmlfile = new File(xmlPath);
            File xslfile = new File(xslPath);
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
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
                    byte [] pdf = out.toByteArray();
                    String base64 = Base64.getEncoder().encodeToString(pdf);
                    return base64;
            }
        } catch (Exception e) {


            // TODO: handle exception
        }
        return "hello";
    }

}
