package edu.javacourse.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocket socket = new ServerSocket(25225);

    System.out.println("Server is started");
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

      StringBuilder sb = new StringBuilder("Hello, ");
      String readLine = reader.readLine();
      System.out.println("Server got string " + readLine);
      Thread.sleep(2000);

      sb.append(readLine);
      writer.write(sb.toString());
      writer.newLine();
      writer.flush();

      client.close();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
