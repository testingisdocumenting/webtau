/*
 * Copyright 2022 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.browser.documentation;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Screenshot {
    private final TakesScreenshot screenshotTaker;
    private final Number pixelRatio;
    private final WebElementLocationAndSizeProvider rootLocationAndSizeProvider;
    private BufferedImage bufferedImage;

    public Screenshot(TakesScreenshot screenshotTaker, Number pixelRatio,
                      WebElementLocationAndSizeProvider rootLocationAndSizeProvider) {
        this.screenshotTaker = screenshotTaker;
        this.pixelRatio = pixelRatio;
        this.rootLocationAndSizeProvider = rootLocationAndSizeProvider;
        take();
    }

    public void save(Path path) {
        saveImage(bufferedImage, path);
    }

    private BufferedImage takeBufferedImage() {
        byte[] screenshotBytes = screenshotTaker.getScreenshotAs(OutputType.BYTES);
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(screenshotBytes));

            if (rootLocationAndSizeProvider == null) {
                return bufferedImage;
            }

            Point location = rootLocationAndSizeProvider.getLocation();
            Dimension size = rootLocationAndSizeProvider.getSize();

            /*

                image width
                ---------
                |       |
                |(rx,ry)|
                |   *******  -- real width
                |   *   | *
                ----*---- *\
                    ******* \
                              real height

             */

            double ratio = pixelRatio.doubleValue();
            int realX = (int) (location.getX() * ratio);
            int realY = (int) (location.getY() * ratio);
            int realWidth = (int) (size.getWidth() * ratio);
            int realHeight = (int) (size.getHeight() * ratio);

            int imageWidth = bufferedImage.getWidth();
            int imageHeight = bufferedImage.getHeight();

            int maxCropWidth = Math.max(0, imageWidth - realX);
            int maxCropHeight = Math.max(0, imageHeight - realY);

            if (maxCropWidth == 0 || maxCropHeight == 0) {
                throw new IllegalArgumentException("element to crop from screenshot is outside of viewport");
            }

            return bufferedImage.getSubimage(
                    Math.min(imageWidth, realX), Math.min(imageHeight, realY),
                    Math.min(maxCropWidth, realWidth), Math.min(maxCropHeight, realHeight));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
