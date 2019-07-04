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
  })
    .catch(error => console.log(error));
}

/**
 * Shows the edit profile button,image form,message form if the user is logged in and viewing their own page.
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
      const messagesContainer = document.getElementById('message-container');
      if (messages.length === 0) {
        messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
      } else {
        messagesContainer.innerHTML = '';
      }
      messages.forEach((message) => {
        const messageDiv = buildMessageDiv(message);
        messagesContainer.appendChild(messageDiv);
      });
    });
}


/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
    `${message.user} - ${new Date(message.timestamp)}`
  ));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
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
