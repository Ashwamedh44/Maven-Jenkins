package com.jenkins_example.app;
public class App {

    private static final String MESSAGE = "Hello World!";

    public App() {
        // creating a class
    }

    public static void main(String[] args) {
        System.out.println(MESSAGE);
    }

    public String getMessage() {
        return MESSAGE;
    }
}
