/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ferreteriamain;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Nicolas
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label labelProductoAgregado;
    
    @FXML
    private TextField campoCodigo;
    
    @FXML
    private TextField campoDescripcion;
      
    @FXML
    private TextField campoPrecio;
    
    @FXML
    private Button botonAgregarProductoNuevo;
    
    @FXML
    private Label labelCodigo, labelDescripcion, labelPrecio;
    
    
    
    
    @FXML
    public void agregarProducto(){
        
     
        try{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        session.beginTransaction();
        
        if(campoCodigo.getText() == null || campoCodigo.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Alguno de los campos esta vacio, volver intentar");
            return;
        }
        
        else if(campoDescripcion.getText() == null || campoDescripcion.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Alguno de los campos esta vacio, volver intentar");
            return;
        }
         
        else  if(campoPrecio.getText() == null || campoPrecio.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Alguno de los campos esta vacio, volver intentar");
            return;
        }
        
        ProductoConformado productoConformado = new ProductoConformado(campoCodigo.getText(), campoDescripcion.getText(), campoPrecio.getText());
        
        
        Object productoConformadoObject = session.save(productoConformado);
        
        session.getTransaction().commit();
        
        
        
        
        
        }catch(HibernateException e){
            JOptionPane.showMessageDialog(null, "Debe completar todos los campos antes de pulsar el boton");
        }
        
        campoCodigo.setText("");
        campoDescripcion.setText("");
        campoPrecio.setText("");
        
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelCodigo.setId("label");
        labelDescripcion.setId("label");
        labelPrecio.setId("label");
    }    
    
}
