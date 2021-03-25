package Database;

import java.util.LinkedHashMap;

// https://stackoverflow.com/questions/21815035/which-is-better-to-use-among-arraylist-and-linkedhashmap-for-better-speed-if-key
// "As far as performance is considered go for LinkedHashMap as get(), put(), remove(). conyainsKey() all are O(1) operations and your insertion order is retained.
// If you use ArrayList it will be O(N)."

public class Record
{
    private LinkedHashMap<String, String> records;

    public Record ()
    {
        records = new LinkedHashMap<String, String>();
        //records.getOrDefault()
    }

    public void addToRecords(String columnName, String columnData)
    {
        records.put(columnName, columnData);
    }

    public String getColumnData(String columnName)
    {
        return records.get(columnName);
    }

    public void removeRecords(String columnName)
    {
        records.remove(columnName);
    }
}
