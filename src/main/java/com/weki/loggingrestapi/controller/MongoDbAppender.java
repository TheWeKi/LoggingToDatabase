package com.weki.loggingrestapi.controller;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.Date;

public class MongoDbAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    @Override
    public void start() {
        super.start();
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("logback");
        collection = database.getCollection("logs");
    }

    @Override
    public void stop() {
        mongoClient.close();
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        Document document = new Document();
        document.append("timestamp", new Date(eventObject.getTimeStamp()))
                .append("level", eventObject.getLevel().toString())
                .append("logger", eventObject.getLoggerName())
                .append("message", eventObject.getMessage());
        if(eventObject.hasCallerData()) {
            document.append(
                    "stacktrace",
                    Arrays.toString(eventObject.getCallerData())
            );
        }
        collection.insertOne(document);
    }
}
