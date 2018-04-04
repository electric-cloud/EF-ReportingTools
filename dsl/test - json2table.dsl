project "Test", {
	procedure "Test - json2table",{
		step 'name',
			subproject : '/plugins/EF-ReportingTools/project',
			subprocedure : 'json2table',
			actualParameter : [
				jsonData: '''
					[
					  {
						"Service Name": "carts",
						"Build Output Type": "Docker Image",
						"CI Type": "Jenkins Job",
						"Build Number": 47,
						"GitHub Repository": "elektrikTomcat/carts",
						"Build Artifact": "731977058523.dkr.ecr.us-east-2.amazonaws.com/carts:jenkinsTest-47",
						"Unit Test Type": "Junit",
						"Unit Test Result": "PASS"
					  },
					  {
						"Service Name": "catalogue",
						"Build Output Type": "Docker Image",
						"CI Type": "Jenkins Job",
						"Build Number": 33,
						"GitHub Repository": "elektrikTomcat/catalogue",
						"Build Artifact": "731977058523.dkr.ecr.us-east-2.amazonaws.com/catalogue:jenkinsTest-33",
						"Unit Test Type": "Script",
						"Unit Test Result": "PASS"
					  },
					  {
						"Service Name": "front-end",
						"Build Output Type": "Docker Image",
						"CI Type": "Jenkins Job",
						"Build Number": 32,
						"GitHub Repository": "elektrikTomcat/front-end",
						"Build Artifact": "731977058523.dkr.ecr.us-east-2.amazonaws.com/catalogue:jenkinsTest-32",
						"Unit Test Type": "Script",
						"Unit Test Result": "FAIL"
					  }
					]
					''',
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
					jsonData: '''
						[
						  {
							"Service Name": "carts",
							"Build Output Type": "Docker Image",
							"CI Type": "Jenkins Job",
							"Build Number": 47,
							"GitHub Repository": "elektrikTomcat/carts",
							"Build Artifact": "731977058523.dkr.ecr.us-east-2.amazonaws.com/carts:jenkinsTest-47",
							"Unit Test Type": "Junit",
							"Unit Test Result": "PASS"
						  },
						  {
							"Service Name": "catalogue",
							"Build Output Type": "Docker Image",
							"CI Type": "Jenkins Job",
							"Build Number": 33,
							"GitHub Repository": "elektrikTomcat/catalogue",
							"Build Artifact": "731977058523.dkr.ecr.us-east-2.amazonaws.com/catalogue:jenkinsTest-33",
							"Unit Test Type": "Script",
							"Unit Test Result": "PASS"
						  },
						  {
							"Service Name": "front-end",
							"Build Output Type": "Docker Image",
							"CI Type": "Jenkins Job",
							"Build Number": 32,
							"GitHub Repository": "elektrikTomcat/front-end",
							"Build Artifact": "731977058523.dkr.ecr.us-east-2.amazonaws.com/catalogue:jenkinsTest-32",
							"Unit Test Type": "Script",
							"Unit Test Result": "FAIL"
						  }
						]
						''',
					reportName: "Test Report", 
					columnOrnamentation: ""
				]
		}
	}
}
