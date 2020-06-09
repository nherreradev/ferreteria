/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ferreteriamain;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.Session;
import pantallaPrincipal.FXMLControllerPantallaPrincipal;

/**
 *
 * @author Nicolas
 */
public class FerreteriaMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(FXMLControllerPantallaPrincipal.class.getResource("FXMLPantallaPrincipal.fxml"));
        
        Scene scene = new Scene(root);
        root.setId("pantallaPrincipal");
        String css = FerreteriaMain.class.getResource("prueba.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Pantalla Principal Ferreteria");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                HibernateUtil.shutdown();
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
