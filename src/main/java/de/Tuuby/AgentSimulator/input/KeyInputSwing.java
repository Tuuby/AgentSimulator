package de.Tuuby.AgentSimulator.input;

import de.Tuuby.AgentSimulator.engine.WorldUpdater;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputSwing implements KeyListener {

    private static boolean[] keys = new boolean[256];

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (0 == (com.jogamp.newt.event.KeyEvent.AUTOREPEAT_MASK & e.getModifiers())) {
            keys[e.getKeyCode()] = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (0 == (com.jogamp.newt.event.KeyEvent.AUTOREPEAT_MASK & e.getModifiers())) {
            keys[e.getKeyCode()] = false;

            if (e.getKeyCode() == KeyEvent.VK_F3)
                WorldUpdater.toggleDebug();
        }
    }

    public static boolean getKey(int keyCode) {
        return keys[keyCode];
    }
}
