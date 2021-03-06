import java.io.File 

def procName = 'Gantt Schedule'
procedure procName, {

	formalParameter "jsonData"
	formalParameter "reportName"
	
	def ReportName = '$[reportName]'?'$[reportName]':'schedule'

	step 'Generate schedule',
    	  command: new File(pluginDir, "dsl/procedures/$procName/steps/GenerateSchedule.groovy").text,
    	  shell: 'ectool evalDsl --dslFile "{0}"'

	step 'Create schedule Directory',
		subproject : '/plugins/EC-FileOps/project',
		subprocedure : 'CreateDirectory',
		actualParameter : [
			Path: 'artifacts'
		]
	
	step 'Save schedule to a file',
		subproject : '/plugins/EC-FileOps/project',
		subprocedure : 'AddTextToFile',
		actualParameter : [
			AddNewLine: '1',
			Append: '1',
			Content: '$[/myJob/ganttHtml]',
			Path: 'artifacts/' + ReportName + '.html'
		] 
  
    step 'Create Job Link',
        command: """
			ectool setProperty \"/myJob/report-urls/${ReportName}\" \"/commander/jobSteps/\$[/myJobStep/jobStepId]/${ReportName}.html\"
		""".stripIndent()
		
	step 'Create Pipeline Link',
		command: """\
			ectool setProperty \"/myPipelineStageRuntime/ec_summary/${ReportName}\" \"<html><a href=\\\"../commander/jobSteps/\$[/myJobStep/jobStepId]/${ReportName}.html\\\" target=\\\"_blank\\\">Link</a></html>\"
			""".stripIndent(),
		condition: '$[/javascript getProperty("/myPipelineStageRuntime")]'
}