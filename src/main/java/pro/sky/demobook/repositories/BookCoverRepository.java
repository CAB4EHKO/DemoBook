package pro.sky.demobook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.demobook.model.BookCover;

import java.util.Optional;

public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
    Optional<BookCover> findByBookId(Long bookId);
}
