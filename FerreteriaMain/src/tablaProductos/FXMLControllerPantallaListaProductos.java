/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablaProductos;

import ferreteriamain.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javax.swing.JOptionPane;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Nicolas
 */
public class FXMLControllerPantallaListaProductos implements Initializable {

    @FXML
    private TextField filtrado;

    @FXML
    private Button botonEliminarDeLista;

    @FXML
    private Button botonCambiarPrecio;

    @FXML
    TextField campoModificarPrecio;

    @FXML
    private TableView<ProductoConformado> tablaProductos = new TableView<>();

    //aca se crea la lista observable principal que era la que ya existia, que es la que siempre vincule al tableView para poder modificarla.
    @FXML
    private ObservableList<ProductoConformado> tablaProductosObservable;

    //esta lista es una listado tambien observable que va a mostrar los datos filtrados.
    @FXML
    private ObservableList<ProductoConformado> ObservableDatosFiltrados;

    // creo las columnas como siempre, clase de la que se va a tomar el valor, y tipo de valor.
    @FXML
    private TableColumn<ProductoConformado, String> colCodigo = new TableColumn();
    @FXML
    private TableColumn<ProductoConformado, String> colDescripcion = new TableColumn();
    @FXML
    private TableColumn<ProductoConformado, String> colPrecio = new TableColumn();

    /* metodo para iniciar session y traer en forma de lista los datos de la base de datos. aca tengo una pequeña duda
    cuando creo el criteria y digo ProductoConformado.class, nose si esta tomando datos de la base o de clase.
    
     */
    public void traerDatosALaTabla() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        Criteria criteria = session.createCriteria(ProductoConformado.class);

        List<ProductoConformado> listaDeProductosCriteria = criteria.list();

        /*Este for es en definitiva el que trae los datos y los agrega a la lista observable
        y por consiguiente al tableView que esta enlazado a la lista observable*/
        for (ProductoConformado productoConformadoi : listaDeProductosCriteria) {

            tablaProductosObservable.add(new ProductoConformado(
                    productoConformadoi.getCodigo(),
                    productoConformadoi.getDescripcion(),
                    productoConformadoi.getPrecio()));

        }

        /* aca le meto a la lista observable de filtro todos los datos que contiene la lista observable maestra, con los datos principales 
        en este punto las lista observables tienen la misma informacion*/
        ObservableDatosFiltrados.addAll(tablaProductosObservable);

        /* aca agrego un escuchador de cambios a la lista maestra, entonces digo, tabla maestra, agrega un escuchador, dentro del constructor
        agrega un nuevo escuchador, que va a escuchar elementos del tipo productoConformado 
        aca surge una duda, porque en definitiva, el metodo onChanged no esta ejecutando ninguna instruccion  */
        tablaProductosObservable.addListener(new ListChangeListener<ProductoConformado>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ProductoConformado> c) {

            }
        });

    }

    /* este metodo re aplicar tabla en orden. aca tengo muchas dudas, sobre todo en el arrayListo que entre diamantes recibe 2 cosas,
    en teoria dice, es un arrayList de columnas, y las columnas son de productoConformado ?>> este ultimo signo no lo entiendo 
    podria estar diciendo la tableColumn va a recibir un atributo de la clase ProductoConformado, y esto ? dice que no sabe aun que tipo
    de atributo va a recibir si String, int, etc*/
    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<ProductoConformado, ?>> sortOrder = new ArrayList<>(tablaProductos.getSortOrder());
        tablaProductos.getSortOrder().clear();
        tablaProductos.getSortOrder().addAll(sortOrder);
    }

    /* aca con el metodo clear, borra todo lo que pueda tener la lista de datos filtrados
    luego recorre con un for, la tabla maestra con los articulos existentes, pero dentro del for crea un if y llama a su ves al metodo
    matchesFiltered o coincidencias filtradas, entonces entiendo que si productoi, que es un objeto de la lista maestra, coincide con lo que yo escribi
    en el textfield que filtra entonces, se agrega a la lista obs de filtrado el item en definitiva. al final aplica el metodo reapplyTableSortOrder o 
    re ordenar tabla en orden*/
    private void updateFilteredData() {
        ObservableDatosFiltrados.clear();

        for (ProductoConformado productoi : tablaProductosObservable) {
            if (matchesFilter(productoi)) {
                ObservableDatosFiltrados.add(productoi);
            }
        }

        // Must re-sort table after items changed
        reapplyTableSortOrder();
    }

    /* matchesFilter o coincidencias filtradas, metodo que toma lo que el Textfield que filtra contienen, lo almacena en un String
    y dice, si, el string que esta en el Textfield es nulo o esta vacio, no hay filtro toda la informacion se mantiene igual
    luego se crea un String que va a almacenar el texto puesto en el textfield buscador ya convertido en minusculas con el metodo
    toLowerCase, una vez hecho esto, se pregunta. si el producto, en este caso el codigo(tambien convertido a minusculas, para que se puedan comparar bien)
    
     */
    private boolean matchesFilter(ProductoConformado producto) {
        String filterString = filtrado.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }

        String lowerCaseFilterString = filterString.toLowerCase();

        if (producto.getCodigo().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (producto.getDescripcion().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false; // Does not match
    }

    int posicionSeleccionada;

    @FXML
    public void eliminarDeListaDeProductos() {

        try {

            String codigo = tablaProductos.getSelectionModel().getSelectedItems().get(posicionSeleccionada).getCodigo();
            String descripcion = tablaProductos.getSelectionModel().getSelectedItems().get(posicionSeleccionada).getDescripcion();
            String precio = tablaProductos.getSelectionModel().getSelectedItems().get(posicionSeleccionada).getPrecio();

            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            ProductoConformado productoObtenidoDeLaTabla = new ProductoConformado(codigo, descripcion, precio);

            Criteria criteria = session.createCriteria(ProductoConformado.class);

            List<ProductoConformado> listaDeProductosConformados = criteria.list();

            for (ProductoConformado productosConformadoi : listaDeProductosConformados) {

                if (productosConformadoi.getCodigo().equals(productoObtenidoDeLaTabla.getCodigo())) {

                    session.delete(productosConformadoi);

                }

            }

            session.getTransaction().commit();

            tablaProductos.getItems().remove(tablaProductos.getSelectionModel().getSelectedIndex());

            tablaProductosObservable.removeAll(tablaProductosObservable);
            ObservableDatosFiltrados.removeAll(ObservableDatosFiltrados);

            session.beginTransaction();

            Criteria criteriaRefrescar = session.createCriteria(ProductoConformado.class);

            List<ProductoConformado> listaDeProductosCriteria = criteriaRefrescar.list();

            /*Este for es en definitiva el que trae los datos y los agrega a la lista observable
        y por consiguiente al tableView que esta enlazado a la lista observable*/
            for (ProductoConformado productoConformadoi : listaDeProductosCriteria) {

                tablaProductosObservable.add(new ProductoConformado(
                        productoConformadoi.getCodigo(),
                        productoConformadoi.getDescripcion(),
                        productoConformadoi.getPrecio()));

            }

            /* aca le meto a la lista observable de filtro todos los datos que contiene la lista observable maestra, con los datos principales 
        en este punto las lista observables tienen la misma informacion*/
            ObservableDatosFiltrados.addAll(tablaProductosObservable);

            /* aca agrego un escuchador de cambios a la lista maestra, entonces digo, tabla maestra, agrega un escuchador, dentro del constructor
        agrega un nuevo escuchador, que va a escuchar elementos del tipo productoConformado 
        aca surge una duda, porque en definitiva, el metodo onChanged no esta ejecutando ninguna instruccion  */
            tablaProductosObservable.addListener(new ListChangeListener<ProductoConformado>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends ProductoConformado> c) {

                }
            });

        } catch (HibernateException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }

    }

    @FXML
    public void cambiarPrecioProducto() {

        String codigo = tablaProductos.getSelectionModel().getSelectedItems().get(posicionSeleccionada).getCodigo();
        String descripcion = tablaProductos.getSelectionModel().getSelectedItems().get(posicionSeleccionada).getDescripcion();
        String precio = tablaProductos.getSelectionModel().getSelectedItems().get(posicionSeleccionada).getPrecio();

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(ProductoConformado.class);

        List<ProductoConformado> listaDeProductosConformados = criteria.list();

        for (ProductoConformado productosConformadoi : listaDeProductosConformados) {

            if (productosConformadoi.getCodigo().equals(codigo) || productosConformadoi.getDescripcion().equals(descripcion)) {

                productosConformadoi.setPrecio(campoModificarPrecio.getText());

                session.update(productosConformadoi);

            }

        }

        campoModificarPrecio.setText("");

        session.getTransaction().commit();

        tablaProductosObservable.removeAll(tablaProductosObservable);
        ObservableDatosFiltrados.removeAll(ObservableDatosFiltrados);

        session.beginTransaction();

        Criteria criteriaRefrescar = session.createCriteria(ProductoConformado.class);

        List<ProductoConformado> listaDeProductosCriteria = criteriaRefrescar.list();

        /*Este for es en definitiva el que trae los datos y los agrega a la lista observable
        y por consiguiente al tableView que esta enlazado a la lista observable*/
        for (ProductoConformado productoConformadoi : listaDeProductosCriteria) {

            tablaProductosObservable.add(new ProductoConformado(
                    productoConformadoi.getCodigo(),
                    productoConformadoi.getDescripcion(),
                    productoConformadoi.getPrecio()));

        }

        /* aca le meto a la lista observable de filtro todos los datos que contiene la lista observable maestra, con los datos principales 
        en este punto las lista observables tienen la misma informacion*/
        ObservableDatosFiltrados.addAll(tablaProductosObservable);

        /* aca agrego un escuchador de cambios a la lista maestra, entonces digo, tabla maestra, agrega un escuchador, dentro del constructor
        agrega un nuevo escuchador, que va a escuchar elementos del tipo productoConformado 
        aca surge una duda, porque en definitiva, el metodo onChanged no esta ejecutando ninguna instruccion  */
        tablaProductosObservable.addListener(new ListChangeListener<ProductoConformado>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ProductoConformado> c) {

            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablaProductosObservable = FXCollections.observableArrayList();

        ObservableDatosFiltrados = FXCollections.observableArrayList();

        tablaProductos.setItems(tablaProductosObservable);
        tablaProductos.setItems(ObservableDatosFiltrados);
        tablaProductos.setEditable(true);

        filtrado.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateFilteredData();
            }
        });

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        traerDatosALaTabla();

    }

}
