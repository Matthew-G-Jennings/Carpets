package Carpets;

import java.util.*;

public class CarpetStrip implements Comparable<CarpetStrip> {

    char[] forw;
    char[] back;
    String colours;
    HashMap<CarpetStrip, Integer> available;

    public CarpetStrip(String colours, HashMap<CarpetStrip, Integer> available) {
        this.available = available;
        this.colours = colours;
        this.forw = new char[colours.length()];
        this.back = new char[colours.length()];
        for (int i = 0; i < colours.length(); i++) {
            this.forw[i] = colours.charAt(i);
            this.back[colours.length() - 1 - i] = colours.charAt(i);
        }
    }

    public String toString() {
        String result = "";
        // String backwards = "";
        for (int i = 0; i < forw.length; i++) {
            result += this.forw[i];
            // backwards += this.back[i];
        }
        return result;
    }

    public int matches(CarpetStrip other) {
        int matchesf = 0;
        int matchesb = 0;
        for (int i = 1; i < this.forw.length-1; i++) {
            if (this.forw[i] == other.forw[i]) {
                matchesf++;
            }
            if (this.forw[i] == other.back[i]) {
                matchesb++;
            }
        }
        return matchesf >= matchesb ? matchesf : -matchesb;
    }

    public boolean zeroMatches(CarpetStrip other) {
        for (int i = 0; i < this.forw.length; i++) {
            if (this.forw[i] == other.forw[i]) {
                return false;
            }
        }
        return true;
    }

    public void flip() {
        char[] temp = this.forw;
        this.forw = this.back;
        this.back = temp;
        colours = new StringBuilder(colours).reverse().toString();
    }

    @Override
    public int compareTo(CarpetStrip other) {
        if (!this.available.containsKey(this)) {
            this.flip();
        }
        if (!other.available.containsKey(other)) {
            other.flip();
        }
        return this.available.get(this) - other.available.get(other);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((colours == null) ? 0 : colours.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CarpetStrip other = (CarpetStrip) obj;
        if (colours == null) {
            if (other.colours != null)
                return false;
        } else if (!colours.equals(other.colours))
            return false;
        return true;
    }
}
