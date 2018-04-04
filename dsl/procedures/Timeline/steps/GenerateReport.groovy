import groovy.xml.MarkupBuilder
import groovy.json.*

def ReportTitle = '$[reportName]'?'$[reportName]':'report'

/* JSON Format:
	[
		{
			"resource": "string",
			"startDate":  "YYYY/MM/dd",
			"endDate": "YYYY/MM/dd"
		},
		{
			"resource": "string",
			"startDate":  "YYYY/MM/dd",
			"endDate": "YYYY/MM/dd"
		}
	]
*/

def GanttData = new JsonSlurper().parseText '''$[jsonData]'''

def dslData = []

GanttData.each { row ->
	def startDate = new Date(row["startDate"]).getTime()
	def endDate = new Date(row["endDate"]).getTime()
	def duration = (endDate - startDate)
	def outRow = [
		'"' + row["resource"] + '"',
		'new Date(' + startDate + ')',
		'new Date(' + endDate + ')'
	]
	dslData << outRow
}

def sb = new StringWriter()
def html = new MarkupBuilder(sb)

html.doubleQuotes = true
html.expandEmptyElements = true
html.omitEmptyAttributes = false
html.omitNullAttributes = false
html.html {
    head {
        H1(ReportTitle)
        title (ReportTitle)
        script (src: 'https://www.gstatic.com/charts/loader.js')
		script ("""
			google.charts.load('current', {'packages':['timeline']});
			google.charts.setOnLoadCallback(drawChart);
			function drawChart() {
				var container = document.getElementById('timeline');
				var chart = new google.visualization.Timeline(container);
				var dataTable = new google.visualization.DataTable();

				dataTable.addColumn({ type: 'string', id: 'President' });
				dataTable.addColumn({ type: 'date', id: 'Start' });
				dataTable.addColumn({ type: 'date', id: 'End' });
				dataTable.addRows($dslData);

				chart.draw(dataTable);
			}
		""")
	}
    body {
        mkp.yieldUnescaped('<!--')
        mkp.yield('<test>')
        mkp.yieldUnescaped('-->')

        div (id:'timeline', style: "height: 180px;")
    }
}
property "/myJob/reportHtml", value: sb.toString()
println sb.toString()