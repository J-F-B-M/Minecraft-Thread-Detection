package minecraft.mission.blueprint.utility;

/**
 * Created by Joachim Brehmer on 13.03.2017.
 */
public class Coordinate {
    public final long x, y, z;

    public Coordinate(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate translate(long x, long y, long z) {
        return new Coordinate(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;
    }

    @Override
    public int hashCode() {
        int result = (int) (x ^ (x >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        result = 31 * result + (int) (z ^ (z >>> 32));
        return result;
    }
}
