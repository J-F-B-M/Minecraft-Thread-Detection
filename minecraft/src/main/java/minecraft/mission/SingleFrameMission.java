package minecraft.mission;

import com.microsoft.msr.malmo.*;
import minecraft.util.FrameProvider;
import minecraft.util.ObservationProvider;
import org.json.JSONObject;
import provider.IObservable;

import java.nio.file.Paths;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by Joachim Brehmer on 24.03.2017.
 */
public class SingleFrameMission {
    static {
        System.load(Paths.get(".\\lib", "MalmoJava.dll").toAbsolutePath().toString()); // attempts to load MalmoJava.dll (on Windows) or libMalmoJava.so (on Linux)
    }

    private static final Logger log = Logger.getLogger(SingleFrameMission.class.getName());
    ClientPool clientPool;
    private FrameProvider frameProvider = new FrameProvider();
    private ObservationProvider observationProvider = new ObservationProvider();
    private AgentHost agentHost = new AgentHost();
    private MissionSpec missionSpec;
    private String uniqueExperimentId = "";

    public SingleFrameMission() {
        this("127.0.0.1", 10000);
    }

    public SingleFrameMission(String clientIpAddress, int port) {
        if (clientIpAddress == null) throw new IllegalArgumentException("clientIpAddress may not be null");

        if (2 << 16 - 1 < port || port < 0) throw new IllegalArgumentException("port outside of range");
        if (11000 < port || port < 10000)
            log.warning("port outside of normal range, consider to choose a port between 10000 and 11000.");

        clientPool = new ClientPool();
        clientPool.add(new ClientInfo(clientIpAddress, port));

        agentHost.setObservationsPolicy(AgentHost.ObservationsPolicy.KEEP_ALL_OBSERVATIONS);
        agentHost.setRewardsPolicy(AgentHost.RewardsPolicy.KEEP_ALL_REWARDS);
        agentHost.setVideoPolicy(AgentHost.VideoPolicy.KEEP_ALL_FRAMES);
    }

    public String getUniqueExperimentId() {
        return uniqueExperimentId;
    }

    public void setUniqueExperimentId(String uniqueExperimentId) {
        if (uniqueExperimentId == null) {
            throw new IllegalArgumentException("uniqueExperimentId may not be null");
        }
        this.uniqueExperimentId = uniqueExperimentId;
    }

    public IObservable<Vector<Short>> getFrameProvider() {
        return frameProvider;
    }

    public IObservable<JSONObject> getObservationProvider() {
        return observationProvider;
    }

    public void setupMission(String missionXML) throws IllegalArgumentException {
        MissionSpec missionSpec;
        try {
            this.missionSpec = new MissionSpec(missionXML, true);
        } catch (Exception e) {
            throw new IllegalArgumentException("missionXML is not compliant with the schema", e);
        }
    }

    public void runMission() {
        MissionRecordSpec missionRecordSpec = new MissionRecordSpec("./saved_data.tgz");
        try {
            agentHost.startMission(missionSpec, clientPool, missionRecordSpec, 0, uniqueExperimentId);
        } catch (Exception e) {
            throw new IllegalStateException("This went wrong...", e);
        }

        WorldState worldState;

        log.info("Waiting for mission to start");
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                log.warning("User interrupted while waiting for mission to start.");
                return;
            }
            worldState = agentHost.getWorldState();
            for (int i = 0; i < worldState.getErrors().size(); i++)
                log.warning("Error: " + worldState.getErrors().get(i).getText());
        } while (!worldState.getIsMissionRunning());

        log.info("Starting mission");

        int iterationCounter = 0;
        do {
            String worldStateString = String.format("video,observations,rewards received: %d, %d, %d", worldState.getNumberOfVideoFramesSinceLastState(), worldState.getNumberOfObservationsSinceLastState(), worldState.getNumberOfRewardsSinceLastState());
            for (int i = 0; i < worldState.getErrors().size(); i++) {
                log.warning(worldState.getErrors().get(i).getTimestamp().toString() + "\t" + worldState.getErrors().get(i).getText());
            }
            for (int i = 0; i < worldState.getObservations().size(); i++) {
                observationProvider.notifyObservers(worldState.getObservations().get(i));
            }
            for (int i = 0; i < worldState.getVideoFrames().size(); i++) {
                frameProvider.notifyObservers(worldState.getVideoFrames().get(i).getPixels());
            }
            try {
                if (iterationCounter == 0) {
                    Thread.sleep(500);

                } else if (iterationCounter == 1) {
                    agentHost.sendCommand("quit");
                    log.info("Send quit, waiting 100 ms for mission to end.");
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                log.warning("User interrupted while waiting for observations.");
                return;
            }
            iterationCounter++;
            worldState = agentHost.getWorldState();
        } while (worldState.getIsMissionRunning());
    }
}
