package repository;
import dto.CreateBookDTO;
import entity.Book;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {

  private final Pool pool;

  public BookRepository(Pool pool){
    this.pool = pool;
  }

  //Create
  public Future<Void> addBook(CreateBookDTO book){
  String sql = "insert into Books (title, author, isbn) values (@p1, @p2, @p3)";

  Tuple parameters = Tuple.of(book.getTitle(), book.getAuthor(), book.getIsbn() );

  return pool.preparedQuery(sql)
             .execute(parameters)
             .mapEmpty();

  }
  //Read
  public Future<List<Book>> getAllBooks(){
    String sql = "SELECT * FROM Books";

    return pool.preparedQuery(sql)
      .execute()
      .map(rows -> {
        List<Book> bookList = new ArrayList<>();

        for (Row row : rows){
          Book book = new Book();

          book.setId(row.getInteger("id"));
          book.setTitle(row.getString("title"));
          book.setAuthor(row.getString("author"));
          book.setIsbn(row.getString("isbn"));
          book.setIsAvailable(row.getBoolean("is_Available"));
          book.setCreatedAt(row.getLocalDateTime("created_at"));

          bookList.add(book);
        }

        return bookList;
      });
  }

}
