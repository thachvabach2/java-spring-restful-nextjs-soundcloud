package vn.bachdao.soundcloud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.Genre;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.repository.GenreRepository;
import vn.bachdao.soundcloud.repository.TrackRepository;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.mapper.GenreMapper;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final TrackRepository trackRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, TrackRepository trackRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.trackRepository = trackRepository;
        this.genreMapper = genreMapper;
    }

    public Optional<Genre> checkNameExist(String name) {
        return this.genreRepository.findOneByNameIgnoreCase(name);
    }

    public Genre handleCreateGenre(Genre genre) {
        return this.genreRepository.save(genre);
    }

    public Optional<Genre> fetchGenreById(long id) {
        return this.genreRepository.findById(id);
    }

    public Genre handleUpdateGenre(Genre genreDB, Genre reqGenre) {
        genreDB.setName(reqGenre.getName());
        return this.genreRepository.save(genreDB);
    }

    public void handleDeleteGenre(long id) {
        Optional<Genre> genreOptional = this.genreRepository.findById(id);
        Genre currentGenre = genreOptional.get();

        // delete genre from track table
        if (currentGenre.getTracks() != null) {
            List<Track> tracks = currentGenre.getTracks();
            this.trackRepository.deleteAll(tracks);
        }

        this.genreRepository.deleteById(id);
    }

    public ResultPaginationDTO handleGetAllGenres(Specification<Genre> spec, Pageable pageable) {
        Page<Genre> genrePage = this.genreRepository.findAll(spec, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();

        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPageNumber(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(genrePage.getTotalPages());
        mt.setTotalElement(genrePage.getTotalElements());

        res.setMeta(mt);
        res.setData(this.genreMapper.genreToResGenreDTO(genrePage.getContent()));

        return res;
    }
}
