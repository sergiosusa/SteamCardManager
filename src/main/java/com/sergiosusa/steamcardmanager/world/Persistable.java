package com.sergiosusa.steamcardmanager.world;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public interface Persistable {

    default void persist(Object object, String filename) {

        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(filename + ".scm"));
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    default Object load(String filename) throws IOException, ClassNotFoundException {

        ObjectInputStream ois;
        ois = new ObjectInputStream(new FileInputStream(filename + ".scm"));
        Object obj = ois.readObject();
        ois.close();
        return obj;

    }

    default String downloadImage(String sourceImageUrl, String filepath) {
        File output = new File(filepath);

        if (!output.exists()) try {
            BufferedImage img = ImageIO.read(new URL(sourceImageUrl));
            ImageIO.write(img, "jpg", output);
            return output.getPath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filepath;
    }
}
