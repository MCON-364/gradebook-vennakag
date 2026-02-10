package edu.course.gradebook;

import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();

    public Optional<List<Integer>> findStudentGrades(String name) {
        return Optional.ofNullable(gradesByStudent.get(name));
    }

    public boolean addStudent(String name) {
        boolean added = false;
        if(!gradesByStudent.containsKey(name)) {
            gradesByStudent.put(name, new ArrayList<>());
            added = true;
        }
        return added;
    }

    public boolean addGrade(String name, int grade) {
        boolean added = false;
        if(gradesByStudent.containsKey(name)) {
            gradesByStudent.get(name).add(grade);
            added = true;
        }
        return added;
    }

    public boolean removeStudent(String name) {
        boolean removed = false;
        if(gradesByStudent.containsKey(name)) {
            gradesByStudent.get(name).remove(0);
            removed = true;
        }
        return removed;
    }

    public Optional<Double> averageFor(String name) {
        if(gradesByStudent.containsKey(name)) {
            var grades = gradesByStudent.get(name);
            double average = 0.0;
            for(int g: grades){
                average += g;
            }
            average = average/grades.size();
            return Optional.of(average);
        }
        return Optional.empty();
    }

    public Optional<String> letterGradeFor(String name) {
        if(gradesByStudent.containsKey(name)&&!gradesByStudent.get(name).isEmpty()){
            var average = averageFor(name);
            String letterGrade = "";
            if(average.get()>89){
                letterGrade = "A";
            }else if(average.get()>79){
                letterGrade = "B";
            }else if(average.get()>69){
                letterGrade = "C";
            }else if(average.get()>59){
                letterGrade = "D";
            }else{
                letterGrade = "F";
            }
            return Optional.of(letterGrade);
        }
        return Optional.empty();
    }

    public Optional<Double> classAverage() {
        if(gradesByStudent.isEmpty()) return Optional.empty();
        var students = gradesByStudent.keySet().iterator();
        double average = 0.0;
        while(students.hasNext()){
            var current = students.next();
            if(averageFor(current).isPresent()){
                average += averageFor(current).get();
            }
        }
        average = average / gradesByStudent.size();
        return Optional.of(average);
    }

    public boolean undo() {
        if(undoStack.isEmpty()) return false;
        undoStack.pop();
        return true;
    }

    public List<String> recentLog(int maxItems) {
        throw new UnsupportedOperationException();
    }
}
