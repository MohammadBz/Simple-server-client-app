package service.client;

import protocol.message.Message;

public interface ResponseProcessor {
    void process(Message message);
}
