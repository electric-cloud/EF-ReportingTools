project "Test", {
	procedure "Environment Inventory", resourceName: 'local', {
		step "Get Inventory", shell: 'ectool evalDsl --dslFile "{0}"', command: '''\
			import groovy.json.*
			def rows=[]
			def fields = [
				//"sprite",
				"projectName",
				"environmentName",
				"artifactName",
				"artifactVersion",
				"status"
			]

			getProjects().each { proj ->
				if (proj.pluginKey==null) {
					getEnvironments(projectName: proj.projectName).each { env ->
						getEnvironmentInventoryItems(projectName: proj.projectName, environmentName: env.environmentName).each { invItem ->
							def row = [:]
							fields.each { field ->
							/*
								if (field == "sprite") {
									row[field] = """<span class='sds-svg-icon__container'><svg role='img' aria-label='Microservice' class='sds-svg-icon sds-svg-icon--icon-service sds-svg-icon--no-offset' style='width: 18px; height: 18px; fill: #c563cb  ;'><use xmlns:xlink='http://www.w3.org/1999/xlink' xlink:href='#icon-service'></use><g><rect class='at-svg-icon-title' x='0' y='0' fill='transparent' stroke='transparent' title='Microservice'></rect></g></svg></span>"""
								} else {
							*/
									row[field] = invItem[field]
							//	}
							}
							rows <<  row
						}
					}
				}
			}
			property "/myJob/inventory", value: JsonOutput.toJson(rows)
		'''.stripIndent()
		step 'Generate Report',
			subproject : '/plugins/EF-ReportingTools/project',
			subprocedure : 'json2table',
			actualParameter : [
				jsonData: '''$[/myJob/inventory]''',
				reportName: "Environment Inventory", 
				columnOrnamentation: ""
			]
	}
}
