function fetchUserData() {
  fetch('/login-status')
    .then(response => response.json())
    .then((loginStatus) => {
      if (loginStatus.isLoggedIn) {
        document.title = `${loginStatus.username} - User Page`;
        fetch(`/profile?user=${loginStatus.username}`)
          .then(resp => resp.json())
          .then((user) => {
            const image = document.getElementById('img');
            if (`${user.imageUrl}` !== '') {
              image.src = `${user.imageUrl}`;
            } else {
              image.src = './img/user-profile.png';
            }
            const username = document.getElementById('name');
            username.value = `${user.name}`;
            quill.root.innerHTML = `${user.aboutMe}`;
        })
          .catch(error => console.log(error));
      } else {
        document.location.href = '/';
	  }
    });
}

function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-upload-url?requester=user-info')
    .then((response) => {
      return response.text();
    })
    .then((uploadUrl) => {
      const profileForm = document.getElementById('profile-form');
      profileForm.action = uploadUrl;
    });
}

function buildUI() {
  fetchUserData();
  fetchBlobstoreUrlAndShowForm();
}
window.onload = buildUI();
