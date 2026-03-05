package saramk.se.Library_Management;

import controller.BookController;
import repository.BookRepository;
import service.BookService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mssqlclient.MSSQLConnectOptions;
import io.vertx.mssqlclient.MSSQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Pool;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  public void start(Promise<Void> startPromise) throws Exception {
    MSSQLConnectOptions connectOptions = new MSSQLConnectOptions()
      .setPort(1433)
      .setHost("localhost")
      .setDatabase("library_db")
      .setUser("sa")
      .setPassword("StrongPass@2026");

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

    Pool pool = MSSQLPool.pool(vertx, connectOptions, poolOptions);


    BookRepository bookRepository = new BookRepository(pool);
    BookService bookService = new BookService(bookRepository);
    BookController bookController = new BookController(bookService);

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    router.post("/api/books").handler(bookController::addBook);
    router.get("/api/books").handler(bookController::getAllBooks);
    router.put("/api/books/:id").handler(bookController::updateBook);
    router.delete("/api/books/:id").handler(bookController::deleteBook);


    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onSuccess(server -> {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888 ");
      })
      .onFailure(error -> {
        startPromise.fail(error);
        System.out.println("Failed to start server ");
        error.printStackTrace();
      });


  }

}
