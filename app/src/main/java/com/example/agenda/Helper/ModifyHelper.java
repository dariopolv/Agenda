package com.example.agenda.Helper;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.HashMap;

public class ModifyHelper {

    private ArrayList<String> workList, timeList, noteList, idList;
    private String date;
    private String modifiedWork, modifiedTime, modifiedNote;
    DatabaseReference databaseReference;


    public ModifyHelper() {
       this.workList = new ArrayList<>();
       this.timeList = new ArrayList<>();
       this.noteList = new ArrayList<>();
       this.idList = new ArrayList<>();
       this.date = "";
       this.modifiedWork = "";
       this.modifiedTime = "";
       this.modifiedNote = "";
    }

    public void setModifiedActivity(String modifiedWork, String modifiedTime, String modifiedNote) {
        setModifiedWork(modifiedWork);
        setModifiedTime(modifiedTime);
        setModifiedNote(modifiedNote);
    }


    public void setAllLists(ArrayList<String> workList, ArrayList<String> timeList, ArrayList<String> idList, ArrayList<String> noteList) {
        this.workList = workList;
        this.timeList = timeList;
        this.idList = idList;
        this.noteList = noteList;
    }



    public void applyChanges(int index) {
        HashMap hashMap = new HashMap();
        hashMap.put(idList.get(index), modifiedWork + " " + modifiedTime + "/" + modifiedNote);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("agenda");
        databaseReference.child(date).updateChildren(hashMap);

    }

    public ArrayList<String> getWorkList() {
        return workList;
    }

    public void setWorkList(ArrayList<String> workList) {
        this.workList = workList;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<String> timeList) {
        this.timeList = timeList;
    }

    public ArrayList<String> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<String> noteList) {
        this.noteList = noteList;
    }

    public ArrayList<String> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<String> idList) {
        this.idList = idList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModifiedWork() {
        return modifiedWork;
    }

    public void setModifiedWork(String modifiedWork) {
        this.modifiedWork = modifiedWork;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifiedNote() {
        return modifiedNote;
    }

    public void setModifiedNote(String modifiedNote) {
        this.modifiedNote = modifiedNote;
    }
}
