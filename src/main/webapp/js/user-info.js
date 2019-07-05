function fetchUserData() {
  fetch('/login-status')
    .then(response => response.json())
    .then((loginStatus) => {
      if (loginStatus.isLoggedIn) {
        document.getElementById('page-title').innerText = `${loginStatus.username}`;
        document.title = `${loginStatus.username} - User Page`;
        fetch(`/profile?user=${loginStatus.username}`)
          .then(resp => resp.json())
          .then((user) => {
            if (`${user.imageUrl}` !== '') {
              document.getElementById('img').src = `${user.imageUrl}`;
            } else {
              document.getElementById('img').src = './img/user-profile.png';
            }
            document.getElementById('name').value = `${user.name}`;
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
