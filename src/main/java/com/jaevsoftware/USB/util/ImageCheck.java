/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageCheck {

    public Boolean checkImageExtension(MultipartFile foto) {
        return foto.getResource().getFilename().endsWith("jpg") || foto.getResource().getFilename().endsWith("png");
    }

    public Boolean checkImageResolution(MultipartFile foto, Integer width, Integer height) throws IOException {
        BufferedImage bufferImage = ImageIO.read(foto.getInputStream());
        return bufferImage.getWidth() != width || bufferImage.getHeight() != height;
    }
}
