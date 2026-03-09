package config;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class HttpServerConfig {

 public static void startServer(Vertx vertx , Router router , Promise<Void> startPromise){

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
