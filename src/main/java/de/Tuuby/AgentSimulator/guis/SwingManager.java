package de.Tuuby.AgentSimulator.guis;

import com.jogamp.opengl.awt.GLCanvas;
import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.logging.DataSet;
import de.Tuuby.AgentSimulator.logging.LoggingHandler;
import de.Tuuby.AgentSimulator.main;
import de.Tuuby.AgentSimulator.resource.PropertiesManager;
import de.Tuuby.AgentSimulator.world.Agent;
import de.Tuuby.AgentSimulator.world.WorldGenerator;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Properties;

public class SwingManager {

    // World tab swing elements
    public static XYChart populationChart;
    public static JPanel chartPanel;

    private static JLabel InfoLabelFoodNumber;
    private static JLabel InfoLabelNutriValue;
    private static JLabel InfoLabelHerbivoreNumber;
    private static JLabel InfoLabelAgentNumber;

    private static JSpinner ParameterGrowthRateSpinner;
    private static JSpinner ParameterHillSizeSpinner;
    private static JSpinner ParameterHillCountSpinner;

    // Population tab swing elements
    private static JLabel InfoLabelHerbivoreNumber2;
    private static JLabel InfoLabelParalyzedHerbs;
    private static JLabel InfoLabelDeadHerbs;
    private static JLabel InfoLabelAllTimeHerbs;

    private static JLabel InfoLabelHerbEnergy;
    private static JLabel InfoLabelHerbFood;
    private static JLabel InfoLabelHerbSpeed;
    private static JLabel InfoLabelHerbVision;

    private static JLabel InfoLabelAgentsCurrent;
    private static JLabel InfoLabelAgentsFemales;
    private static JLabel InfoLabelAgentsMales;
    private static JLabel InfoLabelAgentsAttackers;
    private static JLabel InfoLabelAgentsParalyzers;
    private static JLabel InfoLabelAgentsLeaders;
    private static JLabel InfoLabelAgentsInGroup;
    private static JLabel InfoLabelAgentsAlltime;

    private static JLabel InfoLabelAgentGenerationAvg;
    private static JLabel InfoLabelAgentAge;
    private static JLabel InfoLabelAgentSpeed;
    private static JLabel InfoLabelAgentVision;
    private static JLabel InfoLabelAgentStamina;
    private static JLabel InfoLabelAgentMaxStamina;
    private static JLabel InfoLabelAgentFood;
    private static JLabel InfoLabelAgentFoodCap;

    private static JLabel InfoLabelGroupsAlltime;
    private static JLabel InfoLabelGroupsCurrent;
    private static JLabel InfoLabelGroupsDissolved;

    private static JLabel InfoLabelGroupMembersAlltimeMax;
    private static JLabel InfoLabelGroupMembersAlltimeAvg;
    private static JLabel InfoLabelGroupMembersCurrentMax;
    private static JLabel InfoLabelGroupMembersCurrentAvg;

    private static JLabel InfoLabelGroupSuccessMax;
    private static JLabel InfoLabelGroupSuccessAvg;

    private static JLabel InfoLabelGroupAttackersBest;
    private static JLabel InfoLabelGroupParalyzersBest;
    private static JLabel InfoLabelGroupAttackersAvg;
    private static JLabel InfoLabelGroupParalyzersAvg;

    // Detailed tab swing elements
    public static JSpinner AgentNumberSpinner;
    private static JLabel InfoLabelDetailedGender;
    private static JLabel InfoLabelDetailedSpecial;
    private static JLabel InfoLabelDetailedState;

    private static JLabel InfoLabelDetailedGeneration;
    private static JLabel InfoLabelDetailedAges;
    private static JLabel InfoLabelDetailedSpeed;
    private static JLabel InfoLabelDetailedVision;
    private static JLabel InfoLabelDetailedStamina;
    private static JLabel InfoLabelDetailedFood;
    private static JLabel InfoLabelDetailedReprodInstinct;
    private static JLabel InfoLabelDetailedReprods;
    private static JLabel InfoLabelDetailedParalyzes;

    private static JLabel InfoLabelAncestorAge;
    private static JLabel InfoLabelAncestorSpeed;
    private static JLabel InfoLabelAncestorVision;
    private static JLabel InfoLabelAncestorStamina;
    private static JLabel InfoLabelAncestorFood;
    private static JLabel InfoLabelAncestorReprod;

    private static JLabel InfoLabelGroupLeader;
    private static JLabel InfoLabelGroupMembers;
    private static JLabel InfoLabelGroupAttackers;
    private static JLabel InfoLabelGroupParalyzers;
    private static JLabel InfoLabelGroupAverageFood;

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
        JLabel subtitleLabelWorld = new JLabel("World");
        subtitleLabelWorld.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelWorld.setFont(titleFont);
        worldPanel.add(subtitleLabelWorld);

        JLabel infoLabelWorldSize = new JLabel("Size: " + properties.getProperty("worldWidth") + " x " + properties.getProperty("worldHeight"));
        infoLabelWorldSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabelWorldSize.setFont(infoFont);
        worldPanel.add(infoLabelWorldSize);

        JLabel infoLabelHillNumber = new JLabel("# Hills: " + properties.getProperty("hillCount"));
        infoLabelHillNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabelHillNumber.setFont(infoFont);
        worldPanel.add(infoLabelHillNumber);

        JLabel subtitleLabelFood = new JLabel("Food");
        subtitleLabelFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelFood.setFont(titleFont);
        worldPanel.add(subtitleLabelFood);

        InfoLabelFoodNumber = new JLabel("# Food: " + properties.getProperty("foodCount"));
        InfoLabelFoodNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelFoodNumber.setFont(infoFont);
        worldPanel.add(InfoLabelFoodNumber);

        InfoLabelNutriValue = new JLabel("Avg. Nutrit. Value: 12.000 - 15.000");
        InfoLabelNutriValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelNutriValue.setFont(infoFont);
        worldPanel.add(InfoLabelNutriValue);

        JLabel subtitleLablePopulation = new JLabel("Population");
        subtitleLablePopulation.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLablePopulation.setFont(titleFont);
        worldPanel.add(subtitleLablePopulation);

        InfoLabelHerbivoreNumber = new JLabel("# Herbivores: " + properties.getProperty("herbivoreCount"));
        InfoLabelHerbivoreNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHerbivoreNumber.setFont(infoFont);
        worldPanel.add(InfoLabelHerbivoreNumber);

        InfoLabelAgentNumber = new JLabel("# Agents: " + properties.getProperty("agentCount"));
        InfoLabelAgentNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentNumber.setFont(infoFont);
        worldPanel.add(InfoLabelAgentNumber);

        JLabel subtitleLabelParameters = new JLabel("Parameter");
        subtitleLabelParameters.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelParameters.setFont(titleFont);
        worldPanel.add(subtitleLabelParameters);

        JLabel infoLabelFoodGrowthRate = new JLabel("Food Growth Rate");
        infoLabelFoodGrowthRate.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabelFoodGrowthRate.setFont(infoFont);
        worldPanel.add(infoLabelFoodGrowthRate);

        SpinnerModel growthModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("foodSpawnAmount")), 0, 20, 1);
        ParameterGrowthRateSpinner = new JSpinner(growthModel);
        ParameterGrowthRateSpinner.setMaximumSize(new Dimension(50, 50));
        ParameterGrowthRateSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        worldPanel.add(ParameterGrowthRateSpinner);

        JLabel infoLabelHillSize = new JLabel("Hill size");
        infoLabelHillSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabelHillSize.setFont(infoFont);
        worldPanel.add(infoLabelHillSize);

        SpinnerModel hillSizeModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("hillSize")), 0, 50, 1);
        ParameterHillSizeSpinner = new JSpinner(hillSizeModel);
        ParameterHillSizeSpinner.setMaximumSize(new Dimension(50, 50));
        ParameterHillSizeSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        worldPanel.add(ParameterHillSizeSpinner);

        JLabel infoLabelHillCount = new JLabel("Hill count");
        infoLabelHillCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabelHillCount.setFont(infoFont);
        worldPanel.add(infoLabelHillCount);

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
                PropertiesManager.loadConfig();
                main.worldGen = new WorldGenerator(PropertiesManager.getAppConfig());
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

        JButton DebugButton = new JButton("Debug");
        DebugButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorldUpdater.toggleDebug();
            }
        });
        ButtonPanel.add(DebugButton);

        worldPanel.add(ButtonPanel);

        // Create the second tab for the Tab Panel
        // "Population"-Tab
        JPanel populationPanel = new JPanel();
        populationPanel.setLayout(new BoxLayout(populationPanel, BoxLayout.Y_AXIS));

        JLabel subtitleLabelHerbivores = new JLabel("Herbivores");
        subtitleLabelHerbivores.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelHerbivores.setFont(titleFont);
        populationPanel.add(subtitleLabelHerbivores);

        InfoLabelHerbivoreNumber2 = new JLabel("Current: " + properties.getProperty("herbivoreCount"));
        InfoLabelHerbivoreNumber2.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHerbivoreNumber2.setFont(infoFont);
        populationPanel.add(InfoLabelHerbivoreNumber2);

        InfoLabelParalyzedHerbs = new JLabel("Paralyzed:");
        InfoLabelParalyzedHerbs.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelParalyzedHerbs.setFont(infoFont);
        populationPanel.add(InfoLabelParalyzedHerbs);

        InfoLabelDeadHerbs = new JLabel("Dead:");
        InfoLabelDeadHerbs.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDeadHerbs.setFont(infoFont);
        populationPanel.add(InfoLabelDeadHerbs);

        InfoLabelAllTimeHerbs = new JLabel("All-time:");
        InfoLabelAllTimeHerbs.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAllTimeHerbs.setFont(infoFont);
        populationPanel.add(InfoLabelAllTimeHerbs);

        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        populationPanel.add(separator2);

        JLabel subtitleLabelHerbAttributes = new JLabel("Herbivore Attributes    Avg / Max");
        subtitleLabelHerbAttributes.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelHerbAttributes.setFont(titleFont);
        populationPanel.add(subtitleLabelHerbAttributes);

        InfoLabelHerbEnergy = new JLabel("Energy:");
        InfoLabelHerbEnergy.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHerbEnergy.setFont(infoFont);
        populationPanel.add(InfoLabelHerbEnergy);

        InfoLabelHerbFood = new JLabel("Food:");
        InfoLabelHerbFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHerbFood.setFont(infoFont);
        populationPanel.add(InfoLabelHerbFood);

        InfoLabelHerbSpeed = new JLabel("Speed:");
        InfoLabelHerbSpeed.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHerbSpeed.setFont(infoFont);
        populationPanel.add(InfoLabelHerbSpeed);

        InfoLabelHerbVision = new JLabel("Vision:");
        InfoLabelHerbVision.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelHerbVision.setFont(infoFont);
        populationPanel.add(InfoLabelHerbVision);

        JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
        populationPanel.add(separator3);

        JLabel subtitleLabelAgentStats = new JLabel("Agents");
        subtitleLabelAgentStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelAgentStats.setFont(titleFont);
        populationPanel.add(subtitleLabelAgentStats);

        InfoLabelAgentsCurrent = new JLabel("Current:");
        InfoLabelAgentsCurrent.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsCurrent.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsCurrent);

        InfoLabelAgentsFemales = new JLabel("Females:");
        InfoLabelAgentsFemales.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsFemales.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsFemales);

        InfoLabelAgentsMales = new JLabel("Males:");
        InfoLabelAgentsMales.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsMales.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsMales);

        InfoLabelAgentsAttackers = new JLabel("Attackers:");
        InfoLabelAgentsAttackers.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsAttackers.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsAttackers);

        InfoLabelAgentsParalyzers = new JLabel("Paralyzers:");
        InfoLabelAgentsParalyzers.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsParalyzers.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsParalyzers);

        InfoLabelAgentsLeaders = new JLabel("Leaders:");
        InfoLabelAgentsLeaders.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsLeaders.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsLeaders);

        InfoLabelAgentsInGroup = new JLabel("In a Group:");
        InfoLabelAgentsInGroup.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsInGroup.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsInGroup);

        InfoLabelAgentsAlltime = new JLabel("All-Time:");
        InfoLabelAgentsAlltime.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentsAlltime.setFont(infoFont);
        populationPanel.add(InfoLabelAgentsAlltime);

        JSeparator separator4 = new JSeparator(JSeparator.HORIZONTAL);
        populationPanel.add(separator4);

        JLabel subtitleLabelAgentAttributes = new JLabel("Agent Attributes    Avg/Max");
        subtitleLabelAgentAttributes.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelAgentAttributes.setFont(titleFont);
        populationPanel.add(subtitleLabelAgentAttributes);

        InfoLabelAgentGenerationAvg = new JLabel("Generaion No.:");
        InfoLabelAgentGenerationAvg.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentGenerationAvg.setFont(infoFont);
        populationPanel.add(InfoLabelAgentGenerationAvg);

        InfoLabelAgentAge = new JLabel("Age:");
        InfoLabelAgentAge.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentAge.setFont(infoFont);
        populationPanel.add(InfoLabelAgentAge);

        InfoLabelAgentSpeed = new JLabel("Speed:");
        InfoLabelAgentSpeed.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentSpeed.setFont(infoFont);
        populationPanel.add(InfoLabelAgentSpeed);

        InfoLabelAgentVision = new JLabel("Vision Rage:");
        InfoLabelAgentVision.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentVision.setFont(infoFont);
        populationPanel.add(InfoLabelAgentVision);

        InfoLabelAgentStamina = new JLabel("Stamina:");
        InfoLabelAgentStamina.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentStamina.setFont(infoFont);
        populationPanel.add(InfoLabelAgentStamina);

        InfoLabelAgentMaxStamina = new JLabel("Max. Stamina");
        InfoLabelAgentMaxStamina.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentMaxStamina.setFont(infoFont);
        populationPanel.add(InfoLabelAgentMaxStamina);

        InfoLabelAgentFood = new JLabel("Food:");
        InfoLabelAgentFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentFood.setFont(infoFont);
        populationPanel.add(InfoLabelAgentFood);

        InfoLabelAgentFoodCap = new JLabel("Food Capacity:");
        InfoLabelAgentFoodCap.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAgentFoodCap.setFont(infoFont);
        populationPanel.add(InfoLabelAgentFoodCap);

        JSeparator separator5 = new JSeparator(JSeparator.HORIZONTAL);
        populationPanel.add(separator5);

        // Create the third tab for the Tab Panel
        // "Groups"-Tab
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));

        JLabel subtitleLabelGroups = new JLabel("# Groups");
        subtitleLabelGroups.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelGroups.setFont(titleFont);
        groupPanel.add(subtitleLabelGroups);

        InfoLabelGroupsAlltime = new JLabel("All-Time:");
        InfoLabelGroupsAlltime.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupsAlltime.setFont(infoFont);
        groupPanel.add(InfoLabelGroupsAlltime);

        InfoLabelGroupsCurrent = new JLabel("Current:");
        InfoLabelGroupsCurrent.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupsCurrent.setFont(infoFont);
        groupPanel.add(InfoLabelGroupsCurrent);

        InfoLabelGroupsDissolved = new JLabel("Dissolved:");
        InfoLabelGroupsDissolved.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupsDissolved.setFont(infoFont);
        groupPanel.add(InfoLabelGroupsDissolved);

        JLabel subtitleLabelGroupMembers = new JLabel("Group Member Stats");
        subtitleLabelGroupMembers.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelGroupMembers.setFont(titleFont);
        groupPanel.add(subtitleLabelGroupMembers);

        InfoLabelGroupMembersAlltimeMax = new JLabel("All-Time Max:");
        InfoLabelGroupMembersAlltimeMax.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupMembersAlltimeMax.setFont(infoFont);
        groupPanel.add(InfoLabelGroupMembersAlltimeMax);

        InfoLabelGroupMembersAlltimeAvg = new JLabel("All-Time Avg:");
        InfoLabelGroupMembersAlltimeAvg.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupMembersAlltimeAvg.setFont(infoFont);
        groupPanel.add(InfoLabelGroupMembersAlltimeAvg);

        InfoLabelGroupMembersCurrentMax = new JLabel("Current Max:");
        InfoLabelGroupMembersCurrentMax.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupMembersCurrentMax.setFont(infoFont);
        groupPanel.add(InfoLabelGroupMembersCurrentMax);

        InfoLabelGroupMembersCurrentAvg = new JLabel("Current Avg:");
        InfoLabelGroupMembersCurrentAvg.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupMembersCurrentAvg.setFont(infoFont);
        groupPanel.add(InfoLabelGroupMembersCurrentAvg);

        JLabel subtitleLabelGroupSuccess = new JLabel("Group Success");
        subtitleLabelGroupSuccess.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelGroupSuccess.setFont(titleFont);
        groupPanel.add(subtitleLabelGroupSuccess);

        InfoLabelGroupSuccessMax = new JLabel("Avg:");
        InfoLabelGroupSuccessMax.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupSuccessMax.setFont(infoFont);
        groupPanel.add(InfoLabelGroupSuccessMax);

        InfoLabelGroupSuccessAvg = new JLabel("Max:");
        InfoLabelGroupSuccessAvg.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupSuccessAvg.setFont(infoFont);
        groupPanel.add(InfoLabelGroupSuccessAvg);

        JLabel subtitleLabelGroupConstellations = new JLabel("Group Constellations");
        subtitleLabelGroupConstellations.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelGroupConstellations.setFont(titleFont);
        groupPanel.add(subtitleLabelGroupConstellations);

        JLabel subsubtitleLabelGroupBest = new JLabel("Best Group");
        subsubtitleLabelGroupBest.setAlignmentX(Component.CENTER_ALIGNMENT);
        subsubtitleLabelGroupBest.setFont(infoFont);
        groupPanel.add(subsubtitleLabelGroupBest);

        InfoLabelGroupAttackersBest = new JLabel("# Attackers:");
        InfoLabelGroupAttackersBest.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupAttackersBest.setFont(infoFont);
        groupPanel.add(InfoLabelGroupAttackersBest);

        InfoLabelGroupParalyzersBest = new JLabel("# Paralyzers:");
        InfoLabelGroupParalyzersBest.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupParalyzersBest.setFont(infoFont);
        groupPanel.add(InfoLabelGroupParalyzersBest);

        JLabel subsubtitleLabelGroupAvg = new JLabel("Average Group");
        subsubtitleLabelGroupAvg.setAlignmentX(Component.CENTER_ALIGNMENT);
        subsubtitleLabelGroupAvg.setFont(infoFont);
        groupPanel.add(subsubtitleLabelGroupAvg);

        InfoLabelGroupAttackersAvg = new JLabel("# Attackers:");
        InfoLabelGroupAttackersAvg.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupAttackersAvg.setFont(infoFont);
        groupPanel.add(InfoLabelGroupAttackersAvg);

        InfoLabelGroupParalyzersAvg = new JLabel("# Paralyzers:");
        InfoLabelGroupParalyzersAvg.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupParalyzersAvg.setFont(infoFont);
        groupPanel.add(InfoLabelGroupParalyzersAvg);

        // Create the fourth tab for the Tab Panel
        // "Detailed"-Tab
        JPanel detailedPanel = new JPanel();
        detailedPanel.setLayout(new BoxLayout(detailedPanel, BoxLayout.Y_AXIS));

        // TODO: Rethink the way, to select agents for inspection
        // Currently the agents start around ID: 95
        JLabel subtitleLabelAgentNumber = new JLabel("Agent Number:");
        subtitleLabelAgentNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelAgentNumber.setFont(infoFont);
        detailedPanel.add(subtitleLabelAgentNumber);

        SpinnerModel agentCountNumber = new SpinnerNumberModel(1, 0, 10000000, 1);
        AgentNumberSpinner = new JSpinner(agentCountNumber);
        AgentNumberSpinner.setMaximumSize(new Dimension(50, 50));
        AgentNumberSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailedPanel.add(AgentNumberSpinner);

        InfoLabelDetailedGender = new JLabel("Gender");
        InfoLabelDetailedGender.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedGender.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedGender);

        InfoLabelDetailedSpecial = new JLabel("Special");
        InfoLabelDetailedSpecial.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedSpecial.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedSpecial);

        InfoLabelDetailedState = new JLabel("State");
        InfoLabelDetailedState.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedState.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedState);

        InfoLabelDetailedGeneration = new JLabel("Generation No:");
        InfoLabelDetailedGeneration.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedGeneration.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedGeneration);

        InfoLabelDetailedAges = new JLabel("Age / Retirement Age:");
        InfoLabelDetailedAges.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedAges.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedAges);

        InfoLabelDetailedSpeed = new JLabel("Speed:");
        InfoLabelDetailedSpeed.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedSpeed.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedSpeed);

        InfoLabelDetailedVision = new JLabel("Vision Range:");
        InfoLabelDetailedVision.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedVision.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedVision);

        InfoLabelDetailedStamina = new JLabel("Stamina / Saturation:");
        InfoLabelDetailedStamina.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedStamina.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedStamina);

        InfoLabelDetailedFood = new JLabel("Food / Saturation:");
        InfoLabelDetailedFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedFood.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedFood);

        InfoLabelDetailedReprodInstinct = new JLabel("Reproductive Instinct:");
        InfoLabelDetailedReprodInstinct.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedReprodInstinct.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedReprodInstinct);

        InfoLabelDetailedReprods = new JLabel("# Reproductions:");
        InfoLabelDetailedReprods.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedReprods.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedReprods);

        InfoLabelDetailedParalyzes = new JLabel("# Paralyzes / Kills:");
        InfoLabelDetailedParalyzes.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelDetailedParalyzes.setFont(infoFont);
        detailedPanel.add(InfoLabelDetailedParalyzes);

        JLabel subtitleLabelAncestorAttributes =  new JLabel("Average Ancestor Attributes");
        subtitleLabelAncestorAttributes.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelAncestorAttributes.setFont(titleFont);
        detailedPanel.add(subtitleLabelAgentAttributes);

        InfoLabelAncestorAge = new JLabel("Retirement Age:");
        InfoLabelAncestorAge.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAncestorAge.setFont(infoFont);
        detailedPanel.add(InfoLabelAncestorAge);

        InfoLabelAncestorSpeed = new JLabel("Speed:");
        InfoLabelAncestorSpeed.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAncestorSpeed.setFont(infoFont);
        detailedPanel.add(InfoLabelAncestorSpeed);

        InfoLabelAncestorVision = new JLabel("Range of Vision:");
        InfoLabelAncestorVision.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAncestorVision.setFont(infoFont);
        detailedPanel.add(InfoLabelAncestorVision);

        InfoLabelAncestorStamina = new JLabel("Stamina:");
        InfoLabelAncestorStamina.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAncestorStamina.setFont(infoFont);
        detailedPanel.add(InfoLabelAncestorStamina);

        InfoLabelAncestorFood = new JLabel("Food Capacity:");
        InfoLabelAncestorFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAncestorFood.setFont(infoFont);
        detailedPanel.add(InfoLabelAncestorFood);

        InfoLabelAncestorReprod = new JLabel("Reproducitve Instinct");
        InfoLabelAncestorReprod.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelAncestorReprod.setFont(infoFont);
        detailedPanel.add(InfoLabelAncestorReprod);

        JLabel subtitleLabelGroupStats = new JLabel("Group Statistics");
        subtitleLabelGroupStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabelGroupStats.setFont(titleFont);
        detailedPanel.add(subtitleLabelGroupStats);

        InfoLabelGroupLeader = new JLabel("Leader No:");
        InfoLabelGroupLeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupLeader.setFont(infoFont);
        detailedPanel.add(InfoLabelGroupLeader);

        InfoLabelGroupMembers = new JLabel("Group Members:");
        InfoLabelGroupMembers.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupMembers.setFont(infoFont);
        detailedPanel.add(InfoLabelGroupMembers);

        InfoLabelGroupAttackers = new JLabel("# Attackers:");
        InfoLabelGroupAttackers.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupAttackers.setFont(infoFont);
        detailedPanel.add(InfoLabelGroupAttackers);

        InfoLabelGroupParalyzers = new JLabel("# Paralyzers");
        InfoLabelGroupParalyzers.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupParalyzers.setFont(infoFont);
        detailedPanel.add(InfoLabelGroupParalyzers);

        InfoLabelGroupAverageFood = new JLabel("Average Food:");
        InfoLabelGroupAverageFood.setAlignmentX(Component.CENTER_ALIGNMENT);
        InfoLabelGroupAverageFood.setFont(infoFont);
        detailedPanel.add(InfoLabelGroupAverageFood);

        // Finalizing the tabbed panel
        tabbedPane.add("World", worldPanel);
        tabbedPane.add("Population", populationPanel);
        tabbedPane.add("Groups", groupPanel);
        tabbedPane.add("Detailed", detailedPanel);
        mainPanel.add(tabbedPane, BorderLayout.EAST);
        mainFrame.getContentPane().add(mainPanel);
    }

    public static void updatePopulation(double[] worldAgeData, double[] foodData, double[] herbData, double[] agentData) {
        populationChart.updateXYSeries("Food", worldAgeData, foodData, null);
        populationChart.updateXYSeries("Herbivores", worldAgeData, herbData, null);
        populationChart.updateXYSeries("Carnivores", worldAgeData, agentData, null);

        chartPanel.repaint();
    }

    public static void updateLabels(DataSet infoData) {
        DecimalFormat df0 = new DecimalFormat("###");
        DecimalFormat df2 = new DecimalFormat("###.##");
        String avgEnergy = df0.format(infoData.avgHerbEnergy);
        InfoLabelNutriValue.setText("Avg. Nutrit. Value: " + avgEnergy + "\t");
        InfoLabelFoodNumber.setText("# Food: " + infoData.numberFood);
        InfoLabelHerbivoreNumber.setText("# Herbivore: " + infoData.numberHerbivore);
        InfoLabelAgentNumber.setText("# Agent: " + infoData.numberAgent);
        InfoLabelHerbivoreNumber2.setText("Current: " + infoData.numberHerbivore);
        InfoLabelParalyzedHerbs.setText("Paralyzed: " + infoData.numberParalysedHerbivores);
        InfoLabelDeadHerbs.setText("Dead: " + infoData.numberDeadHerbivores);
        InfoLabelAllTimeHerbs.setText("All-time: " + infoData.numberHerbivoresAlltime);
        InfoLabelHerbEnergy.setText("Energy: " + avgEnergy + "/" + infoData.maxHerbEnergy);
        InfoLabelHerbFood.setText("Food: " + df0.format(infoData.avgHerbFood) + "/" + infoData.maxHerbFood);
        InfoLabelHerbSpeed.setText("Speed: " + df0.format(infoData.avgHerbSpeed) + "/" + infoData.maxHerbSpeed);
        InfoLabelHerbVision.setText("Vision: " + df0.format(infoData.avgHerbVisionRange) + "/" + infoData.maxHerbVisionRange);
        InfoLabelAgentsCurrent.setText("Current: " + infoData.numberAgent);
        InfoLabelAgentsFemales.setText("Females: " + infoData.numberFemaleAgent);
        InfoLabelAgentsMales.setText("Males: " + infoData.numberMaleAgent);
        InfoLabelAgentsAttackers.setText("Attackers: " + infoData.numberAttacker);
        InfoLabelAgentsParalyzers.setText("Paralyzers: " + infoData.numberParalyzer);
        InfoLabelAgentsLeaders.setText("Leaders: " + infoData.numberLeader);
        InfoLabelAgentsInGroup.setText("In a Group: " + infoData.numberAgentsInGroup);
        InfoLabelAgentsAlltime.setText("All-Time: " + infoData.numberAgentAlltime);
        InfoLabelAgentGenerationAvg.setText("Generation No.: " + df0.format(infoData.avgGeneration) + "/" + infoData.maxGeneration);
        InfoLabelAgentAge.setText("Age: " + df2.format(infoData.avgAge) + "/" + infoData.maxAge);
        InfoLabelAgentSpeed.setText("Speed: " + df0.format(infoData.avgAgentSpeed) + "/" + infoData.maxAgentSpeed);
        InfoLabelAgentVision.setText("Vision: " + df0.format(infoData.avgAgentVisionRange) + "/" + infoData.maxAgentVisionRange);
        InfoLabelAgentStamina.setText("Stamina: " + df0.format(infoData.avgAgentStamina) + "/" + infoData.maxAgentStamina);
        InfoLabelAgentMaxStamina.setText("Max. Stamina: " + df0.format(infoData.avgAgentMaxStamina) + "/" + infoData.maxAgentMaxStamina);
        InfoLabelAgentFood.setText("Food: " + df0.format(infoData.avgAgentFood) + "/" + infoData.maxAgentFood);
        InfoLabelAgentFoodCap.setText("Food Cap.: " + df0.format(infoData.avgAgentFoodCapacity) + "/" + infoData.maxAgentFoodCapacity);
        InfoLabelGroupsAlltime.setText("All-Time: " + infoData.numberGroupsAlltime);
        InfoLabelGroupsCurrent.setText("Current: " + infoData.numberGroups);
        InfoLabelGroupsDissolved.setText("Dissolved: " + infoData.numberGroupsDissolved);
        InfoLabelGroupMembersAlltimeMax.setText("All-Time Max:" + infoData.maxNumberGroupMembers);
        InfoLabelGroupMembersAlltimeAvg.setText("All-Time Avg: " + df2.format(infoData.avgNumberGroupMembers));
        InfoLabelGroupMembersCurrentMax.setText("Current Max: " + infoData.maxNumberActiveGroupMembers);
        InfoLabelGroupMembersCurrentAvg.setText("Current Avg: " + df2.format(infoData.avgNumberActiveGroupMembers));
        InfoLabelGroupSuccessMax.setText("Max: " + infoData.maxGroupSuccess);
        InfoLabelGroupSuccessAvg.setText("Avg: " + df2.format(infoData.avgGroupSuccess));
        if (null != infoData.selectedAgent) {
            InfoLabelDetailedGender.setText(infoData.selectedAgent.getDna().male ? "Male" : "Female");
            InfoLabelDetailedSpecial.setText(infoData.selectedAgent.getSpecial().name());
            InfoLabelDetailedState.setText(infoData.selectedAgent.getState().name());
            InfoLabelDetailedGeneration.setText("Generation No: " + infoData.selectedAgent.getDna().generationNo);
            InfoLabelDetailedAges.setText("Age / Retirement Age: " + infoData.selectedAgent.getAge() + " / " + infoData.selectedAgent.getDna().highAge);
            InfoLabelDetailedSpeed.setText("Speed: " + infoData.selectedAgent.getSpeed());
            InfoLabelDetailedVision.setText("Range of Vision: " + infoData.selectedAgent.getDna().visibility);
            InfoLabelDetailedStamina.setText("Stamina / Saturation: " + infoData.selectedAgent.getStamina() + " / " + infoData.selectedAgent.getDna().stamina);
            InfoLabelDetailedFood.setText("Food / Saturation: " + infoData.selectedAgent.getFood() + " / " + infoData.selectedAgent.getDna().foodSaturation);
            InfoLabelDetailedReprodInstinct.setText("Reproductive Instinct: " + infoData.selectedAgent.getDna().reproductInstinct);
            InfoLabelDetailedReprods.setText("# Reproductions: " + infoData.selectedAgent.getReproductions());
            InfoLabelDetailedParalyzes.setText("# Paralyzes / Kills: " + infoData.selectedAgent.getParalysesOrKills());
            InfoLabelAncestorAge.setText("Retirement Age: " +  infoData.selectedAgent.getDna().prevHighAge);
            InfoLabelAncestorSpeed.setText("Speed: " + infoData.selectedAgent.getDna().prevSpeed);
            InfoLabelAncestorVision.setText("Range of Vision: " + infoData.selectedAgent.getDna().prevVisib);
            InfoLabelAncestorStamina.setText("Stamina: " + infoData.selectedAgent.getDna().prevStamina);
            InfoLabelAncestorFood.setText("Food Capacity: " + infoData.selectedAgent.getDna().prevFoodCap);
            InfoLabelAncestorReprod.setText("Reproductive Instinct: " + infoData.selectedAgent.getDna().prevReprod);
            InfoLabelGroupLeader.setText("Leader No: " + infoData.selectedAgent.getKomm().getLeader());
            InfoLabelGroupMembers.setText("Group Members: " + infoData.selectedAgent.getKomm().getPartners().toString());
            InfoLabelGroupAttackers.setText("# Attackers: ");
            InfoLabelGroupParalyzers.setText("# Paralyzers: ");
            if (infoData.selectedAgent.getKomm().getPartners().size() > 0) {
                InfoLabelGroupAverageFood.setText("Average Food: " + df0.format(infoData.selectedAgent.getSuccess() / infoData.selectedAgent.getKomm().getPartners().size()));
            } else {
                InfoLabelGroupAverageFood.setText("Average Food: " + infoData.selectedAgent.getSuccess());
            }
        } else {
            InfoLabelDetailedGender.setText("Gender");
            InfoLabelDetailedSpecial.setText("Special");
            InfoLabelDetailedState.setText("State");
            InfoLabelDetailedGeneration.setText("Generation No: ");
            InfoLabelDetailedAges.setText("Age / Retirement Age: ");
            InfoLabelDetailedSpeed.setText("Speed: ");
            InfoLabelDetailedVision.setText("Range of Vision: ");
            InfoLabelDetailedStamina.setText("Stamina / Saturation: ");
            InfoLabelDetailedFood.setText("Food / Saturation: ");
            InfoLabelDetailedReprodInstinct.setText("Reproductive Instinct: ");
            InfoLabelDetailedReprods.setText("# Reproductions: ");
            InfoLabelDetailedParalyzes.setText("# Paralyzes / Kills: ");
            InfoLabelAncestorAge.setText("Retirement Age: ");
            InfoLabelAncestorSpeed.setText("Speed: ");
            InfoLabelAncestorVision.setText("Range of Vision: ");
            InfoLabelAncestorStamina.setText("Stamina: ");
            InfoLabelAncestorFood.setText("Food Capacity: ");
            InfoLabelAncestorReprod.setText("Reproductive Instinct: ");
            InfoLabelGroupLeader.setText("Leader No: ");
            InfoLabelGroupMembers.setText("Group Members: ");
            InfoLabelGroupAttackers.setText("# Attackers: ");
            InfoLabelGroupParalyzers.setText("# Paralyzers: ");
            InfoLabelGroupAverageFood.setText("Average Food: ");
        }
    }

    private static void writeParametersToConfig(Properties properties) {
        properties.setProperty("foodSpawnAmount", ParameterGrowthRateSpinner.getValue().toString());
        properties.setProperty("hillSize", ParameterHillSizeSpinner.getValue().toString());
        properties.setProperty("hillCount", ParameterHillCountSpinner.getValue().toString());
        PropertiesManager.writeProperties(properties);
    }
}
