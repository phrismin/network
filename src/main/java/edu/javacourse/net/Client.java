package edu.javacourse.net;

import java.io.*;
import java.net.Socket;

public class Client {
  public static void main(String[] args) throws IOException {
    for (int i = 0; i < 10; i++) {
      Socket socket = new Socket("localhost", 25225);
      new Thread(new SimpleClient(socket, i)).start();
    }
  }
}

class SimpleClient implements Runnable {
  private Socket socket;
  private final static String[] COMMAND = {"HELLO", "MORNING", "AFTERNOON", "EVENING", "HI"};
  private int cmdNumber;

  public SimpleClient(Socket socket, int cmdNumber) {
    this.socket = socket;
    this.cmdNumber = cmdNumber;
  }

  @Override
  public void run() {

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

      String cmd = COMMAND[cmdNumber % COMMAND.length];
      String s = cmd + " " + "Alex";
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
