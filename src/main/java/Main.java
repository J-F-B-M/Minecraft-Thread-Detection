import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.msr.malmo.*;

import javax.imageio.ImageIO;

/**
 * Created by Joachim on 04.03.2017.
 */
public class Main {
    static {
        System.load(Paths.get(".\\lib", "MalmoJava.dll").toAbsolutePath().toString()); // attempts to load MalmoJava.dll (on Windows) or libMalmoJava.so (on Linux)
    }

    public static void main(String[] argv) {
        AgentHost agent_host = new AgentHost();
        try {
            StringVector args = new StringVector();
            args.add("JavaExamples_run_mission");
            for (String arg : argv)
                args.add(arg);
            agent_host.parse(args);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            System.err.println(agent_host.getUsage());
            System.exit(1);
        }
        if (agent_host.receivedArgument("help")) {
            System.out.println(agent_host.getUsage());
            System.exit(0);
        }

        MissionSpec my_mission = new MissionSpec();
        my_mission.timeLimitInSeconds(10);
        my_mission.requestVideo(320, 240);
        my_mission.rewardForReachingPosition(19.5f, 0.0f, 19.5f, 100.0f, 1.1f);

        MissionRecordSpec my_mission_record = new MissionRecordSpec("./saved_data.tgz");
        my_mission_record.recordCommands();
        my_mission_record.recordMP4(20, 400000);
        my_mission_record.recordRewards();
        my_mission_record.recordObservations();

        try {
            agent_host.startMission(my_mission, my_mission_record);
        } catch (Exception e) {
            System.err.println("Error starting mission: " + e.getMessage());
            System.exit(1);
        }

        WorldState world_state;

        System.out.print("Waiting for the mission to start");
        do {
            System.out.print(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.err.println("User interrupted while waiting for mission to start.");
                return;
            }
            world_state = agent_host.getWorldState();
            for (int i = 0; i < world_state.getErrors().size(); i++)
                System.err.println("Error: " + world_state.getErrors().get(i).getText());
        } while (!world_state.getIsMissionRunning());
        System.out.println("");

        int iterationCounter = 0;
        // main loop:
        do {
            agent_host.sendCommand("move 1");
            agent_host.sendCommand("turn " + Math.random());
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.err.println("User interrupted while mission was running.");
                return;
            }

            world_state = agent_host.getWorldState();
            System.out.print("video,observations,rewards received: ");
            System.out.print(world_state.getNumberOfVideoFramesSinceLastState() + ",");
            System.out.print(world_state.getNumberOfObservationsSinceLastState() + ",");
            System.out.println(world_state.getNumberOfRewardsSinceLastState());
            for (int i = 0; i < world_state.getRewards().size(); i++) {
                TimestampedReward reward = world_state.getRewards().get(i);
                System.out.println("Summed reward: " + reward.getValue());
            }
            for (int i = 0; i < world_state.getErrors().size(); i++) {
                TimestampedString error = world_state.getErrors().get(i);
                System.err.println("Error: " + error.getText());
            }

            BufferedImage img = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
            ByteVector pixels = world_state.getVideoFrames().get(0).getPixels();
            int size = 320 * 240;
            for (int x = 0; x < 320; x++) {
                for (int y = 0; y < 240; y++) {
                    img.setRGB(x, y, getPixel(x, y, pixels));
                }
            }
            File output = new File(String.format("output\\image_%03d.png", iterationCounter++));
            try {
                ImageIO.write(img, "png", output);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (world_state.getIsMissionRunning());

        System.out.println("Mission has stopped.");
    }

    private static int getPixel(int x, int y, ByteVector pixels) {
        int r = getColor(0, x, y, pixels) << 16;
        int g = getColor(1, x, y, pixels) << 8;
        int b = getColor(2, x, y, pixels) << 0;
        return 0xFF000000 + r + g + b;
    }

    private static short getColor(int i, int x, int y, ByteVector pixels) {
        int w = 320;
        int h = 240;
        return pixels.get(i * w * h + y * w + x);
        //return pixels.get(x*w+y+i);
    }
}
