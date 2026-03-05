package controller;
import dto.CreateBookDTO;
import service.BookService;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.Json;

public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService){
    this.bookService = bookService;
  }

  public void addBook(RoutingContext context){

    try {
      CreateBookDTO bookDTO = context.body().asJsonObject().mapTo(CreateBookDTO.class);
      bookService.addBook(bookDTO)
        .onSuccess(success -> {
          context.response()
            .setStatusCode(201)
            .putHeader("content-type", "application/json")
            .end("{\"message\": \"The book has been successfully added\"}");
        })
        .onFailure(error -> {
          context.response()
            .setStatusCode(400)
            .putHeader("content-type", "application/json")
            .end("{\"error\": \"" + error.getMessage() + "\"}");
        });

    }catch (Exception e){
      context.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end("{\"error\": \"The data format is incorrect or incomplete\"}");
    }

  }

  public void getAllBooks(RoutingContext context){
    bookService.getAllBooks()
      .onSuccess(books -> {
        context.response()
          .setStatusCode(200)
          .putHeader("content-type", "application/json")
          .end(Json.encodePrettily(books));
      })
      .onFailure(error -> {
        context.response()
          .setStatusCode(500)
          .putHeader("content-type", "application/json")
          .end("{\"error\": \"server error occurred\"}");
      });

  }

  public void updateBook(RoutingContext context ){

    try {
      String id = context.pathParam("id");
      if (id == null || id.isEmpty()){
        context.response()
          .setStatusCode(400)
          .putHeader("content-type", "application/json")
          .end("{\"error\": \"The book number is missing\"}");
        return;
      }

      Integer bookId = Integer.parseInt(id);

      CreateBookDTO bookDTO = context.body().asJsonObject().mapTo(CreateBookDTO.class);

      bookService.updateBook(bookId, bookDTO)
        .onSuccess(isUpdated -> {
          if (isUpdated){
            context.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .end("{\"message\": \"The book has been successfully Updated\"}");
          }else {
            context.response()
              .setStatusCode(404)
              .putHeader("content-type", "application/json")
              .end("{\"error\": \"Book not found to update!\"}");
          }
        })
        .onFailure(error ->{
          context.response()
            .setStatusCode(400)
            .putHeader("content-type", "application/json")
            .end("{\"error\": \"" + error.getMessage() + "\"}");
        });
    }
    catch (NumberFormatException e){
      context.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end("{\"error\": \"The book number must be a whole number\"}");
    }
    catch (Exception e){
      context.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end("{\"error\": \"The data format is incorrect or incomplete\"}");
    }
  }

  public void deleteBook(RoutingContext context){
    try{
      String id = context.pathParam("id");
      if (id == null || id.isEmpty()){
        context.response()
          .setStatusCode(400)
          .putHeader("content-type", "application/json")
          .end("{\"error\": \"The book number is missing\"}");
        return;
      }

      Integer bookId = Integer.parseInt(id);
      bookService.deleteBook(bookId)
        .onSuccess(isDeleted -> {
          if (isDeleted) {
            context.response()
              .setStatusCode(200)
              .putHeader("content-type", "application/json")
              .end("{\"message\": \"The book has been successfully Deleted\"}");
          }else {
            context.response()
              .setStatusCode(404)
              .putHeader("content-type", "application/json")
              .end("{\"error\": \"Book not found!\"}");
          }
        })
        .onFailure(error -> {
            context.response()
              .setStatusCode(400)
              .putHeader("content-type", "application/json")
              .end("{\"error\": \"" + error.getMessage() + "\"}");
        });
    }catch (NumberFormatException e){
      context.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end("{\"error\": \"The book number must be a whole number\"}");
    }catch (Exception e){
      context.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end("{\"error\": \"The data format is incorrect or incomplete\"}");
    }
  }
}

