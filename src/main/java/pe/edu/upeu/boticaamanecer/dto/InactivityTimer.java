package pe.edu.upeu.boticaamanecer.dto;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import pe.edu.upeu.boticaamanecer.dto.SessionManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import javafx.geometry.Rectangle2D;

public class InactivityTimer {

    private static final long INACTIVITY_LIMIT = 5 * 30 * 1000; // 5 minutos
    private Timer inactivityTimer;
    private TabPane tabPaneFx;
    private Preferences userPrefs;


    public InactivityTimer(TabPane tabPaneFx) {
        this.tabPaneFx = tabPaneFx;
        this.userPrefs = Preferences.userRoot();
    }

    public void startInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel(); // Cancelar el temporizador actual si existe
        }

        inactivityTimer = new Timer(true);
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> handleInactivityLogout());
            }
        }, INACTIVITY_LIMIT);
    }

    private void handleInactivityLogout() {
        tabPaneFx.getTabs().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);

            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setTitle("SysAlmacen Spring Java-FX");
            loginStage.setResizable(false);

            // Centrar la ventana de login
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            loginStage.setX(bounds.getMinX() + (bounds.getWidth() - loginStage.getWidth()) / 2);
            loginStage.setY(bounds.getMinY() + (bounds.getHeight() - loginStage.getHeight()) / 2);

            loginStage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) tabPaneFx.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void resetInactivityTimer() {
        startInactivityTimer(); // Reiniciar el temporizador al interactuar con la app
    }
}
