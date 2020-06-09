/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pantallaPrincipal;

import tablaProductos.*;
import ferreteriamain.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 *
 * @author Nicolas
 */
public class FXMLControllerPantallaPrincipal implements Initializable {

    @FXML
    private Button agregarProducto;

    @FXML
    private Button verListaProductos;

    @FXML
    private void abrirPantallaAgregarProducto(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(FXMLDocumentController.class.getResource("FXMLPantallaIngresarProducto.fxml"));

        Scene scene = new Scene(root);

        root.setId("pantallaAgregarProducto");

        String css = FerreteriaMain.class.getResource("prueba.css").toExternalForm();

        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void abrirPantallaListaProductos(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(FXMLControllerPantallaListaProductos.class.getResource("FXMLPantallaListaProductos.fxml"));

        Scene scene = new Scene(root);

        root.setId("pantallaListaProducto");

        String css = FerreteriaMain.class.getResource("prueba.css").toExternalForm();

        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
