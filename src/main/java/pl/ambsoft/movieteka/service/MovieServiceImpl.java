package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.MovieMapper;
import pl.ambsoft.movieteka.mapper.RewardMapper;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.RewardDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.photo.PhotoCompressor;
import pl.ambsoft.movieteka.repository.MovieRepository;
import pl.ambsoft.movieteka.repository.RewardRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final RewardRepository rewardRepository;

    private final MovieMapper movieMapper;

    private final RewardMapper rewardMapper;

    private final PhotoCompressor photoCompressor;


    @Override
    public MoviesDto getAllMovies() {
        return MoviesDto.builder().movies(movieRepository.findAll().stream().map(movieMapper::toDto).toList()).build();
    }

    @Override
    public MoviesDto addNewMovie(MovieDto movieDto, MultipartFile photo) {
        var movieEntity = movieMapper.toEntity(movieDto);
        saveAllRewards(movieDto);

        if (photo != null) {
            try {
                var compressedPhoto = photoCompressor.resizeImage(photo, 400, 400);
                movieEntity.setPhoto(compressedPhoto);
            } catch (IOException e) {
                throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
            }
        }

        movieRepository.save(movieEntity);
        return getAllMovies();
    }

    private void saveAllRewards(MovieDto movieDto) {
        for (RewardDto rewardDto : movieDto.getRewardsDto().rewards()) {
            rewardRepository.save(rewardMapper.toEntity(rewardDto));
        }
    }
}
