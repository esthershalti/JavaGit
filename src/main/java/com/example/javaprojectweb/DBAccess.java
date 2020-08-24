package com.example.javaprojectweb;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DBAccess {
    public static List<TeamMember> TeamList = new ArrayList<TeamMember>();
    public static List<Tasks> TasksList = new ArrayList<Tasks>();
    public static List<Implements> implementsList=new ArrayList<Implements>();
    static final String DB_URL = "jdbc:sqlserver://(LocalDb)\\MSSQLLocalDB;DatabaseName=JavaWebDB";
    static final String USER = "DESKTOP-T3AHI49\\win10";
    static final String PASS = "";


    public static List<Tasks> getAllTasks() throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("Connecting to a selected database...");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement sta = conn.createStatement();
            String Sql = "select * from Tasks";
            ResultSet rs = sta.executeQuery(Sql);
            String strSt = rs.getString("taskStatus");
            status s = status.valueOf(strSt);
            List<Tasks> t = new ArrayList<Tasks>();
            while (rs.next()) {
                int mCode = rs.getInt("MemberCode");
                TeamMember x = new TeamMember();
                x.setMemberCode(
                        (Integer) mCode
                );
                t.add(new Tasks(rs.getInt("TaskCode"), rs.getString("TaskTitle"), rs.getString("TaskDescription"),
                        x, rs.getInt("TaskEstimatedTime"), rs.getDate("StartDate"),
                        rs.getDate("EndDate"), s,rs.getLong("Rating")));
            }
            return t;
        } catch (Exception e) {
            System.out.println("cannot connect to sql");
            return TasksList;
        }
    }

    public static List<Tasks> getAllTasksByMemberCode(int MemberCode) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("Connecting to a selected database...");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement sta = conn.createStatement();
            PreparedStatement statement = conn.prepareStatement("select * from Tasks where memberCode = ?");
            statement.setInt(1, MemberCode);
            ResultSet rs = statement.executeQuery();
            String strSt = rs.getString("taskStatus");
            status s = status.valueOf(strSt);
            List<Tasks> t = new ArrayList<Tasks>();
            while (rs.next()) {
                int mCode = rs.getInt("MemberCode");
                TeamMember x = new TeamMember();
                x.setMemberCode(
                        (Integer) mCode
                );
                t.add(new Tasks(rs.getInt("TaskCode"), rs.getString("TaskTitle"), rs.getString("TaskDescription"),
                        x, rs.getInt("TaskEstimatedTime"), rs.getDate("StartDate"),
                        rs.getDate("EndDate"), s,rs.getLong("Rating")));
            }
            return t;
        } catch (Exception e) {
            List<Tasks> TaskL = new ArrayList<Tasks>();
            System.out.println("cannot connect to sql");
            for (Tasks t : TasksList) {
                if (t.getBelongTo().getMemberCode() == MemberCode) {
                    TaskL.add(t);
                }
            }
            return TaskL;

        }
    }

    public static void addTask(Tasks taskToAdd) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("Connecting to a selected database...");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement sta = conn.createStatement();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Tasks values(?,?,?,?,?,?,?) ");
            statement.setInt(1, taskToAdd.getTaskCode());
            statement.setString(2, taskToAdd.getTaskTitle());
            statement.setString(3, taskToAdd.getTaskDescription());
            statement.setInt(4, taskToAdd.getBelongTo().getMemberCode());
            statement.setInt(5, taskToAdd.getTaskEstimatedTime());
            statement.setDate(6, (java.sql.Date) (Date) taskToAdd.getStartDate());
            statement.setDate(7, (java.sql.Date) (Date) taskToAdd.getEndDate());
            ResultSet rs = statement.executeQuery();
            TasksList.add(taskToAdd);
        } catch (Exception e) {
            System.out.println("cannot connect to sql and insert");
            TasksList.add(taskToAdd);
        }
    }
    public static Long updateStatus(int taskCode, status taskStatus) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("Connecting to a selected database...");
        Date now=new Date();
        Date endDate = new Date();
        if (taskStatus == status.done) {
            for (Tasks t : TasksList) {
                if (t.getTaskCode() == taskCode) {
                    endDate = (Date) t.getEndDate();
                    break;
                }
            }
            long diff = now.getTime() - endDate.getTime() / (1000 * 60 * 60 * 24);
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement statement = conn.prepareStatement("UPDATE Tasks SET taskStatus = ?,rating=? WHERE taskCode = ?");
                statement.setString(1, taskStatus.toString());
                statement.setLong(2, diff);
                statement.setInt(3, taskCode);
                return diff;

            } catch (Exception e) {
                System.out.println("cannot connect to sql");
                for (Tasks t : TasksList) {
                    if (t.getTaskCode() == taskCode) {
                        t.setRating(diff);
                        t.setTaskStatus(status.done);
                        break;
                    }
                }
                return diff;
            }
        } else {
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement statement = conn.prepareStatement("UPDATE Tasks SET taskStatus = ? WHERE taskCode = ?");
                statement.setString(1, taskStatus.toString());
                statement.setInt(2, taskCode);
                return null;
            } catch (Exception e) {
                System.out.println("cannot connect to sql and update status");
                for (Tasks t : TasksList) {
                    if (t.getTaskCode() == taskCode) {
                        t.setTaskStatus(taskStatus);
                        break;
                    }
                }
                return null;
            }
        }
    }
    public static List<TeamMember> getAllTeamMember() throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("Connecting to a selected database...");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement sta = conn.createStatement();
            String Sql = "select * from TeamMember";
            ResultSet rs = sta.executeQuery(Sql);
            List<TeamMember> t = new ArrayList<TeamMember>();
            while (rs.next()) {
                String strSt = rs.getString("taskStatus");
                status s = status.valueOf(strSt);
                int mCode = rs.getInt("MemberCode");
                TeamMember x = new TeamMember();
                x.setMemberCode((Integer) mCode);
                t.add(new TeamMember(rs.getInt("memberCode"),rs.getString("memberName"),rs.getString("memberMail")));
            }
            return t;

        } catch (Exception e) {
            System.out.println("cannot connect to sql");
            return TeamList;
        }

    }
    public static void initializing() throws ClassNotFoundException, SQLException {
        TasksList.add(new Tasks(1, "aaa", "12", new TeamMember(111, "lll", "sdssd@gmail.com"), 5, new Date(11 / 12 / 2200), new Date(11 / 12 / 2200), status.New,null));
        TasksList.add(new Tasks(2, "bbb", "23", new TeamMember(222, "nn", "sdssd@gmail.com"), 5, new Date(11 / 12 / 2200), new Date(11 / 12 / 2200), status.done,null));
        TasksList.add(new Tasks(3, "ccc", "34", new TeamMember(333, "g", "sdssd@gmail.com"), 5, new Date(11 / 12 / 2200), new Date(11 / 12 / 2200), status.inProgress,null));
        TasksList.add(new Tasks(4, "ddd", "45", new TeamMember(444, "re", "sdssd@gmail.com"), 5, new Date(11 / 12 / 2200), new Date(11 / 12 / 2200), status.inProgress,null));
        TasksList.add(new Tasks(5, "eee", "56", new TeamMember(555, "w3", "sdssd@gmail.com"), 5, new Date(11 / 12 / 2200), new Date(11 / 12 / 2200), status.done,null));
        TeamList.add(new TeamMember(111, "yosef", "sdssd@gmail.com"));
        TeamList.add(new TeamMember(222, "israel", "rerer@gmail.com"));
        TeamList.add(new TeamMember(333, "simon", "erer@gmail.com"));
        TeamList.add(new TeamMember(444, "izchak", "erere@gmail.com"));
        //        System.out.println( getAllTasks());
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("Connecting to a selected database...");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement sta = conn.createStatement();
            String Sql1 = "select * from Tasks";
            String Sql2 = "select * from TeamMember";

            ResultSet rs1 = sta.executeQuery(Sql1);
            ResultSet rs2 = sta.executeQuery(Sql2);
            String strSt = rs1.getString("taskStatus");
            status s = status.valueOf(strSt);
            while (rs1.next()) {
                int mCode = rs1.getInt("MemberCode");
                TeamMember x1 = new TeamMember();
                x1.setMemberCode(
                        (Integer) mCode
                );
                TasksList.add(new Tasks(rs1.getInt("TaskCode"), rs1.getString("TaskTitle"), rs1.getString("TaskDescription"),
                        x1, rs1.getInt("TaskEstimatedTime"), rs1.getDate("StartDate"),
                        rs1.getDate("EndDate"), s, rs1.getLong("Rating")));
            }
            while (rs2.next()) {
                TeamList.add(new TeamMember(rs2.getInt("memberCode"), rs2.getString("memberName"), rs2.getString("memberMail")));
            }

        } catch (Exception e) {
            System.out.println("cannot connect to sql");

        }
//        addTask(new Tasks(0, "first", "bla", new TeamMember(111, "lll", "sdssd@gmail.com"), 5, new Date(11 / 12 / 2200), new Date(11 / 12 / 2200), status.done));
//
//        for (Tasks t : TasksList) {
//            System.out.println(t);
//        }
//        updateStatus(1, status.done);
//        for (Tasks t : TasksList) {
//            System.out.println(t);
//        }

    }
}



