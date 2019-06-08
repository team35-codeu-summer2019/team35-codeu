google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawBarChart);
google.charts.setOnLoadCallback(drawChartCSV);

google.charts.load('current', {
    'packages': ['geochart'],
    'mapsApiKey': 'AIzaSyDmsBaZDSm50H1fqB3PVmAr-BIwopAdsOg'
});
google.charts.setOnLoadCallback(drawGeoChart);

function drawBarChart() {
    var book_data = new google.visualization.DataTable();
    book_data.addColumn('string', 'User Name');
    book_data.addColumn('number', 'Number of Messages sent per month');

    //add data to book_data
    book_data.addRows([
        ["Lily", 6],
        ["William", 10],
        ["George", 7],
        ["Peggy", 4],
        ["Danny", 8],
        ["Lizzy", 5],
        ["Naomi", 7],
        ["Amy", 3],
        ["Vivian", 5],
        ["Shirley", 3]
    ]);

    var chart = new google.visualization.BarChart(document.getElementById('top10-most-active-users'));

    var chart_options = {
        width: 800,
        height: 400,

        chart: {
            title: 'Top 10 Most Active Users'
        },
        axes: {
            x: {
                brightness: { side: 'bottom', label: 'Number of Messages Sent by user per month' } // Top x-axis.
            }
        }
    };

    chart.draw(book_data, chart_options);
}

function drawChartCSV() {
    fetch("/bookchart")
        .then((response) => {
            return response.json();
        })
        .then((bookJson) => {
            var bookData = new google.visualization.DataTable();
            //define columns for the DataTable instance
            bookData.addColumn('string', 'Book Title');
            bookData.addColumn('number', 'Rating');

            for (i = 0; i < bookJson.length; i++) {
                bookRow = [];
                var title = bookJson[i].title;
                var ratings = bookJson[i].rating;
                bookRow.push(title, ratings);

                bookData.addRow(bookRow);
            }
            var chartOptions = {
                width: 800,
                height: 400
            };
            var bookChart = new google.visualization.BarChart(document.getElementById('book_chart'));
            bookChart.draw(bookData, chartOptions);
        });
}

function drawGeoChart() {
    var data = google.visualization.arrayToDataTable([
        ['Country', 'Popularity'],
        ['Germany', 200],
        ['United States', 300],
        ['Brazil', 400],
        ['Canada', 500],
        ['France', 600],
        ['RU', 700]
    ]);

    var options = {
        width: 900,
        height: 500,

        displayMode: 'text'
    };

    var chart = new google.visualization.GeoChart(document.getElementById('geochart-of-places'));

    chart.draw(data, options);
}

