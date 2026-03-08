package saramk.se.Library_Management;

import controller.BookController;
import repository.BookRepository;
import service.BookService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mssqlclient.MSSQLConnectOptions;
import io.vertx.mssqlclient.MSSQLBuilder;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Pool;

import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.json.schema.SchemaRepository;
import io.vertx.json.schema.JsonSchemaOptions;
import static io.vertx.json.schema.common.dsl.Schemas.*;

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
    Pool pool = setupDatabase();

    // تجهيز الـRouter والـValidation
    Router router = setupRouter(pool);

    //تشغيل السيرفر
    startHttpServer(router, startPromise);
  }

  private Pool setupDatabase() {
    MSSQLConnectOptions connectOptions = new MSSQLConnectOptions()
      .setPort(1433)
      .setHost("127.0.0.1")
      .setDatabase("LibraryDB")
      .setUser("sa")
      .setPassword("StrongPass@2026");

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

    Pool pool = MSSQLBuilder.pool()
      .with(poolOptions)
      .connectingTo(connectOptions)
      .using(vertx)
      .build();

    pool.query("SELECT 1").execute()
      .onSuccess(res -> System.out.println("الداتابيس مشبوكة 100%"))
      .onFailure(err -> {
        System.out.println("السيرفر وصل الباب بس الداتابيس رفضت تدخله، السبب:");
        err.printStackTrace();
      });

    return pool;
  }

  private Router setupRouter(Pool pool) {
    BookRepository bookRepository = new BookRepository(pool);
    BookService bookService = new BookService(bookRepository);
    BookController bookController = new BookController(bookService);

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    JsonSchemaOptions schemaOptions = new JsonSchemaOptions().setBaseUri("http://localhost:8888");
    SchemaRepository schemaRepository = SchemaRepository.create(schemaOptions);

    ValidationHandler bookValidationHandler = ValidationHandlerBuilder.create(schemaRepository)
      .body(Bodies.json(objectSchema()
        .requiredProperty("title", stringSchema())
        .requiredProperty("author", stringSchema())
        .requiredProperty("isbn", stringSchema())
      )).build();

    router.post("/api/books").handler(bookValidationHandler).handler(bookController::addBook);
    router.get("/api/books").handler(bookController::getAllBooks);
    router.put("/api/books/:id").handler(bookController::updateBook);
    router.delete("/api/books/:id").handler(bookController::deleteBook);

    router.errorHandler(400, ctx -> {
      if (ctx.failure() instanceof BadRequestException){
        ctx.response()
          .setStatusCode(400)
          .putHeader("content-type", "application/json")
          .end("{\"error\": \"Invalid data: " + ctx.failure().getMessage() + "\"}");
      } else {
        ctx.response()
          .setStatusCode(400)
          .end("{\"error\": \"Bad Request\"}");
      }
    });

    return router;
  }

  private void startHttpServer(Router router, Promise<Void> startPromise) {
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onSuccess(server -> {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      })
      .onFailure(error -> {
        startPromise.fail(error);
        System.out.println("Failed to start server");
        error.printStackTrace();
      });
  }
}
