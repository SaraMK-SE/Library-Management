package repository;
import dto.CreateBookDTO;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Tuple;
import io.vertx.core.Future;
public class BookRepository {

  private final Pool pool;

  public BookRepository(Pool pool){
    this.pool = pool;
  }

  public Future<Void> addBook(CreateBookDTO book){
  String sql = "insert into Books (title, author, isbn) values (@p1, @p2, @p3) ";

  Tuple parameters = Tuple.of(book.getTitle(), book.getAuthor(), book.getIsbn() );

  return pool.preparedQuery(sql)
             .execute(parameters)
             .mapEmpty();

  }
}
