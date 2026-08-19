import java.util.Scanner;

public class Jeff {
    private static final String DIVIDER = "____________________________________________________________";

    public static void main(String[] args) {
        String banner = "     _  _____  _____  _____ \n"
                + "    | || ____||  ___||  ___|\n"
                + "    | || |__  | |_   | |_   \n"
                + "| |_| ||  __| |  _|  |  _|  \n"
                + " \\___/ |_____||_|    |_|    ";
        System.out.println(DIVIDER);
        System.out.println(banner);
        System.out.println("Hello! I'm Jeff.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                break;
            }
            System.out.println(input);
            System.out.println(DIVIDER);
        }
        scanner.close();
    }
}
