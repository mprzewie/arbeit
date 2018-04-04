package pl.edu.agh.data;

import java.util.Date;

public class Record {
    private String ProcessName;
    private Date startDate;
    private Date endDate;

    public Record(String processName, Date startDate, Date endDate) {
        ProcessName = processName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
