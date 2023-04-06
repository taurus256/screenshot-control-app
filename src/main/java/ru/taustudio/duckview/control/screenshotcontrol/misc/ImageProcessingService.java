package ru.taustudio.duckview.control.screenshotcontrol.misc;

import static ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods.FILE_EXTENSION;
import static ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods.readImage;
import static ru.taustudio.duckview.control.screenshotcontrol.misc.FileUtilMethods.writeImage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import pazone.ashot.Screenshot;
import pazone.ashot.comparison.ImageDiff;
import pazone.ashot.comparison.ImageDiffer;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.cropper.DefaultCropper;
import pazone.ashot.cropper.ImageCropper;

@Service
public class ImageProcessingService {
  ImageCropper cropper = new DefaultCropper();

  public void generatePreview(Long jobId, ByteArrayResource resource)  {
    BufferedImage image = null;
    try {
      image = ImageIO.read(resource.getInputStream());
    Screenshot cropped = cropper.crop(image, Set.of(new Coords(image.getWidth(), image.getWidth())));
    BufferedImage resized = Scalr.resize(cropped.getImage(),300, Scalr.OP_ANTIALIAS);
    String fileName = jobId.toString() + ".preview";
    FileUtilMethods.writeImage(fileName,resized);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void generateDiffs(String sampleId, List<String> instances) {
    try {
      ImageDiffer imageDiffer = new ImageDiffer();
      BufferedImage sampleImage = readImage(sampleId);
      for (String instanceId: instances){
        ImageDiff diff = imageDiffer.makeDiff(sampleImage, readImage(instanceId));
        writeImage(instanceId + ".diff", diff.getTransparentMarkedImage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
