/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ferreteriamain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nicolas
 */
public class ProductoConformado {
    
   private IntegerProperty idProducto = new SimpleIntegerProperty();
   private StringProperty codigo = new SimpleStringProperty();
   private StringProperty descripcion = new SimpleStringProperty();
   private StringProperty precio = new SimpleStringProperty();

    public ProductoConformado() {
    }
    
    
    

    public ProductoConformado(String codigo, String descripcion, String precio) {
        
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleStringProperty(precio);
        
    }

    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getPrecio() {
        return precio.get();
    }

    public void setPrecio(String precio) {
        this.precio.set(precio);
    }

    public int getIdProducto() {
        return idProducto.get();
    }

    public void setIdProducto(int idProducto) {
        this.idProducto.set(idProducto);
    }
    
    
    
     public IntegerProperty propertyIdProducto(){
        return idProducto;
    }
    
    
    public StringProperty propertyCodigo(){
        return codigo;
    }
    
    public StringProperty propertyDescripcion(){
        return descripcion;
    }
    
    public StringProperty propertyPrecio(){
        return precio;
    }
    
    
    
    
    
}
