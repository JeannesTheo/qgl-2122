package fr.unice.polytech.si3.qgl.kihm;

import fr.unice.polytech.si3.qgl.kihm.equipment.Oar;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.structures.Parser;
import fr.unice.polytech.si3.qgl.kihm.structures.Pathfinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
    Parser p;
    Cockpit c;

    /**
     * Tester actions en doubles
     * Test type null
     * test move > 5
     */

    @BeforeEach
    void setUp() {
        c = new Cockpit();
        p = new Parser();
    }

    @Test
    void constructJsonEmpty() {
        assertEquals("[]", p.constructJson(new ArrayList<>()));
    }

    @Test
    void nextRoundWind() {
        Oar oar = new Oar(1, 1);
        oar.setOccupied(true);
        c.initGame("{\"goal\":{\"mode\":\"REGATTA\", \"checkpoints\":[{\"position\":{\"x\":-6, \"y\":0, \"orientation\":0}, \"shape\":{\"type\":\"circle\", \"radius\":4}}]}, \"ship\":{\"type\":\"ship\", \"life\":100, \"position\":{\"x\":0, \"y\":0, \"orientation\":0}, \"name\":\"Lescopaingsd'abord!\", \"deck\":{\"width\":4, \"length\":2}, \"entities\":[{\"type\":\"oar\", \"x\":0, \"y\":0}, {\"type\":\"oar\", \"x\":1, \"y\":1}], \"shape\":{\"type\":\"polygon\", \"vertices\":[{\"x\":3, \"y\":1}, {\"x\":2, \"y\":1}, {\"x\":2, \"y\":2}], \"orientation\":0}}, \"sailors\":[{\"x\":0, \"y\":0, \"id\":0, \"name\":\"EdwardTeach\"}, {\"x\":0, \"y\":0, \"id\":1, \"name\":\"EdwardPouce\"}, {\"x\":1, \"y\":1, \"id\":2, \"name\":\"TomPouce\"}, {\"x\":1, \"y\":1, \"id\":3, \"name\":\"JackTeach\"}], \"shipCount\":1}");
        p.readNextRound("{\"ship\":{\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": -200,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 3,\"length\": 4},\"entities\": [{\"type\": \"oar\",\"x\": 2,\"y\": 0},{\"type\": \"oar\",\"x\": 1,\"y\": 2}],\"shape\": {\"type\": \"rectangle\",\"width\": 3,\"height\": 6,\"orientation\": 0}}, \"wind\" : {\"orientation\": 0.8, \"strength\":10 }}", c.getGame());
        World w = new World();
        w.setWind(new Wind(0.8, 10));
        c.getGame().setWorld(w);
        c.getGame().update(w.getWind(), new ArrayList<>());
        assertEquals(c.getGame().getPathfinder(), new Pathfinder(c.getGame().getShip(), w));
    }
}
