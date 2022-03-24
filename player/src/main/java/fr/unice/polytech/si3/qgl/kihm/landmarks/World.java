package fr.unice.polytech.si3.qgl.kihm.landmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.CHECKPOINT;
import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.STREAM;

public class World {
    private List<CheckPoint> checkpoints;
    private Wind wind;
    private List<Obstacle> entities;

    public World(List<CheckPoint> checkpoints, Wind wind, List<Obstacle> entities) {
        this.checkpoints = checkpoints;
        this.wind = wind;
        entities.removeIf(e -> e.getType().equals(CHECKPOINT));
        this.entities = entities;
    }

    public World() {
        wind = new Wind();
        checkpoints = new ArrayList<>();
        entities = new ArrayList<>();
    }

    public World(List<Obstacle> checkpoints) {
        wind = new Wind();
        this.checkpoints = new ArrayList<>();
        checkpoints.stream().filter(e -> e.getType().equals(CHECKPOINT)).map(CheckPoint.class::cast).forEach(e -> this.checkpoints.add(e));
        entities = new ArrayList<>();
    }

    public List<CheckPoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<CheckPoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public List<Obstacle> getEntities() {
        return entities;
    }

    public void setEntities(List<Obstacle> entities) {
        entities.removeIf(e -> e.getType().equals(CHECKPOINT));
        this.entities = entities;
    }

    public List<Stream> getStreams() {
        return this.entities.stream().filter(obstacle -> obstacle.getType() == STREAM).map(Stream.class::cast).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        World world = (World) o;

        if (!Objects.equals(checkpoints, world.checkpoints)) return false;
        if (!Objects.equals(wind, world.wind)) return false;
        return Objects.equals(entities, world.entities);
    }

    @Override
    public int hashCode() {
        int result = checkpoints != null ? checkpoints.hashCode() : 0;
        result = 31 * result + (wind != null ? wind.hashCode() : 0);
        result = 31 * result + (entities != null ? entities.hashCode() : 0);
        return result;
    }
}
