package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.MovieMapper;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.photo.PhotoCompressor;
import pl.ambsoft.movieteka.repository.MovieRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    private final PhotoCompressor photoCompressor;

    @Override
    public MoviesDto getAllMovies() {
        return MoviesDto.builder().movies(movieRepository.findAll().stream().map(movieMapper::toDto).toList()).build();
    }

    @Override
    public MoviesDto addNewMovie(MovieDto movieDto, MultipartFile photo) {
        var entity = movieMapper.toEntity(movieDto);

        if (photo != null) {
            try {
                var compressedPhoto = photoCompressor.resizeImage(photo, 400, 400);
                entity.setPhoto(compressedPhoto);
            } catch (IOException e) {
                throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
            }
        }

        movieRepository.save(entity);
        return getAllMovies();
    }
}
