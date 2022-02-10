import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;


import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLListElement;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.w3c.dom.html.HTMLDivElement;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {

    public static void main(String[] args) throws IOException {


        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainScene.fxml"));
        stage.setTitle("hello");
        stage.setScene(new Scene(root, 800, 500));

        stage.show();
    }
}