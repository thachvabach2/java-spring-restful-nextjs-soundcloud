package vn.bachdao.soundcloud.web.rest;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bachdao.soundcloud.domain.Genre;
import vn.bachdao.soundcloud.service.GenreService;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.genre.ResGenreDTO;
import vn.bachdao.soundcloud.service.mapper.GenreMapper;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class GenreResource {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreResource(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @PostMapping("/genres")
    @ApiMessage("Create a genre")
    public ResponseEntity<ResGenreDTO> createGenre(@Valid @RequestBody Genre genre) throws IdInvalidException {
        // check name exist
        Optional<Genre> genreOptional = this.genreService.checkNameExist(genre.getName());
        if (genreOptional.isPresent()) {
            throw new IdInvalidException("Genre với name = " + genre.getName() + " đã tồn tại");
        }

        ResGenreDTO genreDTO = this.genreMapper.genreToResGenreDTO(this.genreService.handleCreateGenre(genre));
        return ResponseEntity.status(HttpStatus.CREATED).body(genreDTO);
    }

    @PutMapping("/genres")
    @ApiMessage("Update a genre by id")
    public ResponseEntity<ResGenreDTO> updateGenre(@RequestBody Genre reqGenre) throws IdInvalidException {
        // check id
        Optional<Genre> genreOptional = this.genreService.fetchGenreById(reqGenre.getId());
        if (genreOptional.isEmpty()) {
            throw new IdInvalidException("Genre với id = " + reqGenre.getId() + " không tồn tại");
        }

        // check name exist
        if (this.genreService.checkNameExist(reqGenre.getName()).isPresent()) {
            throw new IdInvalidException("Genre với name = " + reqGenre.getName() + " đã tồn tại");
        }

        ResGenreDTO genreDTO = this.genreMapper
                .genreToResGenreDTO(this.genreService.handleUpdateGenre(genreOptional.get(), reqGenre));

        return ResponseEntity.ok(genreDTO);
    }

    @DeleteMapping("/genres/{id}")
    @ApiMessage("Delete a genre by id")
    public ResponseEntity<Void> deleteGenre(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Optional<Genre> genreOptional = this.genreService.fetchGenreById(id);
        if (genreOptional.isEmpty()) {
            throw new IdInvalidException("Genre với id = " + id + " không tồn tại");
        }

        this.genreService.handleDeleteGenre(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/genres")
    @ApiMessage("Fetch all genres with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllGenres(@Filter Specification<Genre> spec, Pageable pageable) {
        return ResponseEntity.ok(this.genreService.handleGetAllGenres(spec, pageable));
    }
}
