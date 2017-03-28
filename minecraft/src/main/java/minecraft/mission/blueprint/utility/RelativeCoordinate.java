package minecraft.mission.blueprint.utility;

/**
 * Created by Joachim Brehmer on 13.03.2017.
 */
public class RelativeCoordinate {
    public final Coordinate origin;
    public final long xOffset, yOffset, zOffset;
    public final String block;

    public RelativeCoordinate(Coordinate origin, long xOffset, long yOffset, long zOffset, String block) {
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

    public long getX() {
        return origin.x + xOffset;
    }

    public long getY() {
        return origin.y + yOffset;
    }

    public long getZ() {
        return origin.z + zOffset;
    }

    public Coordinate getCoordinate() {
        return origin.translate(xOffset, yOffset, zOffset);
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
        result = 31 * result + (int) (xOffset ^ (xOffset >>> 32));
        result = 31 * result + (int) (yOffset ^ (yOffset >>> 32));
        result = 31 * result + (int) (zOffset ^ (zOffset >>> 32));
        result = 31 * result + block.hashCode();
        return result;
    }
}
