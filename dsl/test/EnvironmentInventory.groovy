project "Test", {
	procedure "Environment Inventory", resourceName: 'local', {
		step "Get Inventory", shell: 'ectool evalDsl --dslFile "{0}"', command: '''\
			import groovy.json.*
			def rows=[]
			def fields = [
				"serviceName",
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
								if (field == "serviceName") {
									row[field] = """<img src=\\\\"../../../flow/public/app/assets/img/svg-icons/icon-service.svg\\\\" height=\\\\"20\\\\" width=\\\\"20\\\\"> """ + invItem[field]
								} else {
									row[field] = invItem[field]
								}
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
