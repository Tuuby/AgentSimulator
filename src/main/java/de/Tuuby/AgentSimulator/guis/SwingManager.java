package de.Tuuby.AgentSimulator.guis;

import com.jogamp.opengl.awt.GLCanvas;
import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.logging.LoggingHandler;
import de.Tuuby.AgentSimulator.main;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Properties;

public class SwingManager {

    public static XYChart populationChart;
    public static JPanel chartPanel;

    public static void build(JFrame mainFrame, GLCanvas glCanvas, Properties properties) {

        Font titleFont = new Font("SansSerif", Font.BOLD, 16);
        Font infoFont = new Font("SansSerif", Font.PLAIN, 12);

        // Make new mainPanel with a Layout to arrange Elements in the window
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(glCanvas, BorderLayout.CENTER);

        // Create a tabbed Panel to allow cycling through different info sections
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        // Create the first tab for the Tab Panel
        // "World"-Tab
        JPanel worldPanel = new JPanel();
        worldPanel.setLayout(new BoxLayout(worldPanel, BoxLayout.Y_AXIS));

        // Create chart to represent populations in real time
        populationChart = new XYChartBuilder()
                .width(300)
                .height(150)
                .title("Population Graph")
                .xAxisTitle("World Age")
                .yAxisTitle("Population")
                .build();
        populationChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideS);
        populationChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        populationChart.getStyler().setMarkerSize(0);

        XYSeries foodSeries = populationChart.addSeries("Food", new double[] { 0 }, new double[] { Double.parseDouble(properties.getProperty("foodCount")) });
        foodSeries.setLineWidth(5);
        foodSeries.setLineColor(Color.RED);
        XYSeries herbSeries = populationChart.addSeries("Herbivores", new double[] { 0 }, new double[] { Double.parseDouble(properties.getProperty("herbivoreCount")) });
        herbSeries.setLineWidth(5);
        herbSeries.setLineColor(Color.GREEN);
        XYSeries agentSeries = populationChart.addSeries("Carnivores", new double[] { 0 }, new double[] { Double.parseDouble(properties.getProperty("agentCount")) });
        agentSeries.setLineWidth(5);
        agentSeries.setLineColor(Color.BLUE);

        chartPanel = new XChartPanel<XYChart>(populationChart);
        worldPanel.add(chartPanel);

        // Create Labels for information
        JLabel SubtitleLabelWorld = new JLabel("World");
        SubtitleLabelWorld.setFont(titleFont);
        worldPanel.add(SubtitleLabelWorld);

        JLabel InfoLabelWorldSize = new JLabel("Size: " + properties.getProperty("worldWidth") + " x " + properties.getProperty("worldHeight"));
        InfoLabelWorldSize.setFont(infoFont);
        worldPanel.add(InfoLabelWorldSize);

        JLabel InfoLabelHillNumber = new JLabel("# Hills: " + properties.getProperty("hillCount"));
        InfoLabelHillNumber.setFont(infoFont);
        worldPanel.add(InfoLabelHillNumber);

        JLabel SubtitleLabelFood = new JLabel("Food");
        SubtitleLabelFood.setFont(titleFont);
        worldPanel.add(SubtitleLabelFood);

        JLabel InfoLabelFoodNumber = new JLabel("# Food: " + properties.getProperty("foodCount"));
        InfoLabelFoodNumber.setFont(infoFont);
        worldPanel.add(InfoLabelFoodNumber);

        JLabel InfoLabelNutriValue = new JLabel("Nutrit. Value per Object: 12.000 - 15.000");
        InfoLabelNutriValue.setFont(infoFont);
        worldPanel.add(InfoLabelNutriValue);

        JLabel SubtitleLablePopulation = new JLabel("Population");
        SubtitleLablePopulation.setFont(titleFont);
        worldPanel.add(SubtitleLablePopulation);

        JLabel InfoLabelHerbivoreNumber = new JLabel("# Herbivores: " + properties.getProperty("herbivoreCount"));
        InfoLabelHerbivoreNumber.setFont(infoFont);
        worldPanel.add(InfoLabelHerbivoreNumber);

        JLabel InfoLabelAgentNumber = new JLabel("# Agents: " + properties.getProperty("agentCount"));
        InfoLabelAgentNumber.setFont(infoFont);
        worldPanel.add(InfoLabelAgentNumber);

        JLabel SubtitleLabelParameters = new JLabel("Parameter");
        SubtitleLabelParameters.setFont(titleFont);
        worldPanel.add(SubtitleLabelParameters);

        JLabel InfoLabelFoodGrowthRate = new JLabel("Food Growth Rate: " + properties.getProperty("foodSpawnAmount"));
        InfoLabelFoodGrowthRate.setFont(infoFont);
        worldPanel.add(InfoLabelFoodGrowthRate);

        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new BoxLayout(ButtonPanel, BoxLayout.X_AXIS));

        JButton PauseButton = new JButton("Pause");
        PauseButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameLoop.toggleHold();
                if (GameLoop.getHold()) {
                    PauseButton.setText("Play");
                } else {
                    PauseButton.setText("Pause");
                }
            }
        });
        ButtonPanel.add(PauseButton);

        JButton RestartButton = new JButton("Restart");
        RestartButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.worldGen.clear();
                LoggingHandler.saveAndExit();
                LoggingHandler.init();
                main.worldGen.generate();
                WorldUpdater.setWorld(main.worldGen.getWorld());
            }
        });
        ButtonPanel.add(RestartButton);

        worldPanel.add(ButtonPanel);

        tabbedPane.add("World", worldPanel);
        mainPanel.add(tabbedPane, BorderLayout.EAST);
        mainFrame.getContentPane().add(mainPanel);
    }

    public static void updatePopulation(double[] worldAgeData, double[] foodData, double[] herbData, double[] agentData) {
        populationChart.updateXYSeries("Food", worldAgeData, foodData, null);
        populationChart.updateXYSeries("Herbivores", worldAgeData, herbData, null);
        populationChart.updateXYSeries("Carnivores", worldAgeData, agentData, null);

        chartPanel.repaint();
    }
}
