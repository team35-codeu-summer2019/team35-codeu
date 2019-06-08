/* eslint-disable quotes */
/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent(
    browserHasGeolocation
      ? "Error: The Geolocation service failed."
      : "Error: Your browser doesn't support geolocation."
  );
  infoWindow.open(map);
}

function recenterToCurrentLocation(map) {
  const infoWindow = new google.maps.InfoWindow();
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition((position) => {
      const pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      infoWindow.setPosition(pos);
      infoWindow.setContent("Location found!");
      infoWindow.open(map);
      map.setCenter(pos);
      const trexMarker = new google.maps.Marker({
        position: { lat: pos.lat, lng: pos.lng },
        map,
        title: "Current Location"
      });
      const trexInfoWindow = new google.maps.InfoWindow({
        content: "Your current location."
      });
      trexMarker.addListener("click", () => {
        trexInfoWindow.open(map, trexMarker);
      });
    });
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false, infoWindow, map.getCenter());
  }
}

function createMap() {
  addLoginOrLogoutLinkToNavigation();
  const map = new google.maps.Map(document.getElementById("customized-map"), {
    center: { lat: 37.422, lng: -122.084 },
    zoom: 16
  });
  recenterToCurrentLocation(map);
}
