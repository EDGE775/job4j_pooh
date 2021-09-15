package ru.job4j.pooh;

import java.util.Arrays;

/**
 * Класс для парсинга входящего сообщения.
 */
public class Req {
    /**
     * Метод POST или GET
     */
    private final String method;
    /**
     * Режим queue или topic
     */
    private final String mode;
    private final String text;
    private final int clientId;
    /**
     * Контекст запроса (имя очереди или название темы)
     */
    private final String context;

    private Req(String method, String mode, String text) {
        this.method = method;
        String[] modeArray = mode.split("/");
        this.mode = modeArray[1];
        this.context = modeArray[2];
        if (this.mode.equals("topic") && modeArray.length > 3) {
            try {
                this.clientId = Integer.parseInt(modeArray[3]);
            } catch (Exception e) {
                throw new IllegalArgumentException("Некорректные входные данные!");
            }
        } else {
            this.clientId = 0;
        }
        this.text = text;
    }

    public static Req of(String content) {
        if (content == null || content.length() == 0) {
            throw new IllegalArgumentException("Некорректные входные данные!");
        }
        String[] contentArray = content.split("\n");
        String[] metaArray = contentArray[0].split(" ");
        String method = metaArray[0];
        String mode = metaArray[1];
        String text = null;
        if (method.equals("POST")) {
            text = contentArray[contentArray.length - 1];
        }
        return new Req(method, mode, text);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }

    public int clientId() {
        return clientId;
    }

    public String context() {
        return context;
    }

}