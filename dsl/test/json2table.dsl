def TableData = '''
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
'''

project "Test", {
	procedure "Test - json2table",{
		step 'Generate Report',
			subproject : '/plugins/EF-ReportingTools/project',
			subprocedure : 'json2table',
			actualParameter : [
				jsonData: TableData,
				reportName: "Test Report", 
				columnOrnamentation: ""
			]
	}

	pipeline "Test - json2table",{
		stage "Test", {
			task "Test",
				taskType : 'PLUGIN',
				subpluginKey : 'EF-ReportingTools',
				subprocedure : 'json2table',
				actualParameter : [
					jsonData: TableData,
					reportName: "Test Report", 
					columnOrnamentation: ""
				]
		}
	}
}
