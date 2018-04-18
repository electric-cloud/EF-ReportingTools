/*
def TimelineData = '''
	[
		{
			"resource": "First",
			"label": "First label",
			"startDate":  "2018/4/10",
			"endDate": "2018/5/1"
		}
	]
'''
*/
project "Test", {
	environment "SIT", {
		reservation "May integration",
			beginDate: '2018-05-01T00:00',
			endDate: '2018-05-09T00:00',
			blackout: '1',
			timeZone: 'America/New_York'
		reservation "June integration",
			beginDate: '2018-06-15T00:00',
			endDate: '2018-06-20T00:00',
			blackout: '1',
			timeZone: 'America/New_York'	
	}
	environment "QA", {
		reservation "May testing",
			beginDate: '2018-05-04T00:00',
			endDate: '2018-05-14T00:00',
			blackout: '1',
			timeZone: 'America/New_York'
		reservation "June testing",
			beginDate: '2018-06-15T00:00',
			endDate: '2018-06-22T00:00',
			blackout: '1',
			timeZone: 'America/New_York'			
	}
	environment "UAT", {
		reservation "May approvals",
			beginDate: '2018-05-25T00:00',
			endDate: '2018-05-30T00:00',
			blackout: '1',
			timeZone: 'America/New_York'
		reservation "June approvals",
			beginDate: '2018-06-24T00:00',
			endDate: '2018-06-29T00:00',
			blackout: '1',
			timeZone: 'America/New_York'			
	}

	
	release "May", plannedStartDate: "2018-05-02" , plannedEndDate: "2018-06-01",{
		pipeline "May",{
			stage "SIT", plannedStartDate: "2018-05-04", plannedEndDate: "2018-05-10"
			stage "QA", plannedStartDate: "2018-05-11", plannedEndDate: "2018-05-20"
			stage "UAT", plannedStartDate: "2018-05-20", plannedEndDate: "2018-05-30"
			stage "Production", plannedStartDate: "2018-06-01"
		}
	}
	release "June", plannedStartDate: "2018-06-02" , plannedEndDate: "2018-07-01"
	release "Aug", plannedStartDate: "2018-07-02" , plannedEndDate: "2018-08-01"

	procedure "Environment and Release Timeline",{
		step "Gather Data", shell: 'ectool evalDsl --dslFile "{0}" ', command: '''
			import groovy.json.*
			def TimelineData = []
			project "Test",{
				getEnvironments().each { env ->
					getReservations(environmentProjectName: projectName, environmentName: env.environmentName).each { res ->
						TimelineData << [
							"resource": "Environment: " + res["environmentName"],
							"label": res["reservationName"],
							"startDate":  new Date().parse('yyyy-MM-dd\\\'T\\\'hh:mm', res["beginDate"]).getTime(),
							"endDate": new Date().parse('yyyy-MM-dd\\\'T\\\'hh:mm', res["endDate"]).getTime()
						]
					}
				}
				getReleases().each { rel ->
					TimelineData << [
						"resource": "Release: " + rel["releaseName"],
						"label": "",
						"startDate": rel["plannedStartTime"].getTime(),
						"endDate": rel["plannedEndTime"].getTime()          
					]
					if (rel.stageCount > 0) getStages(releaseName: rel.releaseName, pipelineName: rel.releaseName).each { stg ->
						def StartDate = new Date().parse('yyyy-MM-dd\\'T\\'hh:mm:ss.SSS\\'Z\\'', stg.plannedStartDate.toString()).getTime()
						def EndDate = (stg.plannedEndDate) ? new Date().parse('yyyy-MM-dd\\'T\\'hh:mm:ss.SSS\\'Z\\'', stg.plannedEndDate.toString() ).getTime() : StartDate
						TimelineData << [
							"resource": "Release: " + rel["releaseName"],
							"label": stg.stageName,
							"startDate": StartDate,
							"endDate": EndDate
						]
					}
				}
			}
			property "/myJob/timelineData", value: JsonOutput.toJson(TimelineData)
		'''.stripIndent()
		
		step 'Generate Report',
			subproject : '/plugins/EF-ReportingTools/project',
			subprocedure : 'Timeline',
			actualParameter : [
				jsonData: '''$[/myJob/timelineData]''',
				reportName: "Environment and Release Timeline"
			]
	} // procedure
} // project
