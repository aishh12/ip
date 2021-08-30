import java.io.IOException;
import java.util.Scanner;

public class Blitz {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a Blitz object with a storage object that
     *     accesses the given file path.
     *
     * @param filePath file path that the storage object will access.
     */
    public Blitz(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadFileContents());
        } catch (BlitzException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Begins running the application.
     */
    public void run() {
        ui.printGreeting();
        ui.printList(tasks, "Here are the tasks in your list:");
        ui.printFormatted("So what can I do for you today?");

        Scanner scanner = new Scanner(System.in);

        //command from the user
        String command = scanner.nextLine();

        while (!command.equals("bye")) {
            try {
                Parser.parse(command, tasks, ui);
            } catch (BlitzException ex) {
                ui.printFormatted(ex.toString());
            }
            command = scanner.nextLine();
        }

        try {
            storage.saveTasksInFile(tasks);
        } catch (IOException e) {
            System.err.println(e);
        }
        ui.printGoodbye();
    }

    public static void main(String[] args) {
        new Blitz("data/blitz.txt").run();
    }
}
