package pe.edu.upeu.boticaamanecer.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pe.edu.upeu.boticaamanecer.componente.StageManager;
import pe.edu.upeu.boticaamanecer.componente.Toast;
import pe.edu.upeu.boticaamanecer.dto.SessionManager;
import pe.edu.upeu.boticaamanecer.modelo.Usuario;
import pe.edu.upeu.boticaamanecer.servicio.UsuarioService;

import java.io.IOException;

@Component
public class LoginController {

    @Autowired
    UsuarioService us;

    @Autowired
    private ApplicationContext context;

    @FXML
    TextField txtUsuario;
    @FXML
    PasswordField txtClave;
    @FXML
    Button btnIngresar;



    @FXML
    public void login(ActionEvent event) throws IOException {
        System.out.println("llego al inicio");
        try {
            Usuario usu=us.loginUsuario(txtUsuario.getText(),
                    new String(txtClave.getText()));
            System.out.println("llego al inicio 1");

            if (usu!=null) {

                SessionManager.getInstance().setUserId(usu.getIdUsuario());
                SessionManager.getInstance().setUserName(usu.getUser());
                SessionManager.getInstance().setNombrePerfil(usu.getIdPerfil().getNombre());
                FXMLLoader loader=null;
                try {
                    System.out.println("llego al inicio 2");
                    loader = new FXMLLoader(getClass().getResource("/view/guimainfx.fxml"));
                }catch (Exception x) {
                    System.out.println("llego al inicio 3"+x.getMessage());
                }

                loader.setControllerFactory(context::getBean);
                Parent mainRoot = loader.load();
                System.out.println("llego aqui 1");
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getBounds();
                Scene mainScene = new Scene(mainRoot,bounds.getWidth(), bounds.getHeight()-30);
                mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResource("/img/store.png").toExternalForm()));
                stage.setScene(mainScene);
                stage.setTitle("Pantalla Principal");
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setResizable(true);
                StageManager.setPrimaryStage(stage);
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
                System.out.println("llego aqui 2");
                stage.show();
            } else {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double with=stage.getWidth()*2;
                double h=stage.getHeight()/2;
                System.out.println(with + " h:"+h);
                Toast.showToast(stage, "Credencial invalido!! intente nuevamente", 2000, with, h);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        } }
}
