package de.Tuuby.AgentSimulator.guis;

import com.jogamp.opengl.awt.GLCanvas;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class SwingManager {

    public static XYChart populationChart;

    public static void build(JFrame mainFrame, GLCanvas glCanvas, Properties properties) {

        Font titleFont = new Font("SansSerif", Font.BOLD, 14);
        Font infoFont = new Font("SansSerif", Font.PLAIN, 10);

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
        populationChart.addSeries("Food", new double[] { 0 }, new double[] { Double.parseDouble(properties.getProperty("foodCount")) });
        populationChart.addSeries("Herbivores", new double[] { 0 }, new double[] { Double.parseDouble(properties.getProperty("herbivoreCount")) });
        populationChart.addSeries("Carnivores", new double[] { 0 }, new double[] { Double.parseDouble(properties.getProperty("agentCount")) });

        JPanel chartPanel = new XChartPanel<XYChart>(populationChart);
        worldPanel.add(chartPanel);

        // Create Labels for information
        JLabel SubtitleLabelWorld = new JLabel("World", JLabel.LEFT);
        SubtitleLabelWorld.setFont(titleFont);
        worldPanel.add(SubtitleLabelWorld);

        JLabel InfoLabelWorldSize = new JLabel("Size: " + properties.getProperty("worldWidth") + " x " + properties.getProperty("worldHeight"), JLabel.CENTER);
        InfoLabelWorldSize.setFont(infoFont);
        worldPanel.add(InfoLabelWorldSize);

        JLabel InfoLabelHillNumber = new JLabel("# Hills: " + properties.getProperty("hillCount"), JLabel.CENTER);
        InfoLabelHillNumber.setFont(infoFont);
        worldPanel.add(InfoLabelHillNumber);

        tabbedPane.add("World", worldPanel);
        mainPanel.add(tabbedPane, BorderLayout.EAST);
        mainFrame.getContentPane().add(mainPanel);
    }

    public static void updatePopulation(double[][] populationCount) {

    }
}
