package vn.bachdao.soundcloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.bachdao.soundcloud.domain.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {
    Optional<Genre> findOneByNameIgnoreCase(String name);

    List<Genre> findByIdIn(List<Long> ids);
}
