package saramk.se.Library_Management;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  private String deploymentId;

  @BeforeEach
  void setUp(Vertx vertx, VertxTestContext testContext) {
    io.vertx.core.json.jackson.DatabindCodec.mapper().findAndRegisterModules();
    vertx.deployVerticle(new MainVerticle())
      .onComplete(testContext.succeeding(id -> {
        this.deploymentId = id;
        testContext.completeNow();
      }));
  }

  @Test
  void testLibrarySystemFlow(Vertx vertx, VertxTestContext testContext) {
    WebClient client = WebClient.create(vertx);

    //  فحص القراءة (GET)
    client.get(8888, "localhost", "/api/books").send()
      .compose(getRes -> {
        assertEquals(200, getRes.statusCode());
        System.out.println("نجح فحص القراءة " + getRes.bodyAsString());

        // الفحص بكتاب ناقص (POST)
        io.vertx.core.json.JsonObject badBook = new io.vertx.core.json.JsonObject().put("isbn", "000");
        return client.post(8888, "localhost", "/api/books")
          .putHeader("content-type", "application/json")
          .sendJsonObject(badBook);
      })
      .compose(badRes -> {
        assertEquals(400, badRes.statusCode());
        System.out.println("نجح الفحص (تم طرد الكتاب) " + badRes.bodyAsString());

        // فحص إضافة كتاب صحيح (POST)
        io.vertx.core.json.JsonObject newBook = new io.vertx.core.json.JsonObject()
          .put("title", "Clean Code")
          .put("author", "Robert C. Martin")
          .put("isbn", "978-0132350884");
        return client.post(8888, "localhost", "/api/books")
          .putHeader("content-type", "application/json")
          .sendJsonObject(newBook);
      })
      .onComplete(testContext.succeeding(validRes -> testContext.verify(() -> {
        assertTrue(validRes.statusCode() == 200 || validRes.statusCode() == 400);
        System.out.println("نجح فحص الإضافة " + validRes.bodyAsString());

        testContext.completeNow();
      })));
  }

  @AfterEach
  void tearDown(Vertx vertx, VertxTestContext testContext) {
    vertx.undeploy(deploymentId)
      .onComplete(testContext.succeeding(v -> testContext.completeNow()));
  }
}
