package edu.javacourse.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket socket = new ServerSocket(25225);

    Map<String, Greetable> handlers = loadHandlers();

    while (true) {
      Socket client = socket.accept();
      new Thread(new SimpleServer(client, handlers)).start();
    }
  }

  private static Map<String, Greetable> loadHandlers() {
    Map<String, Greetable> result = new HashMap<>();

    try (InputStream inputStream = Server.class.getClassLoader().getResourceAsStream("server.properties")) {

      Properties properties = new Properties();
      properties.load(inputStream);

      for (Object command : properties.keySet()) {
        String className = properties.getProperty(command.toString());
        Class<Greetable> clazz = (Class<Greetable>) Class.forName(className);
        Greetable handler = clazz.getConstructor().newInstance();

        result.put(command.toString(), handler);
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return result;
  }
}

class SimpleServer implements Runnable {
  private Socket client;
  private Map<String, Greetable> handlers;

  public SimpleServer(Socket client, Map<String, Greetable> handlers) {
    this.client = client;
    this.handlers = handlers;
  }

  public void run() {
    handleRequest();
  }

  private void handleRequest() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

      String request = reader.readLine();
      String[] strings = request.split("\\s+");
      String command = strings[0];
      String userName = strings[1];
      System.out.println("Server got strings1: " + command);
      System.out.println("Server got strings2: " + userName);

      String response = buildResponse(command, userName);
      writer.write(response);
      writer.newLine();
      writer.flush();

      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String buildResponse(String command, String userName) {
    Greetable handler = handlers.get(command);
    if (handler != null) {
      return handler.buildResponse(userName);
    }
    return "Hello world, " + userName;
  }
}
