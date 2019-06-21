/* eslint-disable no-unused-vars */
/* eslint-disable no-use-before-define */
/* eslint-disable no-undef */
let map;
let editMarker;
const currentLocalImg = {
  url: '../img/people35.png',
  origin: new google.maps.Point(0, 0),
  anchor: new google.maps.Point(0, 32)
};
const image = {
  url:
    'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png',
  size: new google.maps.Size(20, 32),
  origin: new google.maps.Point(0, 0),
  anchor: new google.maps.Point(0, 32)
};
const shape = {
  coords: [1, 1, 1, 20, 18, 20, 18, 1],
  type: 'poly'
};
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent(
    browserHasGeolocation
      ? 'Error: The Geolocation service failed.'
      : "Error: Your browser doesn't support geolocation."
  );
  infoWindow.open(map);
}

function recenterToCurrentLocation() {
  const infoWindow = new google.maps.InfoWindow();
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition((position) => {
      const pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      infoWindow.setPosition(pos);
      infoWindow.setContent('Location found!');
      infoWindow.open(map);
      map.setCenter(pos);
      const trexMarker = new google.maps.Marker({
        position: { lat: pos.lat, lng: pos.lng },
        map,
        icon: currentLocalImg,
        shape,
        title: 'Current Location'
      });
      const trexInfoWindow = new google.maps.InfoWindow({
        content: 'Your current location.'
      });
      trexMarker.addListener('click', () => {
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
  map = new google.maps.Map(document.getElementById('customized-map'), {
    center: { lat: 38.5949, lng: -94.8923 },
    zoom: 4
  });
  // When the user clicks in the map, show a marker with a text box the user can edit.
  map.addListener('click', (event) => {
    createMarkerForEdit(event.latLng.lat(), event.latLng.lng());
  });
  fetchMarkers();
  recenterToCurrentLocation();
}
/** Fetches markers from the backend and adds them to the map. */
function fetchMarkers() {
  fetch('/markers')
    .then(response => response.json())
    .then((markers) => {
      markers.forEach((marker) => {
        createMarkerForDisplay(
          marker.lat,
          marker.lng,
          marker.content,
          image,
          shape
        );
      });
    });
  fetch('/map-data')
    .then(response => response.json())
    .then((markers) => {
      const ms = [];
      markers.forEach((marker) => {
        ms.push(createMarkerForDisplay(
          marker.lat,
          marker.lng,
          marker.content,
          null,
          null
        ));
      });
      const markerCluster = new MarkerClusterer(map, ms);
    });
}
function toggleBounce() {
  if (marker.getAnimation() !== null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
  }
}

/** Creates a marker that shows a read-only info window when clicked. */
function createMarkerForDisplay(lat, lng, content, icon, s) {
  let marker;
  if (icon && s) {
    marker = new google.maps.Marker({
      position: { lat, lng },
      draggable: true,
      animation: google.maps.Animation.DROP,
      icon,
      shape: s,
      map
    });
    const infoWindow = new google.maps.InfoWindow({
      content
    });
    marker.addListener('click', () => {
      infoWindow.open(map, marker);
      toggleBounce();
    });
  } else {
    marker = new google.maps.Marker({
      position: { lat, lng },
      map
    });
  }
  return marker;
}
/** Sends a marker to the backend for saving. */
function postMarker(lat, lng, content) {
  const params = new URLSearchParams();
  params.append('lat', lat);
  params.append('lng', lng);
  params.append('content', content);
  fetch('/markers', {
    method: 'POST',
    body: params
  });
}
/** Creates a marker that shows a textbox the user can edit. */
function createMarkerForEdit(lat, lng) {
  // If we're already showing an editable marker, then remove it.
  if (editMarker) {
    editMarker.setMap(null);
  }
  editMarker = new google.maps.Marker({
    position: { lat, lng },
    draggable: true,
    animation: google.maps.Animation.DROP,
    icon: image,
    shape,
    map
  });
  const infoWindow = new google.maps.InfoWindow({
    content: buildInfoWindowInput(lat, lng)
  });
  // When the user closes the editable info window, remove the marker.
  google.maps.event.addListener(infoWindow, 'closeclick', () => {
    editMarker.setMap(null);
  });
  infoWindow.open(map, editMarker);
}
/** Builds and returns HTML elements that show an editable textbox and a submit button. */
function buildInfoWindowInput(lat, lng) {
  const textBox = document.createElement('textarea');
  const button = document.createElement('button');
  button.appendChild(document.createTextNode('Submit'));
  button.onclick = () => {
    postMarker(lat, lng, textBox.value);
    createMarkerForDisplay(lat, lng, textBox.value, image, shape);
    editMarker.setMap(null);
  };
  const containerDiv = document.createElement('div');
  containerDiv.appendChild(textBox);
  containerDiv.appendChild(document.createElement('br'));
  containerDiv.appendChild(button);
  return containerDiv;
}
