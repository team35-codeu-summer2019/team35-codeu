google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var book_data = new google.visualization.DataTable();
    book_data.addColumn('string', 'Destination');
    book_data.addColumn('number', 'Number of Times');

    //add data to book_data
    book_data.addRows([
        ["Azerbaijan", 6],
        ["Bahamas", 10],
        ["China", 7],
        ["The 57 Bus", 4],
        ["Dominica", 8]
    ]);

    var chart = new google.visualization.BarChart(document.getElementById('top-10-most-popular-places'));

    var chart_options = {
        width: 800,
        height: 400
    };

    chart.draw(book_data, chart_options);
}
