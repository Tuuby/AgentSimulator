package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.main;

import java.util.LinkedList;
import java.util.List;

public class GuiFactory {

    // TODO: think about graphs getting added to this class
    public static void buildUI() {
        List<Button> buttons = new LinkedList<Button>();
        Button button1 = new Button(900, 350, 200, 100, "Reset");
        button1.setMouseListener(new GUIMouseListener() {
            public void onMouseButtonClick() {
                main.worldGen.clear();
                main.worldGen.generate();
                WorldUpdater.setWorld(main.worldGen.getWorld());
            }

            public void onMouseButtonDown() {

            }

            public void onMouseButtonRelease() {

            }

            public void onMouseOver() {

            }
        });

        final Button button2 = new Button(1100, 350, 200, 100, "Pause");
        button2.setMouseListener(new GUIMouseListener() {
            public void onMouseButtonClick() {
                GameLoop.toggleHold();
                if (button2.getText().equals("Pause"))
                    button2.setText("Resume");
                else
                    button2.setText("Pause");
            }

            public void onMouseButtonDown() {

            }

            public void onMouseButtonRelease() {

            }

            public void onMouseOver() {

            }
        });

        buttons.add(button1);
        buttons.add(button2);

        for (Button button : buttons) {
            WorldUpdater.addGUI(button);
            WindowManager.register(button);
        }

        WorldUpdater.addGUI(LegendFactory.initLegend());
    }
}
