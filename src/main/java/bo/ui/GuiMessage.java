package bo.ui;

/**
 * Represents a message waiting to be displayed in the graphical interface.
 */
public record GuiMessage(String text, boolean isError) {
}
