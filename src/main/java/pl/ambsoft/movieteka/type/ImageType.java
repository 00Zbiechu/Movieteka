package pl.ambsoft.movieteka.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {

    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png");

    private final String format;
}
