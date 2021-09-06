package de.Tuuby.AgentSimulator.guis;

import com.jogamp.opengl.awt.GLCanvas;
import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.logging.LoggingHandler;
import de.Tuuby.AgentSimulator.main;
import de.Tuuby.AgentSimulator.resource.PropertiesManager;
import de.Tuuby.AgentSimulator.world.World;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Properties;

public class SwingManager {

    // World tab swing elements
    public static XYChart populationChart;
    public static JPanel chartPanel;

    private static JLabel SubtitleLabelWorld;
    private static JLabel InfoLabelWorldSize;
    private static JLabel InfoLabelHillNumber;
    private static JLabel SubtitleLabelFood;
    private static JLabel InfoLabelFoodNumber;
    private static JLabel InfoLabelNutriValue;
    private static JLabel SubtitleLablePopulation;
    private static JLabel InfoLabelHerbivoreNumber;
    private static JLabel InfoLabelAgentNumber;
    private static JLabel SubtitleLabelParameters;
    private static JLabel InfoLabelFoodGrowthRate;
    private static JLabel InfoLabelHillSize;
    private static JLabel InfoLabelHillCount;

    private static JSpinner ParameterGrowthRateSpinner;
    private static JSpinner ParameterHillSizeSpinner;
    private static JSpinner ParameterHillCountSpinner;

    // Population tab swing elements
    private static JLabel SubtitleLabelHerbivores;

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
        SubtitleLabelWorld = new JLabel("World");
        SubtitleLabelWorld.setAlignmentX(Component.CENTER_ALIGNMENT);
        SubtitleLabelWorld.setFont(titleFont);
        worldPanel.add(SubtitleLabelWorld);

        InfoLabelWorldSize = new JLabel("Size: " + properties.getProperty("worldWidth") + " x " + properties.getProperty("worldHeight"));
        InfoLabelWorldSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelWorldSize.setFont(infoFont);
        worldPanel.add(InfoLabelWorldSize);

        InfoLabelHillNumber = new JLabel("# Hills: " + properties.getProperty("hillCount"));
        InfoLabelHillNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHillNumber.setFont(infoFont);
        worldPanel.add(InfoLabelHillNumber);

        SubtitleLabelFood = new JLabel("Food");
        SubtitleLabelFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        SubtitleLabelFood.setFont(titleFont);
        worldPanel.add(SubtitleLabelFood);

        InfoLabelFoodNumber = new JLabel("# Food: " + properties.getProperty("foodCount"));
        InfoLabelFoodNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelFoodNumber.setFont(infoFont);
        worldPanel.add(InfoLabelFoodNumber);

        InfoLabelNutriValue = new JLabel("Avg. Nutrit. Value: 12.000 - 15.000");
        InfoLabelNutriValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelNutriValue.setFont(infoFont);
        worldPanel.add(InfoLabelNutriValue);

        SubtitleLablePopulation = new JLabel("Population");
        SubtitleLablePopulation.setAlignmentX(Component.CENTER_ALIGNMENT);
        SubtitleLablePopulation.setFont(titleFont);
        worldPanel.add(SubtitleLablePopulation);

        InfoLabelHerbivoreNumber = new JLabel("# Herbivores: " + properties.getProperty("herbivoreCount"));
        InfoLabelHerbivoreNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHerbivoreNumber.setFont(infoFont);
        worldPanel.add(InfoLabelHerbivoreNumber);

        InfoLabelAgentNumber = new JLabel("# Agents: " + properties.getProperty("agentCount"));
        InfoLabelAgentNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentNumber.setFont(infoFont);
        worldPanel.add(InfoLabelAgentNumber);

        SubtitleLabelParameters = new JLabel("Parameter");
        SubtitleLabelParameters.setAlignmentX(Component.CENTER_ALIGNMENT);
        SubtitleLabelParameters.setFont(titleFont);
        worldPanel.add(SubtitleLabelParameters);

        InfoLabelFoodGrowthRate = new JLabel("Food Growth Rate");
        InfoLabelFoodGrowthRate.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelFoodGrowthRate.setFont(infoFont);
        worldPanel.add(InfoLabelFoodGrowthRate);

        SpinnerModel growthModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("foodSpawnAmount")), 0, 20, 1);
        ParameterGrowthRateSpinner = new JSpinner(growthModel);
        ParameterGrowthRateSpinner.setMaximumSize(new Dimension(50, 50));
        ParameterGrowthRateSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        worldPanel.add(ParameterGrowthRateSpinner);

        InfoLabelHillSize = new JLabel("Hill size");
        InfoLabelHillSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHillSize.setFont(infoFont);
        worldPanel.add(InfoLabelHillSize);

        SpinnerModel hillSizeModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("hillSize")), 0, 50, 1);
        ParameterHillSizeSpinner = new JSpinner(hillSizeModel);
        ParameterHillSizeSpinner.setMaximumSize(new Dimension(50, 50));
        ParameterHillSizeSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        worldPanel.add(ParameterHillSizeSpinner);

        InfoLabelHillCount = new JLabel("Hill count");
        InfoLabelHillCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHillCount.setFont(infoFont);
        worldPanel.add(InfoLabelHillCount);

        SpinnerModel hillCountModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("hillCount")), 0, 50, 1);
        ParameterHillCountSpinner = new JSpinner(hillCountModel);
        ParameterHillCountSpinner.setMaximumSize(new Dimension(50, 50));
        ParameterHillCountSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        worldPanel.add(ParameterHillCountSpinner);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        worldPanel.add(separator);

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

        JButton SaveConfigButton = new JButton("Save Config");
        SaveConfigButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeParametersToConfig(properties);
            }
        });
        ButtonPanel.add(SaveConfigButton);

        worldPanel.add(ButtonPanel);

        // Create the second tab for the Tab Panel
        // "Population"-Tab

        JPanel populationPanel = new JPanel();
        populationPanel.setLayout(new BoxLayout(populationPanel, BoxLayout.Y_AXIS));

        SubtitleLabelHerbivores = new JLabel("Herbivores");
        SubtitleLabelHerbivores.setAlignmentX(Component.CENTER_ALIGNMENT);
        SubtitleLabelHerbivores.setFont(titleFont);
        populationPanel.add(SubtitleLabelHerbivores);

        // Finalizing the tabbed panel
        tabbedPane.add("World", worldPanel);
        tabbedPane.add("Population", populationPanel);
        mainPanel.add(tabbedPane, BorderLayout.EAST);
        mainFrame.getContentPane().add(mainPanel);
    }

    public static void updatePopulation(double[] worldAgeData, double[] foodData, double[] herbData, double[] agentData) {
        populationChart.updateXYSeries("Food", worldAgeData, foodData, null);
        populationChart.updateXYSeries("Herbivores", worldAgeData, herbData, null);
        populationChart.updateXYSeries("Carnivores", worldAgeData, agentData, null);

        chartPanel.repaint();
    }

    public static void updateLabels(World world) {
        DecimalFormat df2 = new DecimalFormat("###");
        String avgEnergy = df2.format(world.getAvgHerbEnergy());
        InfoLabelNutriValue.setText("Avg. Nutrit. Value: " + avgEnergy + "\t");
        InfoLabelFoodNumber.setText("# Food: " + world.getFoodCount());
        InfoLabelHerbivoreNumber.setText("# Herbivore: " + world.getHerbivoreCount());
        InfoLabelAgentNumber.setText("# Agent: " + world.getAgentCount());
    }

    private static void writeParametersToConfig(Properties properties) {
        properties.setProperty("foodSpawnAmount", ParameterGrowthRateSpinner.getValue().toString());
        properties.setProperty("hillSize", ParameterHillSizeSpinner.getValue().toString());
        properties.setProperty("hillCount", ParameterHillCountSpinner.getValue().toString());
        PropertiesManager.writeProperties(properties);
    }
}
