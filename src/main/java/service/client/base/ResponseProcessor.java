package service.client.base;

import protocol.message.Message;

public interface ResponseProcessor {
    void process(Message message);
}
