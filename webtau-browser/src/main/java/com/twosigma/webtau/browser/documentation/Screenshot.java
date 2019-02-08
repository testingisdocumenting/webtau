/*
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

package com.twosigma.webtau.browser.documentation;

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
