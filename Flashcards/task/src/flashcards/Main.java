package flashcards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String inS, inF;
        File file;
        boolean work = true;
        DualMap<String, String> map = new DualMap<>();
        while (work) {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            switch (scanner.nextLine().trim()) {
                case "add":
                    System.out.println("The card:");
                    inF = scanner.nextLine();
                    if (!map.containsKey(inF)) {
                        System.out.println("The definition of the card:");
                        inS = scanner.nextLine();
                        if (!map.containsValue(inS)) {
                            map.put(inF, inS);
                            System.out.printf("The pair (\"%s\":\"%s\") has been added\n", inF, inS);
                        } else {
                            System.out.println("The definition already exists");
                        }
                    } else {
                        System.out.println("the card already exists");
                    }
                    break;
                case "remove":
                    System.out.println("Which card?");
                    inF = scanner.nextLine();
                    if (map.containsKey(inF)) {
                        map.remove(inF);
                        System.out.println("The card has been removed.");
                    } else {
                        System.out.printf("Can't remove \"%s\": there is no such card.\n", inF);
                    }
                    break;
                case "import":
                    System.out.println("File name:");
                    file = new File(scanner.nextLine());
                    int it = 0;
                    if (file.canRead()) {
                        Scanner fileScanner = new Scanner(file);
                        while (fileScanner.hasNextLine()) {
                            map.put(fileScanner.nextLine(), fileScanner.nextLine());
                            it++;
                        }
                        System.out.println(it + " cards have been loaded.");
                        fileScanner.close();
                    } else {
                        System.out.println("File not found.");
                    }
                    break;
                case "export":
                    System.out.println("File name:");
                    file = new File(scanner.nextLine());
                    FileWriter writer = new FileWriter(file);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        writer.write(entry.getKey() + "\n" + entry.getValue() + "\n");
                    }
                    System.out.println(map.size() + " cards have been saved.");
                    writer.close();
                    break;
                case "ask":
                    System.out.println("How many times to ask?");
                    int count = scanner.nextInt();
                    scanner.nextLine();
                    for (Map.Entry<String, String> entry = map.randomEntry(); count > 0; count--, entry = map.randomEntry()) {
                        System.out.printf("Print the definition of \"%s\":\n", entry.getKey());
                        inF = scanner.nextLine();
                        if (entry.getValue().equals(inF)) {
                            System.out.println("Correct!");
                        } else {
                            System.out.printf("Wrong. The right answer is \"%s\"", entry.getValue());
                            if (map.containsValue(inF)) {
                                System.out.printf("but your definition is correct for \"%s\".", map.getKey(inF));
                            }
                            System.out.println();
                        }
                    }
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    work = false;
                    break;
            }
        }
    }
}
