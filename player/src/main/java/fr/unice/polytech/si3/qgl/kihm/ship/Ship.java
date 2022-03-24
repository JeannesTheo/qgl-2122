package fr.unice.polytech.si3.qgl.kihm.ship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unice.polytech.si3.qgl.kihm.equipment.*;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

public class Ship {

    /**
     * Name of the Ship.
     */
    private String name;
    /**
     * Life.
     */
    private int life;
    /**
     * Position of the boat.
     */
    private Position position;
    /**
     * Shape of the ship.
     */
    @JsonIgnore
    private Shape shape;
    /**
     * Dimensions width and height of the boat.
     */
    private Deck deck;
    /**
     * List of Equipment on the ship.
     */
    private List<Equipment> equipments;

    public Ship() {
        name = "Default";
        life = 0;
        position = new Position(0, 0, 0);
        equipments = new ArrayList<>();
        deck = new Deck();
        shape = new Area();
    }

    /**
     * Constructor of the ship.
     *
     * @param name       name of the ship
     * @param life       life of the ship
     * @param position   position x,y of the ship
     * @param shape      shape of the ship
     * @param deck       dimensions of the ship's deck
     * @param equipments List of equipments
     */
    public Ship(String name, int life, Position position, Shape shape, Deck deck, List<Equipment> equipments) {
        this.name = name;
        this.life = life;
        this.position = position;
        this.shape = shape;
        this.deck = deck;
        this.equipments = equipments;
    }

    public boolean hasEquipment(Equipment.equipmentTypeEnum type) {
        return this.equipments.stream().anyMatch(equipment -> equipment.getType() == type);
    }

    public List<Equipment> getEquipment(Equipment.equipmentTypeEnum type) {
        return this.equipments.stream().filter(equipment -> equipment.getType() == type).toList();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public List<Oar> getOarLeft() {
        return this.getEquipment(Equipment.equipmentTypeEnum.OAR).stream().map(Oar.class::cast).filter(rudder -> rudder.getY() == 0).toList();
    }

    public List<Oar> getOarRight() {
        return this.getEquipment(Equipment.equipmentTypeEnum.OAR).stream().map(Oar.class::cast).filter(rudder -> rudder.getY() == this.deck.getWidth() - 1).toList();
    }

    public List<Rudder> getRudders() {
        return this.getEquipment(Equipment.equipmentTypeEnum.RUDDER).stream().map(Rudder.class::cast).toList();
    }

    public List<Sail> getSails() {
        return this.getEquipment(Equipment.equipmentTypeEnum.SAIL).stream().map(Sail.class::cast).toList();
    }

    public List<Watch> getWatches() {
        return this.getEquipment(Equipment.equipmentTypeEnum.WATCH).stream().map(Watch.class::cast).toList();
    }

    public List<Canon> getCanons() {
        return this.getEquipment(Equipment.equipmentTypeEnum.CANON).stream().map(Canon.class::cast).toList();
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        if (life != ship.life) return false;
        if (!name.equals(ship.name)) return false;
        if (!position.equals(ship.position)) return false;
        if (!shape.equals(ship.shape)) return false;
        if (!deck.equals(ship.deck)) return false;
        return equipments.equals(ship.equipments);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + life;
        result = 31 * result + position.hashCode();
        result = 31 * result + shape.hashCode();
        result = 31 * result + deck.hashCode();
        result = 31 * result + equipments.hashCode();
        return result;
    }

    // TODO Only sets ship shape to rectangle
    @Override
    public String toString() {
        return "{\"type\": \"ship\", \"life\": " + this.life + ", \"position\": " + this.position + ", \"name\": \"" + this.name + "\", \"deck\": " + this.deck + ", \"entities\": " + this.equipments + ", \"shape\": {\"type\": \"rectangle\", \"width\": " + this.deck.getWidth() + ", \"height\": " + this.deck.getLength() + ", \"orientation\": " + this.getPosition().getOrientation() + "}}";
    }
}
