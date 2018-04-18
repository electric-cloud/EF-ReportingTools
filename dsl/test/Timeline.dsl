def TimelineData = '''
	[
		{
			"resource": "Resource One",
			"label": "First",
			"startDate":  "2018/4/10",
			"endDate": "2018/5/1"
		},
		{
			"resource": "Resource Two",
			"label": "First",
			"startDate":  "2018/4/20",
			"endDate": "2018/5/15"
		},
		{
			"resource": "Resource Two",
			"label": "Second",
			"startDate":  "2018/5/20",
			"endDate": "2018/5/30"
		},		{
			"resource": "Resource Three",
			"label": "First",
			"startDate":  "2018/5/5",
			"endDate": "2018/5/8"
		}
	]
'''


project "Test", {
	procedure "Test - Timeline", resourceName: 'local', {
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
