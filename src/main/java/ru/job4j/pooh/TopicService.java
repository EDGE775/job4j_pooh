package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    ConcurrentHashMap<String,
            ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>>> queue =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Integer id = req.clientId();
        String queueName = req.context();
        queue.putIfAbsent(queueName, new ConcurrentHashMap<>());
        if (req.method().equals("POST") && queue.get(queueName) != null) {
            Map<Integer, ConcurrentLinkedQueue<String>> map = queue.get(queueName);
            for (Integer subscriberId : map.keySet()) {
                map.get(subscriberId).add(req.text());
            }
            return new Resp(String.format(
                    "Данные помещены в очередь %s", queueName), 200);
        } else if (req.method().equals("GET")) {
            Map<Integer, ConcurrentLinkedQueue<String>> map = queue.get(queueName);
            map.putIfAbsent(id, new ConcurrentLinkedQueue<>());
            if (map.get(id).size() > 0) {
                return new Resp(map.get(id).poll(), 200);
            } else {
                return new Resp("Пользователь подписан! Сообщений нет", 200);
            }
        }
        return new Resp("Произошла ошибка!", 400);
    }
}