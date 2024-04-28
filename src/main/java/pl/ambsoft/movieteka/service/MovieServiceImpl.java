package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.MovieMapper;
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.EditMovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.photo.PhotoCompressor;
import pl.ambsoft.movieteka.queryservice.MovieQueryService;
import pl.ambsoft.movieteka.repository.MovieRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    private final PhotoCompressor photoCompressor;

    private final MovieQueryService movieQueryService;

    @Value("${movie.photo.width.size}")
    private Integer moviePhotoWidth;

    @Value("${movie.photo.height.size}")
    private Integer moviePhotoHeight;

    @Override
    public MoviesDto getAllMovies() {
        return MoviesDto.builder().movies(movieRepository.findAll().stream().map(movieMapper::toDto).toList()).build();
    }

    @Override
    public MoviesDto addNewMovie(AddMovieDto addMovieDto, MultipartFile photo) {
        var movieEntity = movieMapper.toEntity(addMovieDto);
        setPhotoForMovieEntity(photo, movieEntity);
        movieRepository.save(movieEntity);
        return getAllMovies();
    }

    @Override
    public MoviesDto editMovie(EditMovieDto editMovieDto, MultipartFile photo) {
        var movieEntity = movieRepository.findById(editMovieDto.getId()).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        movieMapper.updateMovieEntityWithEditMovieDto(editMovieDto, movieEntity);
        setPhotoForMovieEntity(photo, movieEntity);
        movieRepository.save(movieEntity);
        return getAllMovies();
    }

    private void setPhotoForMovieEntity(MultipartFile photo, MovieEntity movieEntity) {
        if (photo != null) {
            try {
                var compressedPhoto = photoCompressor.resizeImage(photo, moviePhotoWidth, moviePhotoHeight);
                movieEntity.setPhoto(compressedPhoto);
            } catch (IOException e) {
                throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public MoviesDto deleteMovie(Long id) {
        var movieEntity = movieRepository.findById(id).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        movieRepository.delete(movieEntity);
        return getAllMovies();
    }

    @Override
    public MoviesDto filterMovieByCategory(String category) {
        return MoviesDto.builder().movies(movieRepository.findByCategory(category).stream().map(movieMapper::toDto).toList()).build();
    }

    @Override
    public MoviesDto searchMovieByTitle(String title) {
        return MoviesDto.builder().movies(movieQueryService.getMovieEntityByTitle(title).stream().map(movieMapper::toDto).toList()).build();
    }
}
