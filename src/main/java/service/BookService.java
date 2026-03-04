package service;

import dto.CreateBookDTO;
import entity.Book;
import repository.BookRepository;
import io.vertx.core.Future;

import java.util.List;


public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository){
    this.bookRepository = bookRepository;
  }

  public Future<Void> addBook(CreateBookDTO book){

    if(book.getTitle() == null || book.getTitle().trim().isEmpty())
      return Future.failedFuture("Error: Title cant be empty");

    if (book.getAuthor() == null || book.getAuthor().trim().isEmpty())
      return Future.failedFuture("Error: Author cant be empty");

    return bookRepository.addBook(book);
  }

  public Future<List<Book>> getAllBooks(){
    return bookRepository.getAllBooks();
  }

  public Future<Boolean> updateBook(Integer id , CreateBookDTO book){
    if (id == null || id <= 0)
     return Future.failedFuture("Error: id number is incorrect.");
    if (book.getTitle() == null || book.getTitle().trim().isEmpty())
      return Future.failedFuture("Error: Title cant be empty");
    if (book.getAuthor() == null || book.getAuthor().trim().isEmpty())
      return Future.failedFuture("Error: Author cant be empty");

    return bookRepository.updateBook(id, book);
  }

  public Future<Boolean> deleteBook(Integer id){
    if (id == null || id <= 0)
      return Future.failedFuture("Error: id number is incorrect.");
    return bookRepository.deleteBook(id);
  }

}
