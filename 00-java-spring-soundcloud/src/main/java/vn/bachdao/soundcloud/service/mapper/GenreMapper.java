package vn.bachdao.soundcloud.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import vn.bachdao.soundcloud.domain.Genre;
import vn.bachdao.soundcloud.service.dto.repsonse.genre.ResGenreDTO;

@Mapper(componentModel = "spring", uses = {})
public interface GenreMapper {
    ResGenreDTO genreToResGenreDTO(Genre genre);

    List<ResGenreDTO> genreToResGenreDTO(List<Genre> genres);
}
