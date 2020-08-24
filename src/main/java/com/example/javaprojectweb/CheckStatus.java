package com.example.javaprojectweb;

public class CheckStatus extends Thread {

    private int taskCode;
    private status taskStatus;

    public CheckStatus(int taskCode, status taskStatus) {
        this.taskCode = taskCode;
        this.taskStatus = taskStatus;
    }

    public int getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(int taskCode) {
        this.taskCode = taskCode;
    }

    public status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(status taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public void run() {
        DBAccess myDb=new DBAccess();
        try {
            myDb.updateStatus(taskCode,taskStatus);
            System.out.println("check status");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
