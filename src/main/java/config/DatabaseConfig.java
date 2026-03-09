package config;

import io.vertx.core.Vertx;
import io.vertx.mssqlclient.MSSQLBuilder;
import io.vertx.mssqlclient.MSSQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseConfig {

 public static Pool createPool(Vertx vertx){
   MSSQLConnectOptions connectOptions = new MSSQLConnectOptions()
     .setPort(1433)
     .setHost("127.0.0.1")
     .setDatabase("LibraryDB")
     .setUser("sa")
     .setPassword("StrongPass@2026");

   PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

   return MSSQLBuilder.pool()
     .with(poolOptions)
     .connectingTo(connectOptions)
     .using(vertx)
     .build();

 }

}
