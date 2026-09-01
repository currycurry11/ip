package bo.gui;

import bo.Tracker;
import bo.command.CommandException;
import bo.parser.Parser;
import bo.ui.GuiUi;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller for Bo's FXML-defined main window.
 */
public class MainWindow {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private final GuiUi ui = new GuiUi();
    private final Tracker tracker = new Tracker(ui);
    private final Parser parser = new Parser();

    /**
     * Initializes automatic scrolling after the FXML controls are injected.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Processes the command entered by the user and displays Bo's response.
     */
    @FXML
    private void handleUserInput() {
        String command = userInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }
        addDialog(command, true);
        userInput.clear();
        if (command.equals("bye")) {
            addDialog("Bye. Hope to see you again soon!", false);
            return;
        }
        try {
            parser.executeCommand(tracker, command);
        } catch (CommandException exception) {
            ui.showError(exception.getMessage());
        }
        String response = ui.takeMessages();
        if (!response.isBlank()) {
            addDialog(response.stripTrailing(), false);
        }
    }

    private void addDialog(String message, boolean isUser) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.getStyleClass().add(isUser ? "user-label" : "reply-label");
        HBox row = new HBox(label);
        row.setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        dialogContainer.getChildren().add(row);
    }
}
