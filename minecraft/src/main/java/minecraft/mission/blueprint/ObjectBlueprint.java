package minecraft.mission.blueprint;

import minecraft.mission.blueprint.utility.Coordinate;
import minecraft.mission.blueprint.utility.RelativeCoordinate;
import minecraft.xml.DrawObjectType;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Joachim Brehmer on 13.03.2017.
 */
public class ObjectBlueprint {
    private final Coordinate origin;
    private final List<RelativeCoordinate> blocks;

    public ObjectBlueprint(Coordinate origin) {
        this(origin, new LinkedList<>());
    }

    private ObjectBlueprint(Coordinate origin, List<RelativeCoordinate> blocks) {
        this.origin = origin;
        this.blocks = blocks;
    }

    public void addBlock(int xOffset, int yOffset, int zOffset, minecraft.mission.blueprint.utility.Block blockType) {
        if (blockType == null) {
            throw new IllegalArgumentException("Need to set a block-type");
        }
        RelativeCoordinate relCoord = new RelativeCoordinate(origin, xOffset, yOffset, zOffset, blockType);
        blocks.add(relCoord);
    }

    public ObjectBlueprint rotateAroundX(int times) {
        return new ObjectBlueprint(origin, blocks.stream().map(r -> r.rotateAroundX(times)).collect(toList()));
    }

    public ObjectBlueprint rotateAroundY(int times) {
        return new ObjectBlueprint(origin, blocks.stream().map(r -> r.rotateAroundY(times)).collect(toList()));
    }

    public ObjectBlueprint rotateAroundZ(int times) {
        return new ObjectBlueprint(origin, blocks.stream().map(r -> r.rotateAroundZ(times)).collect(toList()));
    }

    public List<DrawObjectType> toSchema() {
        List<DrawObjectType> schema = new LinkedList<>();

        for (RelativeCoordinate block : blocks) {
            schema.add(block.toSchema());
        }

        return schema;
    }
}