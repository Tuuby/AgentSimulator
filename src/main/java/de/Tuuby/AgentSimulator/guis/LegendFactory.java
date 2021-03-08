package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.resource.ImageResource;

public class LegendFactory {

    public static GuiElement initLegend() {
        Container legend = new Container(1000, 600, 400, 400);

        ImageView foodImage = new ImageView(850, 500, 50, 50);
        foodImage.setImage(new ImageResource("/GameObjects/Food.png"));
        legend.addElement(foodImage);

        TextElement foodText = new TextElement(900, 500, 200, 200, "Nahrung");
        legend.addElement(foodText);

        ImageView movingFoodImage = new ImageView(850, 600, 50, 50);
        movingFoodImage.setImage(new ImageResource("/GameObjects/MovingFood2.png"));
        legend.addElement(movingFoodImage);

        TextElement movingFoodText = new TextElement(900, 600, 200, 200, "Pflanzenfresser");
        legend.addElement(movingFoodText);

        ImageView agentImage = new ImageView(850, 700, 50, 50);
        agentImage.setImage(new ImageResource("/GameObjects/Agents/AgentNeutral.png"));
        legend.addElement(agentImage);

        TextElement agentText = new TextElement(900, 700, 200, 200, "Fleischfresser");
        legend.addElement(agentText);

        return legend;
    }
}
