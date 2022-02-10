import com.lowagie.text.pdf.codec.Base64;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class JasperExport {

    public void makeReport(String type) throws JRException, SQLException, IOException {
    String userDirectory = System.getProperty("user.dir");
    String fileNameJrxml = userDirectory + "/energyconsumption.jrxml";
    String fileNamePdf = userDirectory + "/Hello.pdf";
    String fileNameHtml = userDirectory + "/Hello.html";
    String fileNameXls = userDirectory + "/Hello.xls";
    String fileNameExc = userDirectory + "/Hello.xlsx";


    //JasperPrint print = new JasperPrint();

        Connection helper = DriverManager.getConnection("jdbc:mysql://localhost:3306/electricityconsumption","root","WebIntegrationProject");
   /* System.out.println("Loading the .JRMXML file ....");
    JasperDesign jasperDesign = JRXmlLoader.load(fileNameJrxml);
    System.out.println("Compiling the .JRMXML file to .JASPER file....");
    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
    HashMap hm = new HashMap();
    System.out.println("filling parameters to .JASPER file....");
        // JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());

    print =  JasperFillManager.fillReport(jasperReport, null, helper);
    */

        switch (type){
        case "PDF":
            try {
                    System.out.println("Loading the .JRMXML file ....");
                    JasperDesign jasperDesign = JRXmlLoader.load(fileNameJrxml);
                    System.out.println("Compiling the .JRMXML file to .JASPER file....");
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    HashMap hm = new HashMap();
                    System.out.println("filling parameters to .JASPER file....");
                    // JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
                    JasperPrint print = (JasperPrint) JasperFillManager.fillReport(jasperReport, null, helper);
                    System.out.println("exporting the JASPER file to PDF file....");
                JasperExportManager.exportReportToPdfFile(print, fileNamePdf);
                System.out.println("Successfully completed the export");

            } catch (Exception e) {
                System.out.print("Exception:" + e);
            }

            break;
        case "HTML":
             Map<String, String> images = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SimpleHtmlExporterOutput simpleHtmlExporterOutput = new SimpleHtmlExporterOutput(outputStream);
            simpleHtmlExporterOutput.setImageHandler(new HtmlResourceHandler() {

                @Override
                public void handleResource(String id, byte[] data) {
                    System.err.println("id" + id);
                    images.put(id, "data:image/jpg;base64," + Base64.encodeBytes(data));
                }

                @Override
                public String getResourcePath(String id) {
                    return images.get(id);
                }
            });

            System.out.println("Loading the .JRMXML file ....");
            JasperDesign jasperDesign1 = JRXmlLoader.load(fileNameJrxml);
            System.out.println("Compiling the .JRMXML file to .JASPER file....");
            JasperReport jasperReport1 = JasperCompileManager.compileReport(jasperDesign1);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport1, null, helper);
            System.out.println("exporting the JASPER file to HTML file....");
            HtmlExporter exporterHTML = new HtmlExporter();
            SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
            exporterHTML.setExporterInput(exporterInput);
            exporterHTML.setExporterOutput(simpleHtmlExporterOutput);

            exporterHTML.exportReport();
            FileUtils.writeByteArrayToFile(new File("hello.html"), outputStream.toByteArray());
            System.out.println("Successfully completed the export");

            break;
        case "XLSX":


            System.out.println("Loading the .JRMXML file ....");
            JasperDesign jasperDesign = JRXmlLoader.load(fileNameJrxml);
            System.out.println("Compiling the .JRMXML file to .JASPER file....");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap hm = new HashMap();
            System.out.println("filling parameters to .JASPER file....");
            // JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
            JasperPrint print = (JasperPrint) JasperFillManager.fillReport(jasperReport, null, helper);
            System.out.println("exporting the JASPER file to XLSX file....");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setIgnoreGraphics(false);
            OutputStream fileOutputStream = new FileOutputStream(fileNameExc);
            // FileUtils.writeByteArrayToFile(new File("hello.xlsx"), outputStream.toByteArray());
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            byteArrayOutputStream.writeTo(fileOutputStream);
            System.out.println("Successfully completed the export");

            break;
            default:
                System.out.println("ninguno");
    }
}
}
