/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceRecSys;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Shawn
 */
public class FileAdapter {

    public static ArrayList<String> getRosterFile(String classNumber) {
        return readRosterFile(classNumber);
    }

    public static ArrayList<String> getRecordFile(String classNumber, String date) {
        return readRecordFile(classNumber,date);
    }

    private static ArrayList<String> readRosterFile(String classNumber) {
        //Input file which needs to be parsed
        String fileToParse = "C:/Users/Shawn/Desktop/OOAD_Project_Files/ClassRoster-" + classNumber + ".csv";
        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        ArrayList<String> s = new ArrayList<>();
        String line = "";
        //Create the file reader
        try {
            fileReader = new BufferedReader(new FileReader(fileToParse));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //Read the file line by line
        try {
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);
                for (String token : tokens) {
                    //Print all tokens
                    s.add(token);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return s;

    }

    private static ArrayList<String> readRecordFile(String classNumber, String date) {
        //Input file which needs to be parsed  attendRec001-2014_02_11
        String FormattedDate = get_date(date);
        String fileToParse = "C:/Users/Shawn/Desktop/OOAD_Project_Files/attendRec" + classNumber + "-" + FormattedDate + ".csv";
        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        ArrayList<String> s = new ArrayList<>();
        String line = "";
        //Create the file reader
        try {
            fileReader = new BufferedReader(new FileReader(fileToParse));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //Read the file line by line
        try {
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);
                for (int i = 2; i < tokens.length; i++) {
                    //Print all tokens
                    s.add(tokens[i]);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return s;

    }

    private static String get_date(String date) {
        // 4/13/2014 --- 2014_04_13
        String month;
        String day;
        String year;
        String[] tokens = date.split("/");
        return tokens[2] + "_" + tokens[0] + "_" + tokens[1];
    }

}
