package de.Tuuby.AgentSimulator.input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;

// Class to catch KeyInput events and pass them to the application
public class KeyInput implements KeyListener {

    // A list to keep track of which key is pressed
    private static boolean[] keys = new boolean[256];

    // This method gets called when any Key is pressed and writes it into the list
    public void keyPressed(KeyEvent keyEvent) {
        if (0 == (KeyEvent.AUTOREPEAT_MASK & keyEvent.getModifiers())) {
            keys[keyEvent.getKeyCode()] = true;
        }
    }

    // This method gets called when any key is released and writes it into the list
    public void keyReleased(KeyEvent keyEvent) {
        if (0 == (KeyEvent.AUTOREPEAT_MASK & keyEvent.getModifiers())) {
            keys[keyEvent.getKeyCode()] = false;

            if (keyEvent.getKeyCode() == KeyEvent.VK_F3)
                WorldUpdater.toggleDebug();
        }
    }

    // This method returns of the key with the specified Code is pressed
    public static boolean getKey(int keyCode) {
        return keys[keyCode];
    }
}
