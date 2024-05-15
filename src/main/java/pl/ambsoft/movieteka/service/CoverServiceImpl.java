package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.cache.CoverCacheService;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.model.dto.wrapper.CoversDto;
import pl.ambsoft.movieteka.model.entity.CoverEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.photo.PhotoCompressor;
import pl.ambsoft.movieteka.repository.CoverRepository;
import pl.ambsoft.movieteka.type.ImageType;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {

    private final CoverCacheService coverCacheService;

    private final PhotoCompressor photoCompressor;

    private final CoverRepository coverRepository;

    @Value("${movie.photo.width.size}")
    private Integer moviePhotoWidth;

    @Value("${movie.photo.height.size}")
    private Integer moviePhotoHeight;

    @Override
    public CoversDto getMovieCover(Long movieId) {
        return coverCacheService.getMovieCover(movieId);
    }

    @CacheEvict(value = "coverByMovieId", allEntries = true, beforeInvocation = true)
    @Override
    public CoversDto addMovieCover(Long movieId, MultipartFile cover) {
        setCoverForMovieEntity(cover, coverCacheService.getMovieEntityById(movieId));
        return getMovieCover(movieId);
    }

    @CacheEvict(value = "coverByMovieId", allEntries = true, beforeInvocation = true)
    @Override
    public CoversDto deleteMovieCover(Long movieId) {
        var movieEntity = coverCacheService.getMovieEntityById(movieId);
        List<CoverEntity> oldCovers = coverRepository.findByMovieEntity(movieEntity);
        coverRepository.deleteAll(oldCovers);
        return getMovieCover(movieId);
    }

    private void setCoverForMovieEntity(MultipartFile photo, MovieEntity movieEntity) {
        if (photo != null) {
            try {
                var compressedPhoto = photoCompressor.resizeImage(photo, moviePhotoWidth, moviePhotoHeight);

                List<CoverEntity> oldCovers = coverRepository.findByMovieEntity(movieEntity);
                coverRepository.deleteAll(oldCovers);

                var coverEntity = CoverEntity.builder()
                        .name(photo.getName())
                        .format(ImageType.JPEG)
                        .photo(compressedPhoto)
                        .build();

                coverEntity.setMovieEntity(movieEntity);
                coverRepository.save(coverEntity);
            } catch (IOException e) {
                throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
