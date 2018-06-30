package com.appirio.service.member.events;

import com.appirio.eventsbus.api.client.EventConsumer;
import com.appirio.eventsbus.api.client.util.jsonevent.EventHandler;
import io.dropwizard.lifecycle.Managed;

import java.util.Map;

public class MemberEventsClientManager implements Managed {

    private final EventConsumer consumer;
    private final Map<String, EventHandler> events;

    public MemberEventsClientManager(EventConsumer consumer, Map<String, EventHandler> events) {
        this.consumer = consumer;
        this.events = events;
    }

    @Override
    public void start() throws Exception {
        consumer.subscribe(events);
    }

    @Override
    public void stop() throws Exception {
        consumer.shutdown();
    }
}