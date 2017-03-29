package minecraft.mission.sections;

/**
 * Created by Joachim on 29.03.2017.
 */
public class About {
    private final String summary;
    private final String description;

    private About(Builder builder) {
        this.summary = builder.summary;
        this.description = builder.description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<About>");

        sb.append("<Summary>").append(summary).append("</Summary>");
        if (description != null) sb.append("<Description>").append(description).append("</Description>");

        sb.append("</About>");

        return sb.toString();
    }

    public static class Builder {

        private String summary = "A Minecraft mission";
        private String description;

        public Builder() {
        }

        public Builder setSummary(String summary) {
            if (summary == null) {
                throw new IllegalArgumentException("summary may not be null");
            }

            this.summary = summary;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public About build() {
            return new About(this);
        }
    }
}
