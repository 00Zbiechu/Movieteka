package pl.ambsoft.movieteka.service;

import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.model.dto.wrapper.CoversDto;

public interface CoverService {

    CoversDto getMovieCover(Long movieId);

    CoversDto addMovieCover(Long movieId, MultipartFile cover);

    CoversDto deleteMovieCover(Long movieId);
}
