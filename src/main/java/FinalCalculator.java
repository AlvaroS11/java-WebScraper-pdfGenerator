import com.lowagie.text.pdf.codec.Base64;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import org.apache.commons.io.FileUtils;
import org.exolab.castor.dsml.Exporter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;

public class FinalCalculator {
    ArrayList<Double> precios = new ArrayList<>();
    int time;
    int hour;
    int time1;
    int hour1;
    double whasing;
    double cooking;
    double taxes;
    double total;
    String company;
    String type;
    double[] endesa;
    double[] energy;
    double[] naturgy;


    public FinalCalculator(ArrayList<Double> precios, double[] endesa, double[] energy, double[] naturgy, int hour, int time, int hour1, int time1, String company, String type) {
        this.precios = precios;
        this.hour = hour;
        this.time = time;
        this.time1 = time1;
        this.hour1 = hour1;
        this.company = company;
        this.type = type;
        this.endesa = endesa;
        this.energy = energy;
        this.naturgy = naturgy;

    }

    public void calculate() throws SQLException, JRException, IOException {
        //whashing machine
        whasing = precios.get(hour)* 2.5 * time * 4;
        System.out.println(whasing + "€ al mes");
        cooking = precios.get(hour1)* 1.5 * time1 * 30;
        System.out.println(cooking + "€ al mes");
        taxes = ((whasing + cooking) * 21) / 100;
        total = whasing + cooking + taxes;
        writeSql();
        //checkcompany();
    }
/*
    public void endesa(double data []){
        if(hour <4) {
            whasing = data[0] * 2.5 * time * 4;
            cooking = data[0] *

        }
    }
*/
    public void writeSql() throws SQLException, JRException, IOException {

        String userDirectory = System.getProperty("user.dir");
        String fileNameJrxml = userDirectory + "/energyconsumption.jrxml";
        String fileNamePdf = userDirectory + "/Hello.pdf";
        String fileNameHtml = userDirectory + "/Hello.html";
        String fileNameXls = userDirectory + "/Hello.xls";
        String fileNameExc = userDirectory + "/Hello.xlsx";

        Connection helper = DriverManager.getConnection("jdbc:mysql://localhost:3306/electricityconsumption","root","WebIntegrationProject");



        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/electricityconsumption","root","WebIntegrationProject");
//here sonoo is database name, root is username and password
            Statement stmt=con.createStatement();

    //        INSERT INTO `electricityconsumption`.`appliances` (`id`, `cost`) VALUES ('1', '30');
       //     INSERT INTO `electricityconsumption`.`appliances` (`id`, `cost`) VALUES ('2', '20');

           // PreparedStatement pstmt = con.prepareStatement("UPDATE `appliances` SET (id,washing,cooking) VALUE (?,?,?)");
            PreparedStatement pstmt = con.prepareStatement("UPDATE `appliances` SET `price` = ? WHERE (`name` = 'washing')");
            pstmt.setDouble(1, whasing);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE `appliances` SET `price` = ? WHERE (`name` = 'cooking')");
            preparedStatement.setDouble(1, cooking);
            pstmt.executeUpdate();
            preparedStatement.executeUpdate();
            PreparedStatement tax = con.prepareStatement("UPDATE `appliances` SET `price` = ? WHERE (`name` = 'taxes')");
            tax.setDouble(1, taxes);
            tax.executeUpdate();
            PreparedStatement tot = con.prepareStatement("UPDATE `appliances` SET `price` = ? WHERE (`name` = 'total')");
            tot.setDouble(1, total);
            tot.executeUpdate();



            System.out.println();
            System.out.println(endesa[0]);
            System.out.println(endesa[1]);
            System.out.println(endesa[2]);
            System.out.println();

            PreparedStatement comp = con.prepareStatement("UPDATE `companies` SET `punta` = ? WHERE (`name` = 'ENDESA')");
            comp.setDouble(1, endesa[0]);
            PreparedStatement comp1 = con.prepareStatement("UPDATE `companies` SET `llano` = ? WHERE (`name` = 'ENDESA')");
            comp1.setDouble(1, endesa[1]);
            PreparedStatement comp2 = con.prepareStatement("UPDATE `companies` SET `valle` = ? WHERE (`name` = 'ENDESA')");
            comp2.setDouble(1, endesa[2]);
            comp.executeUpdate();
            comp1.executeUpdate();
            comp2.executeUpdate();

            System.out.println();
            System.out.println("NATURGY");
            System.out.println(naturgy[0]);
            System.out.println(naturgy[1]);
            System.out.println(naturgy[2]);
            System.out.println();

            PreparedStatement comp4 = con.prepareStatement("UPDATE `companies` SET `punta` = ? WHERE (`name` = 'NATURGY')");
            comp4.setDouble(1, naturgy[0]);
            PreparedStatement comp5 = con.prepareStatement("UPDATE `companies` SET `llano` = ? WHERE (`name` = 'NATURGY')");
            comp5.setDouble(1, naturgy[1]);
            PreparedStatement comp6 = con.prepareStatement("UPDATE `companies` SET `valle` = ? WHERE (`name` = 'NATURGY')");
            comp6.setDouble(1, naturgy[2]);
            comp4.executeUpdate();
            comp5.executeUpdate();
            comp6.executeUpdate();
            System.out.println();
            System.out.println("TOTALENERGIES");
            System.out.println(energy[0]);
            System.out.println(energy[1]);
            System.out.println(energy[2]);
            System.out.println();

            PreparedStatement comp7 = con.prepareStatement("UPDATE `companies` SET `punta` = ? WHERE (`name` = 'TOTAL_ENERGIES')");
            comp7.setDouble(1, energy[0]);
            PreparedStatement comp8 = con.prepareStatement("UPDATE `companies` SET `llano` = ? WHERE (`name` = 'TOTAL_ENERGIES')");
            comp8.setDouble(1, energy[1]);
             PreparedStatement comp9 = con.prepareStatement("UPDATE `companies` SET `valle` = ? WHERE (`name` = 'TOTAL_ENERGIES')");
            comp9.setDouble(1, energy[2]);
            comp7.executeUpdate();
            comp8.executeUpdate();
            comp9.executeUpdate();

           /* System.out.println("error primero");
            PreparedStatement del = (PreparedStatement) con.prepareStatement("DELETE FROM `hour` WHERE ('id' = ?)");
            for(int i = 1;i<precios.size(); i++){
                del.setInt(1,i);
                del.executeUpdate();
            }

            System.out.println("error sergundo");

*/
           PreparedStatement del = con.prepareStatement("DELETE FROM hour");
           del.executeUpdate();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("insert into hour values(?,?,?)");

            for(int i = 1; i<precios.size(); i++){
                ps.setInt(1,i);
                ps.setString(2, i + ":00");
                ps.setDouble(3,precios.get(i));
                ps.executeUpdate();
            }




            con.close();
        }catch(Exception e){ System.out.println(e);}








        JasperExport export = new JasperExport();
        System.out.println(type);
        export.makeReport(type);
        /*

        JasperPrint print = new JasperPrint();
        try {
            System.out.println("Loading the .JRMXML file ....");
            JasperDesign jasperDesign = JRXmlLoader.load(fileNameJrxml);
            System.out.println("Compiling the .JRMXML file to .JASPER file....");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap hm = new HashMap();
            System.out.println("filling parameters to .JASPER file....");
            // JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, hm, new JREmptyDataSource());
             print =  JasperFillManager.fillReport(jasperReport, null, helper);
        } catch (JRException e) {
            e.printStackTrace();
        }
        switch (type){
            case "PDF":
                try {
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
                JasperDesign jasperDesign = JRXmlLoader.load(fileNameJrxml);
                System.out.println("Compiling the .JRMXML file to .JASPER file....");
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, helper);
                HtmlExporter exporterHTML = new HtmlExporter();
                SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                exporterHTML.setExporterInput(exporterInput);
                exporterHTML.setExporterOutput(simpleHtmlExporterOutput);

                exporterHTML.exportReport();
                FileUtils.writeByteArrayToFile(new File("hello.html"), outputStream.toByteArray());

                break;
            case "XLS":
                //JRXlsExporter exporter = new JRXlsExporter();
                //JasperExportManager.exportReportToXml(print, fileNameXls);
            //    JasperExportManager.exportReportToXmlFile(print, fileNameXls, true);
               // JasperExportManager.exportReportToXmlF



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

                }
                */
        }

        }









