package com.hackathon.hackathon2014;

import com.hackathon.hackathon2014.model.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class AnswerHolder {
    private static List<Answer> answers = new ArrayList<Answer>();

    public static int size() {
        return answers.size();
    }

    public static boolean addAll(Collection<? extends Answer> answers) {
        return AnswerHolder.answers.addAll(answers);
    }

    public static boolean isEmpty() {
        return answers.isEmpty();
    }

    public static boolean addAll(int i, Collection<? extends Answer> answers) {
        return AnswerHolder.answers.addAll(i, answers);
    }

    public static void clear() {
        answers.clear();
    }

    public static boolean containsAll(Collection<?> objects) {
        return answers.containsAll(objects);
    }

    public static boolean removeAll(Collection<?> objects) {
        return answers.removeAll(objects);
    }

    public static Answer remove(int i) {
        return answers.remove(i);
    }

    public static Answer get(int i) {
        return answers.get(i);
    }

    public static boolean remove(Object o) {
        return answers.remove(o);
    }

    public static boolean retainAll(Collection<?> objects) {
        return answers.retainAll(objects);
    }

    public static boolean contains(Object o) {
        return answers.contains(o);
    }

    public static void add(Answer answer){
        if(answers.contains(answer)){
            answers.remove(answer);
        }
        answers.add(answer);
    }

    public static List<Answer> getAll() {
        return answers;
    }

    public static int indexOf(Answer answer) {
        return answers.indexOf(answer);
    }
}
