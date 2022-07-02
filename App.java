package Carpets;

import java.util.*;

public class App {

    public static HashMap<CarpetStrip, Integer> available = new HashMap<CarpetStrip, Integer>();

    public static void main(String[] args) {
        int length = Integer.parseInt(args[0]);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String carStripColours = scanner.nextLine();
            CarpetStrip carStrip = new CarpetStrip(carStripColours, available);
            addStripToInv(carStrip);
        }

        if (args[1].equals("-n")) {
            noMatches(length);
        } else if (args[1].equals("-m")) {
            // call max match method, ignore for now
            System.out.println("Max matches not implemented");
        } else if (args[1].equals("-b")) {
            balancedMatches(length);
        } else {
            error();
        }
    }

    public static void balancedMatches(int length) {
        System.out.println("Doing best balanced match");
        System.out.println("Available strips: " + available);
        Carpet carpet = new Carpet(length,available);
        System.out.println("Initial carpet: " + carpet);
        int runs = 100;
        int absValue = carpet.randomizedAttempts(runs);
        System.out.println("Final Carpet: " + carpet + " absValue: " + absValue);
    }
        
    public static void noMatches(int length) {
        // System.out.println("Doing no match");
        // System.out.println("Available strips: " + available);
        Stack<Carpet> stack = new Stack<Carpet>();
        for (CarpetStrip c : available.keySet()) {
            Carpet carpet = new Carpet(c, available);
            stack.add(carpet);
            while (!stack.isEmpty()) {
                carpet = stack.pop();
                for (CarpetStrip d : reOrder(carpet.stripsNoMatch())) {
                    Carpet next = new Carpet(carpet);
                    next.addStrip(d);
                    if (next.length == length) {
                        // System.out.println("No match found " + length + " " + next);
                        printCarpet(next);
                        return;
                    } else {
                        // System.out.println("Pushing: " + next);
                        stack.push(next);
                    }
                }
            }
        }
        System.out.println("not possible");
    }
    public static void printCarpet(Carpet carpet) {
        for (CarpetStrip c : carpet.strips) {
            System.out.println(c);
        }
    }


    public static ArrayList<CarpetStrip> reOrder(ArrayList<CarpetStrip> noMatches) {
        ArrayList<CarpetStrip> result = new ArrayList<CarpetStrip>(noMatches);
        Collections.sort(result);
        return result;
    }

    public static void addStripToInv(CarpetStrip strip) {
        if (available.containsKey(strip)) {
            available.put(strip, available.get(strip) + 1);
        } else {
            available.put(strip, 1);
        }
    }
    
    public static void error() {
        System.out.println("Usage: java Carpets.App <length> <type>");
    }
}
