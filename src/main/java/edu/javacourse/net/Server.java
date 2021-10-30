package edu.javacourse.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocket socket = new ServerSocket(25225);

    while (true) {
      Socket client = socket.accept();
      new Thread(new SimpleServer(client)).start();
    }
  }
}

class SimpleServer implements Runnable {
  private Socket client;

  public SimpleServer(Socket client) {
    this.client = client;
  }

  public void run() {
    handleRequest();
  }

  private void handleRequest() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {


      String request = reader.readLine();
      String[] strings = request.split("\\s+");
      String cmd = strings[0];
      String userName = strings[1];
      System.out.println("Server got strings1: " + cmd);
      System.out.println("Server got strings2: " + userName);

      String response = buildResponse(cmd, userName);
      writer.write(response);
      writer.newLine();
      writer.flush();

      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String buildResponse(String cmd, String userName) {
    return switch (cmd) {
      case "HELLO" -> "Hello, " + userName;
      case "MORNING" -> "Good Morning, " + userName;
      case "AFTERNOON" -> "Good Afternoon, " + userName;
      case "EVENING" -> "Good Evening, " + userName;
      case "HI" -> "Hi, " + userName;
      default -> "Bye, " + userName;
    };
  }
}
