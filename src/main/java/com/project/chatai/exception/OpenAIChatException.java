package com.project.chatai.exception;

public class OpenAIChatException extends RuntimeException {

  public OpenAIChatException(String message) {
    super(message);
  }

  public OpenAIChatException(String message, Throwable cause) {
    super(message, cause);
  }
}
