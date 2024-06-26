package pl.ambsoft.movieteka.photo;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.validator.FileValidator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PhotoCompressor {

    private final FileValidator fileValidator;

    public byte[] resizeImage(MultipartFile photo, int targetWidth, int targetHeight) throws IOException {

        fileValidator.validatePhoto(photo);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(photo.getBytes());
        BufferedImage originalImage = ImageIO.read(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("jpeg")
                .outputQuality(0.7)
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }
}
