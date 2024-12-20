package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import militarylogistics.EarthOrbitSimulation;
import militarylogistics.TroopNodesFly;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class Main extends Application {

    // Rotation properties
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final SimpleDoubleProperty angleX = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty angleY = new SimpleDoubleProperty(0);
    private List<Troop> troopNames; // List to store troop names
    private Stage primaryStage; // Reference to the primary stage
    @Override
    public void start(Stage primaryStage) {
    	 this.primaryStage = primaryStage; // Store the primary stage reference
    	 // Create an instance of LogisticsVisualization2 to load troop names
        LogisticsVisualization2 logisticsVis = new LogisticsVisualization2(null); // Pass null for the initial scene
        Pane troopPane = logisticsVis.display(); // Call display to initialize and load troop names
        troopNames = logisticsVis.getTroopNames(); // Now get the troop names

        // Ensure troop names are loaded
        if (troopNames == null || troopNames.isEmpty()) {
            System.out.println("No troop names available.");
            return; // Exit if no troop names are available
        }

        // Create the main grid for the three title columns
        GridPane mainGridPane = new GridPane();
        mainGridPane.setStyle("-fx-background-color: #1a1a1a;"); // Light black background

        // Set main column constraints for three equal columns
        for (int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(25); // Each main column takes up 1/3 of the width
            columnConstraints.setHgrow(Priority.ALWAYS); // Ensure the columns grow evenly
            mainGridPane.getColumnConstraints().add(columnConstraints);
        }

        // Titles for each main column
        String[] columnTitles = {"Offutt Air Force Base", "Naval Air Station North Island", "Fort Gregg-Adams", "1st Marine Corps District"};
        
        // Loop through each title column
        for (int i = 0; i < columnTitles.length; i++) {
            // Create and style the title label
            Label title = new Label(columnTitles[i]);
            title.setFont(new Font(18)); // Set font size for title
            title.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-text-fill: #39FF14;"); // Neon green text
            title.setAlignment(Pos.CENTER); // Center the title within each column
            GridPane.setHalignment(title, HPos.CENTER); // Center alignment in the grid cell
            mainGridPane.add(title, i, 0); // Add title label to the main grid

            // Create a nested grid pane for names under each title
            GridPane nestedGridPane = new GridPane();
            nestedGridPane.setHgap(10); // Add horizontal gap between sub-columns
            nestedGridPane.setVgap(5);  // Add vertical gap between rows

            // Add an initial spacer column at the start of each nested grid
            ColumnConstraints spacerColumn = new ColumnConstraints();
            spacerColumn.setPercentWidth(10); // Adjust the width of the spacer as needed
            nestedGridPane.getColumnConstraints().add(spacerColumn);

            // Set constraints for the three main sub-columns
            for (int j = 0; j < 3; j++) {
                ColumnConstraints nestedColumnConstraints = new ColumnConstraints();
                nestedColumnConstraints.setPercentWidth(10); // Each sub-column takes up ~30% after the spacer
                nestedGridPane.getColumnConstraints().add(nestedColumnConstraints);
            }

            // Populate the nested grid with troop names in smaller font
            int count = 0;
            for (int k = i; k < troopNames.size(); k += 3) { // Distribute names across each title
                Troop troop = troopNames.get(k); // Get each troop name and branch

                // Create a label for each troop with smaller font size
                Label troopLabel = new Label(troop.getName() + " (" + troop.getBranch() + ")");
                troopLabel.setFont(new Font(10)); // Smaller font for names
                troopLabel.setStyle("-fx-text-fill: #39FF14;"); // Neon green text

                // Calculate sub-column and row for the nested grid
                int subColumnIndex = (count % 3) + 1; // Offset by 1 to start after the spacer
                int rowIndex = count / 3; // Determine the row index within the nested grid
                nestedGridPane.add(troopLabel, subColumnIndex, rowIndex); // Add the label to the nested grid

                count++;
            }

            // Add the nested grid pane under each title column in the main grid
            mainGridPane.add(nestedGridPane, i, 1);
        }

        // Combine troopPane and mainGridPane in a VBox layout
        VBox layout = new VBox(10, troopPane, mainGridPane);
        Scene troopScene = new Scene(layout, 1800, 1000);

        // Add a delay before switching to the second scene
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            createEarthAndSpaceScene();
        });
        pause.play();

        primaryStage.setTitle("Troop Names");
        primaryStage.setScene(troopScene);
        primaryStage.show();
    }

	private void createEarthAndSpaceScene() {
		  try {
			  StackPane root = new StackPane();

		        Galaxy galaxy = new Galaxy();
		        ImageView galaxyBackground = galaxy.getGalaxyBackground();
		        root.getChildren().add(galaxyBackground);

		        Earth earth = new Earth();
		        Sphere earthSphere = earth.getEarthSphere();

		        // Create an instance of LogisticsVisualization2 to load troop names and base nodes
		        LogisticsVisualization2 logisticsVis = new LogisticsVisualization2(null);
		        logisticsVis.display(); 
		        List<BaseNode> baseNodes = logisticsVis.getBaseNodes(); // Use pre-initialized base nodes
		        List<Troop> troopNames = logisticsVis.getTroopNames();

		        // Fixed center point for the Earth sphere
		        Point3D earthCenter = new Point3D(earthSphere.getTranslateX(), earthSphere.getTranslateY(), earthSphere.getTranslateZ());

		        List<TroopNodesFly> allTroopBalls = new ArrayList<>();
		        for (BaseNode baseNode : baseNodes) {
		            baseNode.generateTroopBalls(baseNode.getTroopCount(), earthSphere);
		            allTroopBalls.addAll(baseNode.troopNodes);
		        }

		        Group earthGroup = new Group(earthSphere);
		        earthGroup.getChildren().addAll(baseNodes.stream().map(BaseNode::getBaseSphere).toArray(Node[]::new));

		        Group troopGroup = new Group();
		        troopGroup.getChildren().addAll(allTroopBalls);
		        Troop myTrooper = troopNames.get(0);
		        EarthOrbitSimulation orbitSim = new EarthOrbitSimulation(myTrooper, allTroopBalls, earthCenter);
		        orbitSim.startOrbitSimulation(logisticsVis);

		        SmartGroup smartGroup = new SmartGroup();
		        smartGroup.getChildren().addAll(earthGroup, troopGroup);

		        PointLight pointLight = new PointLight(Color.WHITE);
		        pointLight.setTranslateX(500);
		        pointLight.setTranslateY(-300);
		        pointLight.setTranslateZ(-500);
		        AmbientLight ambientLight = new AmbientLight(Color.rgb(200, 200, 200, 0.3));

		        root.getChildren().addAll(pointLight, ambientLight, smartGroup);

		        Scene scene = new Scene(root, 1400, 1000, true);
		        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		        initMouseControl(smartGroup, scene);

		        scene.setOnKeyPressed(event -> {
		            if (event.getCode() == KeyCode.ESCAPE) {
		                Platform.exit();
		            }
		        });

		        primaryStage.setScene(scene);
		        primaryStage.setTitle("Logistics Network with Rotatable and Zoomable Earth");
		        primaryStage.show();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
    // Initialize mouse control for rotating the Earth and scene
    private void initMouseControl(SmartGroup group, Scene scene) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        group.getTransforms().addAll(xRotate, yRotate); // Add rotations to the group

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}