package minecraft.mission.sections;

import minecraft.types.EntityTypes;
import minecraft.types.Weather;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joachim on 29.03.2017.
 */
public class ServerSection {
    private static final int XML_DECIMAL_MAX_LENGTH = 18;

    private final ServerInitialConditions serverInitialConditions;
    private final ServerHandlers serverHandlers;

    private ServerSection(Builder builder) {
        this.serverInitialConditions = builder.serverInitialConditions;
        this.serverHandlers = builder.serverHandlers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ServerSection>");

        if (serverInitialConditions != null) {
            sb.append(serverInitialConditions);
        }
        sb.append(serverHandlers.toString());

        sb.append("</ServerSection>");
        return sb.toString();
    }

    public static class Builder {
        private ServerInitialConditions serverInitialConditions;
        private ServerHandlers serverHandlers;

        public Builder(ServerHandlers serverHandlers) {
            this.serverHandlers = serverHandlers;
        }

        public Builder setServerInitialConditions(ServerInitialConditions serverInitialConditions) {
            this.serverInitialConditions = serverInitialConditions;
            return this;
        }

        public Builder setServerHandlers(ServerHandlers serverHandlers) {
            if (serverHandlers == null) {
                throw new IllegalArgumentException("serverHandlers may not be null");
            }
            this.serverHandlers = serverHandlers;
            return this;
        }

        public ServerSection build() {
            return new ServerSection(this);
        }


    }

    public static class ServerInitialConditions {
        private final Integer time;
        private final Boolean allowPassageOfTime;
        private final Weather weather;
        private final Boolean allowSpawning;
        private final List<EntityTypes> allowedMobs;

        public static class Builder {
            private Integer time;
            private Boolean allowPassageOfTime;
            private Weather weather;

            public Builder setTime(Integer time) {
                this.time = time;
                return this;
            }

            public Builder setAllowPassageOfTime(Boolean allowPassageOfTime) {
                this.allowPassageOfTime = allowPassageOfTime;
                return this;
            }

            public Builder setWeather(Weather weather) {
                this.weather = weather;
                return this;
            }

            public Builder setAllowSpawning(Boolean allowSpawning) {
                this.allowSpawning = allowSpawning;
                return this;
            }

            public Builder setAllowedMobs(List<EntityTypes> allowedMobs) {
                this.allowedMobs = allowedMobs;
                return this;
            }

            private Boolean allowSpawning;
            private List<EntityTypes> allowedMobs;

        }

        private ServerInitialConditions(Builder builder) {
            time = builder.time;
            allowPassageOfTime = builder.allowPassageOfTime;
            weather = builder.weather;
            allowSpawning = builder.allowSpawning;
            allowedMobs = builder.allowedMobs;
        }

        @Override
        public String toString() {
            if (time == null && allowPassageOfTime == null && weather == null && allowSpawning == null && allowedMobs == null)
                return "";
            StringBuilder sb = new StringBuilder();

            sb.append("<ServerInitialConditions>");

            if (time != null || allowPassageOfTime != null) {
                sb.append("<Time>");
                if (time != null) {
                    sb.append("<StartTime>").append(time % 24000).append("</StartTime>");
                }
                if (allowPassageOfTime != null) {
                    sb.append("<AllowPassageOfTime>").append(allowPassageOfTime).append("</AllowPassageOfTime>");
                }
                sb.append("</Time>");
            }

            if (weather != null) {
                sb.append("<Weather>").append(weather.toString().toLowerCase()).append("</Weather>");
            }

            if (allowSpawning != null) {
                sb.append("<AllowSpawning>").append(allowSpawning).append("</AllowSpawning>");
            }

            if (allowedMobs != null) {
                sb.append("<AllowedMobs>").append(allowedMobs.stream().map(e -> e.toString()).collect(Collectors.joining(" "))).append("</AllowedMobs>");
            }

            sb.append("</ServerInitialConditions>");
            return sb.toString();
        }
    }

    public static class ServerHandlers {
        public ServerHandlers(Builder builder) {
            this.generator = builder.generator;
            this.generatorString = builder.generatorString;
            this.forceReset = builder.forceReset;
            this.seed = builder.seed;
            this.uri = builder.uri;
            this.decorators = builder.decorators;
            this.timeLimitMs = builder.timeLimitMs;
            this.timeUpDescription = builder.timeUpDescription;
            this.agentFinishDescription = builder.agentFinishDescription;
        }

        // Generator Settings
        private final WorldGenerator generator;
        private final String generatorString;
        private final Boolean forceReset;
        private final String seed;
        private final URI uri;

        // Decorator Settings
        private final List<Decorator> decorators;

        // ServerQuit Settings
        private final Long timeLimitMs;
        private final String timeUpDescription;
        private final String agentFinishDescription;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("<ServerHandlers>");

            // WorldGenerator
            sb.append("<").append(generator.toString());
            if (generator.hasURI) sb.append(" src=\"").append(uri.toString()).append('"');
            if (generator.hasGeneratorString && generatorString != null)
                sb.append(" generatorString=\"").append(generatorString).append('"');
            if (generator.hasSeed && seed != null) sb.append(" seed=\"").append(seed).append('"');
            if (forceReset != null) sb.append(" forceReset=\"").append(forceReset).append('"');
            sb.append("/>");

            for (Decorator decorator : decorators) {
                sb.append(decorator.toString());
            }

            if (timeLimitMs != null) {
                sb.append("<ServerQuitFromTimeUp");
                sb.append(" timeLimitMs=\"").append(timeLimitMs).append('"');
                if (timeUpDescription != null) {
                    sb.append(" description=\"").append(timeUpDescription).append('"');
                }
                sb.append("/>");
            }
            if (agentFinishDescription != null) {
                sb.append("<ServerQuitWhenAnyAgentFinishes description=\"").append(agentFinishDescription).append("\"/>");
            }

            sb.append("</ServerHandlers>");

            return sb.toString();
        }

        public static class Builder {
            // Generator Settings
            private WorldGenerator generator;
            private String generatorString;
            private Boolean forceReset;
            private String seed;
            private URI uri;

            // Decorator Settings
            private List<Decorator> decorators;

            // ServerQuit Settings
            private Long timeLimitMs;
            private String timeUpDescription;
            private String agentFinishDescription;

            public Builder setGenerator(WorldGenerator generator) {
                if (generator == null) {
                    throw new IllegalArgumentException("generator may not be null");
                }
                this.generator = generator;

                return this;
            }

            public Builder setGeneratorString(String generatorString) {
                this.generatorString = generatorString;
                return this;
            }

            public Builder setForceReset(Boolean forceReset) {
                this.forceReset = forceReset;
                return this;
            }

            public Builder setSeed(String seed) {
                this.seed = seed;
                return this;
            }

            public Builder setUri(URI uri) {
                this.uri = uri;
                return this;
            }

            public Builder addDecorator(Decorator decorator) {
                if (decorator == null) {
                    throw new IllegalArgumentException("decorator may not be null");
                }

                decorators.add(decorator);
                return this;
            }

            public Builder setTimeLimitMs(Long timeLimitMs) {
                if (timeLimitMs < 0) throw new IllegalArgumentException("timeLimitMs may not be smaller than 0");
                if (timeLimitMs.toString().length() > XML_DECIMAL_MAX_LENGTH)
                    throw new IllegalArgumentException("timeLimitMs too big. May not exceed 18 characters length due to XML-Limitations");
                this.timeLimitMs = timeLimitMs;
                return this;
            }

            public Builder setTimeUpDescription(String timeUpDescription) {
                this.timeUpDescription = timeUpDescription;
                return this;
            }

            public Builder setAgentFinishDescription(String agentFinishDescription) {
                this.agentFinishDescription = agentFinishDescription;
                return this;
            }

            public Builder setAgentFinish() {
                this.agentFinishDescription = "";
                return this;
            }


            public Builder(WorldGenerator generator) {
                this.generator = generator;
                decorators = new LinkedList<>();
            }

            public ServerHandlers build() {
                if (generator == WorldGenerator.FileWorldGenerator && uri == null)
                    throw new IllegalStateException("FileWorldGenerator needs an URI");
                return new ServerHandlers(this);
            }
        }

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

        public static abstract class Decorator {
            public static abstract class Builder {

                public abstract Decorator build();
            }

            public abstract String toString();
        }

        public static class DrawingDecorator extends Decorator {


            @Override
            public String toString() {
                // TODO
                return null;
            }

            private DrawingDecorator(Builder builder) {
                // TODO
            }

            public static class Builder extends Decorator.Builder {


                @Override
                public Decorator build() {
                    return new DrawingDecorator(this);
                }
            }
        }
    }
}
