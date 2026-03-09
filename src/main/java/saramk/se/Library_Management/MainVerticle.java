package saramk.se.Library_Management;

import config.DatabaseConfig;
import config.HttpServerConfig;
import controller.BookController;
import repository.BookRepository;
import router.ApiRouter;
import service.BookService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.sqlclient.Pool;


public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    io.vertx.core.json.jackson.DatabindCodec.mapper().findAndRegisterModules();

    io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
    vertx.deployVerticle(new MainVerticle())
      .onSuccess(id -> System.out.println(" تم تشغيل الـ Verticle بنجاح"))
      .onFailure(err -> {
        System.out.println("فشل تشغيل الـ Verticle السبب:");
        err.printStackTrace();
      });
  }

  @Override
  public void start(Promise<Void> startPromise) {
    // تجهيز الداتابيس
    Pool pool = DatabaseConfig.createPool(vertx);

    pool.query("SELECT 1").execute()
      .onSuccess(res -> System.out.println("الداتابيس مشبوكة 100%"))
      .onFailure(err -> {
        System.out.println("السيرفر وصل الباب بس الداتابيس رفضت تدخله، السبب:");
        err.printStackTrace();
      });

    // Dependency Injection
    BookRepository bookRepository = new BookRepository(pool);
    BookService bookService = new BookService(bookRepository);
    BookController bookController = new BookController(bookService);


    // تجهيز الـRouter والـValidation
    Router router = ApiRouter.create(vertx, bookController);

    //تشغيل السيرفر
    HttpServerConfig.startServer(vertx , router , startPromise);
  }


}
