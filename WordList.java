import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.TimeUnit;

/**import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JTextField;*/
public class WordList {
    private HashMap<String, String> words;
    private int width;
    private int height;
    WordList(int w, int h) {
        width = w;
        height = h;
        initialize();
    }
    public void initialize() {
        edu.princeton.cs.introcs.StdDraw.setCanvasSize(width,height);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setPenRadius();
    }
    public static void main(String[] args) {
        System.out.println("Welcome to NZ wordList game!" +
                "\nPlease select your game mode.\n Press 0 for study \n Press 1 for test.");
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        if (i == 1) {
            System.out.println("Not implemented yet!");
            System.exit(0);
        } else if (i == 0) {
            startStudy();
        }
    }

    // User interface at the beginning.
    public static void startStudy() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/Users/yubohan/Desktop/EverythingInIt/study.csv"));


        } catch (FileNotFoundException e) {
            System.out.println("This is your first time studying.");
            initializeStudy();
            return;
        }
        System.out.println("Detected previous study record. Would you like to continue studying or restart?" +
                "\nPress 0 for continue.\nPress 1 for restart");
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        if (i == 0) {
            continueStudy();
        } else {
            initializeStudy();
        }
    }

    // Helper method for reading the whole wordlist.
    // Remember to change the filename in it!!!
    public static WordList readWords() {
        WordList w = new WordList(1200, 800);
        // Read in the wordlist.
        HashMap<String, String> words = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/Users/yubohan/Desktop/EverythingInIt/wordlist.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            String line = null;
            try {
                line=reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
            }
            String[] a = line.split(",");
            words.put(a[0], a[1]);
        }
        w.words = words;
        return w;
    }

    // Called when the user wants to start a new study.
    public static void initializeStudy() {
        System.out.println("Starting a new study session for you...\nPress 0 if you don't recognize the word. " +
                "\n Press 1 if you recognize the word. \n Press 2 if you want to end the study.");
        WordList w = readWords();

        // Start displaying words in alphabetical order.
        Set<String> keys= w.words.keySet();
        List<String> key = new ArrayList<>(keys);
        Collections.sort(key);
        String def;
        List<String> result = new ArrayList<>();
        List<String> known = new ArrayList<>();
        double startX = w.width * 0.5;
        char c;
        int total = key.size();
        int count = 0;
        int j, k;
        for (String i: key) {
            reset();
            j = result.size();
            k = known.size();
            def = w.words.get(i).trim();
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(0.1 * w.width, w.height * 0.95, count + "/" + total);
            StdDraw.text(0.15 * w.width, w.height * 0.9, "words unknown: " + j);
            StdDraw.text(0.15 * w.width, w.height * 0.85, "words known: " + k);

            count++;
            StdDraw.text(startX,w.height * 0.9, "the word is:");
            StdDraw.text(startX, w.height * 0.7, "Its definition is:");
            StdDraw.text(startX, w.height * 0.4, "Press 0 if you don't recognize it.");
            StdDraw.text(startX, w.height * 0.3, "Press 1 if you recognize it.");
            StdDraw.text(startX, w.height * 0.2, "Press 2 to quit.");
            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.text(startX, w.height * 0.8, i);
            StdDraw.text(startX, w.height * 0.6, def);
            StdDraw.show();
            c = nextChar(0);
            if (c == '0') {
                result.add(i);
            }
            if (c == '1') {
                known.add(i);
            }
            if (c == '2') {
                EndStudy(result, known);
                return;
            }
        }
    }

    // Save the result of study.
    // Called when the user press 2 to quit.
    public static void EndStudy(List<String> result, List<String> known) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("study.csv");
            for (String i: result) {
                fw.append(i + ",");
            }
            fw.append("\n");
            for (String i: known) {
                fw.append(i + ",");
            }
            fw.flush();
            fw.close();
            System.out.println("Successfully saved your study progress!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Called when the user continues the previous study.
    public static void continueStudy() {
        WordList w = readWords();

        List<String> result = new ArrayList<>();
        List<String> known = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("study.csv"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String[] s1 = line1.split(",");
            String[] s2 = line2.split(",");
            for (String i: s1) {
                result.add(i);
            }
            for (String i: s2) {
                known.add(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = result.size() + known.size();
        int total = w.words.size();
        Set<String> keys= w.words.keySet();
        List<String> key = new ArrayList<>(keys);
        Collections.sort(key);
        String k;
        String def;
        double startX = 0.5 * w.width;
        char c;
        int a, b;
        for (int i = count; i < total; i++) {
            k = key.get(i);
            def = w.words.get(k).trim();
            reset();
            a = result.size();
            b = known.size();
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(0.1 * w.width, w.height * 0.95, count + "/" + total);
            count++;
            StdDraw.text(0.15 * w.width, w.height * 0.9, "words unknown: " + a);
            StdDraw.text(0.15* w.width, w.height * 0.85, "words known: " + b);
            StdDraw.text(startX,w.height * 0.9, "the word is:");
            StdDraw.text(startX, w.height * 0.7, "Its definition is:");
            StdDraw.text(startX, w.height * 0.4, "Press 0 if you don't recognize it.");
            StdDraw.text(startX, w.height * 0.3, "Press 1 if you recognize it.");
            StdDraw.text(startX, w.height * 0.2, "Press 2 to quit.");
            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.text(startX, w.height * 0.8, k);
            StdDraw.text(startX, w.height * 0.6, def);
            StdDraw.show();
            c = nextChar(0);
            if (c == '0') {
                result.add(k);
            }
            if (c == '1') {
                known.add(k);
            }
            if (c == '2') {
                EndStudy(result, known);
                return;
            }
        }
    }

    /**private static void displayText(double x, double y, String text) {
        StdDraw.text(x, y, text);
        StdDraw.show();
    }*/
    // Below are some helpful functions.
    // Declared private so that you can only use them in this class.

    // Let the system wait for user input.
    private static void pause(int sleeptime) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleeptime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Clear the canvas.
    private static void reset() {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.show();
    }

    // Get the nextChar of user Input.
    private static char nextChar(int sleeptime) {
        while (!StdDraw.hasNextKeyTyped()) {
            pause(sleeptime);
        }
        return Character.toLowerCase(StdDraw.nextKeyTyped());
    }

}
