package de.Tuuby.AgentSimulator.engine;

import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.input.KeyInput;
import de.Tuuby.AgentSimulator.logging.LoggingHandler;

import java.awt.event.KeyEvent;

// Class that controls in what time intervals render() and update() get called
public class GameLoop {

    // Variable to track if the Gameloop is running
    private static boolean running = false;
    private static boolean onHold = false;

    // Variables for MAX and current updates in a single render Cycle
    private static int updates = 0;
    private static final int MAX_UPDATES = 5;

    // Variable for the last updateTime to avoid too many updates
    private static long lastUpdateTime = 0;

    // Variables for setting the target Frames per Second and target Time per Frame;
    private static final int targetFPS = 60;
    private static final int targetTime = 1000000000 / targetFPS;

    // Starts the Gameloop that checks for input, then updates the World and renders the final result
    public static void start() {
        final Thread thread = new Thread() {
            @Override
            public void run() {

                running = true;

                lastUpdateTime = System.nanoTime();

                int fps = 0;
                long lastFpsCheck = System.nanoTime();

                while (running) {
                    if (KeyInput.getKey(KeyEvent.VK_ESCAPE)) {
                        GameLoop.stop();
                        LoggingHandler.saveAndExit();
                        Renderer.stop();
                    }
                    long currentTime = System.nanoTime();

                    updates = 0;

                    while (currentTime - lastUpdateTime > targetTime) {
                        if (!onHold)
                            WorldUpdater.update();
                        lastUpdateTime += targetTime;
                        updates++;

                        if (updates > MAX_UPDATES)
                            break;
                    }

                    Renderer.render();

                    fps++;
                    if (System.nanoTime() >= lastFpsCheck + 1000000000) {
                        //System.out.println(fps);
                        fps = 0;
                        lastFpsCheck = System.nanoTime();
                    }

                    long timeTaken = System.nanoTime() - currentTime;
                    if (targetTime > timeTaken) {
                        try {
                            Thread.sleep((targetTime - timeTaken) / 1000000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        thread.setName("GameLoop");
        thread.start();
    }

    // Sets the attribute "running" to false and causes  the GameLoop to stop
    public static void stop() {
        running = false;
    }

    // Method to return the targeted time for a single frame in fractions of a second i think
    public static float updateDelta() {
        return 1.0f / 1000000000 * targetTime;
    }

    public static void toggleHold() {
        onHold = !onHold;
    }
}
