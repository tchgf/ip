package jeff.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jeff.Jeff;
import jeff.parser.Parser;

/**
 * A very basic JavaFX GUI for Jeff: a scrollable transcript of the
 * conversation, a text field for typing commands, and a button to send them.
 * All command handling is delegated to {@link Jeff#getResponse(String)}, so
 * this class only wires up the window and forwards user input to it.
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        TextArea transcript = new TextArea();
        transcript.setEditable(false);
        transcript.setWrapText(true);
        transcript.appendText(Jeff.getWelcomeMessage() + "\n\n");

        TextField input = new TextField();
        input.setPromptText("Type a command, e.g. todo read book");

        Button sendButton = new Button("Send");
        Runnable sendInput = () -> sendInput(transcript, input);
        sendButton.setOnAction(event -> sendInput.run());
        input.setOnAction(event -> sendInput.run());

        HBox inputBar = new HBox(8, input, sendButton);
        HBox.setHgrow(input, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(8));
        root.setCenter(transcript);
        root.setBottom(inputBar);
        BorderPane.setMargin(inputBar, new Insets(8, 0, 0, 0));

        stage.setTitle("Jeff");
        stage.setScene(new Scene(root, 480, 600));
        stage.show();
    }

    /** Sends the text field's contents to Jeff, appends the exchange to the transcript, and clears it. */
    private void sendInput(TextArea transcript, TextField input) {
        String text = input.getText();
        if (text.isBlank()) {
            return;
        }
        transcript.appendText("You: " + text + "\n");
        transcript.appendText(Jeff.getResponse(text) + "\n\n");
        input.clear();
        if (Parser.parseCommandType(text) == Parser.Command.BYE) {
            Platform.exit();
        }
    }
}
