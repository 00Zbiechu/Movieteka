package pl.ambsoft.movieteka.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.service.MovieService;
import pl.ambsoft.movieteka.validatior.MovieValidator;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieValidator movieValidator;

    @InitBinder("movieDto")
    public void validateMovie(WebDataBinder binder) {
        binder.addValidators(movieValidator);
    }

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<MoviesDto> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MoviesDto> addNewMovie(@RequestPart(required = false) MultipartFile photo, @Valid @RequestPart MovieDto movieDto) {
        return new ResponseEntity<>(movieService.addNewMovie(movieDto, photo), HttpStatus.CREATED);
    }
}
