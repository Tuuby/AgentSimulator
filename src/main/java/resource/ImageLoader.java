package resource;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

// Class to load multiple images from the same folder into one list
// TODO: Deal with old images in the list
public class ImageLoader {

    // List for all the images that have been loaded
    private LinkedList<ImageResource> images;

    // Constructor that creates the list
    public ImageLoader() {
        images = new LinkedList<ImageResource>();
    }

    // Method to load all images from a resource folder into the list images
    public void load(String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = null;

        try {
            path = url.toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = new File(path).listFiles();

        for (File f : files) {
            images.add(new ImageResource(f.getPath()));
        }
    }

    // Method to return the content of images in an ImageResource array
    public ImageResource[] getImages() {
        ImageResource[] imageResources = new ImageResource[images.size()];
        for (int i = 0; i < images.size(); i++) {
            imageResources[i] = images.get(i);
        }

        return imageResources;
    }
}
