package Gui;

import Classes.Animal;
import Classes.MapWithBorders;
import Classes.SimulationEngine;
import Classes.Vector2d;
import Interfaces.IMapElement;
import Interfaces.IMapObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class App extends Application implements IMapObserver {
    private SimulationEngine engine;
    private boolean isPaused = false;
    private Stage stage;
    private int stageWidth = 500;
    private int stageHeight = 500;
    private GridPane mapGrid;
    private XYChart.Series<Number, Number> numberOfAnimalsSeries, numberOfPlantsSeries,
            averageEnergySeries, averageLifeLengthSeries, averageNumberOfChildrenSeries;
    private LineChart<Number, Number> numberOfAnimalsChart, numberOfPlantsChart,
            averageEnergyChart, averageLifeLengthChart, averageNumberOfChildrenChart;
    private Animal trackedAnimal = null;
    private VBox trackedAnimalVBox;

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
            this.createGrid();
            this.combinePlotsAndMap();
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
        this.clickGrid();
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
        Button pauseStartButton = new Button("||");
        pauseStartButton.setMinWidth(50);
        Label mostFrequentGenotype = new Label("Dominujący genotyp:\n");
        HBox buttonsToChangePlot = new HBox(numberOfAnimalsButton, numberOfPlantsButton, averageEnergyButton,
                averageLifeLengthButton, averageNumberOfChildrenButton);
        VBox plotWithButtons = new VBox(this.numberOfAnimalsChart, buttonsToChangePlot, mostFrequentGenotype);
        VBox mapWithButton = new VBox(5, this.mapGrid, pauseStartButton);
        mapWithButton.setAlignment(Pos.CENTER);
        this.combinedView = new HBox(plotWithButtons, mapWithButton);
        Scene scene = new Scene(this.combinedView, stageWidth * 2, stageHeight + 60);
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
        pauseStartButton.setOnAction(action -> {
            if(this.isPaused) {
                this.continueSimulation();
            }
            else {
                this.pauseSimulation();
            }
        });
    }

    private void pauseSimulation() {
        this.isPaused = true;
        this.engine.pause();
        Button button = (Button) ((VBox) this.combinedView.getChildren().get(1)).getChildren().get(1);
        Button showAllWithDominantGenotype = new Button("Pokaż zwierzęta z domiującym genotypem");
        ((VBox) this.combinedView.getChildren().get(1)).getChildren().add(showAllWithDominantGenotype);
        button.setText(">");
        showAllWithDominantGenotype.setOnAction(event -> {
            VBox animalsInformation = new VBox(5);
            for(Animal animal : this.engine.map.animalsList) {
                if(animal.getGenotype().equals(this.engine.stats.dominantGenotype)) {
                    Label animalPosition = new Label("Pozycja zwierzęcia: " + animal.getPosition().toString());
                    Label animalEnergy = new Label("Energia zwierzęcia: " + animal.getEnergy());
                    Label animalAge = new Label("Wiek zwierzęcia: " + animal.getAge());
                    VBox animalInformation = new VBox(animalPosition, animalEnergy, animalAge);
                    animalsInformation.getChildren().add(animalInformation);
                }
            }
            ScrollPane scrollPane = new ScrollPane(animalsInformation);
            scrollPane.setFitToHeight(true);

            Scene scene = new Scene(scrollPane, 400, 200);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Zwierzęta z dominującym genotypem");
            stage.show();
        });
    }

    private void continueSimulation() {
        this.isPaused = false;
        this.engine.resume();
        Button button = (Button) ((VBox) this.combinedView.getChildren().get(1)).getChildren().get(1);
        ((VBox) this.combinedView.getChildren().get(1)).getChildren().remove(2);
        button.setText("||");
    }

    public void clickGrid() {
        this.mapGrid.getChildren().forEach(item -> {
            item.setOnMouseClicked(event -> {
                if(this.isPaused) {
                    Node clickedNode = event.getPickResult().getIntersectedNode();
                    Node parent = clickedNode.getParent();
                    while(parent != this.mapGrid) {
                        clickedNode = parent;
                        parent = clickedNode.getParent();
                    }
                    Integer colIndex = GridPane.getColumnIndex(clickedNode);
                    Integer rowIndex = GridPane.getRowIndex(clickedNode);
                    Vector2d clickedPosition = new Vector2d(colIndex - 1,
                            this.mapGrid.getRowConstraints().size() - rowIndex - 1);

                    Object clickedObject = this.engine.map.objectAt(clickedPosition);
                    this.trackCLickedAnimal(clickedObject);
                }
            });
        });
    }

    public void trackCLickedAnimal(Object clickedObject) {
        if(clickedObject != null) {
            if(clickedObject instanceof Animal) {
                if(this.trackedAnimal == null) {
                    this.trackedAnimal = (Animal) clickedObject;
                    this.trackedAnimal.track();
                    Stage animalStage = new Stage();
                    Label selectedAnimalGenotype = new Label("Genotyp:\n" + this.trackedAnimal.getGenotype().toString());
                    Label numberOfChildren = new Label("Liczba dzieci: 0");
                    Label numberOfDescendant = new Label("Liczba potomków: 0");
                    Label deathAge = new Label("Zmarło w epoce: Wciąż żyje");
                    this.trackedAnimalVBox = new VBox(selectedAnimalGenotype, numberOfChildren, numberOfDescendant, deathAge);
                    Scene animalScene = new Scene(this.trackedAnimalVBox, 400, 200);
                    animalStage.setScene(animalScene);
                    animalStage.setOnCloseRequest(event -> {
                        this.trackedAnimal = null;
                    });
                    animalStage.setTitle("Śledzone zwierzę");
                    animalStage.show();
                }
            }
        }
    }

    public void updateTrackedAnimalVbox() {
        this.trackedAnimalVBox.getChildren().set(1, new Label("Liczba dzieci: " + this.trackedAnimal.trackedChildren.size()));
        this.trackedAnimalVBox.getChildren().set(2, new Label("Liczba potomków: " + this.trackedAnimal.calculateDescendant(this.trackedAnimal)));
        if(this.trackedAnimal.getDayOfDeath() > 0) {
            this.trackedAnimalVBox.getChildren().set(3, new Label("Zmarło w epoce: " + this.trackedAnimal.getDayOfDeath()));
        }
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
            ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(2,
                    new Label("Dominujący genotyp:\n:" + this.engine.stats.dominantGenotype));
            if(this.trackedAnimal != null) {
                this.updateTrackedAnimalVbox();
            }
        });
    }

    @Override
    public void animalDied(int age) {

    }
}
