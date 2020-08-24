package com.example.javaprojectweb;

import java.sql.SQLException;
import java.util.*;

public class Controller {
   public static DBAccess myDb = new DBAccess();

    public static void invokeThreads(int taskCode, status taskStatus){
    UpdateStatus updateStatus=new UpdateStatus(taskCode,taskStatus);
    CheckStatus checkStatus=new CheckStatus(taskCode,taskStatus);
    updateStatus.start();
    checkStatus.start();
}
public static String existTeam(String name, String mail) throws ClassNotFoundException {
    List<TeamMember> TeamList=new ArrayList<TeamMember>();
        TeamList=myDb.getAllTeamMember();
    Map<String, String> TeamMap=new HashMap<String, String>();
    for(TeamMember t:myDb.TeamList)
    {
        TeamMap.put(t.getMemberName(),t.getMemberMail());
    }
    String getMAil=TeamMap.get(name);
    if(getMAil.equals(mail)){
        return "exist team member";
    }
    else return "the member team isn't exist";

}
public static List<Tasks> getAllTasksByMemberCode(int TeamCode) throws ClassNotFoundException{


    //get all tasks by member code(A)
    System.out.println("Enter Team Code to get your tasks list");

   return myDb.getAllTasksByMemberCode(TeamCode);
}
    public static List<TeamMember> getallTeamMember() throws ClassNotFoundException {
        List<TeamMember> TeamList = new ArrayList<TeamMember>();
        TeamList= myDb.getAllTeamMember();
        Collections.sort(TeamList,TeamMember::compareTo);
        return TeamList;

    }
    public static void initial() throws SQLException, ClassNotFoundException {
        myDb.initializing();
    }
}
