package me.avocardo.guilds.utilities;

import java.util.Comparator;
import java.util.Map;

import me.avocardo.guilds.Guild;

public class ValueComparator implements Comparator<Guild> {

    Map<Guild, Double> base;
    
    public ValueComparator(Map<Guild, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Guild a, Guild b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}