package pl.ambsoft.movieteka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.wrapper.CoversDto;
import pl.ambsoft.movieteka.service.CoverService;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;

@RestController
@RequestMapping("/api/cover")
@Tag(name = "Cover API")
@RequiredArgsConstructor
public class CoverController {

    private final CoverService coverService;

    @Operation(summary = "Get movie cover by movie id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return cover of movie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CoversDto.class))}),
            @ApiResponse(responseCode = "404", description = "Movie does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @GetMapping
    public ResponseEntity<CoversDto> getMovieCover(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId) {
        return new ResponseEntity<>(coverService.getMovieCover(movieId), HttpStatus.OK);
    }

    @Operation(summary = "Add movie cover by movie id and file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return cover of movie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CoversDto.class))}),
            @ApiResponse(responseCode = "400", description = "Wrong file format",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "Movie does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA)
    public ResponseEntity<CoversDto> addMovieCover(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId,
                                                   @RequestParam @Parameter(description = "Movie cover") MultipartFile cover) {
        return new ResponseEntity<>(coverService.addMovieCover(movieId, cover), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete movie cover by movie id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return empty cover of movie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CoversDto.class))}),
            @ApiResponse(responseCode = "404", description = "Movie does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "Cover does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @DeleteMapping
    public ResponseEntity<CoversDto> deleteMovieCover(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId) {
        return new ResponseEntity<>(coverService.deleteMovieCover(movieId), HttpStatus.OK);
    }
}
