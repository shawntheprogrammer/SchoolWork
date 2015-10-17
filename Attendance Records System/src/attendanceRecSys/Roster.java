/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package attendanceRecSys;

import java.util.ArrayList;

/**
 *
 * @author Shawn
 */
public class Roster {
    
    private ArrayList<Student> students;
    private String classNumber;
    
    public Roster(String classNumber){
        this.classNumber=classNumber;
        students=new ArrayList<Student>();
        setStudents(FileAdapter.getRosterFile(classNumber));
    }
    
    private void setStudents(ArrayList<String> names){
        for(int i=0;i<names.size();i++)
            students.add(new Student(names.get(i)));
    }
    
    public ArrayList<Student> getStudents(){
        return this.students;
    }
}
