package net.kuujo.copycat.vertx.protocol.impl;

import net.kuujo.copycat.protocol.InstallRequest;
import net.kuujo.copycat.protocol.InstallResponse;
import net.kuujo.copycat.protocol.PingRequest;
import net.kuujo.copycat.protocol.PingResponse;
import net.kuujo.copycat.protocol.PollRequest;
import net.kuujo.copycat.protocol.PollResponse;
import net.kuujo.copycat.protocol.ProtocolClient;
import net.kuujo.copycat.protocol.SubmitRequest;
import net.kuujo.copycat.protocol.SubmitResponse;
import net.kuujo.copycat.protocol.SyncRequest;
import net.kuujo.copycat.protocol.SyncResponse;
import net.kuujo.copycat.util.AsyncCallback;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

public class EventBusProtocolClient implements ProtocolClient {
  private final String address;
  private final Vertx vertx;

  public EventBusProtocolClient(String address, Vertx vertx) {
    this.address = address;
    this.vertx = vertx;
  }

  @Override
  public void ping(PingRequest request, final AsyncCallback<PingResponse> callback) {
    JsonObject message = new JsonObject();
    vertx.eventBus().sendWithTimeout(address, message, 5000, new Handler<AsyncResult<Message<JsonObject>>>() {
      @Override
      public void handle(AsyncResult<Message<JsonObject>> result) {
        if (result.failed()) {
          callback.fail(result.cause());
        } else {
          String status = result.result().body().getString("status");
          if (status.equals("ok")) {
            callback.complete(new PingResponse(result.result().body().getLong("term")));
          } else if (status.equals("error")) {
            callback.complete(new PingResponse(result.result().body().getString("message")));
          }
        }
      }
    });
  }

  @Override
  public void sync(SyncRequest request, final AsyncCallback<SyncResponse> callback) {
    JsonObject message = new JsonObject();
    vertx.eventBus().sendWithTimeout(address, message, 5000, new Handler<AsyncResult<Message<JsonObject>>>() {
      @Override
      public void handle(AsyncResult<Message<JsonObject>> result) {
        if (result.failed()) {
          callback.fail(result.cause());
        } else {
          String status = result.result().body().getString("status");
          if (status.equals("ok")) {
            callback.complete(new SyncResponse(result.result().body().getLong("term"), result.result().body().getBoolean("succeeded")));
          } else if (status.equals("error")) {
            callback.complete(new SyncResponse(result.result().body().getString("message")));
          }
        }
      }
    });
  }

  @Override
  public void install(InstallRequest request, final AsyncCallback<InstallResponse> callback) {
    JsonObject message = new JsonObject();
    vertx.eventBus().sendWithTimeout(address, message, 5000, new Handler<AsyncResult<Message<JsonObject>>>() {
      @Override
      public void handle(AsyncResult<Message<JsonObject>> result) {
        if (result.failed()) {
          callback.fail(result.cause());
        } else {
          String status = result.result().body().getString("status");
          if (status.equals("ok")) {
            callback.complete(new InstallResponse(result.result().body().getLong("term"), result.result().body().getBoolean("succeeded")));
          } else if (status.equals("error")) {
            callback.complete(new InstallResponse(result.result().body().getString("message")));
          }
        }
      }
    });
  }

  @Override
  public void poll(PollRequest request, final AsyncCallback<PollResponse> callback) {
    JsonObject message = new JsonObject();
    vertx.eventBus().sendWithTimeout(address, message, 5000, new Handler<AsyncResult<Message<JsonObject>>>() {
      @Override
      public void handle(AsyncResult<Message<JsonObject>> result) {
        if (result.failed()) {
          callback.fail(result.cause());
        } else {
          String status = result.result().body().getString("status");
          if (status.equals("ok")) {
            callback.complete(new PollResponse(result.result().body().getLong("term"), result.result().body().getBoolean("voteGranted")));
          } else if (status.equals("error")) {
            callback.complete(new PollResponse(result.result().body().getString("message")));
          }
        }
      }
    });
  }

  @Override
  public void submit(SubmitRequest request, final AsyncCallback<SubmitResponse> callback) {
    JsonObject message = new JsonObject();
    vertx.eventBus().sendWithTimeout(address, message, 5000, new Handler<AsyncResult<Message<JsonObject>>>() {
      @Override
      public void handle(AsyncResult<Message<JsonObject>> result) {
        if (result.failed()) {
          callback.fail(result.cause());
        } else {
          String status = result.result().body().getString("status");
          if (status.equals("ok")) {
            callback.complete(new SubmitResponse(result.result().body().getObject("result").toMap()));
          } else if (status.equals("error")) {
            callback.complete(new SubmitResponse(result.result().body().getString("message")));
          }
        }
      }
    });
  }

}