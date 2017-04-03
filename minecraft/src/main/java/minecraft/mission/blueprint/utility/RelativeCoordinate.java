package minecraft.mission.blueprint.utility;

import minecraft.xml.DrawBlock;

/**
 * Created by Joachim Brehmer on 13.03.2017.
 */
public class RelativeCoordinate {
    private final Coordinate origin;
    private final int xOffset, yOffset, zOffset;
    public final Block block;

    public RelativeCoordinate(Coordinate origin, int xOffset, int yOffset, int zOffset, Block block) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin required");
        }

        if (block == null) {
            throw new IllegalArgumentException("Need to set a block-type");
        }

        this.origin = origin;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.block = block;
    }

    /**
     * Represents a counterclockwise (positive) rotation by 90° * times. An input of 0 leaves the coordinates unchanged, an input of 2 mirrors through the origin and an input. An input greater than 3 is equivalent to <code>times%4</code>.
     *
     * @param times An integer in the range 0-3. While it can be outside this range, it will be taken modulo 4, projecting it in this range.
     * @return A new object with the projected coordinates.
     */
    public RelativeCoordinate rotateAroundX(int times) {
        times = times % 4;

        switch (times) {
            case 0:
                return this; //simplification, this is immutable
            case 1:
                return new RelativeCoordinate(origin, xOffset, -zOffset, yOffset, block);
            case 2:
                return new RelativeCoordinate(origin, xOffset, -yOffset, -zOffset, block);
            case 3:
                return new RelativeCoordinate(origin, xOffset, zOffset, -yOffset, block);
            default:
                // Should not happen
                throw new IllegalStateException("times was not in the range 0-3. Actual value was " + times);
        }
    }

    public RelativeCoordinate rotateAroundY(int times) {
        times = times % 4;

        switch (times) {
            case 0:
                return this; //simplification, this is immutable
            case 1:
                return new RelativeCoordinate(origin, -zOffset, yOffset, xOffset, block);
            case 2:
                return new RelativeCoordinate(origin, -xOffset, yOffset, -zOffset, block);
            case 3:
                return new RelativeCoordinate(origin, zOffset, yOffset, -xOffset, block);
            default:
                // Should not happen
                throw new IllegalStateException("times was not in the range 0-3. Actual value was " + times);
        }
    }

    public RelativeCoordinate rotateAroundZ(int times) {
        times = times % 4;

        switch (times) {
            case 0:
                return this; //simplification, this is immutable
            case 1:
                return new RelativeCoordinate(origin, -yOffset, xOffset, zOffset, block);
            case 2:
                return new RelativeCoordinate(origin, -xOffset, -yOffset, zOffset, block);
            case 3:
                return new RelativeCoordinate(origin, yOffset, -xOffset, zOffset, block);
            default:
                // Should not happen
                throw new IllegalStateException("times was not in the range 0-3. Actual value was " + times);
        }
    }

    public int getX() {
        return origin.x + xOffset;
    }

    public int getY() {
        return origin.y + yOffset;
    }

    public int getZ() {
        return origin.z + zOffset;
    }

    public Coordinate getCoordinate() {
        return origin.translate(xOffset, yOffset, zOffset);
    }

    public DrawBlock toSchema() {
        DrawBlock drawBlock = new DrawBlock();
        drawBlock.setType(block.type);
        if (block.variation != null) drawBlock.setVariant(block.variation);
        if (block.colour != null) drawBlock.setColour(block.colour);
        if (block.face != null) drawBlock.setFace(block.face);
        drawBlock.setX(getX());
        drawBlock.setY(getY());
        drawBlock.setZ(getZ());

        return drawBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelativeCoordinate that = (RelativeCoordinate) o;

        if (xOffset != that.xOffset) return false;
        if (yOffset != that.yOffset) return false;
        if (zOffset != that.zOffset) return false;
        if (!origin.equals(that.origin)) return false;
        return block.equals(that.block);
    }

    @Override
    public int hashCode() {
        int result = origin.hashCode();
        result = 31 * result + xOffset;
        result = 31 * result + yOffset;
        result = 31 * result + zOffset;
        result = 31 * result + block.hashCode();
        return result;
    }
}
