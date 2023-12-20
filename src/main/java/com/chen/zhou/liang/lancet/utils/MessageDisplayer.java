package com.chen.zhou.liang.lancet.utils;

import javafx.scene.control.TextField;

public class MessageDisplayer {
    private TextField messageTextField = null;

    public void initialize(TextField messageTextField) {
        this.messageTextField = messageTextField;
    }

    public void displayMessage(String message) {
        messageTextField.setText(message);
    }

    public void displayMessage(String message, Throwable e) {
        // TODO: print stack trace.
        displayMessage(message + ", 原因:\n" + e.getMessage());
    }

    public void displayException(DisplayableException e) {
        displayMessage(e.getMessage());
    }

    public void displayException(String message, DisplayableException e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            displayMessage(message + e.getMessage(), cause);
        } else {
            displayMessage(message + e.getMessage());
        }
    }
}
