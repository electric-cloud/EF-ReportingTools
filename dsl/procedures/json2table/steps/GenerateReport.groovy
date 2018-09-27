import groovy.xml.MarkupBuilder
import groovy.json.*

def ReportTitle = '$[reportName]'?'$[reportName]':'report'

def TableData = new JsonSlurper().parseText '''$[jsonData]'''

def ColumnOrnamentation = new JsonSlurper().parseText '''$[columnOrnamentation]'''.contains('[')?'''$[columnOrnamentation]''':'[]'

//assert TableData.getClass() == "java.util.ArrayList"

// Identify all the Column names in the JSON object
ColumnHeaders = []
TableData.each { row ->
	//assert row.getClass() == "groovy.json.internal.LazyMap"
	row.each { key, value ->
		if (!(key in ColumnHeaders)) ColumnHeaders.push(key)
	}
}
ColumnHeaders

def sb = new StringWriter()
def html = new MarkupBuilder(sb)

html.doubleQuotes = true
html.expandEmptyElements = true
html.omitEmptyAttributes = false
html.omitNullAttributes = false
html.html {
    head {
        title (ReportTitle)
        script (src: 'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js', type: 'text/javascript', integrity:'sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa', crossorigin:'anonymous')
        link (rel:'stylesheet', href:'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css', integrity: 'sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u', crossorigin:'anonymous')
        link (rel:'stylesheet', href:'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css', integrity: 'sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp', crossorigin:'anonymous')
        style(type:"text/css", ''' body{margin:0;font-family:Arial, Helvetica, sans-serif;}.topnav{overflow:hidden;background-color:#333;}.topnav a{float:left;color:#f2f2f2;text-align:center;padding:10px 16px;text-decoration:none;font-size:17px;} ''')

        style(type:"text/css", '''
                .myInput { 
                  background-image: url('https://www.w3schools.com//css/searchicon.png');
                  background-position: 10px 10px;
                  background-repeat: no-repeat;
                  width: 500px;
                  font-size: 16px;
                  padding: 12px 20px 12px 40px;
                  border: 1px solid #ddd;
                  margin-bottom: 12px;
                  float: right;
                }''');

        script {
            mkp.yieldUnescaped '''
            (function(document) {
                'use strict';

                var LightTableFilter = (function(Arr) {

                    var _input;

                    function _onInputEvent(e) {
                        _input = e.target;
                        var tables = document.getElementsByClassName('reportTable');
                        Arr.forEach.call(tables, function(table) {
                            Arr.forEach.call(table.tBodies, function(tbody) {
                                Arr.forEach.call(tbody.rows, _filter);
                            });
                        });
                    }

                    function _filter(row) {
                        var text = row.textContent.toLowerCase(), val = _input.value.toLowerCase();
                        row.style.display = text.indexOf(val) === -1 ? 'none' : 'table-row';
                    }

                    return {
                        init: function() {
                            var inputs = document.getElementsByClassName('light-table-filter');
                            Arr.forEach.call(inputs, function(input) {
                                input.oninput = _onInputEvent;
                            });
                        }
                    };
                })(Array.prototype);

                document.addEventListener('readystatechange', function() {
                    if (document.readyState === 'complete') {
                        LightTableFilter.init();
                    }
                });

            })(document);'''
        }
        
        
        
    }
    body {
    
      div (class:'topnav') {
      
          a (href="/flow") {
            img (src:'/commander/images/logo.gif')
          }
        }
        
        
        
        mkp.yieldUnescaped('<!--')
        mkp.yield('<test>')
        mkp.yieldUnescaped('-->')

        
        
        div (class:'container-fluid'){
            div (class:'row'){
                div (class:'col-md-12'){
                    H2(ReportTitle)
                    input(type: "search", class: "myInput light-table-filter", placeholder: "Filter")
                    table (class:'reportTable table-hover table-striped table') {
                        thead () {
                            tr () {
                                ColumnHeaders.each { ColumnHeader -> 
                                    th (ColumnHeader)
                                }
                            }
                        }
                        tbody () {
                            TableData.each { row ->
                                tr (class: 'active', style:'box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)') {    
                                    ColumnHeaders.each { column ->
                                        // TODO: add column ornamentation
                                        def contents = row[column]?row[column]:"" // accommodate null
                                        td(){mkp.yieldUnescaped contents}
                                    }
                                }

                         }
                        }
                    }
                }
            }    
        }
    }
}

property "/myJob/reportHtml", value: sb.toString()
println sb.toString()