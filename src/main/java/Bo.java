public class Bo {
    public static void main(String[] args) {
        String banner = """
                 ____
                | __ )  ___
                |  _ \\ / _ \\
                | |_) | (_) |
                |____/ \\___/
                """;

        printSeparator();
        System.out.println(banner);
        System.out.println("Hello! I'm Bo.");
        System.out.println("What can I do for you?");
        printSeparator();
        System.out.println();
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();
    }

    // Prints a horizontal line to separate sections of Bo's messages.
    public static void printSeparator() {
        System.out.println("----------------------------------------");
    }
}
