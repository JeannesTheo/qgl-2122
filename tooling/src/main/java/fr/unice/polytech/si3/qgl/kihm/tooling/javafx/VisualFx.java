package fr.unice.polytech.si3.qgl.kihm.tooling.javafx;

import fr.unice.polytech.si3.qgl.kihm.Cockpit;
import fr.unice.polytech.si3.qgl.kihm.landmarks.CheckPoint;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.simulator.Simulator;
import fr.unice.polytech.si3.qgl.kihm.structures.Parser;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

/*
--module-path $PATH_TO_FX$ --add-modules=javafx.controls,javafx.fxml
 */
public class VisualFx extends Application {

    private final Cockpit cockpit = new Cockpit();
    private final double initWidth = 0.75 * Toolkit.getDefaultToolkit().getScreenSize().width;
    private final double initHeight = 0.75 * Toolkit.getDefaultToolkit().getScreenSize().height;
    private final StackPane root = new StackPane();
    private final double draggingSensitivity = 5;
    private final ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<CheckPoint> checkPoints;
    private ArrayList<CheckPoint> checkPointsShown;
    private Ship ship;
    private Text text;
    private double deZoom = 3;
    private double xOffset = 0;
    private double yOffset = 0;
    private double lastXOffset;
    private double lastYOffset;
    private int roundNumber = 0;
    private Stage primaryStage;

    public static void main(String[] args) {
        Simulator.get().setInitGameString(Parser.fileToString("tooling/src/main/resources/Games/week6/initGame.json"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.cockpit.initGame(Simulator.get().initGameString());
        this.ship = this.cockpit.getGame().getShip();
        this.checkPoints = (ArrayList<CheckPoint>) this.cockpit.getGame().getWorld().getCheckpoints();
        this.checkPointsShown = new ArrayList<>(this.checkPoints);
        this.centerScreen();
        this.updateGame();

        // Init
        this.primaryStage.setScene(new Scene(this.root, initWidth, initHeight));
        this.primaryStage.setTitle("KIHM - Le Fils Rouge");
//        this.primaryStage.getIcons().add(new Image("tooling/src/main/resources/icon.png"));
        this.primaryStage.getScene().setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case ESCAPE -> this.primaryStage.close();
                case R -> {
                    this.deZoom = 3;
                    this.centerScreen();
                    this.updateScene();
                }
                case RIGHT -> {
                    this.nextRound();
                    this.updateScene();
                }
            }
        });
        this.primaryStage.getScene().setOnScroll(mouseEvent -> {
            this.deZoom += mouseEvent.getDeltaY() > 0 ? -1 : 1;
            this.deZoom = Math.max(this.deZoom, 1);
            this.updateScene();
        });
        this.primaryStage.getScene().setOnMousePressed(mouseEvent -> {
            this.lastXOffset = mouseEvent.getSceneX();
            this.lastYOffset = mouseEvent.getSceneY();
        });
        this.primaryStage.getScene().setOnMouseDragged(mouseEvent -> {
            this.xOffset += (mouseEvent.getSceneX() - this.lastXOffset) * this.draggingSensitivity;
            this.yOffset += (mouseEvent.getSceneY() - this.lastYOffset) * this.draggingSensitivity;
            this.lastXOffset = mouseEvent.getSceneX();
            this.lastYOffset = mouseEvent.getSceneY();
            this.updateScene();
        });

        // Background to Water
        this.drawOcean();

        // Draw world
        this.drawCheckPoints();
        this.drawShip();
        this.primaryStage.show();
    }

    private void updateGame() {
        this.ship = this.cockpit.getGame().getShip();
        this.checkPoints = (ArrayList<CheckPoint>) cockpit.getGame().getWorld().getCheckpoints();
    }

    private void updateScene() {
        this.root.getChildren().clear();
        this.drawOcean();
        this.drawCheckPoints();
        this.drawLines();
        this.drawShip();
    }

    private void centerScreen() {
        this.xOffset = -1 * this.ship.getPosition().getX();
        this.yOffset = -1 * this.ship.getPosition().getY();
    }

    private void drawOcean() {
        Node bg = new VBox();
        bg.setStyle("-fx-background-color: #37b0ee");
        bg.setOnMouseClicked(event -> System.out.println(event.getX() + "," + event.getY()));
        this.root.getChildren().add(bg);
    }

    private void drawCheckPoints() {
        int number = 1;
        for (CheckPoint cp : this.checkPointsShown) {
            this.text = new Text(Integer.toString(number++));
            this.text.setTranslateX(-5);
            this.text.setTranslateY(5);
            Circle circle = new Circle();
            circle.setRadius(cp.getShape().getBounds().height / this.deZoom);
            this.root.getChildren().add(this.drawShape(circle, Color.web("#ffdd64"), cp));
        }
    }

    private void drawShip() {
        this.text = new Text(this.ship.getName());
        this.text.setTranslateX(-20);
        this.text.setTranslateY(20);
        double[] points = new double[]{30, 0, 20, 10, -10, 10, -10, -10, 20, -10};
        for (int i = 0; i < points.length; i++) {
            points[i] /= deZoom / 2;
        }
        Polygon polygon = new Polygon(points);
        polygon.getTransforms().add(new Rotate(Math.toDegrees(this.ship.getPosition().getOrientation()), 0, 0));
        this.root.getChildren().add(this.drawShape(polygon, Color.web("#b05d00"), this.ship));
    }

    private Group drawShape(Shape shape, Paint colour, Object obj) {
        shape.setFill(colour);
        shape.setOnMouseClicked(mouseEvent -> System.out.println(obj));
        Group drawnShape;
        if (this.text != null) {
            drawnShape = new Group(shape, this.text);
            this.text = null;
        } else {
            drawnShape = new Group(shape);
        }
        double x = 0;
        double y = 0;
        if (obj instanceof Ship s) {
            x = s.getPosition().getX();
            y = s.getPosition().getY();
        }
        if (obj instanceof CheckPoint cp) {
            x = cp.getPosition().getX();
            y = cp.getPosition().getY();
        }
        if (obj instanceof Line l) {
            x = l.getStartX();
            y = l.getStartY();
        }
        x += this.xOffset;
        y += this.yOffset;
        drawnShape.setTranslateX(x / this.deZoom);
        drawnShape.setTranslateY(y / this.deZoom);
        return drawnShape;
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        Line line = new Line();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        this.lines.add(line);
    }

    private void drawLines() {
        for (Line line : this.lines) {
            this.root.getChildren().add(this.drawShape(line, Color.web("#c02928"), line));
        }
    }

    private double[] shapeToPoints(java.awt.Shape s) {
        PathIterator i = s.getPathIterator(null);
        ArrayList<Double> listPoints = new ArrayList<>();
        float[] coords = new float[6];
        while (!i.isDone()) {
            i.currentSegment(coords);
            listPoints.add(25 * coords[0] / this.deZoom);
            listPoints.add(25 * coords[1] / this.deZoom);
            i.next();
        }
        listPoints.add(listPoints.get(0));
        listPoints.add(listPoints.get(1));
        double[] points = new double[2 * listPoints.size()];
        int j = 0;
        for (Double d : listPoints) {
            points[j++] = d;
        }
        return points;
    }

    private void nextRound() {
        this.roundNumber++;
        if (Simulator.get().notFinished()) {
            Position start = this.ship.getPosition();
            Simulator.get().getActionsString(this.cockpit.nextRound(Simulator.get().nextRoundString()));
            this.updateGame();
            this.centerScreen();
            Position end = this.ship.getPosition();
            this.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
        this.primaryStage.setTitle("KIHM - Le Fils Rouge - Tour n°" + this.roundNumber);
    }
}