package experiments;

import minecraft.mission.SingleFrameMission;
import minecraft.util.SchemaHelper;
import minecraft.xml.*;
import org.json.JSONObject;
import provider.IObservable;
import provider.IObserver;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by Joachim on 01.04.2017.
 */
public class SavingObservations {
    private static final Logger log = Logger.getLogger(SavingObservations.class.getName());

    public static void main(String[] args) throws JAXBException {
        Mission mission = new Mission();

        setAbout(mission);
        setServerSection(mission);
        setAgentSection(mission);

        Paths.get("output").toFile().mkdir();

        SingleFrameMission singleFrameMission = new SingleFrameMission();

        System.out.println(SchemaHelper.serialiseObject(mission, Mission.class));

        singleFrameMission.setupMission(SchemaHelper.serialiseObject(mission, Mission.class));
        IObserver<Vector<Short>> frameObserver = new IObserver<Vector<Short>>() {
            private int frameNumber = 0;

            @Override
            public void update(IObservable<Vector<Short>> observable, Vector<Short> argument) {
                BufferedImage colorImg = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
                BufferedImage depthImg = new BufferedImage(320, 240, BufferedImage.TYPE_BYTE_GRAY);
                int size = 320 * 240;
                for (int x = 0; x < 320; x++) {
                    for (int y = 0; y < 240; y++) {
                        Color c = new Color(argument.get((320 * y + x) * 4), argument.get((320 * y + x) * 4 + 1), argument.get((320 * y + x) * 4 + 2));
                        colorImg.setRGB(x, y, c.getRGB());
                        short gray = argument.get((320 * y + x) * 4 + 3);
                        depthImg.setRGB(x, y, new Color(gray, gray, gray).getRGB());
                    }
                }
                File colorOutput = new File(String.format("output\\color_%03d.png", frameNumber));
                File depthOutput = new File(String.format("output\\depth_%03d.png", frameNumber));
                try {
                    ImageIO.write(colorImg, "png", colorOutput);
                    ImageIO.write(depthImg, "png", depthOutput);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                frameNumber++;
            }
        };
        singleFrameMission.getFrameProvider().addObserver(frameObserver);
        IObserver<JSONObject> observationObserver = new IObserver<JSONObject>() {
            List<JSONObject> observations = new LinkedList<>();

            @Override
            public void update(IObservable<JSONObject> observable, JSONObject argument) {
                observations.add(argument);
            }
        };
        singleFrameMission.getObservationProvider().addObserver(observationObserver);

        singleFrameMission.runMission();
        log.info("All Done");
    }

    private static void setAbout(Mission mission) {
        About about = new About();
        mission.setAbout(about);
        about.setSummary("OutputTest");
    }

    private static void setServerSection(Mission mission) {
        ServerSection serverSection = new ServerSection();
        mission.setServerSection(serverSection);

        // ServerInitialConditions
        ServerInitialConditions serverInitialConditions = new ServerInitialConditions();
        serverSection.setServerInitialConditions(serverInitialConditions);

        Time time = new Time();
        serverInitialConditions.setTime(time);
        time.setStartTime(6000);
        time.setAllowPassageOfTime(false);

        serverInitialConditions.setWeather("clear");

        // ServerHandlers
        ServerHandlers serverHandlers = new ServerHandlers();
        serverSection.setServerHandlers(serverHandlers);

        DefaultWorldGenerator defaultWorldGenerator = new DefaultWorldGenerator();
        serverHandlers.setWorldGenerator(defaultWorldGenerator);
        defaultWorldGenerator.setSeed("SavingObservations");
        defaultWorldGenerator.setForceReset(true);
        defaultWorldGenerator.setDestroyAfterUse(true);
    }

    private static void setAgentSection(Mission mission) {
        AgentSection agentSection = new AgentSection();
        mission.getAgentSection().add(agentSection);

        agentSection.setMode(GameMode.CREATIVE);
        agentSection.setName("JFBM");

        AgentStart agentStart = new AgentStart();
        agentSection.setAgentStart(agentStart);

        AgentHandlers agentHandlers = new AgentHandlers();
        agentSection.setAgentHandlers(agentHandlers);
        List<Object> agentMissionHandlers = agentHandlers.getAgentMissionHandlers();
        agentMissionHandlers.add(new ObservationFromRecentCommands());
        agentMissionHandlers.add(new ObservationFromHotBar());
        agentMissionHandlers.add(new ObservationFromFullStats());
        agentMissionHandlers.add(new ObservationFromFullInventory());
        ObservationFromGrid observationFromGrid = new ObservationFromGrid();
        GridDefinition gridDefinition = new GridDefinition();
        Pos min = new Pos();
        min.setX(new BigDecimal(-10));
        min.setY(new BigDecimal(-10));
        min.setZ(new BigDecimal(-10));
        Pos max = new Pos();
        max.setX(new BigDecimal(10));
        max.setY(new BigDecimal(10));
        max.setZ(new BigDecimal(10));
        gridDefinition.setName("surroundings");
        gridDefinition.setMin(min);
        gridDefinition.setMax(max);
        observationFromGrid.getGrid().add(gridDefinition);
        agentMissionHandlers.add(observationFromGrid);
        agentMissionHandlers.add(new ObservationFromDiscreteCell());
        agentMissionHandlers.add(new ObservationFromChat());
        ObservationFromNearbyEntities observationFromNearbyEntities = new ObservationFromNearbyEntities();
        RangeDefinition rangeDefinition = new RangeDefinition();
        rangeDefinition.setName("NearbyEntities");
        rangeDefinition.setUpdateFrequency(1);
        rangeDefinition.setXrange(new BigDecimal(10));
        rangeDefinition.setYrange(new BigDecimal(10));
        rangeDefinition.setZrange(new BigDecimal(10));
        observationFromNearbyEntities.getRange().add(rangeDefinition);
        agentMissionHandlers.add(observationFromNearbyEntities);
        agentMissionHandlers.add(new ObservationFromRay());
        VideoProducer videoProducer = new VideoProducer();
        videoProducer.setWantDepth(true);
        videoProducer.setWidth(320);
        videoProducer.setHeight(240);
        agentMissionHandlers.add(videoProducer);
        agentMissionHandlers.add(new MissionQuitCommands());
    }


}
