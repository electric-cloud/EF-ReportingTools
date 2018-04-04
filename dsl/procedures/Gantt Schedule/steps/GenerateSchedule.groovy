import groovy.xml.MarkupBuilder
import groovy.json.*

def ChartTitle = '$[ganttName]'?'$[ganttName]':'schedule'

/* JSON Format:
	[
		{
			"taskName": "string",
			"startDate":  "YYYY/MM/dd",
			"endDate": "YYYY/MM/dd"
		},
		{
			"taskName": "string",
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
		'"' + row["taskName"] + '"',
		'"' + row["taskName"] + '"',
		'new Date(' + startDate + ')',
		'new Date(' + endDate + ')',
		duration,
		0,
		null
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
        title (ChartTitle)
        script (src: 'https://www.gstatic.com/charts/loader.js')
		script ("""
			google.charts.load('current', {'packages':['gantt']});
			google.charts.setOnLoadCallback(drawChart);

			function drawChart() {

			  var data = new google.visualization.DataTable();
			  data.addColumn('string', 'Task ID');
			  data.addColumn('string', 'Task Name');
			  data.addColumn('date', 'Start Date');
			  data.addColumn('date', 'End Date');
			  data.addColumn('number', 'Duration');
			  data.addColumn('number', 'Percent Complete');
			  data.addColumn('string', 'Dependencies');

			  data.addRows($dslData);

			  var options = {
				height: 275
			  };

			  var chart = new google.visualization.Gantt(document.getElementById('chart_div'));

			  chart.draw(data, options);
			}
		""")
	}
    body {
        mkp.yieldUnescaped('<!--')
        mkp.yield('<test>')
        mkp.yieldUnescaped('-->')

        div (id:'chart_div')
    }
}
property "/myJob/ganttHtml", value: sb.toString()
println sb.toString()