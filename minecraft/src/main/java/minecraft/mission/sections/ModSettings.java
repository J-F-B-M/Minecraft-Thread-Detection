package minecraft.mission.sections;

/**
 * Created by Joachim on 29.03.2017.
 */
public class ModSettings {

    private final int MsPerTick;
    private final boolean PrioritiseOffscreenRendering;

    private ModSettings(Builder builder) {
        this.MsPerTick = builder.MsPerTick;
        this.PrioritiseOffscreenRendering = builder.PrioritiseOffscreenRendering;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ModSettings>");

        sb.append("<MsPerTick>").append(MsPerTick).append("</MsPerTick>");
        sb.append("<PrioritiseOffscreenRendering>").append(PrioritiseOffscreenRendering).append("</PrioritiseOffscreenRendering>");

        sb.append("</ModSettings>");

        return sb.toString();
    }

    public static class Builder {
        private int MsPerTick = 50;
        private boolean PrioritiseOffscreenRendering = false;

        public Builder() {
        }

        public Builder setMsPerTick(int msPerTick) {
            if (msPerTick < 1) throw new IllegalArgumentException("msPerTick cannot be smaller than 1");

            MsPerTick = msPerTick;
            return this;
        }

        public Builder setPrioritiseOffscreenRendering(boolean prioritiseOffscreenRendering) {
            PrioritiseOffscreenRendering = prioritiseOffscreenRendering;
            return this;
        }

        public ModSettings build() {
            return new ModSettings(this);
        }
    }
}
