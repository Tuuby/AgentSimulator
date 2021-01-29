package de.Tuuby.AgentSimulator.graphics;

import de.Tuuby.AgentSimulator.resource.ImageResource;

import java.util.Dictionary;
import java.util.Hashtable;

public class StatusIconFactory {
    private static Dictionary<String, ImageResource> statuses;

    public static void initStatuses() {
        statuses = new Hashtable<String, ImageResource>();

        statuses.put("Dead", new ImageResource("/GameObjects/StatusIcons/DeadIcon.png"));
    }

    public static ImageResource getStatusIcon(String key) {
        return statuses.get(key);
    }
}
