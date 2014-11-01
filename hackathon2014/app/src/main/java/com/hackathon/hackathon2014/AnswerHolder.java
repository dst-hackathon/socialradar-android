package com.hackathon.hackathon2014;

import com.hackathon.hackathon2014.model.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class AnswerHolder {
    private static List<Option> options = new ArrayList<Option>();

    public static int size() {
        return options.size();
    }

    public static boolean addAll(Collection<? extends Option> answers) {
        return AnswerHolder.options.addAll(answers);
    }

    public static boolean isEmpty() {
        return options.isEmpty();
    }

    public static boolean addAll(int i, Collection<? extends Option> answers) {
        return AnswerHolder.options.addAll(i, answers);
    }

    public static void clear() {
        options.clear();
    }

    public static boolean containsAll(Collection<?> objects) {
        return options.containsAll(objects);
    }

    public static boolean removeAll(Collection<?> objects) {
        return options.removeAll(objects);
    }

    public static Option remove(int i) {
        return options.remove(i);
    }

    public static Option get(int i) {
        return options.get(i);
    }

    public static boolean remove(Object o) {
        return options.remove(o);
    }

    public static boolean retainAll(Collection<?> objects) {
        return options.retainAll(objects);
    }

    public static boolean contains(Object o) {
        return options.contains(o);
    }

    public static void add(Option option){
        if(options.contains(option)){
            options.remove(option);
        }
        options.add(option);
    }

    public static List<Option> getAll() {
        return options;
    }

    public static int indexOf(Option option) {
        return options.indexOf(option);
    }
}
