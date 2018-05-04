package com.twosigma.webtau.documentation;

import org.eclipse.jetty.io.RuntimeIOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Screenshot {
    private TakesScreenshot screenshotTaker;
    private BufferedImage bufferedImage;

    public Screenshot(TakesScreenshot screenshotTaker) {
        this.screenshotTaker = screenshotTaker;
        take();
    }

    public void save(Path path) {
        saveImage(bufferedImage, path);
    }

    private BufferedImage takeBufferedImage() {
        byte[] screenshotBytes = screenshotTaker.getScreenshotAs(OutputType.BYTES);
        try {
            return ImageIO.read(new ByteArrayInputStream(screenshotBytes));
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    private void saveImage(BufferedImage bufferedImage, Path path)  {
        try {
            ImageIO.write(bufferedImage, "png", path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void take() {
        bufferedImage = takeBufferedImage();
    }
}
