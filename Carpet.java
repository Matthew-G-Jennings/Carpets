package Carpets;

import java.util.*;

public class Carpet {

    public HashMap<CarpetStrip, Integer> available;
    public ArrayList<CarpetStrip> strips;
    public int length;
    public CarpetStrip last;

    public Carpet(CarpetStrip first, HashMap<CarpetStrip, Integer> available) {
        this.available = new HashMap<CarpetStrip, Integer>(available);
        this.strips = new ArrayList<CarpetStrip>();
        this.strips.add(first);
        if (this.available.get(first) == 1) {
            this.available.remove(first);
        } else {
            this.available.put(first, this.available.get(first) - 1);
        }
        this.last = first;
        this.length = 1;
    }

    public Carpet(int creationLength, HashMap<CarpetStrip, Integer> available) {
        this.available = new HashMap<CarpetStrip, Integer>(available);
        this.strips = new ArrayList<CarpetStrip>();
        this.length = 0;
        for (int i = 0; i < creationLength;i++){
            Random generator = new Random();
            Object[] values = this.available.keySet().toArray();
            CarpetStrip randomValue = (CarpetStrip)values[generator.nextInt(values.length)];
            this.strips.add(randomValue);
            invRemove(randomValue);
            this.length++;
        }
    }

    public Carpet(Carpet carpet) {
        this.length = 0;
        this.available = new HashMap<CarpetStrip, Integer>(carpet.available);
        this.strips = new ArrayList<CarpetStrip>();
        for (CarpetStrip strip : carpet.strips) {
            this.strips.add(strip);
            this.length++;
        }
        this.last = carpet.last;
    }

    public void addStrip(CarpetStrip strip) {
        boolean flip = false;
        if (!this.available.containsKey(strip)) {
            // this is a flipped strip (or something has gone horribly wrong)
            flip = true;
            strip.flip();
            if (!this.available.containsKey(strip)) {
                System.err.println("If you are reading this message, something has gone horribly wrong");
            }
        }
        if (this.available.get(strip) == 1) {
            this.available.remove(strip);
        } else {
            this.available.put(strip, this.available.get(strip) - 1);
        }
        if (flip) {
            strip.flip();
        }

        this.strips.add(strip);
        this.last = strip;
        this.length++;
    }

    public void invAdd(CarpetStrip strip) {
        if (this.available.containsKey(strip)) {
            this.available.put(strip, available.get(strip) + 1);
        } else {
            this.available.put(strip, 1);
        }
    }

    public void invRemove(CarpetStrip strip) {
        int returned = this.available.remove(strip);
        if (returned != 1){
            this.available.put(strip, returned - 1);
        }
    }

    public ArrayList<CarpetStrip> stripsNoMatch() {
        ArrayList<CarpetStrip> result = new ArrayList<CarpetStrip>();
        for (CarpetStrip c : available.keySet()) {
            if (last.zeroMatches(c)) {
                result.add(c);
                continue;
            }
            CarpetStrip flipped = new CarpetStrip(c.colours, available);
            flipped.flip();
            if (last.zeroMatches(flipped)) {
                result.add(flipped);
            }
        }
        return result;
    }

    public int randomizedAttempts(int runs){
        int count = 0;
        int absValue = stripsAbsValue();
        Random generator = new Random();
        while (count < runs){
            absValue = stripsAbsValue();
            if (absValue == 0){
                return absValue;
            }
            Object[] values = this.available.keySet().toArray();
            CarpetStrip randomValue = (CarpetStrip)values[generator.nextInt(values.length)];
            int randomIndex = generator.nextInt(this.length);
            CarpetStrip readdStrip = modifyStrips(randomIndex, randomValue);
            int newAbsValue = stripsAbsValue();
            System.out.println(absValue + " > " + newAbsValue);
            if (absValue >= newAbsValue){
                System.out.println("Swapping");
                invRemove(randomValue);
                invAdd(readdStrip);
            } else {
                undo(randomIndex, readdStrip);
                count++;
            } 
        }
        return absValue;
    }

    public CarpetStrip modifyStrips(int index, CarpetStrip strip) {
        this.strips.add(index, strip);
        CarpetStrip removedStrip = this.strips.remove(index+1);
        return removedStrip;
    }

    public void undo(int index, CarpetStrip strip){
        this.strips.add(index, strip);
        this.strips.remove(index+1);
    }

    public int stripsAbsValue() {
        int matchesCount = 0;
        for (int i=0; i < this.strips.size()-1;i++){
            matchesCount = matchesCount + this.strips.get(i).matches(this.strips.get(i+1));
        }
        int matches = matchesCount;
        int total = (this.length - 1) * this.strips.get(0).colours.length();
        int nonMatches = (total - matches);
        int absValue = 0;
        if (matches > nonMatches) {
            absValue = matches - nonMatches;
        } else {
            absValue = nonMatches - matches;
        }
        return absValue;
    }

    public String toString() {
        String result = "";

        result += "Strips in this carpet: ";
        for (CarpetStrip c : this.strips) {
            result += c + " ";
        }
        result += "Strips still available: ";

        result += this.available;

        return result;
    }
}
