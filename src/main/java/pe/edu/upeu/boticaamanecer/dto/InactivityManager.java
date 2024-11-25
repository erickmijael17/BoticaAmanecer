package pe.edu.upeu.boticaamanecer.dto;

import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;

public class InactivityManager {
    private static final long INACTIVITY_LIMIT = 5 * 60 * 1000; // 5 minutos
    private Timer inactivityTimer;
    private Runnable onInactivity;

    public InactivityManager(Runnable onInactivity) {
        this.onInactivity = onInactivity;
    }

    public void start() {
        reset(); // Inicia el temporizador
    }

    public void reset() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }

        inactivityTimer = new Timer(true); // Timer en segundo plano
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(onInactivity); // Maneja la inactividad en el hilo de JavaFX
            }
        }, INACTIVITY_LIMIT);
    }

    public void stop() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
            inactivityTimer = null;
        }
    }
}

