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

// function createNavBar() {
//   document.getElementById('nav-bar').innerHTML =
//     // eslint-disable-next-line no-multi-str
//     '<nav>\
//         <div class="brand">\
//             <h2> Play Even Harder </h2>\
//         </div>\
//         <ul>\
//             <li><a href="./index.html">Home</a></li>\
//             <li><a href="./aboutus.html">Our Team</a></li>\
//         </ul>\
//     </nav>\
//     <section class="sec1"></section>\
//     <section class="sec2"></section>';
//     var styleE1 = document.createElement('style');
//     document.appendChild(styleE1);
//   ;
// }

/**
 * Creates an li element.
 * @param {Element} childElement
 * @return {Element} li element
 */
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

function addLoginOrLogoutLinkToNavigation() {
//   createNavBar();
  const navigationElement = document.getElementById('navigation');
  if (!navigationElement) {
    console.warn('Navigation element not found!');
    return;
  }

  fetch('/login-status')
    .then(response => response.json())
    .then((loginStatus) => {
      if (loginStatus.isLoggedIn) {
        navigationElement.appendChild(createListItem(createLink(`/user-page.html?user=${loginStatus.username}`, 'Your Page')));

        navigationElement.appendChild(createListItem(createLink('/feed.html', 'Public Feeds')));

        navigationElement.appendChild(createListItem(createLink('/stats.html', 'Statistics')));

        navigationElement.appendChild(createListItem(createLink('/community.html', 'Community')));

        navigationElement.appendChild(
          createListItem(createLink('/logout', 'Logout'))
        );
      }else {
        navigationElement.appendChild(
          createListItem(createLink('/login', 'Login'))
        );
      }
    });
}


// initial nav bar
// '<nav class="navbar navbar-expand-lg navbar-dark bg-dark">\
//     <a class="navbar-brand" href="">Team 35</a>\
//     <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"\
//       aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">\
//       <span class="navbar-toggler-icon"></span>\
//     </button>\
//     <div class="collapse navbar-collapse" id="navbarNav">\
//       <ul class="navbar-nav" id="navigation">\
//         <li class="nav-item">\
//           <a class="nav-link" href="./index.html">Home</a>\
//         </li>\
//         <li class="nav-item ">\
//           <a class="nav-link" href="./aboutus.html">Our Team </a>\
//         </li>\
//       </ul>\
//     </div>\
//   </nav>'