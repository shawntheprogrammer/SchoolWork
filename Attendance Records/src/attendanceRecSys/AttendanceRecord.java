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
public class AttendanceRecord {
    private String classNumber;
    private String date;
    private ArrayList<Student> missingStudents;
    public AttendanceRecord(String classNumber,String date){
        this.classNumber=classNumber;
        this.date=date;
        missingStudents=new ArrayList<Student>();
        setMissingStudents();
    }
    
    private void setMissingStudents(){
        ArrayList<String> students=FileAdapter.getRecordFile(classNumber,date);
        for(int i=0;i<students.size();i++)
            missingStudents.add(new Student(students.get(i)));
    }
    
    public ArrayList<Student> getMissingStudents(){
        return this.missingStudents;
    }
}
