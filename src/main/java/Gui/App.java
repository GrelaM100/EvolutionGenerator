package Gui;

import Classes.*;
import Interfaces.IWorldMap;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class App extends Application {
    private Stage stage;

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("Evolution Generator");
        createMenu();
        this.stage.show();
    }

    private void createMenu() {
        HBox widthHbox = this.createMenuHBox("Szerokość mapy: ", "15");
        HBox heightHbox = this.createMenuHBox("Wysokość mapy: ", "15");
        HBox numberOfAnimalsHbox = this.createMenuHBox("Ilość zwierząt: ", "30");
        HBox startEnergyHbox = this.createMenuHBox("Początkowa energia zwierząt: ", "500");
        HBox moveEnergyHbox = this.createMenuHBox("Energia tracona każdego dnia: ", "5");
        HBox planEnergyHbox = this.createMenuHBox("Energia zyskiwana podczas jedzenia: ", "50");
        HBox jungleRatioHbox = this.createMenuHBox("Proporcja dżungli do sawanny: ", "0.15");
        HBox reproduceBorders = this.createMenuChoice("mapy z murem: ");
        HBox reproduceNoBorders = this.createMenuChoice("mapy zawiniętej: ");
        HBox moveDelayHbox = this.createMenuHBox("Opóźnienie między ruchami (ms): ", "300");
        Button startButton = new Button("Start");

        VBox menu = new VBox(3, widthHbox, heightHbox, numberOfAnimalsHbox, startEnergyHbox, moveEnergyHbox, planEnergyHbox,
                jungleRatioHbox, reproduceBorders, reproduceNoBorders, moveDelayHbox, startButton);
        menu.setAlignment(Pos.BASELINE_RIGHT);
        this.stage.setScene(new Scene(menu, 450, 300));

        startButton.setOnAction(action -> {
            int width = Integer.parseInt(getTextFromMenu(0, menu));
            int height = Integer.parseInt(getTextFromMenu(1, menu));
            int numberOfAnimals = Integer.parseInt(getTextFromMenu(2, menu));
            int startEnergy = Integer.parseInt(getTextFromMenu(3, menu));
            int moveEnergy = Integer.parseInt(getTextFromMenu(4, menu));
            int plantEnergy = Integer.parseInt(getTextFromMenu(5, menu));
            double jungleRation = Double.parseDouble(getTextFromMenu(6, menu));
            boolean evolutionMethodBorders = getFromChoiceBox(7, menu);
            boolean evolutionMethodNoBorders = getFromChoiceBox(8, menu);
            int moveDelay = Integer.parseInt(getTextFromMenu(9, menu));
            IWorldMap mapBorders = new MapWithBorders(width, height, startEnergy, moveEnergy, plantEnergy, jungleRation, evolutionMethodBorders);
            IWorldMap mapNoBorders = new MapNoBorders(width, height, startEnergy, moveEnergy, plantEnergy, jungleRation, evolutionMethodNoBorders);
            MapGui mapBordersGui = new MapGui(mapBorders, numberOfAnimals, moveDelay, "Mapa z murem");
            MapGui mapNoBordersGui = new MapGui(mapNoBorders, numberOfAnimals, moveDelay, "Mapa zawinięta");
            HBox main = new HBox(10, mapBordersGui.combinedView, mapNoBordersGui.combinedView);
            Scene scene = new Scene(main, 2100, 600);
            this.stage.setScene(scene);

            Thread engineThread1 = new Thread(mapBordersGui.engine);
            Thread engineThread2 = new Thread(mapNoBordersGui.engine);
            engineThread1.start();
            engineThread2.start();
            this.stage.setOnCloseRequest(event -> {
                mapBordersGui.engine.stop();
                mapNoBordersGui.engine.stop();
            });
        });
    }

    private HBox createMenuHBox(String labelText, String initialValue) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        textField.setText(initialValue);
        HBox hBox = new HBox(label, textField);
        hBox.setAlignment(Pos.BASELINE_RIGHT);

        return hBox;
    }

    private HBox createMenuChoice(String name) {
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().add("Zwykła");
        choiceBox.getItems().add("Magiczna");
        choiceBox.setValue("Zwykła");
        HBox hbox = new HBox(new Label("Zasada ewolucji dla" + name), choiceBox);
        hbox.setAlignment(Pos.BASELINE_RIGHT);

        return hbox;
    }

    private String getTextFromMenu(int id, VBox menu) {
        HBox hbox = (HBox) menu.getChildren().get(id);
        TextField textField = (TextField) hbox.getChildren().get(1);
        return textField.getText();
    }

    private boolean getFromChoiceBox(int id, VBox menu) {
        HBox hbox = (HBox) menu.getChildren().get(id);
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) hbox.getChildren().get(1);
        return choiceBox.getValue().equals("Magiczna");
    }

}
