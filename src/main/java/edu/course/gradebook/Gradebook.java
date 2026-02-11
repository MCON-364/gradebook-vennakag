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
            activityLog.push("Added student " + name);
        }
        return added;
    }

    public boolean addGrade(String name, int grade) {
        boolean added = false;
        if(gradesByStudent.containsKey(name)) {
            gradesByStudent.get(name).add(grade);
            added = true;
            activityLog.push("Added grade " + grade + " for student " + name);
            undoStack.push(new UndoAction() {
                @Override
                public void undo(Gradebook gradebook) {
                    var grades = gradesByStudent.get(name);
                    if (grades != null && !grades.isEmpty()) {
                        grades.remove(grades.size() - 1); // remove last added grade
                    }
                    activityLog.push("Undo add grade " + grade + " for student " + name);
                }
            });
        }
        return added;
    }

    public boolean removeStudent(String name) {
        boolean removed = false;
        if(gradesByStudent.containsKey(name)) {
            List<Integer> grades = gradesByStudent.get(name);
            gradesByStudent.remove(name);
            removed = true;
            activityLog.push("Removed student " + name);
            undoStack.push(new UndoAction() {
                @Override
                public void undo(Gradebook gradebook) {
                    gradesByStudent.put(name, grades);
                    activityLog.push("Undo remove student " + name);
                }
            });
        }
        return removed;
    }

    public Optional<Double> averageFor(String name) {
        if(gradesByStudent.containsKey(name)) {
            if(gradesByStudent.get(name).isEmpty()) {
                return Optional.empty();
            }
            var grades = gradesByStudent.get(name);
            double average = 0.0;
            for(int g: grades){
                average += g;
            }
            average = average/grades.size();
            activityLog.push("Retrieved average for student " + name);
            return Optional.of(average);
        }
        return Optional.empty();
    }

    public Optional<String> letterGradeFor(String name) {
        if(gradesByStudent.containsKey(name)&&!gradesByStudent.get(name).isEmpty()){
            var average = averageFor(name);
            String letterGrade = switch ((int) (average.get() / 10)) {
                case 10, 9 -> "A";
                case 8 -> "B";
                case 7 -> "C";
                case 6 -> "D";
                default -> "F";
            };
            activityLog.push("Retrieved letter grade for student " + name);
            return Optional.of(letterGrade);
        }
        return Optional.empty();
    }

    public Optional<Double> classAverage() {
        if(gradesByStudent.isEmpty()) return Optional.empty();
        var students = gradesByStudent.keySet().iterator();
        double average = 0.0;
        int gradeCount = 0;
        while(students.hasNext()){
            var current = students.next();
            var grades = gradesByStudent.get(current);
            for(var grade : grades){
                average += grade;
                gradeCount++;
            }
        }
        if (gradeCount == 0) return Optional.empty();
        average = average / gradeCount;
        activityLog.push("Retrieved average for class average");
        return Optional.of(average);
    }

    public boolean undo() {
        if(undoStack.isEmpty()) return false;
        UndoAction curr = undoStack.pop();
        curr.undo(this);
        return true;
    }

    public List<String> recentLog(int maxItems) {
        if(activityLog.isEmpty()) return new LinkedList<>();
        List<String> logs = new LinkedList<>();
        int items = Math.min(maxItems, activityLog.size());
        for(int i = activityLog.size()-1; i > activityLog.size() - items; i--) {
            logs.add(activityLog.get(i));
        }
        return logs;
    }
}
