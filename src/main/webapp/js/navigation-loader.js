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

/**
 * Adds a login or logout link to the page, depending on whether the user is
 * already logged in.
 */

/**
 * Creates an li element.
 * @param {Element} childElement
 * @return {Element} li element
 */
const navSlide = () =>{

  const burger = document.querySelector('.burger');
  const nav = document.querySelector('nav ul');

  burger.addEventListener('click', ()=>{
    nav.classList.toggle('nav-active');
  });
}

function createListItem(childElement) {
  const listItemElement = document.createElement('li');
  listItemElement.appendChild(childElement);
  listItemElement.className = 'nav-item';
  return listItemElement;
}

/**
 * Creates an anchor element.
 * @param {string} url
 * @param {string} text
 * @return {Element} Anchor element
 */
function createLink(url, text) {
  const linkElement = document.createElement('a');
  linkElement.appendChild(document.createTextNode(text));
  linkElement.href = url;
  linkElement.className = 'nav-link';
  return linkElement;
}

// eslint-disable-next-line no-unused-vars
function addLoginOrLogoutLinkToNavigation() {
  const navigationElement = document.getElementById('navigation');
  if (!navigationElement) {
    console.warn('Navigation element not found!');
    return;
  }

  fetch('/login-status')
    .then(response => response.json())
    .then((loginStatus) => {
      if (loginStatus.isLoggedIn) {
        navigationElement.appendChild(
          createListItem(createLink('/aboutus.html', 'Our Team'))
        );

        navigationElement.appendChild(
          createListItem(createLink('/user-info.html', 'Your Profile'))
        );

        navigationElement.appendChild(
          createListItem(
            createLink(
              `/user-page.html?user=${loginStatus.username}`,
              'Your Page'
            )
          )
        );

        navigationElement.appendChild(
          createListItem(createLink('/see-around.html', 'See Around'))
        );

        navigationElement.appendChild(
          createListItem(createLink('/featured-story.html', 'Featured Story'))
        );

        navigationElement.appendChild(
          createListItem(createLink('/one-glance.html', 'One Glance'))
        );

        navigationElement.appendChild(
          createListItem(createLink('https://www.google.com/maps/', 'Map'))
        );

        navigationElement.appendChild(
          createListItem(createLink('/logout', 'Logout'))
        );

        // Probably this is not needed in our prototype
        // navigationElement.appendChild(
        //   createListItem(createLink('/community.html', 'Community'))
        // );
      } else {
        navigationElement.appendChild(
          createListItem(createLink('/index.html', 'Home'))
        );

        navigationElement.appendChild(
          createListItem(createLink('/aboutus.html', 'Our Team'))
        );

        navigationElement.appendChild(
          createListItem(createLink('/login', 'Login'))
        );
      }
    });
}

function goToAnchor(anchor) {
  window.location.hash=anchor;
}

// Fetch data and populate the UI of the page.
function buildUI() {
  navSlide();
  addLoginOrLogoutLinkToNavigation();
}
window.onload = buildUI();
