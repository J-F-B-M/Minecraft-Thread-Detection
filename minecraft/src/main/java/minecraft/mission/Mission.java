package minecraft.mission;

import com.microsoft.msr.malmo.AgentHost;
import com.microsoft.msr.malmo.ByteVector;
import com.microsoft.msr.malmo.MissionSpec;
import logging.LogObserver;
import minecraft.util.FrameProvider;
import provider.IObservable;

import java.nio.file.Paths;
import java.util.Vector;
import java.util.stream.IntStream;

/**
 * Created by Joachim Brehmer on 24.03.2017.
 */
public class Mission {
    private FrameProvider frameProvider = new FrameProvider();

    AgentHost agentHost = new AgentHost();

    public IObservable<Vector<Byte>> getFrameProvider() {
        return frameProvider;
    }

    public void setupMission(MissionBuilder missionBuilder) {
        MissionSpec spec = missionBuilder.getMissionSpec();
    }

    public void runMission() {

    }
}
