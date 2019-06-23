google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawBarChartTopTenUsers);
google.charts.setOnLoadCallback(drawBarChartTopTenPlaces);
google.charts.load('current', {
  'packages': ['geochart'],
  'mapsApiKey': 'AIzaSyDmsBaZDSm50H1fqB3PVmAr-BIwopAdsOg'
});
google.charts.setOnLoadCallback(drawGeoChart);

function drawBarChartTopTenUsers() {
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
          title: 'Top Ten Active Users'
        },
        hAxis: {
          title: 'Number of messages sent',
          minValue: 0,
        },
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
        var country = locationJson[i].location;
        console.log("country " + country);
        var occurence = locationJson[i].frequency;
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



function drawBarChartTopTenPlaces() {
  fetch("/top-10-places")
    .then((response) => {
      return response.json();
    })
    .then((placeJson) => {
      var placeData = new google.visualization.DataTable();
      //define columns for the DataTable instance
      placeData.addColumn('string', 'Places');
      placeData.addColumn('number', 'Rating');

      for (i = 0; i < placeJson.length; i++) {
        placeRow = [];
        var place = placeJson[i].place;
        var rating = placeJson[i].rating;
        placeRow.push(place, rating);

        placeData.addRow(placeRow);
      }
      var chartOptions = {
        width: 800,
        height: 400
      };
      var placeChart = new google.visualization.BarChart(document.getElementById('top10-places'));
      placeChart.draw(placeData, chartOptions);
    });
}
