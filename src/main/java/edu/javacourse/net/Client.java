package edu.javacourse.net;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class Client {
  public static void main(String[] args) throws IOException {
    for (int i = 0; i < 10; i++) {
      Socket socket = new Socket("localhost", 25225);
      Thread thread = new Thread(new SimpleClient(socket));
      thread.start();
    }
  }
}

class SimpleClient implements Runnable {
  private Socket socket;

  public SimpleClient(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

      System.out.println("Client started: " + LocalDateTime.now());

      String s = "Alex";
      writer.write(s);
      writer.newLine();
      writer.flush();

      String readLine = reader.readLine();
      System.out.println("Client got " + readLine);

    } catch (IOException e) {
      e.printStackTrace(System.out);
    }
  }
}
