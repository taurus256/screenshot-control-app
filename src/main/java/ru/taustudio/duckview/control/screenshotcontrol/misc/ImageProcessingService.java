package ru.taustudio.duckview.control.screenshotcontrol.misc;

import static ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods.FILE_EXTENSION;
import static ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods.readImage;
import static ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods.writeImage;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.css.Rect;
import pazone.ashot.Screenshot;
import pazone.ashot.comparison.DiffMarkupPolicy;
import pazone.ashot.comparison.ImageDiff;
import pazone.ashot.comparison.ImageDiffer;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.cropper.DefaultCropper;
import pazone.ashot.cropper.ImageCropper;

@Service
public class ImageProcessingService {
  ImageCropper cropper = new DefaultCropper();

  public void generatePreview(String jobUUID, ByteArrayResource resource)  {
    BufferedImage image = null;
    try {
      image = ImageIO.read(resource.getInputStream());
    Screenshot cropped = cropper.crop(image, Set.of(new Coords(image.getWidth(), image.getWidth())));
    BufferedImage resized = Scalr.resize(cropped.getImage(),300, Scalr.OP_ANTIALIAS);
    String fileName = jobUUID + ".preview";
    FileUtilMethods.writeImage(fileName,resized);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Set<String> generateDiffs(String sampleUUID, List<String> instances) {
    Set<String> resultSet = new HashSet<>();
    try {
      BufferedImage sampleImage = readImage(sampleUUID);
      for (String instanceUuid: instances){
        ImageDiffer imageDiffer = new ImageDiffer();
        ImageDiff diff = imageDiffer.makeDiff(sampleImage, readImage(instanceUuid));
        writeImage(instanceUuid + ".diff", diff.getTransparentMarkedImage());
        BufferedImage resized = Scalr.resize(diff.getTransparentMarkedImage(),300, Scalr.OP_ANTIALIAS);
        if (resized.getWidth()>100){
          resized.setData(resized.getData(new Rectangle(0,0,99, Math.min(resized.getHeight(), 299))));
        }
        writeImage(instanceUuid + ".diff.preview", resized);
        resultSet.add(instanceUuid);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  return resultSet;
  }
}
