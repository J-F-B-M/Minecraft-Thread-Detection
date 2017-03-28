package minecraft.mission.blueprint;

import minecraft.mission.blueprint.utility.Coordinate;

/**
 * Created by Joachim Brehmer on 13.03.2017.
 */
public class PointSymmetricalRotation extends ObjectBlueprint {

    public PointSymmetricalRotation(Coordinate origin) {
        super(origin);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            ObjectBlueprint rotated = this.rotateAroundY(i);
            sb.append(rotated.toString());
        }

        return sb.toString();
    }
}
