import edu.course.gradebook.Gradebook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GradebookTest {

    static Gradebook gradebook;

    @BeforeEach
    void setup(){
        gradebook = new Gradebook();
        gradebook.addStudent("Sarah");
        gradebook.addStudent("Devorah");
        gradebook.addStudent("Juliette");
        gradebook.addGrade("Sarah", 90);
        gradebook.addGrade("Sarah", 87);
        gradebook.addGrade("Devorah", 85);
        gradebook.addGrade("Devorah", 83);
        gradebook.addGrade("Juliette", 62);
        gradebook.addGrade("Juliette", 70);
    }

    @Test
    void testFindStudentGrades(){
        var grades = gradebook.findStudentGrades("Sarah");
        assertNotNull(grades);
        assertEquals(2, grades.get().size());
    }

    @Test
    void testFindStudentGradesForInvalidStudent(){

        assertTrue(gradebook.findStudentGrades("Elisheva").isEmpty());
    }

    @Test
    void testAddStudent(){
        assertFalse(gradebook.addStudent("Sarah"));
        assertTrue(gradebook.addStudent("Tanya"));
        assertEquals(0, gradebook.findStudentGrades("Tanya").get().size());
    }

    @Test
    void testAddGrade(){
        assertTrue(gradebook.addGrade("Sarah", 95));
        assertFalse(gradebook.addGrade("Paula", 90));
        assertTrue(gradebook.findStudentGrades("Sarah").get().contains(95));
    }

    @Test
    void testRemoveStudent(){
        gradebook.addStudent("Paula");
        assertTrue(gradebook.removeStudent("Paula"));
        assertFalse(gradebook.removeStudent("Hanna"));
    }

    @Test
    void testAverageFor(){
        assertEquals(88.5, gradebook.averageFor("Sarah").get());
        assertTrue(gradebook.averageFor("Paula").isEmpty());
    }

    @Test
    void testLetterGradeFor(){
        assertEquals("B",  gradebook.letterGradeFor("Sarah").get());
        assertEquals("D", gradebook.letterGradeFor("Juliette").get());
        assertTrue(gradebook.letterGradeFor("Paula").isEmpty());
    }

    @Test
    void testClassAverage(){
        assertEquals(79.5, gradebook.classAverage().get());
    }

    @Test
    void testUndo(){
        gradebook.addGrade("Sarah", 70);
        gradebook.undo();
        assertEquals("B",  gradebook.letterGradeFor("Sarah").get());
    }

}
