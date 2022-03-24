package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Moving;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;
import fr.unice.polytech.si3.qgl.kihm.equipment.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipment.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipment.Rudder;
import fr.unice.polytech.si3.qgl.kihm.equipment.Sail;

import java.awt.*;
import java.util.List;
import java.util.*;

import static fr.unice.polytech.si3.qgl.kihm.actions.Action.actionTypeEnum.*;

public class SailorManager {
    public static final int MOVEMENT = 5;
    private final List<Sailor> sailors;
    private final Ship ship;

    public SailorManager(List<Sailor> sailors, Ship ship) {
        this.sailors = sailors;
        this.ship = ship;
    }

    private Action action(Sailor s, Action.actionTypeEnum type) {
        return new Action(s.getId(), type);
    }

    private Action move(Sailor s, int x, int y) {
        s.setPosition(new Point(s.getPosition().x + x, s.getPosition().y + y));
        return new Moving(s.getId(), x, y);
    }

    private Action turn(Sailor s, double angle) {
        return new Turn(s.getId(), angle);
    }

    /**
     * Remove all sailors on equipments needed, then move remaining sailors to used equipments without a sailor
     *
     * @param layout equipment to use
     * @return actions to do
     */
    public List<Action> moveSailors(Map<String, Double> layout) {
        List<Action> actions = new ArrayList<>();
        List<Sailor> sailorsToUse = new ArrayList<>(this.sailors);
        List<Sailor> sailorsInRange = new ArrayList<>();
        List<Sailor> leftOverSailors = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        if (layout.containsKey(Equipment.OAR_LEFT)) {
            for (int i = 0; i < layout.get(Equipment.OAR_LEFT).intValue(); i++) {
                equipments.add(this.ship.getOarLeft().get(i));
            }
        }
        if (layout.containsKey(Equipment.OAR_RIGHT)) {
            for (int i = 0; i < layout.get(Equipment.OAR_RIGHT).intValue(); i++) {
                equipments.add(this.ship.getOarRight().get(i));
            }
        }
        if (layout.containsKey(Equipment.RUDDER) && layout.get(Equipment.RUDDER) != 0.0) {
            equipments.add(this.ship.getRudders().get(0));
        }
        if (layout.containsKey(Equipment.SAIL)) {
            equipments.addAll(this.ship.getSails());
        } else {
            layout.put(Equipment.SAIL, 0.0);
        }

        int sailsNeeded = layout.get(Equipment.SAIL).intValue();
        int sailsOpened = (int) this.ship.getSails().stream().filter(Sail::isOpened).count();
        int sailsNotToChange = Math.min(sailsNeeded, sailsOpened);
        boolean sailOpenStatus = sailsNeeded > sailsOpened;
        if (sailsNeeded == sailsOpened) {
            sailOpenStatus = sailsNeeded != 0;
        }

        // Keep Necessary Sails.
        for (Equipment equipment : new ArrayList<>(equipments)) {
            if (sailsNotToChange > 0 && equipment.getType() == Equipment.equipmentTypeEnum.SAIL && ((Sail) equipment).isOpened() == sailOpenStatus) {
                equipments.remove(equipment);
                sailsNotToChange--;
            }
        }

        // On enlève les sailor qui sont trop loin des équipements nécessaires.
        for (Sailor sailor : sailorsToUse) {
            if (this.inRange(sailor, equipments).isEmpty()) {
                leftOverSailors.add(sailor);
            } else {
                sailorsInRange.add(sailor);
            }
        }

        // Parcourir en ordre de priorité les sailors qui doivent aller sur un équipement spécifique
        int i = 1;
        while (i <= equipments.size()) {
            int tmpI = i;
            List<Sailor> sailorsInRangeI = new ArrayList<>(sailorsInRange.stream().filter(sailor -> this.inRange(sailor, equipments).size() == tmpI).toList());
            if (!sailorsInRangeI.isEmpty()) {
                for (Sailor sailor : sailorsInRangeI) {
                    List<Equipment> inRange = this.inRange(sailor, equipments);
                    if (!inRange.isEmpty()) {
                        Equipment chosen = inRange.get(0);
                        int[] movement = sailor.moveTo(chosen);
                        if (movement[0] != 0 || movement[1] != 0) actions.add(move(sailor, movement[0], movement[1]));
                        equipments.remove(chosen);
                        sailorsInRange.remove(sailor);
                        i--;
                    }
                }
            }
            i++;
        }

        if (!leftOverSailors.isEmpty()) {
            Deck shipDimensions = this.ship.getDeck();
            Point middleOfBoat = new Point(shipDimensions.getLength() / 2, shipDimensions.getWidth() / 2);
            for (Sailor sailor : leftOverSailors) {
                if (!sailor.getPosition().equals(middleOfBoat)) {
                    int[] movement = sailor.moveTo(middleOfBoat);
                    actions.add(this.move(sailor, movement[0], movement[1]));
                }
            }
        }
        return actions;
    }

    private List<Equipment> inRange(Sailor sailor, List<Equipment> equipments) {
        List<Equipment> inRange = new ArrayList<>(equipments.stream().filter(e -> sailor.distanceTo(e) <= MOVEMENT).toList());
        inRange.sort(Comparator.comparingInt(sailor::distanceTo));
        return inRange;
    }

    /**
     * Create a List with all actions used for the turn
     *
     * @param oarLayout array which complied following conditions [oarLefts,oarRights,rudderAngle,sails]
     * @return actions's list
     */
    public List<Action> whoUseEquipments(Map<String, Double> oarLayout) {
        List<Action> actions = new ArrayList<>();
        for (Sailor s : sailors) {
            actions.addAll(assignOars(oarLayout, s));
            actions.addAll(assignRudder(oarLayout, s));
            actions.addAll(assignSails(oarLayout, s));
        }
        return actions;
    }

    /**
     * Create a List with turn actions used for the sailor
     *
     * @param oarLayout array which complied following conditions [oarLefts,oarRights,rudderAngle,sails]
     * @param s         Sailor to use
     * @return action's list - empty if no oars used
     */
    public List<Action> assignRudder(Map<String, Double> oarLayout, Sailor s) {
        List<Action> actions = new ArrayList<>();
        for (Rudder r : ship.getRudders()) {
            if (r.getPosition().equals(s.getPosition()) && !r.isOccupied() && oarLayout.get(Equipment.RUDDER) != 0) {
                r.setOccupied(true);
                actions.add(turn(s, oarLayout.get(Equipment.RUDDER)));
                break;
            }
        }
        return actions;
    }

    /**
     * Create a List with oars actions used for the sailor
     *
     * @param oarLayout array which complied following conditions [oarLefts,oarRights,rudderAngle,sails]
     * @param s         Sailor to use
     * @return action's list - empty if no oars used
     */
    public List<Action> assignOars(Map<String, Double> oarLayout, Sailor s) {
        List<Action> actions = new ArrayList<>();
        for (Oar o : ship.getOarRight()) {
            if (o.getPosition().equals(s.getPosition()) && !o.isOccupied() && oarLayout.get(Equipment.OAR_RIGHT) > 0) {
                oarLayout.put(Equipment.OAR_RIGHT, oarLayout.get(Equipment.OAR_RIGHT) - 1);
                o.setOccupied(true);
                actions.add(action(s, OAR));
                break;
            }
        }
        for (Oar o : ship.getOarLeft()) {
            if (o.getPosition().equals(s.getPosition()) && !o.isOccupied() && oarLayout.get(Equipment.OAR_LEFT) > 0) {
                oarLayout.put(Equipment.OAR_LEFT, oarLayout.get(Equipment.OAR_LEFT) - 1);
                o.setOccupied(true);
                actions.add(action(s, OAR));
                break;
            }
        }
        return actions;
    }

    /**
     * Create a List with sails actions used for the sailor
     *
     * @param oarLayout array which complied following conditions [oarLefts,oarRights,rudderAngle,sails]
     * @param s         Sailor to use
     * @return action's list - empty if no oars used
     */
    public List<Action> assignSails(Map<String, Double> oarLayout, Sailor s) {
        List<Action> actions = new ArrayList<>();
        List<Sail> sails = ship.getSails();
        List<Sail> sailsOpened = sails.stream().filter(Sail::isOpened).toList();
        List<Sail> sailsClosed = sails.stream().filter(e -> !sailsOpened.contains(e)).toList();

        for (Sail r : ship.getSails()) {
            boolean actionLeft = true;
            if (r.getPosition().equals(s.getPosition()) && !r.isOccupied() && oarLayout.get(Equipment.SAIL) > 0 && sailsClosed.contains(r)) {
                r.setOccupied(true);
                actions.add(action(s, LIFT_SAIL));
                actionLeft = false;
            } else if (r.getPosition().equals(s.getPosition()) && !r.isOccupied() && oarLayout.get(Equipment.SAIL) == 0 && sailsOpened.contains(r)) {
                r.setOccupied(true);
                actions.add(action(s, LOWER_SAIL));
                actionLeft = false;
            }
            if (!actionLeft) {
                break;
            }
        }
        return actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SailorManager that = (SailorManager) o;
        return Objects.equals(sailors, that.sailors) && Objects.equals(ship, that.ship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sailors, ship);
    }

    @Override
    public String toString() {
        return "{\"sailors\": " + this.sailors + ", \"ship\": " + this.ship + '}';
    }
}
