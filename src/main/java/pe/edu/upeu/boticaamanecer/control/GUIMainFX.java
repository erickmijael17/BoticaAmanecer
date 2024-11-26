package pe.edu.upeu.boticaamanecer.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pe.edu.upeu.boticaamanecer.dto.InactivityManager;
import pe.edu.upeu.boticaamanecer.dto.InactivityTimer;
import pe.edu.upeu.boticaamanecer.dto.MenuMenuItenTO;
import pe.edu.upeu.boticaamanecer.dto.SessionManager;
import pe.edu.upeu.boticaamanecer.servicio.MenuMenuItemDao;
import pe.edu.upeu.boticaamanecer.servicio.MenuMenuItenDaoI;
import pe.edu.upeu.boticaamanecer.utils.UtilsX;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;

@Component
public class GUIMainFX {

    @Autowired
    private ApplicationContext context;
    Preferences userPrefs = Preferences.userRoot();
    UtilsX util = new UtilsX();
    Properties myresources = new Properties();
    MenuMenuItenDaoI mmiDao;
    @FXML
    private TabPane tabPaneFx;
    List<MenuMenuItenTO> lista;
    @FXML
    private BorderPane bp;
    @FXML
    private MenuBar menuBarFx;

    private Parent parent;
    Stage stage;

    private InactivityTimer inactivityTimer;
    private InactivityManager inactivityManager;

    private void handleInactivityLogout() {
        tabPaneFx.getTabs().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);

            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setTitle("Cierre por Inactividad");
            loginStage.setResizable(false);
            loginStage.show();

            Stage currentStage = (Stage) tabPaneFx.getScene().getWindow();
            currentStage.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void initialize() {
        inactivityManager = new InactivityManager(this::handleInactivityLogout);
        inactivityManager.start(); // Inicia el temporizador
        tabPaneFx.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                System.out.println("El título del stage es: " + stage.getTitle());
                inactivityTimer.startInactivityTimer(); // Iniciar el temporizador cuando se abre la escena
            }
        });


    myresources = util.detectLanguage(userPrefs.get("IDIOMAX", "en"));
        mmiDao = new MenuMenuItemDao();
        String perf= SessionManager.getInstance().getNombrePerfil();
        lista = mmiDao.listaAccesos(perf, myresources);
        System.out.println("VERR:"+lista.size());
        int[] mmi = contarMenuMunuItem(lista);
        Menu[] menu = new Menu[mmi[0]];
        MenuItem[] menuItem = new MenuItem[mmi[1]];
        menuBarFx = new MenuBar();
        MenuItemListener d = new MenuItemListener();
        SampleMenuListener m = new SampleMenuListener();
        String menuN = "";
        int menui = 0, menuitem = 0;
        char conti = 'N';
        for (MenuMenuItenTO mmix : lista) {
            if (!mmix.menunombre.equals(menuN)) {
                menu[menui] = new Menu(mmix.menunombre);
                menu[menui].setId("m" + mmix.nombreObj);
                menu[menui].setOnShowing(m::menuSelected);
                if (!mmix.menuitemnombre.equals("")) {
                    menuItem[menuitem] = new MenuItem(mmix.menuitemnombre);
                    menuItem[menuitem].setId("mi" + mmix.nombreObj);
                    menuItem[menuitem].setOnAction(d::handle);
                    menu[menui].getItems().add(menuItem[menuitem]);
                    menuitem++;
                }
                menuBarFx.getMenus().add(menu[menui]);
                menuN = mmix.menunombre;
                conti = 'N';
                menui++;
            } else {
                conti = 'S';
            }
            if (!mmix.menuitemnombre.equals("") && mmix.menunombre.equals(menuN) && conti == 'S') {
                menuItem[menuitem] = new MenuItem(mmix.menuitemnombre);
                menuItem[menuitem].setId("mi" + mmix.nombreObj);
                menuItem[menuitem].setOnAction(d::handle);
                menu[menui - 1].getItems().add(menuItem[menuitem]);
                menuitem++;
            }
        }

        bp.setTop(menuBarFx);
        bp.setCenter(tabPaneFx);
    }


    public int[] contarMenuMunuItem(List<MenuMenuItenTO> data) {
        int menui = 0, menuitem = 0;
        String menuN = "";
        for (MenuMenuItenTO mmi : data) {
            if (!mmi.menunombre.equals(menuN)) {
                menuN = mmi.menunombre;
                menui++;
            }
            if (!mmi.menuitemnombre.equals("")) {
                menuitem++;
            }
        }
        return new int[]{menui, menuitem};
    }


    class MenuItemListener {
        public void handle(javafx.event.ActionEvent e) {
            inactivityManager.reset(); // Reinicia el temporizador

            if (((MenuItem) e.getSource()).getId().equals("mimiregproduct")) {
                tabPaneFx.getTabs().clear();
                FXMLLoader loader = new
                        FXMLLoader(getClass().getResource("/view/main_producto.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent paneFromFXML;
                try {
                    paneFromFXML = loader.load(); // Cargar el contenido FXML
                    ScrollPane dd= new ScrollPane(paneFromFXML);
                    //mc.setContexto(ctx);
                    Tab clienteTab = new Tab("Reg. Producto",dd );
                    tabPaneFx.getTabs().add(clienteTab);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if(((MenuItem) e.getSource()).getId().equals("mimiventa")){
                tabPaneFx.getTabs().clear();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_venta.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent paneFromFXML;
                try {
                    paneFromFXML = loader.load(); // Cargar el contenido FXML
                    ScrollPane dd= new ScrollPane(paneFromFXML);
                    //mc.setContexto(ctx);
                    Tab clienteTab = new Tab("Registro Venta",dd );
                    tabPaneFx.getTabs().add(clienteTab);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if (((MenuItem) e.getSource()).getId().equals("mimicliente")) {
                tabPaneFx.getTabs().clear();
                FXMLLoader loader = new
                        FXMLLoader(getClass().getResource("/view/addnew_cliente.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent paneFromFXML;
                try {
                    paneFromFXML = loader.load(); // Cargar el contenido FXML
                    ScrollPane dd= new ScrollPane(paneFromFXML);
                    //mc.setContexto(ctx);
                    Tab clienteTab = new Tab("Reg. Cliente",dd );
                    tabPaneFx.getTabs().add(clienteTab);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if (((MenuItem) e.getSource()).getId().equals("mimisalir")) {
                tabPaneFx.getTabs().clear();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
                    fxmlLoader.setControllerFactory(context::getBean);
                    Parent parent = fxmlLoader.load();
                    Scene scene = new Scene(parent);

                    Stage loginStage = new Stage();
                    loginStage.setScene(scene);
                    loginStage.setTitle("SysAlmacen Spring Java-FX");
                    loginStage.setResizable(false);

                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    loginStage.setX(bounds.getMinX() + (bounds.getWidth() - loginStage.getWidth()) / 2); // Centrar horizontalmente
                    loginStage.setY(bounds.getMinY() + (bounds.getHeight() - loginStage.getHeight()) / 2); // Centrar verticalmente

                    loginStage.show();

                    Stage currentStage = (Stage) tabPaneFx.getScene().getWindow();
                    currentStage.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }



            if (((MenuItem) e.getSource()).getId().equals("mimiselectall")) {
                tabPaneFx.getTabs().clear();
                // Añade la lógica para "mimiselectall"
            }
        }
    }
    class SampleMenuListener {
        public void menuSelected(javafx.event.Event e) {
            if (((Menu) e.getSource()).getId().equals("mmiver1")) {
                System.out.println("llego help");
            }
        }
    }


}