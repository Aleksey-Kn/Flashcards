package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    private static FileWriter writer;
    private static final LinkedList<Pair<String, Integer>> wrongs = new LinkedList<>();
    private static final Comparator<Pair<String, Integer>> comparator = Comparator.comparing(o -> o.getSecond() * -1);
    private static final DualMap<String, String> map = new DualMap<>();
    private static final LinkedList<String> log = new LinkedList<>();

    private static String toCorrectForm(String in){
        return in.toLowerCase().replace(",", "").replace(".", "").replace(" ", "");
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String inS, inF;
        File file, imp = null, exp = null;
        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-import")){
                imp = new File(args[++i]);
            } else if(args[i].equals("-export")){
                exp = new File(args[++i]);
            }
        }
        if(imp != null){
            importing(imp);
        }
        boolean work = true, contains;
        while (work) {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            log.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            switch (scanner.nextLine().trim()) {
                case "add":
                    log.add("add");
                    System.out.println("The card:");
                    log.add("The card:");
                    inF = scanner.nextLine().trim();
                    log.add(inF);
                    if (!map.containsKey(inF)) {
                        System.out.println("The definition of the card:");
                        log.add("The definition of the card:");
                        inS = scanner.nextLine().trim();
                        log.add(inS);
                        if (!map.containsValue(inS)) {
                            map.put(inF, inS);
                            System.out.printf("The pair (\"%s\":\"%s\") has been added\n", inF, inS);
                            log.add(String.format("The pair (\"%s\":\"%s\") has been added\n", inF, inS));
                        } else {
                            System.out.println("The definition \"" + inS +"\" already exists");
                            log.add("The definition \"" + inS +"\" already exists");
                        }
                    } else {
                        System.out.println("The card \"" + inF +"\" already exists");
                        log.add("The card \"" + inF +"\" already exists");
                    }
                    break;
                case "remove":
                    log.add("remove");
                    System.out.println("Which card?");
                    log.add("Which card?");
                    inF = scanner.nextLine().trim();
                    log.add(inF);
                    if (map.containsKey(inF)) {
                        map.remove(inF);
                        for(Pair<String, Integer> pair: wrongs){
                            if(pair.getFirst().equals(inF)){
                                wrongs.remove(pair);
                            }
                        }
                        System.out.println("The card has been removed.");
                        log.add("The card has been removed.");
                    } else {
                        System.out.printf("Can't remove \"%s\": there is no such card.\n", inF);
                        log.add(String.format("Can't remove \"%s\": there is no such card.\n", inF));
                    }
                    break;
                case "reset stats":
                    log.add("reset stats");
                    wrongs.clear();
                    System.out.println("Card statistics have been reset.");
                    log.add("Card statistics have been reset.");
                    break;
                case "hardest card":
                    log.add("hardest card");
                    if(wrongs.isEmpty()){
                        System.out.println("There are no cards with errors.");
                        log.add("There are no cards with errors.");
                    } else {
                        wrongs.sort(comparator);
                        if(wrongs.size() > 1 && wrongs.peekFirst().getSecond().equals(wrongs.get(1).getSecond())) {
                            System.out.print("The hardest cards are ");
                            log.add("The hardest cards are ");
                            for(Pair<String, Integer> pair: wrongs){
                                if(pair.getSecond().equals(wrongs.peekFirst().getSecond())) {
                                    System.out.printf("\"%s\" ", pair.getFirst());
                                    log.add(String.format("\"%s\" ", pair.getFirst()));
                                }
                            }
                            System.out.printf(". You have %d errors answering them.\n", wrongs.peekFirst().getSecond());
                            log.add(String.format(". You have %d errors answering them.",  wrongs.peekFirst().getSecond()));
                        } else{
                            System.out.printf("The hardest card is \"%s\". You have %d errors answering it.\n",
                                    wrongs.peekFirst().getFirst(), wrongs.peekFirst().getSecond());
                            log.add(String.format("The hardest card is \"%s\". You have %d errors answering it.",
                                    wrongs.peekFirst().getFirst(), wrongs.peekFirst().getSecond()));
                        }
                    }
                    break;
                case "import":
                    log.add("import");
                    System.out.println("File name:");
                    log.add("File name:");
                    file = new File(scanner.nextLine());
                    log.add(file.getName());
                    importing(file);
                    break;
                case "export":
                    log.add("export");
                    System.out.println("File name:");
                    log.add("File name:");
                    file = new File(scanner.nextLine());
                    log.add(file.getName());
                    export(file);
                    break;
                case "ask":
                    log.add("ask");
                    System.out.println("How many times to ask?");
                    log.add("How many times to ask?");
                    int count = scanner.nextInt();
                    log.add(Integer.toString(count));
                    scanner.nextLine();
                    for (Map.Entry<String, String> entry = map.randomEntry(); count > 0; count--, entry = map.randomEntry()) {
                        System.out.printf("Print the definition of \"%s\":\n", entry.getKey());
                        log.add(String.format("Print the definition of \"%s\":\n", entry.getKey()));
                        inF = scanner.nextLine();
                        log.add(inF);
                        if (toCorrectForm(entry.getValue()).equals(toCorrectForm(inF))) {
                            System.out.println("Correct!");
                            log.add("Correct!");
                        } else {
                            System.out.printf("Wrong. The right answer is \"%s\"", entry.getValue());
                            log.add(String.format("Wrong. The right answer is \"%s\"", entry.getValue()));
                            contains = false;
                            for(Pair<String, Integer> pair: wrongs){
                                if(pair.getFirst().equals(entry.getKey())){
                                    contains = true;
                                    wrongs.remove(pair);
                                    wrongs.add(new Pair<>(pair.getFirst(), pair.getSecond() + 1));
                                    break;
                                }
                            }
                            if(!contains){
                                wrongs.add(new Pair<>(entry.getKey(), 1));
                            }
                            if (map.containsValue(inF)) {
                                System.out.printf("but your definition is correct for \"%s\".", map.getKey(inF));
                                log.add(String.format("but your definition is correct for \"%s\".", map.getKey(inF)));
                            }
                            System.out.println();
                        }
                    }
                    break;
                case "log":
                    log.add("log");
                    System.out.println("File name:");
                    log.add("File name:");
                    file = new File(scanner.nextLine());
                    log.add(file.getName());
                    writer = new FileWriter(file);
                    System.out.println("The log has been saved.");
                    log.add("The log has been saved.");
                    while (!log.isEmpty()){
                        writer.write(log.pop());
                        writer.write("\r\n");
                    }
                    writer.close();
                    break;
                case "exit":
                    if(exp != null){
                        export(exp);
                    }
                    System.out.println("Bye bye!");
                    work = false;
                    break;
            }
        }
    }

    private static void export(File file) throws IOException {
        writer = new FileWriter(file);
        wrongs.sort(comparator);
        int k = 0;
        for (Pair<String, Integer> pair: wrongs){
            if(k++ == 3){
                break;
            }
            writer.write(pair.getFirst());
            writer.write(":");
            writer.write(Integer.toString(pair.getSecond()));
            writer.write(":");
        }
        writer.write("\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            writer.write(entry.getKey() + "\n" + entry.getValue() + "\n");
        }
        System.out.println(map.size() + " cards have been saved.");
        log.add(map.size() + " cards have been saved.");
        writer.close();
    }

    private static void importing(File file) throws FileNotFoundException {
        int it = 0;
        if (file.canRead()) {
            Scanner fileScanner = new Scanner(file);
            String[] wrongsString = fileScanner.nextLine().split(":");
            for(int i = 0; i < wrongsString.length - 1; i += 2){
                for(Pair<String, Integer> pair: wrongs){
                    if(pair.getFirst().equals(wrongsString[i])){
                        wrongs.remove(pair);
                        break;
                    }
                }
                wrongs.add(new Pair<>(wrongsString[i], Integer.parseInt(wrongsString[i + 1])));
            }
            while (fileScanner.hasNextLine()) {
                map.put(fileScanner.nextLine().trim(), fileScanner.nextLine().trim());
                it++;
            }
            System.out.println(it + " cards have been loaded.");
            log.add(it + " cards have been loaded.");
            fileScanner.close();
        } else {
            System.out.println("File not found.");
            log.add("File not found.");
        }
    }
}
