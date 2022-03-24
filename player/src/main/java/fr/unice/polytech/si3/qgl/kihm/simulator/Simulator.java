package fr.unice.polytech.si3.qgl.kihm.simulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.unice.polytech.si3.qgl.kihm.Cockpit;
import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Moving;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;
import fr.unice.polytech.si3.qgl.kihm.equipment.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipment.Sail;
import fr.unice.polytech.si3.qgl.kihm.equipment.Watch;
import fr.unice.polytech.si3.qgl.kihm.landmarks.CheckPoint;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Stream;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.serializers.ObstacleDeserializer;
import fr.unice.polytech.si3.qgl.kihm.serializers.ShapeFunctions;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import lombok.Data;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.*;

import static fr.unice.polytech.si3.qgl.kihm.equipment.Equipment.*;

@Data
public class Simulator {

    private static final int ITERATION = 100;
    private static Simulator instance;
    private String initGameString;
    private Cockpit cockpitSim;
    private int roundNumber = 0;
    private int maxRound = Integer.MAX_VALUE;
    private Wind wind = new Wind();
    private List<String> seaEntitiesJson = new ArrayList<>();
    private boolean watch = false;
    private Random random;

    public static Simulator get() {
        if (instance == null) {
            instance = new Simulator();
            instance.random = new Random();
        }
        return instance;
    }

    public void setInitGameString(String initGameString) {
        this.initGameString = initGameString;
    }

    public String initGameString() {
        String initGame = this.initGameString.replace(" ", "");

        String seaEntitiesString = "seaEntities";
        if (initGame.contains(seaEntitiesString)) {
            String seaEntitiesJson = this.getObject(initGame, seaEntitiesString).replace("\"seaEntities\":[", "");
            if (seaEntitiesJson.charAt(seaEntitiesJson.length() - 1) == ']') {
                seaEntitiesJson = seaEntitiesJson.substring(0, seaEntitiesJson.length() - 1);
            }
            this.seaEntitiesJson = this.jsonArrayToStringList(seaEntitiesJson);
        }

        if (initGame.contains("maxRound")) {
            this.maxRound = Integer.parseInt(this.getValueOf(initGame, "maxRound"));
        }

        String startPosition = "";
        String orientationString = "orientation";
        if (initGame.contains("startingPositions")) {
            String startingPosJson = this.getObject(initGame, "startingPositions");
            startPosition = "\"position\": {\"x\": " + this.getValueOf(startingPosJson, "x") + ",\"y\": " + this.getValueOf(startingPosJson, "y") + ",\"orientation\": " + this.getValueOf(startingPosJson, orientationString) + "}";
        }

        if (initGame.contains("wind")) {
            String windJson = this.getObject(initGame, "wind");
            double orientation = 0.0;
            if (windJson.contains(orientationString)) {
                orientation = Double.parseDouble(this.getValueOf(windJson, orientationString));
            }
            if (windJson.contains("direction")) {
                orientation = Double.parseDouble(this.getValueOf(windJson, "direction"));
            }
            this.wind = new Wind(orientation, Double.parseDouble(this.getValueOf(windJson, "strength")));
        }

        int minCrewSize = 2;
        int maxCrewSize = minCrewSize;
        if (initGame.contains("entities")) {
            maxCrewSize = this.sizeOfObjectArray(this.getObject(initGame, "entities"));
        }
        if (initGame.contains("minumumCrewSize")) {
            minCrewSize = Integer.parseInt(this.getValueOf(initGame, "minumumCrewSize"));
        }
        if (initGame.contains("maximumCrewSize")) {
            maxCrewSize = Integer.parseInt(this.getValueOf(initGame, "maximumCrewSize"));
        }

        int width = 2;
        int length = 1;
        if (initGame.contains("deck")) {
            String deckJson = this.getObject(initGame, "deck");
            width = Integer.parseInt(this.getValueOf(deckJson, "width"));
            length = Integer.parseInt(this.getValueOf(deckJson, "length"));
        }


        StringBuilder sailors = new StringBuilder("\"sailors\": [");
        int crewSize = minCrewSize < maxCrewSize ? this.random.nextInt(minCrewSize, maxCrewSize + 1) : minCrewSize;
        for (int i = 0; i < crewSize; i++) {
            sailors.append("{\"x\": ").append(this.random.nextInt(0, length + 1)).append(", \"y\": ").append(this.random.nextInt(0, width + 1)).append(", \"id\": ").append(i).append(", \"name\": \"sailor").append(i + 1).append("\"}");
            if (i + 1 < crewSize) {
                sailors.append(", ");
            }
        }
        sailors.append("]");

        String ship = this.getObject(initGame, "ship");

        String shape = "";
        if (!ship.contains("shape")) {
            shape = "\"shape\":{\"type\":\"rectangle\", \"width\":" + width + ", \"height\":" + length + ", \"orientation\":" + this.getValueOf(startPosition, orientationString) + "}";
        }

        String shipAttr = startPosition + (shape.equals("") ? "" : "," + shape);
        if (!shipAttr.equals("")) {
            ship = ship.substring(0, ship.indexOf('{') + 1) + shipAttr + ", " + ship.substring(ship.indexOf('{') + 1);
        }

        initGame = "{" + this.getObject(initGame, "goal") + "," + ship + "," + sailors + "}";
        initGame = initGame.replace(" ", "");

        this.cockpitSim = new Cockpit();
        this.cockpitSim.initGame(initGame);
        this.cockpitSim.getGame().update(this.wind, new ArrayList<>());
        Printer.get().config("Simulator InitGame: " + initGame);
        return initGame;
    }

    public String nextRoundString() {
        this.roundNumber++;
        String nextRound = "{" + this.shipToString() + ", " + this.windToString() + ", " + this.visibleEntitiesToString() + "}";
        Printer.get().config("Simulator NextRound: " + nextRound);
        return nextRound;
    }

    public void getActionsString(String actions) {
        ArrayList<String> actionList = new ArrayList<>();
        actions = actions.replace("\\[", "").replace("]", "");
        while (actions.contains("{") || actions.contains("}")) {
            String action = actions.substring(actions.indexOf('{') + 1, actions.indexOf('}'));
            actionList.add(action);
            actions = actions.replace("{" + action + "}", "");
        }

        ArrayList<Moving> movement = new ArrayList<>();
        ArrayList<Action> liftSail = new ArrayList<>();
        ArrayList<Action> lowerSail = new ArrayList<>();
        ArrayList<Turn> turning = new ArrayList<>();
        ArrayList<Action> paddle = new ArrayList<>();
        ArrayList<Action> useWatch = new ArrayList<>();

        for (String action : actionList) {
            String type = this.getValueOf(action, "type");
            final String sailorIdString = "sailorId";
            switch (type) {
                case "MOVING" -> movement.add(new Moving(Integer.parseInt(this.getValueOf(action, sailorIdString)), Integer.parseInt(this.getValueOf(action, "xdistance")), Integer.parseInt(this.getValueOf(action, "ydistance"))));
                case "LIFT_SAIL" -> liftSail.add(new Action(Integer.parseInt(this.getValueOf(action, sailorIdString)), Action.actionTypeEnum.LIFT_SAIL));
                case "LOWER_SAIL" -> lowerSail.add(new Action(Integer.parseInt(this.getValueOf(action, sailorIdString)), Action.actionTypeEnum.LOWER_SAIL));
                case "TURN" -> turning.add(new Turn(Integer.parseInt(this.getValueOf(action, sailorIdString)), Double.parseDouble(this.getValueOf(action, "rotation"))));
                case "OAR" -> paddle.add(new Action(Integer.parseInt(this.getValueOf(action, sailorIdString)), Action.actionTypeEnum.OAR));
                case "USE_WATCH" -> useWatch.add(new Action(Integer.parseInt(this.getValueOf(action, sailorIdString)), Action.actionTypeEnum.USE_WATCH));
                default -> Printer.get().warning("Couldn't find: " + type);
            }
        }

        Ship ship = this.getCockpitSim().getGame().getShip();
        List<Sailor> sailors = this.getCockpitSim().getGame().getSailors();

        // Moving sailors to new location
        for (Moving a : movement) {
            Sailor sailor = sailors.stream().filter(s -> s.id == a.getSailorId()).findFirst().orElse(null);
            if (sailor != null) {
                Point pos = sailor.getPosition();
                sailor.setPosition(new Point(pos.x + a.getXdistance(), pos.y + a.getYdistance()));
            }
        }

        // Oars & Sail
        ArrayList<Oar> leftOarsUsing = new ArrayList<>();
        ArrayList<Oar> rightOarsUsing = new ArrayList<>();
        for (Sailor sailor : sailors) {
            for (Action a : paddle) {
                if (sailor.id == a.getSailorId()) {
                    for (Oar oar : ship.getOarLeft()) {
                        if (sailor.getPosition().equals(oar.getPosition())) {
                            leftOarsUsing.add(oar);
                        }
                    }
                    for (Oar oar : ship.getOarRight()) {
                        if (sailor.getPosition().equals(oar.getPosition())) {
                            rightOarsUsing.add(oar);
                        }
                    }
                }
            }
            for (Action a : liftSail) {
                if (sailor.id == a.getSailorId()) {
                    for (Sail s : ship.getSails()) {
                        if (sailor.getPosition().equals(s.getPosition())) {
                            s.openSail();
                        }
                    }
                }
            }
            for (Action a : lowerSail) {
                if (sailor.id == a.getSailorId()) {
                    for (Sail s : ship.getSails()) {
                        if (sailor.getPosition().equals(s.getPosition())) {
                            s.closeSail();
                        }
                    }
                }
            }
            for (Action a : useWatch) {
                if (sailor.id == a.getSailorId()) {
                    for (Watch w : ship.getWatches()) {
                        if (sailor.getPosition().equals(w.getPosition())) {
                            this.watch = true;
                            break;
                        }
                    }
                }
            }
        }

        Wind gameWind = this.cockpitSim.getGame().getWorld().getWind();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addDeserializer(Obstacle.class, new ObstacleDeserializer()));
        List<Obstacle> entities;
        entities = this.getVisibleObstacles(ship.getPosition(), this.watch ? 5000 : 1000).stream().map(s -> {
            try {
                return objectMapper.readValue(s, Obstacle.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).map(Obstacle.class::cast).toList();
        List<Stream> streams = entities.stream().filter(obstacle -> obstacle.getType() == Obstacle.obstacleTypeEnum.STREAM).map(Stream.class::cast).toList();

        // Const speeds
        double oarSpeed = 165 * (double) (rightOarsUsing.size() + leftOarsUsing.size()) / (ship.getOarLeft().size() + ship.getOarRight().size());
        double windStrength = gameWind.getStrength() * (!ship.getSails().isEmpty() ? ((double) ship.getSails().stream().filter(Sail::isOpened).count() / ship.getSails().size()) : 0.0);
        double angle = (rightOarsUsing.size() - leftOarsUsing.size()) * Math.PI / (ship.getOarLeft().size() + ship.getOarRight().size());
        if (!turning.isEmpty()) angle += turning.get(0).getRotation();

        // Updating position
        ship.setPosition(this.calculatingNextRoundPosition(ship.getPosition(), ship.getShape(), oarSpeed, angle, windStrength, gameWind.getOrientation(), streams));

        // Updating Shape
        String newShapeJson = "{\"type\":\"rectangle\",\"width\":" + ship.getDeck().getWidth() + ",\"height\":" + ship.getDeck().getLength() + ",\"orientation\":" + ship.getPosition().getOrientation() + "}";
        try {
            JsonNode shapeNode = new ObjectMapper().readTree(newShapeJson);
            ship.setShape(new ShapeFunctions().nodeToShape(ship.getPosition(), shapeNode));
        } catch (Exception e) {
            e.printStackTrace();
        }

        CheckPoint nextCheckpoint = this.cockpitSim.getGame().getWorld().getCheckpoints().get(0);
        if (this.collision(nextCheckpoint.getShape(), ship.getShape()) || nextCheckpoint.distance(ship.getPosition()) <= nextCheckpoint.getShape().getBounds().getHeight() * 0.5) {
            this.cockpitSim.getGame().getWorld().getCheckpoints().remove(nextCheckpoint);
        }
    }

    public Position calculatingNextRoundPosition(Position position, Shape shape, double oarSpeed, double turningAngle, double windStrength, double windOrientation, List<Stream> streams) {
        Shape s = shape;
        Position pos = new Position(position.getX(), position.getY(), position.getOrientation());
        AffineTransform affineTransform = new AffineTransform();
        for (int i = 0; i < ITERATION; i++) {
            double windSpeed = windStrength * Math.cos(windOrientation - pos.getOrientation());
            double xMovement = ((oarSpeed + windSpeed) / ITERATION) * Math.cos(pos.getOrientation());
            double yMovement = ((oarSpeed + windSpeed) / ITERATION) * Math.sin(pos.getOrientation());
            pos.moveX(xMovement);
            pos.moveY(yMovement);
            affineTransform.setToTranslation(xMovement, yMovement);
            s = affineTransform.createTransformedShape(s);
            if (!streams.isEmpty()) {
                Shape finalShape = s;
                for (Stream stream : streams.stream().filter(stream -> this.collision(finalShape, stream.getShape())).toList()) {
                    double streamSpeedX = (stream.getStrength() / ITERATION) * Math.cos(stream.getPosition().getOrientation());
                    double streamSpeedY = (stream.getStrength() / ITERATION) * Math.sin(stream.getPosition().getOrientation());
                    pos.moveX(streamSpeedX);
                    pos.moveY(streamSpeedY);
                    affineTransform.setToTranslation(streamSpeedX, streamSpeedY);
                    s = affineTransform.createTransformedShape(s);
                }
            }
            pos.rotate(turningAngle / ITERATION);
            affineTransform.setToRotation(turningAngle / ITERATION);
            s = affineTransform.createTransformedShape(s);
        }
        return pos;
    }

    public double calculateOarSpeed(Map<String, Double> layout, int totalOars) {
        return 165 * (layout.get(OAR_RIGHT) + layout.get(OAR_LEFT)) / totalOars;
    }

    public double calculateTurningAngle(Map<String, Double> layout, int totalOars) {
        return (layout.get(OAR_RIGHT) - layout.get(OAR_LEFT)) * Math.PI / totalOars + layout.get(RUDDER);
    }

    public double calculateWindStrength(Wind wind, List<Sail> sails) {
        return wind.getStrength() * (sails.isEmpty() ? 0.0 : sails.stream().filter(Sail::isOpened).count() / (double) sails.size());
    }

    public List<String> getVisibleObstacles(Position position, double range) {
        List<String> seen = new ArrayList<>();
        Shape shown = new Ellipse2D.Double(position.getX() - range, position.getY() - range, 2 * range, 2 * range);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addDeserializer(Obstacle.class, new ObstacleDeserializer()));
        for (String s : this.seaEntitiesJson) {
            try {
                if (this.collision(shown, objectMapper.readValue(s, Obstacle.class).getShape())) {
                    seen.add(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return seen;
    }

    public boolean collision(Shape a, Shape b) {
        Area zone = new Area(a);
        zone.intersect(new Area(b));
        return !zone.isEmpty();
    }

    protected List<String> jsonArrayToStringList(String jsonArray) {
        List<String> array = new ArrayList<>();
        while (!jsonArray.isEmpty()) {
            if (jsonArray.charAt(0) == ',') {
                jsonArray = jsonArray.substring(1);
            }
            int i = 0;
            int end = 0;
            for (char c : jsonArray.toCharArray()) {
                if (c == ',' && i == 0) {
                    break;
                }
                if (c == '{') {
                    i++;
                }
                if (c == '}') {
                    i--;
                }
                end++;
            }
            String jsonObject = jsonArray.substring(0, end);
            jsonArray = jsonArray.substring(end);
            array.add(jsonObject);
        }
        return array;
    }

    protected String getValueOf(String jsonObject, String key) {
        int start = jsonObject.indexOf(key) + key.length() + 2;
        int end = jsonObject.indexOf(',', start);
        if (end == -1) {
            String tmp = jsonObject.substring(start);
            return tmp.replace("\"", "").replace("}", "").replace("]", "");
        }
        String tmp = jsonObject.substring(start, end);
        return tmp.replace("\"", "");
    }

    protected String getObject(String jsonObjects, String jsonObject) {
        int start = jsonObjects.indexOf(jsonObject) - 1;
        int end = 0;
        int count = 0;
        String objectString = jsonObjects.substring(start);
        for (char c : objectString.toCharArray()) {
            if (c == ',' && count == 0) {
                break;
            }
            if (c == '{' || c == '[') {
                count++;
            }
            if (c == '}' || c == ']') {
                count--;
            }
            end++;
        }
        end += count;
        return objectString.substring(0, end);
    }

    protected int sizeOfObjectArray(String jsonObject) {
        String tmp = jsonObject.substring(jsonObject.indexOf('['));
        int count = 0;
        int i = 0;
        int j = 0;
        for (char c : tmp.toCharArray()) {
            if (c == '[') {
                i++;
            }
            if (c == ']') {
                i--;
            }
            if (c == '{') {
                if (j == 0) {
                    count++;
                }
                j++;
            }
            if (c == '}') {
                j--;
            }
            if (i == 0) {
                break;
            }
        }
        return count;
    }

    public boolean notFinished() {
        boolean status = this.cockpitSim.getGame().getWorld().getCheckpoints().isEmpty();
        if (status) {
            Printer.get().info("=== Game has finished in " + Simulator.get().getRoundNumber() + " tours ===");
        }
        return !status;
    }

    protected String shipToString() {
        return "\"ship\": " + this.cockpitSim.getGame().getShip();
    }

    protected String windToString() {
        return "\"wind\": " + this.wind;
    }

    protected String visibleEntitiesToString() {
        StringBuilder entities = new StringBuilder();
        List<String> visible = this.getVisibleObstacles(this.cockpitSim.getGame().getShip().getPosition(), this.watch ? 5000 : 1000);
        this.watch = false;
        for (String entity : visible) {
            entities.append(entity).append(", ");
        }
        if (!entities.isEmpty() && entities.charAt(entities.length() - 1) == ' ') {
            entities.deleteCharAt(entities.length() - 1);
        }
        if (!entities.isEmpty() && entities.charAt(entities.length() - 1) == ',') {
            entities.deleteCharAt(entities.length() - 1);
        }
        return "\"visibleEntities\":[" + entities + "]";
    }

    public void reset() {
        this.initGameString = null;
        this.cockpitSim = null;
        this.roundNumber = 0;
        this.maxRound = Integer.MAX_VALUE;
        this.wind = new Wind();
        this.seaEntitiesJson = new ArrayList<>();
        this.watch = false;
    }

    public void run(String initGameString) {
        Simulator.get().setInitGameString(initGameString);
        Cockpit c = new Cockpit();
        c.initGame(Simulator.get().initGameString());
        while (Simulator.get().notFinished() && this.roundNumber < this.maxRound) {
            Simulator.get().getActionsString(c.nextRound(Simulator.get().nextRoundString()));
        }
        Printer.get().flush();
    }
}
