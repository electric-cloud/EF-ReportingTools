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
		},
		{
			"Text": "<b>Bold</b>",
			"Image": "<img src=\\\\"../../../flow/public/app/assets/img/svg-icons/icon-nodes.svg\\\\" height=\\\\"20\\\\" width=\\\\"20\\\\">",
			"Link": "<a href=\\\\"../..\\\\">Commander UI</a>"
		}
	]
'''

project "Test", {
	procedure "Test - json2table", resourceName: 'local', {
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
