package pe.edu.upeu.boticaamanecer.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.boticaamanecer.modelo.Cliente;
import pe.edu.upeu.boticaamanecer.servicio.ClienteService;

@Component
public class AgregarNuevoCliente {

    @FXML
    private TextField txtDNI, txtNombre;
    @FXML
    private ComboBox<String> Combobox;
    @FXML
    private Label mesage;
    @FXML
    private TableView<Cliente> tableview;
    @FXML
    private Button btnADD;

    @Autowired
    private ClienteService clienteService;

    private ObservableList<Cliente> clientesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabla();
        cargarTiposDocumento();
        listarClientes();

        btnADD.setOnAction(event -> agregarCliente());
    }

    private void configurarTabla() {
        TableColumn<Cliente, String> colDNI = new TableColumn<>("DNI/RUC");
        colDNI.setCellValueFactory(new PropertyValueFactory<>("dniruc"));
        colDNI.setPrefWidth(100);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombres");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colNombre.setPrefWidth(150);

        TableColumn<Cliente, String> colRepLegal = new TableColumn<>("Rep. Legal");
        colRepLegal.setCellValueFactory(new PropertyValueFactory<>("repLegal"));
        colRepLegal.setPrefWidth(150);

        TableColumn<Cliente, String> colTipoDoc = new TableColumn<>("Tipo Documento");
        colTipoDoc.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
        colTipoDoc.setPrefWidth(100);

        tableview.getColumns().addAll(colDNI, colNombre, colRepLegal, colTipoDoc);
        tableview.setItems(clientesList);
    }

    private void cargarTiposDocumento() {
        Combobox.setItems(FXCollections.observableArrayList("DNI", "RUC", "Pasaporte", "Otro"));
        Combobox.getSelectionModel().selectFirst(); // Seleccionar el primer tipo por defecto
    }

    private void listarClientes() {
        clientesList.clear();
        // Cambia obtenerTodosLosClientes() por list()
        clientesList.addAll(clienteService.list());
    }

    private void agregarCliente() {
        String dni = txtDNI.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipoDoc = Combobox.getSelectionModel().getSelectedItem();

        if (dni.isEmpty() || nombre.isEmpty() || tipoDoc == null) {
            mesage.setText("Por favor complete los campos obligatorios.");
            mesage.setStyle("-fx-text-fill: red;");
            return;
        }

        Cliente nuevoCliente = Cliente.builder()
                .dniruc(dni)
                .nombres(nombre)
                .tipoDocumento(tipoDoc)
                .build();

        try {
            // Log para verificar si se está llamando correctamente a save()
            System.out.println("Guardando cliente: " + nuevoCliente);
            clienteService.save(nuevoCliente);
            mesage.setText("Cliente agregado correctamente.");
            mesage.setStyle("-fx-text-fill: green;");
            listarClientes();
            limpiarCampos();
        } catch (Exception e) {
            mesage.setText("Error al agregar cliente: " + e.getMessage());
            mesage.setStyle("-fx-text-fill: red;");
            e.printStackTrace();  // Esto te ayudará a ver la causa exacta del error
        }
    }

    private void limpiarCampos() {
        txtDNI.clear();
        txtNombre.clear();
        Combobox.getSelectionModel().selectFirst();
    }
}
