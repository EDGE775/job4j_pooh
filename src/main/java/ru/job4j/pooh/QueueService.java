package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String queueName = req.context();
        queue.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
        if (req.method().equals("POST")) {
            queue.get(queueName).add(req.text());
            return new Resp(String.format(
                    "Данные помещены в очередь %s", queueName), 200);
        } else if (req.method().equals("GET")) {
            return new Resp(queue.get(queueName).poll(), 200);
        }
        return new Resp("Произошла ошибка!", 400);
    }
}