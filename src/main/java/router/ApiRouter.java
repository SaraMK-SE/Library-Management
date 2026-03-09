package router;

import config.ValidationConfig;
import controller.BookController;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.validation.BadRequestException;

public class ApiRouter {

  public static Router create(Vertx vertx, BookController bookController){

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.post("/api/books").handler(ValidationConfig.buildBookValidation()).handler(bookController :: addBook);
    router.get("/api/books").handler(bookController :: getAllBooks);
    router.put("/api/books/:id").handler(bookController :: updateBook);
    router.delete("/api/books/:id").handler(bookController :: deleteBook);

    router.errorHandler(400, ctx -> {
      if (ctx.failure() instanceof BadRequestException){
        ctx.response()
          .setStatusCode(400)
          .putHeader("content-type" , "application/json")
          .end("{\"error\": \"Invalid data: " + ctx.failure().getMessage() + "\"}");
      } else {
        ctx.response()
          .setStatusCode(400)
          .end("{\"error\": \"Bad Request\"}");
      }
    });

    return router;
  }

}
