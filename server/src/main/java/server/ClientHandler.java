package server;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
    private static Socket clientDialog;

    public ClientHandler(Socket client) {
        this.clientDialog = client;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;
        String fio = "Job by: Nefedov Andrey Alexeevich;\n";
        String group = "Number of group: IKBO-16-18;\n";
        String number = "Number of work: 14;\n";
        String text = "Text of work: Opredelitel matrix;\n";

        try {
            // канал чтения из сокета
            in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            // канал записи в сокет (для HEADER)
            out = new PrintWriter(clientDialog.getOutputStream());
            // канал записи в сокет (для данных)
            dataOut = new BufferedOutputStream(clientDialog.getOutputStream());


            // первая строка запроса
            String input = in.readLine();
            // разбираем запрос по токенам
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // получаем HTTP метод от клиента
            // текст запроса от клиента
            fileRequested = parse.nextToken().toLowerCase();


            System.out.println("Method: " + method);
            System.out.println("Request: " + fileRequested.substring(1));


            // пока поддерживаем GET and HEAD запросы
            if (method.equals("GET") || method.equals("HEAD")) {

                String content = getContentType(fileRequested);
                String body = getBody(fio, group, number, text, fileRequested.substring(1));

                if (method.equals("GET")) {
                    // GET method - возвращаем ответ

                    // шлем HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    //Длина ответа - эхо запроса без первого "/"
                    out.println("Content-length: " + body.length());
                    out.println(); // Пустая строка между headers и содержимым!
                    out.flush();

                    dataOut.write(body.getBytes(), 0, body.length());
                    dataOut.flush();
                }

                System.out.println("Ответ отослан: " + body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Возвращаем поддерживываемый  MIME Types
    private String getContentType(String fileRequested) {
//        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
            return "text/html";
//        else
//            return "text/plain";
    }

    private String getBody(String fio, String group, String number, String text, String request) {
        Integer matrix[][] = new Integer[2][2];
        int x;
        String[] subStr;
        String delimeter = "_"; // Разделитель
        subStr = request.split(delimeter);

        matrix[0][0]= Integer.parseInt(subStr[0]);
        matrix[0][1]= Integer.parseInt(subStr[1]);
        matrix[1][0]= Integer.parseInt(subStr[2]);
        matrix[1][1]= Integer.parseInt(subStr[3]);

        for (int i=0;i<2;i++){
            for (int j=0;j<2;j++){
                System.out.println(matrix[i][j]);
            }
        }
        x=matrix[0][0]*matrix[1][1]-matrix[1][0]*matrix[0][1];

       request= Integer.toString(x);
        return "<h1>"+ fio + "\n" + group + "\n" + number + "\n" + text + "\n" +  request + "</h1>";
    }

//    private Integer
}