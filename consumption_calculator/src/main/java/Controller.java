
import com.gargoylesoftware.htmlunit.html.HtmlCaption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller {

    @FXML private TextField timesWash;
    @FXML private TextField hourWash;
    @FXML private ChoiceBox<String> time;
    @FXML private ChoiceBox<String> hour;
    @FXML private ChoiceBox<String> time1;
    @FXML private ChoiceBox<String> hour1;
    @FXML private ChoiceBox<String> companies;
    @FXML private ChoiceBox<String> exportType;
    @FXML private Button pdf;
    @FXML private Button html;


    ObservableList<String> hours = FXCollections.observableArrayList(
            "00", "01", "02", "03", "04", "05", "06","07", "08", "09", "10", "11", "12", "13","14", "15", "16", "17", "18", "19", "20","21", "22", "23");


    ObservableList<String> times = FXCollections.observableArrayList(
            "0", "1", "2", "3", "4" );

    ObservableList<String> company = FXCollections.observableArrayList(
            "Mercado regulado","Endesa", "Naturgy", "TotalEnergies", "Repsol", "Iberdrola"
    );

    ObservableList<String> types = FXCollections.observableArrayList(
            "PDF","HTML"
            , "XLSX"
    );


    @FXML private void initialize(){
        hour.setItems(hours);
        time.setItems(times);
        companies.setItems(company);
        hour1.setItems(hours);
        time1.setItems(times);
        exportType.setItems(types);
    }

    @FXML
    public void pdfButton(ActionEvent event) throws IOException, SQLException, JRException {
        System.out.println("Button clicked");
        String hoursWashing = hour.getValue();
        String timesWashing = time.getValue();
       // System.out.println(hoursWashing);


        boolean missing = false;

        if (hoursWashing == null) {
            missing = true;
        } else System.out.println(Integer.valueOf(hoursWashing));
        if (timesWashing == null) {
            missing = true;
        } else System.out.println(Integer.valueOf(timesWashing));
        if(hour1.getValue() == null)
            missing = true;
        else System.out.println(Integer.valueOf(hour1.getValue()));
        if(time1.getValue() == null)
            missing = true;
        else System.out.println(Integer.valueOf(time1.getValue()));
        if(companies.getValue() == null) {
            missing = true;
        }if(exportType.getValue() ==null){
            missing = true;
        }


        if(missing == false) {
            System.out.println("Scraper creted");
            HtmlUnitScraper scraper = new HtmlUnitScraper();
            scraper.start(Integer.valueOf(hoursWashing), Integer.valueOf(timesWashing), Integer.valueOf(hour1.getValue()), Integer.valueOf(time1.getValue()), companies.getValue(),exportType.getValue().toString() );
        }
    }

}

