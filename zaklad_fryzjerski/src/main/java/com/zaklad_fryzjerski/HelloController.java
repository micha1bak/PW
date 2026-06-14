package com.zaklad_fryzjerski;

import com.zaklad_fryzjerski.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HelloController implements SimulationObserver {

    @FXML private TextField nField, pTotalField, lField, capacityField;
    @FXML private VBox specializationContainer;
    @FXML private Button startButton, stopButton;
    @FXML private Pane simulationPane;

    private List<TextField> pFields = new ArrayList<>();
    private SimulationManager simulationManager;
    private final ConcurrentHashMap<Integer, Pane> visualEntities = new ConcurrentHashMap<>();
    private final Map<Pane, TranslateTransition> activeTransitions = new HashMap<>();
    private Label waitingCounterLabel;

    private final double WAITING_ROOM_X = 150;
    private final double WAITING_ROOM_Y = 80;
    private final double CHAIRS_X = 450;
    private final double CHAIRS_Y = 50;
    private final double ENTITY_SPACING = 55;
    private final double CHAIR_SPACING = 90;

    @FXML
    public void initialize() {
        updateSpecializationFields();
    }

    @FXML
    protected void onNFieldChanged() {
        updateSpecializationFields();
    }

    private void updateSpecializationFields() {
        try {
            int n = Integer.parseInt(nField.getText());
            if (n < 1) return;
            specializationContainer.getChildren().clear();
            pFields.clear();
            specializationContainer.getChildren().add(new Label("Liczba fryzjerów na usługę:"));
            for (int i = 1; i <= n; i++) {
                VBox box = new VBox(2);
                box.getChildren().addAll(new Label("Fryzjerzy dla usługi P" + i + ":"), new TextField("1"));
                pFields.add((TextField) box.getChildren().get(1));
                specializationContainer.getChildren().add(box);
            }
        } catch (NumberFormatException ignored) {}
    }

    private void drawShopLayout(int capacity, int l) {
        simulationPane.getChildren().clear();
        
        Rectangle waitingRoom = new Rectangle(WAITING_ROOM_X, WAITING_ROOM_Y, 80, capacity * ENTITY_SPACING + 10);
        waitingRoom.setFill(Color.web("#f5f5f5"));
        waitingRoom.setStroke(Color.GRAY);
        
        simulationPane.getChildren().add(waitingRoom);
        simulationPane.getChildren().add(new Label("Poczekalnia") {{
            setLayoutX(WAITING_ROOM_X); setLayoutY(WAITING_ROOM_Y - 25);
            setStyle("-fx-font-weight: bold;");
        }});

        waitingCounterLabel = new Label("Ludzie: 0 / " + capacity);
        waitingCounterLabel.setLayoutX(WAITING_ROOM_X);
        waitingCounterLabel.setLayoutY(WAITING_ROOM_Y + capacity * ENTITY_SPACING + 15);
        waitingCounterLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #388e3c;");
        simulationPane.getChildren().add(waitingCounterLabel);

        for (int i = 0; i < l; i++) {
            double cy = CHAIRS_Y + (i * CHAIR_SPACING) + 30;
            Circle chair = new Circle(CHAIRS_X, cy, 28);
            chair.setFill(Color.WHITE);
            chair.setStroke(Color.web("#d32f2f"));
            chair.setStrokeWidth(2);
            
            Label chairLabel = new Label("Fotel " + (i+1));
            chairLabel.setLayoutX(CHAIRS_X - 20); 
            chairLabel.setLayoutY(cy + 32); 
            chairLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f0000;");
            
            simulationPane.getChildren().addAll(chair, chairLabel);
        }
    }

    private Pane createEntityCircle(String labelText, Color color, Color strokeColor) {
        Pane pane = new Pane();
        Circle circle = new Circle(22, color);
        circle.setStroke(strokeColor);
        circle.setStrokeWidth(2.5);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: black;");
        label.setLayoutX(5); label.setLayoutY(14);
        pane.getChildren().addAll(circle, label);
        return pane;
    }

    @FXML
    protected void onStartSimulation() {
        if (!validateInputs()) return;

        int n = Integer.parseInt(nField.getText());
        int pTotal = Integer.parseInt(pTotalField.getText());
        int l = Integer.parseInt(lField.getText());
        int capacity = Integer.parseInt(capacityField.getText());

        drawShopLayout(capacity, l);
        visualEntities.clear();
        activeTransitions.clear();
        
        List<Set<Integer>> barberSpecs = new ArrayList<>();
        for (int i = 0; i < pTotal; i++) barberSpecs.add(new HashSet<>());
        List<Integer> rolesPool = new ArrayList<>();
        for (int i = 0; i < pFields.size(); i++) {
            int neededForThisSpec = Integer.parseInt(pFields.get(i).getText());
            for (int j = 0; j < neededForThisSpec; j++) rolesPool.add(i + 1);
        }
        for (int i = 0; i < rolesPool.size(); i++) {
            barberSpecs.get(i % pTotal).add(rolesPool.get(i));
        }

        simulationManager = new SimulationManager(n, pTotal, l, capacity, barberSpecs);
        simulationManager.setObserver(this);
        
        for (int i = 0; i < pTotal; i++) {
            String labelText = barberSpecs.get(i).stream()
                    .sorted().map(s -> "P" + s).collect(Collectors.joining(","));
            
            Pane bCircle = createEntityCircle(labelText, Color.web("#e3f2fd"), Color.web("#1976d2"));
            bCircle.setLayoutX(650); bCircle.setLayoutY(50 + (i * 55));
            visualEntities.put(1000 + i, bCircle);
            simulationPane.getChildren().add(bCircle);
        }

        simulationManager.start();
        startButton.setDisable(true);
        stopButton.setDisable(false);
        toggleFields(true);
    }

    @FXML
    protected void onStopSimulation() {
        if (simulationManager != null) simulationManager.stop();
        startButton.setDisable(false);
        stopButton.setDisable(true);
        toggleFields(false);
    }

    @Override
    public void onClientArrived(Client client) {
        Platform.runLater(() -> {
            Pane cCircle = createEntityCircle("P" + client.getRequiredServiceId(), Color.web("#e8f5e9"), Color.web("#388e3c"));
            cCircle.setLayoutX(50);
            cCircle.setLayoutY(WAITING_ROOM_Y + 10);
            visualEntities.put(client.getClientId(), cCircle);
            simulationPane.getChildren().add(cCircle);
        });
    }

    @Override
    public void onClientEnteredWaitingRoom(Client client, int position) {
        Platform.runLater(() -> {
            Pane cCircle = visualEntities.get(client.getClientId());
            if (cCircle != null) {
                moveEntity(cCircle, WAITING_ROOM_X + 35, WAITING_ROOM_Y + 30 + (position * ENTITY_SPACING));
            }
        });
    }

    @Override
    public void onWaitingRoomSizeChanged(int currentSize) {
        Platform.runLater(() -> {
            if (waitingCounterLabel != null) {
                int capacity = Integer.parseInt(capacityField.getText());
                waitingCounterLabel.setText("Ludzie: " + currentSize + " / " + capacity);
            }
        });
    }

    @Override
    public void onBarberStartedService(Barber barber, Client client, int chairIndex) {
        Platform.runLater(() -> {
            Pane bCircle = visualEntities.get(1000 + barber.getBarberId() - 1);
            Pane cCircle = visualEntities.get(client.getClientId());
            double targetY = CHAIRS_Y + (chairIndex * CHAIR_SPACING) + 8;
            if (bCircle != null) moveEntity(bCircle, CHAIRS_X - 60, targetY);
            if (cCircle != null) moveEntity(cCircle, CHAIRS_X + 18, targetY);
        });
    }

    @Override
    public void onBarberFinishedService(Barber barber, Client client, int chairIndex) {
        Platform.runLater(() -> {
            Pane bCircle = visualEntities.get(1000 + barber.getBarberId() - 1);
            Pane cCircle = visualEntities.get(client.getClientId());
            if (bCircle != null) moveEntity(bCircle, 650, 50 + (barber.getBarberId()-1) * 55);
            if (cCircle != null) {
                moveEntity(cCircle, 850, 400);
                new Thread(() -> {
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    Platform.runLater(() -> {
                        simulationPane.getChildren().remove(cCircle);
                        visualEntities.remove(client.getClientId());
                    });
                }).start();
            }
        });
    }

    @Override
    public void onClientLeft(Client client) {
        Platform.runLater(() -> {
            Pane cCircle = visualEntities.get(client.getClientId());
            if (cCircle != null) {
                moveEntity(cCircle, -60, 550);
                new Thread(() -> {
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    Platform.runLater(() -> {
                        simulationPane.getChildren().remove(cCircle);
                        visualEntities.remove(client.getClientId());
                    });
                }).start();
            }
        });
    }

    private void moveEntity(Pane entity, double x, double y) {
        // Zatrzymaj poprzednią animację dla tego obiektu
        if (activeTransitions.containsKey(entity)) {
            activeTransitions.get(entity).stop();
        }

        TranslateTransition tt = new TranslateTransition(Duration.millis(600), entity);
        tt.setToX(x - entity.getLayoutX());
        tt.setToY(y - entity.getLayoutY());
        
        activeTransitions.put(entity, tt);
        tt.setOnFinished(e -> activeTransitions.remove(entity));
        tt.play();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean validateInputs() {
        try {
            int n = Integer.parseInt(nField.getText());
            int pTotal = Integer.parseInt(pTotalField.getText());
            int l = Integer.parseInt(lField.getText());
            int cap = Integer.parseInt(capacityField.getText());
            
            if (n <= 0 || pTotal <= 0 || l <= 0 || cap <= 0) {
                showAlert("Błąd", "Wszystkie wartości muszą być większe od 0.");
                return false;
            }

            int sumPx = 0;
            for (int i = 0; i < pFields.size(); i++) {
                int px = Integer.parseInt(pFields.get(i).getText());
                if (px > pTotal) {
                    showAlert("Błąd", "Liczba fryzjerów P" + (i+1) + " nie może być większa niż całkowita liczba P.");
                    return false;
                }
                sumPx += px;
            }
            if (sumPx < pTotal) {
                showAlert("Błąd", "Suma P1+P2+... musi być >= P.");
                return false;
            }
            if (l >= pTotal) {
                showAlert("Błąd", "Liczba foteli L musi być mniejsza od liczby fryzjerów P.");
                return false;
            }
            return true;
        } catch (Exception e) { 
            showAlert("Błąd", "Wprowadź poprawne liczby całkowite.");
            return false; 
        }
    }

    private void toggleFields(boolean disable) {
        nField.setDisable(disable); pTotalField.setDisable(disable);
        lField.setDisable(disable); capacityField.setDisable(disable);
        pFields.forEach(f -> f.setDisable(disable));
    }
}
