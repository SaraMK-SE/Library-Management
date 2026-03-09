package config;

import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.JsonSchemaOptions;
import io.vertx.json.schema.SchemaRepository;

import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

public class ValidationConfig {

  public static ValidationHandler buildBookValidation(){

    JsonSchemaOptions schemaOptions = new JsonSchemaOptions().setBaseUri("http://localhost:8888");
    SchemaRepository schemaRepository = SchemaRepository.create(schemaOptions);

    return ValidationHandlerBuilder.create(schemaRepository)
      .body(Bodies.json(objectSchema()
        .requiredProperty("title", stringSchema())
        .requiredProperty("author", stringSchema())
        .requiredProperty("isbn", stringSchema())
      ))
      .build();

  }



}
