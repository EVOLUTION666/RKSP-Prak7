package server;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleHttpServer {

    /**
     * @param args
     */
    public static void main(String[] args) {


        try {
            ServerSocket serverConnect = new ServerSocket(8080);
            System.out.println("Сокет создан на порту 8080 - ждем запросов от клиентов");

            // начинаем слушать запросы
            while (true) {
                ClientHandler myServer = new ClientHandler(serverConnect.accept());

                System.out.println("Соединение установлено");

                // создаем отдельный поток для обработки запроса и формирования ответа
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}