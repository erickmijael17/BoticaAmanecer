package pe.edu.upeu.boticaamanecer.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.boticaamanecer.modelo.VentaDetalle;
import pe.edu.upeu.boticaamanecer.servicio.VentaDetalleService;

@Component
public class VerVentas {

    @FXML
    private TableView<VentaDetalle> tableviewVentas;

    @Autowired
    private VentaDetalleService ventaDetalleService;

    private ObservableList<VentaDetalle> ventasList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabla();
        listarVentas();
    }

    private void configurarTabla() {
        // Configuración de las columnas de la tabla
        TableColumn<VentaDetalle, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colProducto.setPrefWidth(150);

        TableColumn<VentaDetalle, Double> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidad.setPrefWidth(100);

        TableColumn<VentaDetalle, Double> colPrecioUnitario = new TableColumn<>("Precio Unitario");
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("pu"));
        colPrecioUnitario.setPrefWidth(120);

        TableColumn<VentaDetalle, Double> colDescuento = new TableColumn<>("Descuento");
        colDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));
        colDescuento.setPrefWidth(100);

        TableColumn<VentaDetalle, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colSubtotal.setPrefWidth(120);

        // Agregar las columnas a la tabla
        tableviewVentas.getColumns().addAll(colProducto, colCantidad, colPrecioUnitario, colDescuento, colSubtotal);
        tableviewVentas.setItems(ventasList);
    }

    private void listarVentas() {
        ventasList.clear();
        // Cargar los datos de los detalles de ventas desde el servicio
        ventasList.addAll(ventaDetalleService.list());
    }
}

