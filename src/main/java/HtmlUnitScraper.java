import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
//import jdk.javadoc.internal.doclets.formats.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.w3c.dom.html.HTMLDivElement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.StringBuilder;


import java.sql.*;
import java.util.stream.DoubleStream;

public class HtmlUnitScraper {


    public void start(int hour, int time, int hour1, int time1, String company, String type) throws IOException, SQLException, JRException {

        String url = "https://tarifasgasluz.com/comparador/precio-kwh";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        HtmlPage page = webClient.getPage(url);

        int hours [] = new int [24];
        System.out.println(page.getTitleText());



        List<HtmlTable> dates = page.getByXPath("//table[contains(@class, 'table')]");
        final HtmlTable price = dates.get(0);
      //  System.out.println(price.asXml());
        System.out.println("Cell (1,2)=" + price.getCellAt(1,2));

       List<HtmlTableRow> rows = new ArrayList<HtmlTableRow>();
       ArrayList<Double> prices = new ArrayList<Double>();


        for (final HtmlTableBody body : price.getBodies()) {
            //List<HtmlTableRow> rows = body.getRows();
            rows.add(body.getRows().get(0));

           // System.out.println(body.getRows().get(0).getCells().get(1).getTextContent());

        }
        System.out.println(price.getBodies().get(0).getRows().size());
        for (int i = 0; i<price.getBodies().get(0).getRows().size(); i++) {
            prices.add(Double.parseDouble(price.getBodies().get(0).getRows().get(i).getCells().get(1).getTextContent().replaceFirst(" €/kWh", "").replaceFirst(",",".")));

        }
            //System.out.println(body.getRows().get(0).getCells().get(1).getTextContent());

        for (Double dinero : prices) {
            System.out.println(dinero.toString());
        }




//ENDESA


        final HtmlPage pa = webClient.getPage("https://www.endesa.com/es/blog/blog-de-endesa/luz/cuanto-cuesta-electricidad");
        System.out.println(pa.getTitleText());
        List<HtmlSpan> date = pa.getByXPath("//span[contains(@class, 'spanRow')]");
        System.out.println(date.size());

        System.out.println( date.get(4).getTextContent().replaceFirst("€/kW", "").replaceFirst(",","."));

        double endesa [] = new double[3];

        System.out.println("ENDESA");
        StringBuilder stringBuilder[] = new StringBuilder[3];

        for(int i = 3; i<6; i++){
            stringBuilder[i-3] = new StringBuilder(date.get(i).getTextContent().replaceFirst("€/kW", "").replaceFirst("h", "").replaceFirst(",","."));
            stringBuilder[i-3].deleteCharAt(stringBuilder[i-3].length()-1);
            stringBuilder[i-3].deleteCharAt(8);
            endesa[i-3] = Double.parseDouble(stringBuilder[i-3].toString());
            System.out.println(endesa[i-3]);
        }


        double [] energie = naturEnergie();
        double [] naturgy = naturgy();
        FinalCalculator calculator = new FinalCalculator(prices, endesa, energie, naturgy, hour, time, hour1, time1, company, type);
        calculator.calculate();





        //GENERATING PDF


      //  Path currentRelativePath = Paths.get("");
    /*    String userDirectory = System.getProperty("user.dir");
        System.out.println(userDirectory);
        //assertTrue(userDirectory.endsWith(CURRENT_DIR));

        //String fileNameJrxml = "D:\\Erasmushogeschool\\Asignaturas\\JavaAdvanced\\EnergyConsumption\\jasperreports-6.18.1-project\\consumption_calculator\\energyconsumption.jrxml";
        //String fileNamePdf = "D:\\Erasmushogeschool\\Asignaturas\\JavaAdvanced\\EnergyConsumption\\jasperreports-6.18.1-project\\consumption_calculator\\Hello.pdf";

        String fileNameJrxml = userDirectory + "/energyconsumption.jrxml";
        String fileNamePdf = userDirectory + "/Hello.pdf";



        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/electricityconsumption","root","WebIntegrationProject");
//here sonoo is database name, root is username and password
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from emp");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
            con.close();
        }catch(Exception e){ System.out.println(e);}



        Connection helper = DriverManager.getConnection("jdbc:mysql://localhost:3306/electricityconsumption","root","WebIntegrationProject");



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

*/

    }

    public double[] naturgy() throws IOException {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage pa = webClient.getPage("https://tarifaluzhora.es/companias/naturgy/tarifas");
        System.out.println(pa.getTitleText());
        List<HtmlDivision> date = pa.getByXPath("//div[contains(@class, 'col-lg-12 var')]");
        System.out.println(date.size());
        double naturgy [] = new double[3];
        for (int i = 3; i<6; i++){
            naturgy[i-3] = Double.parseDouble(date.get(i).getTextContent().replace(" €/kWh", ""));
            System.out.println(naturgy[i-3]);
        }
        return naturgy;
        }

        public  double[] naturEnergie() throws IOException {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            final HtmlPage pa = webClient.getPage("https://comparadorluz.com/companias/totalenergies/tarifas");
            System.out.println(pa.getTitleText());
            List<HtmlTable> date = pa.getByXPath("//table[contains(@class, 'table table--bordered')]");
            System.out.println(date.size());

            System.out.println(date.get(3).getRows().get(2).getTextContent());
            double totalenergies [] = new double[3];
            for (int i = 3; i<= 5; i++){
                totalenergies[i -3] = Double.parseDouble(date.get(3).getRows().get(i).getCells().get(1).getTextContent().replace(" €/kWh", ""));
            }
            return totalenergies;
        }
        }


