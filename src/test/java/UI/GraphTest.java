package UI;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.guis.Graph;
import de.Tuuby.AgentSimulator.resource.PropertiesLoader;

public class GraphTest {
    public static void main(String[] args) {
        PropertiesLoader.loadConfig();
        Renderer.init();

        Graph graph = new Graph(200, 200, 100, 100, true, 5, "Test");
        graph.setColor(1, 0, 0, 1);
        graph.addValue(4);
        graph.addValue(5);
        graph.addValue(5);
        graph.addValue(6);
        graph.addValue(8);
        graph.addValue(8);
        graph.addValue(4);
        graph.addValue(3);
        graph.addValue(2);
        graph.addValue(2);
        graph.addValue(1);
        graph.addValue(1);
        graph.addValue(1);
        graph.addValue(0);
        graph.addValue(0);
        graph.addValue(1);
        graph.addValue(1);
        graph.addValue(1);
        graph.addValue(1);
        graph.addValue(1);
        WorldUpdater.addGUI(graph);
        GameLoop.start();
    }
}
