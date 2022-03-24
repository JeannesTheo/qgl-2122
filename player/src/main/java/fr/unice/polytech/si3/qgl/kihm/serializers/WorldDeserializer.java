package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.unice.polytech.si3.qgl.kihm.landmarks.CheckPoint;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;

import java.io.IOException;
import java.util.List;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.CHECKPOINT;
import static java.util.Objects.isNull;

/**
 * The type Ship deserializer.
 */
public class WorldDeserializer extends StdDeserializer<World> {
    private final ObjectMapper om;

    /**
     * Instantiates a new Ship deserializer.
     */
    public WorldDeserializer() {
        this(null);
    }

    /**
     * Instantiates a new Ship deserializer.
     *
     * @param vc the vc
     */
    protected WorldDeserializer(Class<?> vc) {
        super(vc);
        om = new ObjectMapper();
        SimpleModule equipmentModule = new SimpleModule();
        equipmentModule.addDeserializer(Obstacle.class, new ObstacleDeserializer());
        om.registerModule(equipmentModule);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    //Define each field then create a Ship with that
    @Override
    public World deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        World world = new World();
        JsonNode visibleEntities = isNull(node.get("visibleEntities")) ? null : node.get("visibleEntities");
        JsonNode wind = isNull(node.get("wind")) ? null : node.get("wind");
        JsonNode checkpoints = isNull(node.get("checkpoints")) ? null : node.get("checkpoints");
        if (!isNull(wind))
            world.setWind(om.readValue(node.get(wind.toString()).toString(), Wind.class));
        if (!isNull(visibleEntities))
            world.setEntities(om.readValue(visibleEntities.toString(), new TypeReference<>() {
            }));
        if (!isNull(checkpoints)) {
            List<Obstacle> lo = om.readValue(checkpoints.toString(), new TypeReference<>() {
            });
            //Retrieve the checkpoints list from the JSON file
            world.setCheckpoints(lo.stream().filter(e -> e.getType().equals(CHECKPOINT)).map(CheckPoint.class::cast).toList());
        }

        return world;
    }
}
