/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Fetch user data and add them to the page. */
function fetchUserData() {
  fetch(`/profile?user=${parameterUsername}`)
    .then(resp => resp.json())
    .then((user) => {
      const username = document.getElementById('username');
      username.innerText = `${user.name}`;
      document.title = `${user.name} - Page`;
      const image = document.getElementById('img');
      if (`${user.imageUrl}` !== '') {
        image.src = `${user.imageUrl}`;
      } else {
        image.src = './img/user-profile.png';
      }
      const about = document.getElementById('about');
      about.innerHTML = `${user.aboutMe}`;
    });
}

/**
 * Shows the hidden form and button if the user is logged in and viewing their own page.
 */
function removeHiddensIfViewingSelf() {
  fetch('/login-status')
    .then(response => response.json())
    .then((loginStatus) => {
      if (loginStatus.isLoggedIn
        && loginStatus.username === parameterUsername) {
        const messageForm = document.getElementById('message-form');
        messageForm.classList.remove('hidden');
        const imageForm = document.getElementById('image-form');
        imageForm.classList.remove('hidden');
        const editProfileButton = document.getElementById('edit-profile-button');
        editProfileButton.classList.remove('hidden');
      }
    });
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = `/messages?user=${parameterUsername}`;
  fetch(url)
    .then(response => response.json())
    .then((messages) => {
      const messageContainer = document.getElementById('message-container');
      if (messages.length === 0) {
        messageContainer.innerHTML = '<p>This user has no posts yet.</p>';
      } else {
        messageContainer.innerHTML = '';
      }
      let profilePromise = fetch(`/profile?user=${parameterUsername}`)
        .then(res => {return res.json(); });
      var messageIndex = 0;
      messages.forEach((message) => {
        const messageDiv = buildMessageDiv(message, messageIndex, profilePromise);
        messageContainer.appendChild(messageDiv);
        messageIndex += 1;
      });
    });
}


/** fetch blobstore upload url */
function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-upload-url?requester=user-page')
    .then((response) => {
      return response.text();
    })
    .then((imageUploadUrl) => {
      const imageForm = document.getElementById('image-form');
      imageForm.action = imageUploadUrl;
    })
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  fetchUserData();
  removeHiddensIfViewingSelf();
  fetchMessages();
  fetchBlobstoreUrlAndShowForm();
}
window.onload = buildUI();
