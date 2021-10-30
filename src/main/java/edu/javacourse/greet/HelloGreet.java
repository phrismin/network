package edu.javacourse.greet;

import edu.javacourse.net.Greetable;

public class HelloGreet extends Greetable {
  @Override
  public String buildResponse(String userName) {
    return "Hello, " + userName;
  }
}
