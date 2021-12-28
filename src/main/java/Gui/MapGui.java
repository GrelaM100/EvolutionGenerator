package Gui;

import Classes.Animal;
import Classes.SimulationEngine;
import Classes.Vector2d;
import Interfaces.IMapElement;
import Interfaces.IMapObserver;
import Interfaces.IWorldMap;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MapGui implements IMapObserver {
    public SimulationEngine engine;
    private boolean isPaused = false;
    private GridPane mapGrid;
    private final ArrayList<XYChart.Series<Number, Number>> xYSeries = new ArrayList<>();
    private final ArrayList<XYChart<Number, Number>> charts = new ArrayList<>();

    private Animal trackedAnimal = null;
    private VBox trackedAnimalVBox;
    private final String name;

    public HBox combinedView;


    public MapGui(IWorldMap map, int numberOfAnimals, int moveDelay, String name) {
        this.name = name;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Zwierzęta magicznie wyewoluowały na mapie (" + this.name + ")");
        this.engine = new SimulationEngine(map, numberOfAnimals, moveDelay, this);
        this.preparePlots();
        mapGrid = new GridPane();
        this.createGrid();
        this.combinePlotsAndMap();
    }


    private void createGrid() {
        GridPane gridPane = this.mapGrid;
        int width = engine.map.getWidth();
        int height = engine.map.getHeight();
        int stageWidth = 500;
        int columnSize = stageWidth / (width + 1);
        int stageHeight = 500;
        int rowSize = stageHeight / (height + 1);
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

    private XYChart.Series<Number, Number> setSeries(float firstValue, String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(0, firstValue));
        series.setName(name);
        return series;
    }

    private LineChart<Number, Number> setChart(XYChart.Series<Number, Number> series) {
        NumberAxis xAxis, yAxis;
        xAxis = new NumberAxis();
        xAxis.setLabel("Epoka");
        xAxis.setForceZeroInRange(false);

        yAxis = new NumberAxis();

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.getData().add(series);
        return chart;
    }

    public void preparePlots() {
        this.xYSeries.add(this.setSeries(this.engine.stats.numberOfAnimals, "Liczba zwierząt"));
        this.xYSeries.add(this.setSeries(this.engine.stats.numberOfPlants, "Liczba roślin"));
        this.xYSeries.add(this.setSeries(this.engine.stats.averageEnergy, "Średnia energia"));
        this.xYSeries.add(this.setSeries(this.engine.stats.averageLifeLength, "Średnia długość życia"));
        this.xYSeries.add(this.setSeries(this.engine.stats.averageNumberOfChildren, "Średnia ilość dzieci"));

        for(XYChart.Series<Number, Number> series : this.xYSeries) {
            this.charts.add(this.setChart(series));
        }
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
        Label mapName = new Label(this.name);
        HBox buttonsToChangePlot = new HBox(numberOfAnimalsButton, numberOfPlantsButton, averageEnergyButton,
                averageLifeLengthButton, averageNumberOfChildrenButton);
        VBox plotWithButtons = new VBox(this.charts.get(0), buttonsToChangePlot, mostFrequentGenotype);
        plotWithButtons.setAlignment(Pos.CENTER);
        VBox mapWithButton = new VBox(5, mapName, this.mapGrid, pauseStartButton);
        mapWithButton.setAlignment(Pos.CENTER);
        this.combinedView = new HBox(plotWithButtons, mapWithButton);

        numberOfAnimalsButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.charts.get(0)));
        numberOfPlantsButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.charts.get(1)));
        averageEnergyButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.charts.get(2)));
        averageLifeLengthButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.charts.get(3)));
        averageNumberOfChildrenButton.setOnAction(action ->
                ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(0, this.charts.get(4)));

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
        Button button = (Button) ((VBox) this.combinedView.getChildren().get(1)).getChildren().get(2);
        Button showAllWithDominantGenotype = new Button("Pokaż zwierzęta z domiującym genotypem");
        Button saveMapStats = new Button("Zapisz statystyki mapy");
        ((VBox) this.combinedView.getChildren().get(1)).getChildren().add(showAllWithDominantGenotype);
        ((VBox) this.combinedView.getChildren().get(1)).getChildren().add(saveMapStats);
        button.setText(">");
        showAllWithDominantGenotype.setOnAction(event -> {
            VBox animalsInformation = new VBox(5);
            for(Animal animal : this.engine.map.getAnimalsList()) {
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
            stage.setTitle(this.name);
            stage.show();
        });
        saveMapStats.setOnAction(event -> this.engine.stats.saveToCSV(this.name.replace(" ", "")+"Statystyki"));
    }

    private void continueSimulation() {
        this.isPaused = false;
        this.engine.resume();
        Button button = (Button) ((VBox) this.combinedView.getChildren().get(1)).getChildren().get(2);
        ((VBox) this.combinedView.getChildren().get(1)).getChildren().remove(4);
        ((VBox) this.combinedView.getChildren().get(1)).getChildren().remove(3);
        button.setText("||");
    }

    public void clickGrid() {
        this.mapGrid.getChildren().forEach(item -> item.setOnMouseClicked(event -> {
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
        }));
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
                    animalStage.setOnCloseRequest(event -> this.trackedAnimal = null);
                    animalStage.setTitle("Śledzone zwierzę (" + this.name + ")");
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
        double[] data = new double[]{this.engine.stats.numberOfAnimals, this.engine.stats.numberOfPlants,
                this.engine.stats.averageEnergy, this.engine.stats.averageLifeLength, this.engine.stats.averageNumberOfChildren};
        for(int i = 0; i < data.length; i++) {
            this.xYSeries.get(i).getData().add(new XYChart.Data<>(this.engine.stats.day, data[i]));
            if(this.xYSeries.get(i).getData().size() > 15) {
                this.xYSeries.get(i).getData().remove(0);
            }
        }
    }

    @Override
    public void dayPassed(IWorldMap map) {
        Platform.runLater(() -> {
            this.mapGrid.getChildren().clear();
            this.mapGrid.getRowConstraints().clear();
            this.mapGrid.getColumnConstraints().clear();
            this.mapGrid.setGridLinesVisible(false);
            this.createGrid();
            this.updateSeries();
            ((VBox) this.combinedView.getChildren().get(0)).getChildren().set(2,
                    new Label("Dominujący genotyp:\n" + this.engine.stats.dominantGenotype));
            if(this.trackedAnimal != null) {
                this.updateTrackedAnimalVbox();
            }
        });
    }

    @Override
    public void animalDied(int age) {

    }

    @Override
    public void magicHappened() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Zwierzęta wyewoluowały w sposób magiczny: " + this.name);
            alert.show();
        });
    }
}
