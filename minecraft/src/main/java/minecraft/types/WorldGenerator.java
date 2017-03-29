package minecraft.types;

/**
 * Created by Joachim on 29.03.2017.
 */
public enum WorldGenerator {

    FlatWorldGenerator(true, true, false),
    FileWorldGenerator(false, false, true),
    DefaultWorldGenerator(false, true, false);

    final boolean hasGeneratorString;
    final boolean hasSeed;
    final boolean hasURI;

    WorldGenerator(boolean hasGeneratorString, boolean hasSeed, boolean hasURI) {
        this.hasGeneratorString = hasGeneratorString;
        this.hasSeed = hasSeed;
        this.hasURI = hasURI;
    }
}
