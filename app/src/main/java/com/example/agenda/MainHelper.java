package com.example.agenda;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainHelper {

    private String varDate;
    private DBController dbController;
    private ListHelper listHelper;
    private ArrayList<String> timeList, workList, idList;
    private Date date;
    private SimpleDateFormat sdt;
    private ArrayAdapter workAdapter;
    private ArrayAdapter timeAdapter;
    private Context context;
    private ListView workListView, timeListView;

    public MainHelper() {

        this.listHelper = new ListHelper();
        this.dbController = new DBController();
        this.timeList = new ArrayList<>();
        this.workList = new ArrayList<>();
        this.idList = new ArrayList<>();
        this.date = new Date();

    }

    public void currentDaySetup() {
        sdt = new SimpleDateFormat();
        sdt.applyPattern("dd-MM-yyyy");
        setVarDate(sdt.format(date));
    }

    public String nextDaySetup() {
        int todayDay = dbController.formatDate(varDate, 0,1,1,2);
        int todayMonth = dbController.formatDate(varDate, 3,4,4,5);
        int nextDay = 0;
        int nextMonth = 0;
        String year = varDate.substring(6,10);
        String tomorrowDay = "";
        String tomorrowMonth = "";
        String tomorrow = "";

        if (todayDay == 30 && (todayMonth == 4 || todayMonth == 6 || todayMonth == 9 || todayMonth == 11)){
            nextDay =  nextDay + 1;
            nextMonth = nextMonth + 1; //manca controllo sull'anno
        }
        else if (todayDay == 31 && (todayMonth == 1 || todayMonth == 3 || todayMonth == 5 || todayMonth == 7 || todayMonth ==8 || todayMonth == 10 ||
                todayMonth == 12)) {
            nextDay = nextDay + 1;
            nextMonth = todayMonth + 1;
        }
        else if (todayDay == 28 && todayMonth == 2) {
            nextDay = nextDay + 1;
            nextMonth = todayMonth + 1;
        }
        else {
            nextDay = todayDay + 1;
            nextMonth = todayMonth;
        }
        if (nextDay < 10) {
            tomorrowDay = "0" + nextDay;
        } else {
            tomorrowDay = tomorrowDay + nextDay;
        }

        if (nextMonth < 10) {
            tomorrowMonth = "0" + nextMonth;
        } else {
            tomorrowMonth = tomorrowMonth + nextMonth;
        }

        tomorrow = tomorrowDay + "-" + tomorrowMonth + "-" + year;

        return tomorrow;
    }

    public String formatDate(int day, int month, int year){
        String dayString = "" + day;
        String monthString = "" + (month+1);
        String stringDate;
        if(day < 10) {
            dayString = "0" + day;
        }
        if (month+1 < 10) {
            monthString = "0" + (month+1);
        }
        stringDate = dayString + "-" + monthString + "-" + year;
        return stringDate;
    }

    public void clickToRemoveJob(int index) {
        listHelper.clickToRemoveJob(workList, timeList, idList, workAdapter, timeAdapter, varDate, index );
    }

    public void deletePreviousAgenda() {
        dbController.deletePreviousAgenda(varDate);
    }

    public void listLoading(DataSnapshot snapshot) {
        if (snapshot.getValue() != null) {
            String key = snapshot.getValue().toString();
            int index = key.indexOf("-");
            int count = 0;
            while (count != key.length() - 2) {
                if (index > 0) {
                    count = key.indexOf("}", index);
                    String id = key.substring(index, index + 20);
                    index = key.indexOf("-", index + 20);
                    String value = snapshot.child(id).getValue(String.class);
                    String timeVal = value.substring(value.length() - 5, value.length());
                    String workVal = value.substring(0, value.length() - 6);
                    loadList(workVal, timeVal, id);
                } else {
                    count = key.length() - 2;
                }

            }
        }
    }

    public void listLoadingRefresh(DataSnapshot snapshot) {
        if (snapshot.getValue() == null) {
            refreshList();
            timeAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, timeList);
            workAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, workList);
            workListView.setAdapter(workAdapter);
            timeListView.setAdapter(timeAdapter);
        }
    }

    public void loadList(String workValue, String timeValue, String id) {
        if (check(idList, id)) {
            sortList(timeList, timeValue, workList, workValue, idList, id);
        }
        timeAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, timeList);
        workAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, workList);
        workListView.setAdapter(workAdapter);
        timeListView.setAdapter(timeAdapter);
            }

    public boolean check(ArrayList<String> arrayList, String compareValue) {
        boolean result = true;
        for(int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(compareValue)) {
                result = false;
                return result;
            }
            else {
                result = true;
            }
        }
        return result;
    }

    public void sortList(ArrayList<String> arrayListTime, String valueTime, ArrayList<String> arrayListWork, String valueWork, ArrayList<String> arrayListId, String valueId) {
        boolean check = true;
        if (arrayListTime.size() > 0) {
            for (int i = 0; i < arrayListTime.size(); i++) {
                int hour = formatTime(arrayListTime.get(i), 0, 1, 1, 2);
                int minute = formatTime(arrayListTime.get(i), 3, 4, 4, 5);
                int compareHour = formatTime(valueTime, 0, 1, 1, 2);
                int compareMinute = formatTime(valueTime, 3, 4, 4, 5);
                if (compareHour < hour || (compareMinute < minute) && (compareHour <= hour)) {
                    listAdd(arrayListTime, i, valueTime);
                    listAdd(arrayListWork, i, valueWork);
                    listAdd(arrayListId, i, valueId);
                    check = false;
                    break;
                }
            }
        }
        if (check == true) {
            arrayListTime.add(valueTime);
            arrayListWork.add(valueWork);
            arrayListId.add(valueId);
        }
    }

    public void listAdd(ArrayList<String> arrayList, int index, String content) {
        String nextContent = arrayList.get(index);
        arrayList.remove(index);
        arrayList.add(index, content);
        arrayList.add(index+1, nextContent);
    }

    public int formatTime(String time, int param1, int param2, int param3, int param4) {
        int firstParam = Integer.parseInt(time.substring(param1,param2));
        int secondParam = Integer.parseInt(time.substring(param3,param4));
        switch(firstParam) {
            case 1: firstParam = 10; break;
            case 2: firstParam = 20; break;
            case 3: firstParam = 30; break;
            case 4: firstParam = 40; break;
            case 5: firstParam = 50; break;
        }
        int result = firstParam + secondParam;
        return result;
    }

    public void refreshList() {
        this.timeList = new ArrayList<>();
        this.workList = new ArrayList<>();
        this.idList = new ArrayList<>();
    }

    public String getVarDate() {
        return varDate;
    }

    public void setVarDate(String varDate) {
        this.varDate = varDate;
    }

    public DBController getDbController() {
        return dbController;
    }

    public void setDbController(DBController dbController) {
        this.dbController = dbController;
    }

    public ListHelper getListHelper() {
        return listHelper;
    }

    public void setListHelper(ListHelper listHelper) {
        this.listHelper = listHelper;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<String> timeList) {
        this.timeList = timeList;
    }

    public ArrayList<String> getWorkList() {
        return workList;
    }

    public void setWorkList(ArrayList<String> workList) {
        this.workList = workList;
    }

    public ArrayList<String> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<String> idList) {
        this.idList = idList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SimpleDateFormat getSdt() {
        return sdt;
    }

    public void setSdt(SimpleDateFormat sdt) {
        this.sdt = sdt;
    }

    public ArrayAdapter getWorkAdapter() {
        return workAdapter;
    }

    public void setWorkAdapter(ArrayAdapter workAdapter) {
        this.workAdapter = workAdapter;
    }

    public ArrayAdapter getTimeAdapter() {
        return timeAdapter;
    }

    public void setTimeAdapter(ArrayAdapter timeAdapter) {
        this.timeAdapter = timeAdapter;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ListView getWorkListView() {
        return workListView;
    }

    public void setWorkListView(ListView workListView) {
        this.workListView = workListView;
    }

    public ListView getTimeListView() {
        return timeListView;
    }

    public void setTimeListView(ListView timeListView) {
        this.timeListView = timeListView;
    }
}
