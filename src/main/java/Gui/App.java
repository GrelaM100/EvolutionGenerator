package Gui;

import Classes.MapWithBorders;
import Classes.SimulationEngine;
import Classes.Vector2d;
import Interfaces.IMapElement;
import Interfaces.IMapObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class App extends Application implements IMapObserver {
    private SimulationEngine engine;
    private Stage stage;
    private int stageWidth = 500;
    private int stageHeight = 500;
    private GridPane mapGrid;
    private XYChart.Series<Number, Number> numberOfAnimalsSeries, numberOfPlantsSeries,
            averageEnergySeries, averageLifeLengthSeries, averageNumberOfChildrenSeries;
    private LineChart<Number, Number> numberOfAnimalsChart, numberOfPlantsChart,
            averageEnergyChart, averageLifeLengthChart, averageNumberOfChildrenChart;

    private HBox combinedView;


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
        HBox moveDelayHbox = this.createMenuHBox("Opóźnienie między ruchami (ms): ", "300");
        Button startButton = new Button("Start");

        VBox menu = new VBox(widthHbox, heightHbox, numberOfAnimalsHbox, startEnergyHbox, moveEnergyHbox, planEnergyHbox,
                jungleRatioHbox, moveDelayHbox, startButton);
        menu.setAlignment(Pos.BASELINE_RIGHT);
        this.stage.setScene(new Scene(menu, 450, 250));

        startButton.setOnAction(action -> {
            int width = Integer.parseInt(getTextFromMenu(0, menu));
            int height = Integer.parseInt(getTextFromMenu(1, menu));
            int numberOfAnimals = Integer.parseInt(getTextFromMenu(2, menu));
            int startEnergy = Integer.parseInt(getTextFromMenu(3, menu));
            int moveEnergy = Integer.parseInt(getTextFromMenu(4, menu));
            int plantEnergy = Integer.parseInt(getTextFromMenu(5, menu));
            double jungleRation = Double.parseDouble(getTextFromMenu(6, menu));
            int moveDelay = Integer.parseInt(getTextFromMenu(7, menu));
            MapWithBorders map = new MapWithBorders(width, height, startEnergy, moveEnergy, plantEnergy, jungleRation);
            this.engine = new SimulationEngine(map, numberOfAnimals, moveDelay, this);
            this.preparePlots();
            mapGrid = new GridPane();
            createGrid();
            combinePlotsAndMap();
            Thread engineThread = new Thread(this.engine);
            engineThread.start();
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

    private String getTextFromMenu(int id, VBox menu) {
        HBox hbox = (HBox) menu.getChildren().get(id);
        TextField textField = (TextField) hbox.getChildren().get(1);
        return textField.getText();
    }


    private void createGrid() {
        GridPane gridPane = this.mapGrid;
        int width = engine.map.width;
        int height = engine.map.height;
        int columnSize = this.stageWidth / (width + 1);
        int rowSize = this.stageHeight / (height + 1);
        Label currentLabel;

        for(int i = 0; i < height + 1; i++) {
            gridPane.getRowConstraints().add(new RowConstraints(rowSize));
        }
        for(int j = 0; j < width + 1; j++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(columnSize));
        }

        for(int i = 0; i < height + 1; i++) {
            for(int j = 0; j < width + 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                if(i == 0) {
                    currentLabel = new Label(String.valueOf(j - 1));
                    gridPane.add(currentLabel, j, i);
                }
                else if(j == 0) {
                    currentLabel = new Label(String.valueOf(height - i));
                    gridPane.add(currentLabel, j, i);
                }
                else {
                    Vector2d position = new Vector2d(j - 1, i - 1);
                    Object mapElement = engine.map.objectAt(position);
                    currentLabel = new Label(" ");
                    currentLabel.setMinWidth(columnSize);
                    currentLabel.setMinHeight(rowSize);
                    if(this.engine.map.isInJungle(position)) {
                        currentLabel.setStyle("-fx-background-color: rgb(0,100,0)");
                    }
                    else {
                        currentLabel.setStyle("-fx-background-color: rgb(255,255,102)");
                    }
                    if(mapElement != null) {
                        String color = ((IMapElement) mapElement).getColor();
                        String style = "-fx-background-color: " + color;
                        currentLabel.setStyle(style);
                    }

                    gridPane.add(currentLabel, j, height - i + 1);
                }
            }
        }
        gridPane.setGridLinesVisible(true);
        this.mapGrid = gridPane;
    }

    private XYChart.Series<Number, Number> setSeries(double fistValue, String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(0, fistValue));
        series.setName(name);
        return series;
    }

    private LineChart<Number, Number> setChart(XYChart.Series<Number, Number> series) {
        NumberAxis xAxis, yAxis;
        xAxis = new NumberAxis();
        xAxis.setLabel("Dzień");
        xAxis.setForceZeroInRange(false);

        yAxis = new NumberAxis();

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.getData().add(series);
        return chart;
    }

    public void preparePlots() {
        this.numberOfAnimalsSeries = this.setSeries(this.engine.stats.numberOfAnimals, "Liczba zwierząt");
        this.numberOfAnimalsChart = this.setChart(this.numberOfAnimalsSeries);

        this.numberOfPlantsSeries = this.setSeries(this.engine.stats.numberOfPlants, "Liczba roślin");
        this.numberOfPlantsChart = this.setChart(this.numberOfPlantsSeries);

        this.averageEnergySeries = this.setSeries(this.engine.stats.averageEnergy, "Średnia energia");
        this.averageEnergyChart = this.setChart(this.averageEnergySeries);

        this.averageNumberOfChildrenSeries = this.setSeries(this.engine.stats.averageNumberOfChildren,
                "Średnia ilość dzieci");
        this.averageNumberOfChildrenChart = this.setChart(this.averageNumberOfChildrenSeries);

        this.averageLifeLengthSeries = this.setSeries(this.engine.stats.averageLifeLength, "Średnia długość życia");
        this.averageLifeLengthChart = this.setChart(this.averageLifeLengthSeries);
    }

    public void combinePlotsAndMap() {
        Button numberOfAnimalsButton = new Button("Zwierzęta");
        Button numberOfPlantsButton = new Button("Rośliny");
        Button averageEnergyButton = new Button("Energia");
        Button averageLifeLengthButton = new Button("Długość życia");
        Button averageNumberOfChildrenButton = new Button("Ilość dzieci");
        HBox buttonsToChangePlot = new HBox(numberOfAnimalsButton, numberOfPlantsButton, averageEnergyButton,
                averageLifeLengthButton, averageNumberOfChildrenButton);
        VBox plotWithButtons = new VBox(this.numberOfAnimalsChart, buttonsToChangePlot);
        this.combinedView = new HBox(plotWithButtons, this.mapGrid);
        Scene scene = new Scene(this.combinedView, stageWidth * 2, stageHeight);
        this.stage.setScene(scene);
        numberOfAnimalsButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.numberOfAnimalsChart));
        numberOfPlantsButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.numberOfPlantsChart));
        averageEnergyButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.averageEnergyChart));
        averageNumberOfChildrenButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.averageNumberOfChildrenChart));
        averageLifeLengthButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.averageLifeLengthChart));
    }

    public void updateSeries() {
        this.numberOfAnimalsSeries.getData().add(new XYChart.Data<>(this.engine.stats.day, this.engine.stats.numberOfAnimals));
        this.numberOfPlantsSeries.getData().add(new XYChart.Data<>(this.engine.stats.day, this.engine.stats.numberOfPlants));
        this.averageEnergySeries.getData().add(new XYChart.Data<>(this.engine.stats.day, this.engine.stats.averageEnergy));
        this.averageNumberOfChildrenSeries.getData().add(new XYChart.Data<>(this.engine.stats.day, this.engine.stats.averageNumberOfChildren));
        this.averageLifeLengthSeries.getData().add(new XYChart.Data<>(this.engine.stats.day, this.engine.stats.averageLifeLength));
        if(this.numberOfAnimalsSeries.getData().size() > 15) {
            this.numberOfAnimalsSeries.getData().remove(0);
            this.numberOfPlantsSeries.getData().remove(0);
            this.averageEnergySeries.getData().remove(0);
            this.averageNumberOfChildrenSeries.getData().remove(0);
            this.averageLifeLengthSeries.getData().remove(0);
        }
    }

    @Override
    public void dayPassed(MapWithBorders map) {
        Platform.runLater(() -> {
            this.mapGrid.getChildren().clear();
            this.mapGrid.getRowConstraints().clear();
            this.mapGrid.getColumnConstraints().clear();
            this.mapGrid.setGridLinesVisible(false);
            this.createGrid();
            this.updateSeries();
        });
    }

    @Override
    public void animalDied(int age) {

    }
}
