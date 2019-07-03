import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;

public class FerrySystem extends Application {
    private ArrayList<Vehicle> vehiclesLeftSide = new ArrayList<>();
    private ArrayList<Vehicle> vehiclesRightSide = new ArrayList<>();
    private Label labelVehicleType = new Label("Vehicle type: ");
    private Label labelVehicleWeight = new Label("Vehicle weight: ");
    private Label labelLoadWeight = new Label("Load weight: ");
    private Label labelWeightDistribution = new Label("Weight distribution: ");
    private Label labelSpace = new Label("Space: ");

    @Override
    public void start(Stage primaryStage) {
        initializeList();

        Label labelHeadline = new Label("Ferry System 2019");
        labelHeadline.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));

        Label labelExtraInfo = new Label("Click a vehicle for info:");
        labelExtraInfo.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

        VBox vBoxExtraInfo = new VBox(labelExtraInfo, labelVehicleType, labelVehicleWeight, labelLoadWeight, labelWeightDistribution, labelSpace);
        vBoxExtraInfo.setPadding(new Insets(0, 50, 0, 0));

        int totalWeightLeftSide = getWeight(vehiclesLeftSide);
        int totalWeightRightSide = getWeight(vehiclesRightSide);

        Label labelWeightLeftSide = new Label("Total weight: " + totalWeightLeftSide + " kg.");
        labelWeightLeftSide.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        Label labelWeightRightSide = new Label("Total weight: " + totalWeightRightSide + " kg.");
        labelWeightRightSide.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        Label labelDifferenceInWeight = new Label(String.format("Difference: %.2f %%", Math.abs((double) (totalWeightLeftSide - totalWeightRightSide) / ((totalWeightLeftSide + totalWeightRightSide) / 2) * 100)));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(30);
        gridPane.addRow(0, getPane(vehiclesLeftSide, "l"), getPane(vehiclesRightSide, "r"), vBoxExtraInfo);
        gridPane.addRow(1, labelWeightLeftSide, labelWeightRightSide, labelDifferenceInWeight);
        GridPane.setHalignment(labelWeightLeftSide, HPos.CENTER);
        GridPane.setHalignment(labelWeightRightSide, HPos.CENTER);

        VBox vBox = new VBox(labelHeadline, gridPane);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        primaryStage.setTitle("Ferry System 2019");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initializeList() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        int availableSpaces = 200;

        // This loop bases the number of the different vehicles in the list on the amount of space they take up on the ferry.
        // Using this loop, cars take up 75 % ferry space, trucks with no load take up 5 % ferry space and loaded trucks take up 20 % ferry space
//        while (availableSpaces > 0) {
//            if (availableSpaces > 150) {
//                vehicles.add(new Car(800 + (int) (Math.random() * 1201)));
//                --availableSpaces;
//            } else if (availableSpaces > 140) {
//                vehicles.add(new Truck(1500 + (int) (Math.random() * 2001), 0, 0));
//                --availableSpaces;
//            } else {
//                int loadWeight = 3000 + (int) (Math.random() * 12001);
//                int loadSpace = loadWeight / 3000;
//
//                if (availableSpaces >= 1 + loadSpace) {
//                    vehicles.add(new Truck(1500 + (int) (Math.random() * 2001), loadSpace, loadWeight));
//                    availableSpaces -= 1 + loadSpace;
//                } else
//                    break;
//            }
//        }

        // This loop bases the number of the different vehicles in the list on the amount of space they take up in the list itself.
        // Using this loop, cars take up 75 % list space, trucks with no load take up 5 % list space and loaded trucks take up 20 % list space
        while (availableSpaces > 0) {
            if (availableSpaces > 110) {
                int loadWeight = 3000 + (int) (Math.random() * 12001);
                int loadSpace = loadWeight < 7000 ? 2 : (loadWeight < 11000 ? 3 : 4);

                vehicles.add(new Truck(1500 + (int) (Math.random() * 2001), loadSpace, loadWeight));
                availableSpaces -= 1 + loadSpace;
            } else if (availableSpaces > 100) {
                vehicles.add(new Truck(1500 + (int) (Math.random() * 2001), 0, 0));
                --availableSpaces;
            } else {
                vehicles.add(new Car(800 + (int) (Math.random() * 1201)));
                --availableSpaces;
            }
        }

//        printStuffForDebugging(vehicles);

//        Collections.sort(vehicles, Collections.reverseOrder());
        vehicles.sort(Comparator.comparing(Vehicle::getWeightDistribution).reversed()); // This is cooler

        partitionList(vehicles);
    }

    public void partitionList(ArrayList<Vehicle> vehicles) {
        int maxSpaceRight = 0, maxSpaceLeft = 0;

        for (Vehicle v : vehicles) {
            if (getSumOfWeightDistribution(vehiclesLeftSide) <= getSumOfWeightDistribution(vehiclesRightSide) && maxSpaceLeft + v.getSpace() <= 100) { // Add the vehicle to the left side 1) if the sum of the weight distribution on the left side is less than or equal to the sum of the weight distribution on the right side and 2) if there is sufficient space for the vehicle
                vehiclesLeftSide.add(v);
                maxSpaceLeft += v.getSpace();
            } else if (maxSpaceRight + v.getSpace() <= 100) { // Else add the vehicle to the right side, if the right side has space
                vehiclesRightSide.add(v);
                maxSpaceRight += v.getSpace();
            } else // Add the vehicle to the left side even if the sum of the weight distribution on the left side is greater than the sum of the weight distribution on the right side, if the other side is full. No body is left behind. There is no need to check if the left side has space here - it is guaranteed.
                vehiclesLeftSide.add(v);
        }
    }

    public int getSumOfWeightDistribution(ArrayList<Vehicle> vehicles) {
        int sum = 0;

        for (Vehicle v : vehicles)
            sum += v.getWeightDistribution();

        return sum;
    }

    public GridPane getPane(ArrayList<Vehicle> vehicles, String side) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);

        boolean[][] grid = new boolean[10][10];

        for (Vehicle v : vehicles) {
            ArrayList<int[]> indices = checkSpace(v.getSpace(), grid, side);

            for (int i = 0; i < indices.size(); i++) {
                int[] index = indices.get(i);
                grid[index[0]][index[1]] = true; // Set the corresponding index to true, since the space is now taken

                StackPane pane = new StackPane();
                pane.setPrefWidth(40);
                pane.setPrefHeight(50);

                if (indices.size() == 1) // If the vehicle only takes up one space, only one pane is needed
                    pane.setStyle("-fx-border-width: 4; -fx-border-color: black;" + ((v instanceof Car) ? "-fx-background-color: blue" : "-fx-background-color: red"));
                else { // If the vehicle takes up more than one space, multiple panes with different border styles are needed
                    if (i == 0)
                        pane.setStyle("-fx-border-style: solid solid hidden solid; -fx-border-width: 4; -fx-border-color: black;" + ((v instanceof Car) ? "-fx-background-color: blue" : "-fx-background-color: red"));
                    else if (i == indices.size() - 1)
                        pane.setStyle("-fx-border-style: hidden solid solid solid; -fx-border-width: 4; -fx-border-color: black;" + ((v instanceof Car) ? "-fx-background-color: blue" : "-fx-background-color: red"));
                    else
                        pane.setStyle("-fx-border-style: hidden solid hidden solid; -fx-border-width: 4; -fx-border-color: black;" + ((v instanceof Car) ? "-fx-background-color: blue" : "-fx-background-color: red"));
                }

//                pane.getChildren().add(new Text(v.getSpace() + ""));

                pane.setOnMouseClicked(event -> clickVehicle(v)); // Add the EventHandler for showing info upon mouse clicks

                gridPane.add(pane, index[1], index[0]); // The add method takes the column parameter before the row parameter, so the indices must be reversed
            }
        }

        return gridPane;
    }

    public ArrayList<int[]> checkSpace(int spaceNeeded, boolean[][] grid, String side) {
        ArrayList<int[]> indices = new ArrayList<>();

        if (side.equals("r")) // If the side is right, start by placing the heaviest vehicles to the left
            for (int i = 0; i < grid.length; i++) {
                int spaceAvailable = 0;
                indices.clear();
                for (int j = 0; j < grid.length; j++) {
                    if (!grid[j][i]) {
                        ++spaceAvailable;
                        indices.add(new int[]{j, i});
                    } else {
                        spaceAvailable = 0;
                        indices.clear();
                    }

                    if (spaceAvailable == spaceNeeded)
                        return indices;
                }
            }
        else // If the side is left, start by placing the heaviest vehicles to the right
            for (int i = grid.length - 1; i >= 0; i--) {
                int spaceAvailable = 0;
                indices.clear();
                for (int j = 0; j < grid.length; j++) {
                    if (!grid[j][i]) {
                        ++spaceAvailable;
                        indices.add(new int[]{j, i});
                    } else {
                        spaceAvailable = 0;
                        indices.clear();
                    }

                    if (spaceAvailable == spaceNeeded)
                        return indices;
                }
            }

        return indices;
    }


    public int getWeight(ArrayList<Vehicle> vehicles) {
        int weight = 0;

        for (Vehicle v : vehicles)
            weight += v.getWeightDistribution();

        return weight;
    }

    public void clickVehicle(Vehicle v) {
        labelVehicleType.setText((v instanceof Car) ? "Vehicle type: Car" : "Vehicle type: Truck");
        labelVehicleWeight.setText("Vehicle weight: " + v.getVehicleWeight());
        labelLoadWeight.setText("Load weight: " + ((v instanceof Truck) ? ((Truck) v).getLoadWeight() : "N/A"));
        labelWeightDistribution.setText("Weight distribution: " + v.getWeightDistribution());
        labelSpace.setText("Space: " + v.getSpace());
    }

    public void printStuffForDebugging(ArrayList<Vehicle> vehicles) {
        int counter = 1, spaceCounter = 0;

        for (Vehicle v : vehicles) {
            System.out.println(counter++ + ": " + (v instanceof Truck ? "Truck" : "Car") + " " + v.getWeightDistribution() + " " + v.getSpace());
            spaceCounter += v.getSpace();
        }

        System.out.println(spaceCounter);

        int rSpace = 0, lSpace = 0, rWeight = 0, lWeight = 0;

        for (Vehicle v : vehiclesRightSide) {
            rWeight += v.getWeightDistribution();
            rSpace += v.getSpace();
        }

        for (Vehicle v : vehiclesLeftSide) {
            lWeight += v.getWeightDistribution();
            lSpace += v.getSpace();
        }

        System.out.println(lWeight);
        System.out.println(rWeight);
        System.out.println(lSpace);
        System.out.println(rSpace);
    }
}
