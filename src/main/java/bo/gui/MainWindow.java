package bo.gui;

import bo.Tracker;
import bo.command.CommandException;
import bo.parser.Parser;
import bo.ui.GuiUi;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

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
    private final Image userImage = new Image(MainWindow.class.getResourceAsStream("/images/DaUser.png"));
    private final Image boImage = new Image(MainWindow.class.getResourceAsStream("/images/DaBo.png"));

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
        dialogContainer.getChildren().add(DialogBox.getUserDialog(command, userImage));
        userInput.clear();
        if (command.equals("bye")) {
            dialogContainer.getChildren().add(DialogBox.getBoDialog(
                    "Bye. Hope to see you again soon!", boImage));
            return;
        }
        try {
            parser.executeCommand(tracker, command);
        } catch (CommandException exception) {
            ui.showError(exception.getMessage());
        }
        String response = ui.takeMessages();
        if (!response.isBlank()) {
            dialogContainer.getChildren().add(DialogBox.getBoDialog(response.stripTrailing(), boImage));
        }
    }
}
