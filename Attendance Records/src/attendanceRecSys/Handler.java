/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package attendanceRecSys;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Shawn
 */
public class Handler {
    Roster roster;
    AttendanceRecord record;
    public Handler(String classNumber, String date){
        this.roster=new Roster(classNumber);
        this.record=new AttendanceRecord(classNumber,date);
    }
    
    public HashMap<String,Boolean> getRosterRecord(){
        HashMap<String,Boolean> map=new HashMap<String,Boolean>();
        
        ArrayList<Student> attendedStudents=this.roster.getStudents();
        ArrayList<Student> missingStudents=this.record.getMissingStudents();
        
        for(int i=0;i<attendedStudents.size();i++)
            map.put(attendedStudents.get(i).getName(),true);
        
        for(int j=0;j<missingStudents.size();j++)
            if(map.containsKey(missingStudents.get(j).getName()))
                map.put(missingStudents.get(j).getName(), false);
            
        return map;
    }
    
    public Roster getRoster(){
        return roster;
    }
    
    public AttendanceRecord getRecord(){
        return record;
    }
}
