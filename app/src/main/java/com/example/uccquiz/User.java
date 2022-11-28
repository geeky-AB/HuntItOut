package com.example.uccquiz;

public class User {
    private String email;
    private String name;
    private String teamID;
    private int pathNo;
    private String status;
    private int progCount;
    private String phn;
    private int wrongCnt;
    private int wrongCountOneStage;
    private String disqualified;
    private int phaseNo;

    public User(String email, String name, String teamID, int pathNo, String status, int progCount, String phn, int wrongCnt, int wrongCountOneStage, String disqualified, int phaseNo) {
        this.email = email;
        this.name = name;
        this.teamID = teamID;
        this.pathNo = pathNo;
        this.status = status;
        this.progCount = progCount;
        this.phn = phn;
        this.wrongCnt = wrongCnt;
        this.wrongCountOneStage = wrongCountOneStage;
        this.disqualified = disqualified;
        this.phaseNo = phaseNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public User(int pathNo) {
        this.pathNo = pathNo;
    }

    public void setPathNo(int pathNo) {
        this.pathNo = pathNo;
    }

    public int getPathNo() { return pathNo; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public int getProgCount() { return progCount; }

    public void setProgCount(int progCount) { this.progCount = progCount; }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public int getWrongCnt() {
        return wrongCnt;
    }

    public void setWrongCnt(int wrongCnt) {
        this.wrongCnt = wrongCnt;
    }

    public int getWrongCountOneStage() {
        return wrongCountOneStage;
    }

    public void setWrongCountOneStage(int wrongCountOneStage) {
        this.wrongCountOneStage = wrongCountOneStage;
    }

    public String getDisqualified() {
        return disqualified;
    }

    public void setDisqualified(String disqualified) {
        this.disqualified = disqualified;
    }

    public int getPhaseNo() {
        return phaseNo;
    }

    public void setPhaseNo(int phaseNo) {
        this.phaseNo = phaseNo;
    }

    public User(){

    }
}
