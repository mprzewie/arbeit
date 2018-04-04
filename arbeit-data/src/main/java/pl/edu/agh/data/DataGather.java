package pl.edu.agh.data;

import java.util.LinkedList;
import java.util.List;

public class DataGather {
    private List<Record> recordList;

    public DataGather(List<Record> recordList) {
        this.recordList = new LinkedList<Record>();
    }

    public void addRecord(Record record) {
        recordList.add(record);
    }
}
