package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;

import java.util.LinkedList;
import java.util.List;

public class GuiFactory {

    public static void buildUI() {
        List<Button> buttons = new LinkedList<Button>();
        Button button1 = new Button(900, 350, 200, 100, "Reset");
        button1.setMouseListener(new GUIMouseListener() {
            public void onMouseButtonClick() {
                System.out.println("Reset!");
            }

            public void onMouseButtonDown() {

            }

            public void onMouseButtonRelease() {

            }

            public void onMouseOver() {

            }
        });

        Button button2 = new Button(1100, 350, 200, 100, "Stop");
        button2.setMouseListener(new GUIMouseListener() {
            public void onMouseButtonClick() {
                GameLoop.stop();
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
    }
}
