google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawBarChart);
google.charts.setOnLoadCallback(drawChartCSV);
google.charts.load('current', {
  'packages': ['geochart'],
  'mapsApiKey': 'AIzaSyDmsBaZDSm50H1fqB3PVmAr-BIwopAdsOg'
});
google.charts.setOnLoadCallback(drawGeoChart);

function drawBarChart() {
  fetch("/top-10-active-users")
    .then((response) => {
      return response.json();
    })
    .then((activeUserJson) => {
      var activeUserData = new google.visualization.DataTable();
      //define columns for the DataTable instance
      activeUserData.addColumn('string', 'User');
      activeUserData.addColumn('number', 'Number of messages sent');

      for (i = 0; i < activeUserJson.length; i++) {
        activeUserRow = [];
        var user = activeUserJson[i].user;
        var number = activeUserJson[i].numberMsg;
        activeUserRow.push(user, number);

        activeUserData.addRow(activeUserRow);
      }

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

      var chart = new google.visualization.BarChart(document.getElementById('top10-most-active-users'));
      chart.draw(activeUserData, chart_options);
    });
}



function drawGeoChart() {
  fetch("/locations")
    .then((response) => {
      return response.json();
    })
    .then((locationJson) => {
      console.log(locationJson);
      var test = locationJson.length;
      console.log(test);
      var locationData = new google.visualization.DataTable();
      //define columns for the DataTable instance
      locationData.addColumn('string', 'Country');
      locationData.addColumn('number', 'Occurence');

      for (i = 0; i < locationJson.length; i++) {
        console.log("test is this loop is executed");
        locationRow = [];
        var country = locationJson[i][0];
        console.log("country " + country);
        var occurence = locationJson[i][1];
        console.log("occurence " + occurence);
        locationRow.push(country, occurence);

        locationData.addRow(locationRow);
      }

      var options = {
        width: 900,
        height: 500,
        chart: {
          title: 'Where are our users from?'
        },
      };

      var chart = new google.visualization.GeoChart(document.getElementById('geochart-of-places'));
      chart.draw(locationData, options);
    });
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
