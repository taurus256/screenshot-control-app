package ru.taustudio.duckview.control.screenshotcontrol.misc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import org.apache.commons.io.FileUtils;

public class FileUtilMethods {
  static final String FILE_DIRECTORY = "/tmp/";
  static final String FILE_EXTENSION = ".png";

  public static void writeImage(String filename, byte[]  data) throws IOException {
    File outputFile = new File(FILE_DIRECTORY + filename + FILE_EXTENSION);

    try (FileImageOutputStream fio = new FileImageOutputStream(outputFile)) {
      fio.write(data);
    }
  }

  public static void writeImage(String filename, BufferedImage image) throws IOException {
    File outputFile = new File(FILE_DIRECTORY + filename + FILE_EXTENSION);
    ImageIO.write(image, "PNG", outputFile);
  }

  public static BufferedImage readImage(String id) throws IOException {
    File f = new File(FILE_DIRECTORY + id + FILE_EXTENSION);
    return ImageIO.read(f);
  }

  public static void deleteFile(String id) throws IOException {
    File f = new File(FILE_DIRECTORY + id + FILE_EXTENSION);
    FileUtils.delete(f);
  }
}
