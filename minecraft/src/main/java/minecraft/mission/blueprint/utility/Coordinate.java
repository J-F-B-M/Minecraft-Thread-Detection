package minecraft.mission.blueprint.utility;

/**
 * Created by Joachim Brehmer on 13.03.2017.
 */
public class Coordinate {
    public final int x, y, z;

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate translate(int x, int y, int z) {
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
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
