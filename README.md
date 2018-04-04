## EC-ReportingTools
Electric Flow Procedures to create reports.  Report links are attached to the job and the pipeline if present.

## Procedures
### json2table
Create a table from a JSON string:
```JSON
[
	{
		"col1": "123",
		"col2": "456",
	},
	{
		"col1": "789",
		"col2": "012",
		"col3": "345"
	}
]
```

col1 | col2 | col3
-----|------|-----
123|456
789|012|345

![Sample Table](/pages/images/Table Sample.PNG)

#### Parameters
jsonData: required, string, JSON array with one or more key-value pairs for each array element
reportName: optional, string, report name to be used as file name as well

### Gantt Schedule
jsonData: required, string, JSON array with items defined by taskName, startDate, and endData.
```JSON
[
	{
		"taskName": "First Task",
		"startDate":  "2018/4/10",
		"endDate": "2018/5/1"
	},
	{
		"taskName": "Second Task",
		"startDate":  "2018/4/20",
		"endDate": "2018/5/15"
	},
	{
		"taskName": "Third Task",
		"startDate":  "2018/5/5",
		"endDate": "2018/5/8"
	}
]
```
ganttName: optional, string, Gantt Chart name to be used as file name as well

![Sample Gantt Chart](/pages/images/Gantt Chart Sample.PNG)
