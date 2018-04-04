def TimelineData = '''
	[
		{
			"resource": "First",
			"startDate":  "2018/4/10",
			"endDate": "2018/5/1"
		},
		{
			"resource": "Second",
			"startDate":  "2018/4/20",
			"endDate": "2018/5/15"
		},
		{
			"resource": "Second",
			"startDate":  "2018/5/20",
			"endDate": "2018/5/30"
		},		{
			"resource": "Third",
			"startDate":  "2018/5/5",
			"endDate": "2018/5/8"
		}
	]
'''


project "Test", {
	procedure "Test - Timeline",{
		step 'name',
			subproject : '/plugins/EF-ReportingTools/project',
			subprocedure : 'Timeline',
			actualParameter : [
				jsonData: TimelineData,
				reportName: "Test Timeline"
			]
	}

	pipeline "Test - Timeline",{
		stage "Test", {
			task "Test",
				taskType : 'PLUGIN',
				subpluginKey : 'EF-ReportingTools',
				subprocedure : 'Timeline',
				actualParameter : [
					jsonData: TimelineData,
					reportName: "Test Timeline"
				]
		}
	}
}
