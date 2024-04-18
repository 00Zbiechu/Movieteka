package pl.ambsoft.movieteka.validatior;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.type.ImageType;

@Component
public class FileValidator {

    public void validatePhoto(MultipartFile photo) {
        validateIsNotEmpty(photo);
        validateImageFormat(photo);
    }

    private void validateIsNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateImageFormat(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !imageHasValidFormat(originalFilename)) {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean imageHasValidFormat(String filename) {
        String[] parts = filename.split("\\.");
        if (parts.length > 1) {
            String fileExtension = parts[parts.length - 1].toLowerCase();
            for (ImageType imageType : ImageType.values()) {
                if (imageType.getFormat().equals(fileExtension)) {
                    return true;
                }
            }
        }
        return false;
    }
}
