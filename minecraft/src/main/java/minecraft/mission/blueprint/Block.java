package minecraft.mission.blueprint;

import minecraft.mission.blueprint.utility.Coordinate;

/**
 * Created by Joachim Brehmer on 13.03.2017.
 */
public class Block extends ObjectBlueprint{

    public Block(Coordinate origin, String type) {
        super(origin);
        addBlock(0,0,0,type);
    }
}
