def ScheduleData = '''
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
'''


project "Test", {
	procedure "Test - Gantt Schedule",{
		step 'name',
			subproject : '/plugins/EF-ReportingTools/project',
			subprocedure : 'Gantt Schedule',
			actualParameter : [
				jsonData: ScheduleData,
				ganttName: "Test Schedule"
			]
	}

	pipeline "Test - Gantt Schedule",{
		stage "Test", {
			task "Test",
				taskType : 'PLUGIN',
				subpluginKey : 'EF-ReportingTools',
				subprocedure : 'Gantt Schedule',
				actualParameter : [
					jsonData: ScheduleData,
					ganttName: "Test Schedule"
				]
		}
	}
}
