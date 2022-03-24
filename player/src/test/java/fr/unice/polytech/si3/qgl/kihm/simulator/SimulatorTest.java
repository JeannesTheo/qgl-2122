package fr.unice.polytech.si3.qgl.kihm.simulator;

import fr.unice.polytech.si3.qgl.kihm.landmarks.Stream;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

    private Simulator simulator;
    private String intGame;
    private Wind wind;
    private Ship ship;
    private Position position;
    private Shape shape;

    @BeforeEach
    void setUp() {
        this.simulator = Simulator.get();
        this.intGame = "{" +
                "  \"goal\": {" +
                "    \"mode\": \"REGATTA\"," +
                "    \"checkpoints\": [" +
                "      {" +
                "        \"type\": \"checkpoint\"," +
                "        \"position\": {" +
                "          \"x\": 4036.4115963579497," +
                "          \"y\": 264.956362612613" +
                "        }," +
                "        \"shape\": {" +
                "          \"type\": \"circle\"," +
                "          \"radius\": 100" +
                "        }" +
                "      }," +
                "      {" +
                "        \"type\": \"checkpoint\"," +
                "        \"position\": {" +
                "          \"x\": 6866.105430167673," +
                "          \"y\": 277.9420045045067" +
                "        }," +
                "        \"shape\": {" +
                "          \"type\": \"circle\"," +
                "          \"radius\": 100" +
                "        }" +
                "      }," +
                "      {" +
                "        \"type\": \"checkpoint\"," +
                "        \"position\": {" +
                "          \"x\": 8019.417976524843," +
                "          \"y\": 1210.6559684684694" +
                "        }," +
                "        \"shape\": {" +
                "          \"type\": \"circle\"," +
                "          \"radius\": 100" +
                "        }" +
                "      }," +
                "      {" +
                "        \"type\": \"checkpoint\"," +
                "        \"position\": {" +
                "          \"x\": 8100.5085279370105," +
                "          \"y\": 2245.8826013513526" +
                "        }," +
                "        \"shape\": {" +
                "          \"type\": \"circle\"," +
                "          \"radius\": 100" +
                "        }" +
                "      }," +
                "      {" +
                "        \"type\": \"checkpoint\"," +
                "        \"position\": {" +
                "          \"x\": 4652.274679791037," +
                "          \"y\": 2802.9983108108113" +
                "        }," +
                "        \"shape\": {" +
                "          \"type\": \"circle\"," +
                "          \"radius\": 100" +
                "        }" +
                "      }" +
                "    ]" +
                "  }," +
                "  \"ship\": {" +
                "    \"name\": \"Licorne\"," +
                "    \"deck\": {" +
                "      \"length\": 7," +
                "      \"width\": 3" +
                "    }," +
                "    \"entities\": [" +
                "      {" +
                "        \"type\": \"rudder\"," +
                "        \"x\": 6," +
                "        \"y\": 1" +
                "      }," +
                "      {" +
                "        \"type\": \"sail\"," +
                "        \"x\": 3," +
                "        \"y\": 1" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 1," +
                "        \"y\": 2" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 2," +
                "        \"y\": 2" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 3," +
                "        \"y\": 2" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 5," +
                "        \"y\": 2" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 4," +
                "        \"y\": 2" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 1," +
                "        \"y\": 0" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 2," +
                "        \"y\": 0" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 4," +
                "        \"y\": 0" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 3," +
                "        \"y\": 0" +
                "      }," +
                "      {" +
                "        \"type\": \"oar\"," +
                "        \"x\": 5," +
                "        \"y\": 0" +
                "      }" +
                "    ]," +
                "    \"life\": 1050" +
                "  }," +
                "  \"wind\": {" +
                "    \"orientation\": 0," +
                "    \"strength\": 50" +
                "  }," +
                "  \"seaEntities\": [" +
                "    {" +
                "      \"position\": {" +
                "        \"x\": 8153.389030097254," +
                "        \"y\": 351.1753941441404," +
                "        \"orientation\": 0.4537856055185257" +
                "      }," +
                "      \"type\": \"reef\"," +
                "      \"shape\": {" +
                "        \"type\": \"rectangle\"," +
                "        \"width\": \"400\"," +
                "        \"height\": \"1350\"" +
                "      }" +
                "    }," +
                "    {" +
                "      \"position\": {" +
                "        \"x\": 6186.903137789905," +
                "        \"y\": 2006.7567567567567," +
                "        \"orientation\": -0.17453292519943295" +
                "      }," +
                "      \"type\": \"reef\"," +
                "      \"shape\": {" +
                "        \"type\": \"rectangle\"," +
                "        \"width\": 500," +
                "        \"height\": \"1750\"" +
                "      }" +
                "    }," +
                "    {" +
                "      \"position\": {" +
                "        \"x\": 6630.286493860851," +
                "        \"y\": 3209.4594594594596," +
                "        \"orientation\": -0.24434609527920614" +
                "      }," +
                "      \"type\": \"reef\"," +
                "      \"shape\": {" +
                "        \"type\": \"rectangle\"," +
                "        \"width\": 500," +
                "        \"height\": \"1750\"" +
                "      }" +
                "    }," +
                "    {" +
                "      \"position\": {" +
                "        \"x\": 7174.22818123444," +
                "        \"y\": 1102.9701576576585," +
                "        \"orientation\": 0.6981317007977318" +
                "      }," +
                "      \"type\": \"reef\"," +
                "      \"shape\": {" +
                "        \"type\": \"rectangle\"," +
                "        \"width\": \"250\"," +
                "        \"height\": \"1200\"" +
                "      }" +
                "    }," +
                "    {" +
                "      \"position\": {" +
                "        \"x\": 7409.664698840839," +
                "        \"y\": 701.646959459459," +
                "        \"orientation\": 0.7330382858376184" +
                "      }," +
                "      \"type\": \"stream\"," +
                "      \"shape\": {" +
                "        \"type\": \"rectangle\"," +
                "        \"width\": \"400\"," +
                "        \"height\": \"1000\"" +
                "      }," +
                "      \"strength\": 10" +
                "    }," +
                "    {" +
                "      \"position\": {" +
                "        \"x\": 6384.720327421553," +
                "        \"y\": 2581.0810810810804," +
                "        \"orientation\": 3.0019663134302466" +
                "      }," +
                "      \"type\": \"stream\"," +
                "      \"shape\": {" +
                "        \"type\": \"rectangle\"," +
                "        \"width\": 200," +
                "        \"height\": 1000" +
                "      }," +
                "      \"strength\": 10" +
                "    }" +
                "  ]," +
                "  \"maxRound\": 500," +
                "  \"minumumCrewSize\": 12," +
                "  \"maximumCrewSize\": 12," +
                "  \"startingPositions\": [" +
                "    {" +
                "      \"x\": 2852.173913043478," +
                "      \"y\": 1978.827361563518," +
                "      \"orientation\": -1.0297442586766543" +
                "    }" +
                "  ]" +
                "}";
        this.intGame = this.intGame.replace(" ", "");
        this.simulator.setInitGameString(this.intGame);
        this.simulator.initGameString();
        this.wind = this.simulator.getWind();
        this.ship = this.simulator.getCockpitSim().getGame().getShip();

        this.position = new Position();
        this.shape = new Rectangle(-2, -2, 4, 4);
    }

    @AfterEach
    void reset() {
        this.simulator.reset();
    }

    @Test
    void testNextRound() {
        String nextRoundString = this.simulator.nextRoundString();
        assertNotEquals("", nextRoundString);
        assertEquals("{\"ship\": {\"type\": \"ship\", \"life\": 1050, \"position\": {\"x\": 2852.173913043478, \"y\": 1978.827361563518, \"orientation\": -1.0297442586766543}, \"name\": \"Licorne\", \"deck\": {\"width\": 3, \"length\": 7}, \"entities\": [{\"type\": \"rudder\", \"x\": 6, \"y\": 1}, {\"type\": \"sail\", \"x\": 3, \"y\": 1, \"opened\": false}, {\"type\": \"oar\", \"x\": 1, \"y\": 2}, {\"type\": \"oar\", \"x\": 2, \"y\": 2}, {\"type\": \"oar\", \"x\": 3, \"y\": 2}, {\"type\": \"oar\", \"x\": 5, \"y\": 2}, {\"type\": \"oar\", \"x\": 4, \"y\": 2}, {\"type\": \"oar\", \"x\": 1, \"y\": 0}, {\"type\": \"oar\", \"x\": 2, \"y\": 0}, {\"type\": \"oar\", \"x\": 4, \"y\": 0}, {\"type\": \"oar\", \"x\": 3, \"y\": 0}, {\"type\": \"oar\", \"x\": 5, \"y\": 0}], \"shape\": {\"type\": \"rectangle\", \"width\": 3, \"height\": 7, \"orientation\": -1.0297442586766543}}, \"wind\": {\"orientation\": 0.0, \"strength\": 50.0}, \"visibleEntities\":[]}", nextRoundString);
    }

    @Test
    void testNotFinished() {
        assertTrue(this.simulator.notFinished());
        this.simulator.run(this.intGame);
        assertFalse(this.simulator.notFinished());
    }

    @Test
    void getObjects() {
        assertEquals("\"wind\":{\"orientation\":0,\"strength\":50}", simulator.getObject(this.intGame, "wind"));
        assertEquals(this.intGame, this.simulator.getInitGameString());
        assertNotEquals(null, this.simulator.getRandom());
    }

    @Test
    void testToString() {
        assertEquals("\"wind\": " + this.wind, this.simulator.windToString());
        assertEquals("\"ship\": " + this.ship, this.simulator.shipToString());
    }

    @Test
    void testNumberOfTurns() {
        assertEquals(0, this.simulator.getRoundNumber());
        this.simulator.run(this.intGame);
        assertTrue(this.simulator.getRoundNumber() <= 58);
    }

    @Test
    void calculatingNextTurnPositionFullSpeedTest() {
        assertEquals(new Position(165.0000000000003, 0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionFullSpeedAndTurnTest() {
        assertEquals(new Position(148.79306545015345, 60.94865331860557, 0.7853981633974472), Simulator.get().calculatingNextRoundPosition(position, shape, 165, Math.toRadians(45), 0, 0, new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionNotFullSpeedAndTurnTest() {
        assertEquals(new Position(66.92458314173447, 61.47566832700389, 1.5009831567151206), Simulator.get().calculatingNextRoundPosition(position, shape, 100, Math.toRadians(86), 0, 0, new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindBehindTest() {
        Wind wind = new Wind(0.0, 100);
        assertEquals(new Position(265.0000000000003, 0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindInFrontTest() {
        Wind wind = new Wind(Math.toRadians(180), 100);
        assertEquals(new Position(64.99999999999991, 0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindSideTest() {
        Wind wind = new Wind(Math.toRadians(90), 100);
        assertEquals(new Position(165.0000000000003, 0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindDiagBehindTest() {
        Wind wind = new Wind(Math.toRadians(45), 100);
        assertEquals(new Position(235.71067811865515, 0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithWindDiagFrontTest() {
        Wind wind = new Wind(Math.toRadians(135), 100);
        assertEquals(new Position(94.28932188134526, 0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, wind.getStrength(), wind.getOrientation(), new ArrayList<>()));
    }

    @Test
    void calculatingNextTurnPositionWithStreamBehindTest() {
        List<Stream> streams = List.of(new Stream(new Position(), new Rectangle(-10, -10, 500, 20), 100));
        assertEquals(new Position(265.0000000000003, 0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, streams));
    }

    @Test
    void calculatingNextTurnPositionWithStreamFrontTest() {
        List<Stream> streams = List.of(new Stream(new Position(0, 0, Math.toRadians(180)), new Rectangle(-10, -10, 500, 20), 100));
        assertEquals(new Position(64.99999999999993, 1.2246467991473518E-14), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, streams));
    }

    @Test
    void calculatingNextTurnPositionWithStreamSideTest() {
        List<Stream> streams = List.of(new Stream(new Position(0, 0, Math.toRadians(90)), new Rectangle(-10, -10, 500, 20), 100));
        assertEquals(new Position(165.0000000000003, 12.0), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, streams));
    }

    @Test
    void calculatingNextTurnPositionWithStreamDiagTest() {
        List<Stream> streams = List.of(new Stream(new Position(0, 0, Math.toRadians(45)), new Rectangle(-10, -10, 500, 20), 100));
        assertEquals(new Position(177.02081528017163, 12.02081528017131), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, streams));
    }

    @Test
    void calculatingNextTurnPositionWithMultipleStreamTest() {
        List<Stream> streams = List.of(new Stream(new Position(0, 0, Math.toRadians(45)), new Rectangle(-10, -10, 500, 20), 100), new Stream(new Position(), new Rectangle(-10, -10, 500, 20), 100));
        assertEquals(new Position(194.02081528017172, 12.02081528017131), Simulator.get().calculatingNextRoundPosition(position, shape, 165, 0, 0, 0, streams));
    }
}