package pro.sky.demobook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.demobook.model.Book;
import pro.sky.demobook.model.BookCover;
import pro.sky.demobook.service.BookCoverService;
import pro.sky.demobook.service.BookService;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BooksController {


    private final BookService bookService;
    private final BookCoverService bookCoverService;

    public BooksController(BookService bookService, BookCoverService bookCoverService) {
        this.bookService = bookService;
        this.bookCoverService = bookCoverService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Book> getBookInfo(@PathVariable Long id) {
        Book book = bookService.findBook(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity findBooks(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String author,
                                    @RequestParam(required = false) String namePart) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(bookService.findByName(name));
        }
        if (author != null && !author.isBlank()) {
            return ResponseEntity.ok(bookService.findBookByAuthor(author));
        }
        if (namePart != null && !namePart.isBlank()) {
            return ResponseEntity.ok(bookService.findByNamePart(namePart));
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping(value = "/{id}/cover/preview")
    public ResponseEntity<byte[]> downloadCover(@PathVariable Long id) {
        BookCover bookCover = bookCoverService.findBookCover(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(bookCover.getMediaType()));
        headers.setContentLength(bookCover.getPreview().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bookCover.getPreview());
    }

    @GetMapping

    @PostMapping
    public Book creatBook(Book book) {
        return bookService.creatBook(book);
    }

    @PostMapping(value = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCover(@PathVariable Long id, @RequestParam MultipartFile cover) throws IOException {
        if (cover.getSize() >= 1024 * 300) {
            ResponseEntity.badRequest().body("File is too big");
        }
        bookCoverService.uploadCover(id, cover);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Book> editBook(Book book) {
        Book foundBook = bookService.editBook(book);
        if (foundBook == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundBook);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

}
