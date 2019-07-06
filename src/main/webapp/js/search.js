const urlParams = new URLSearchParams(window.location.search);
const pattern = urlParams.get('q');

/**
 * Builds a list element that contains a link to a user page, e.g.
 * <li><a href="/user-page.html?user=test@example.com">test@example.com</a></li>
 */
function buildUserListItem(user) {
  const userLink = document.createElement('a');
  userLink.setAttribute('href', '/user-page.html?user='.concat(user));
  userLink.appendChild(document.createTextNode(user));
  const userListItem = document.createElement('li');
  userListItem.appendChild(userLink);
  return userListItem;
}

/** Fetches users and adds them to the page. */
function fetchUserList() {
  const url = '/search?q='.concat(pattern);
  fetch(url)
  .then( response => {
    return response.json();
  }).then((users) => {
    const list = document.getElementById('list');
    list.innerHTML = '';

    users.forEach((user) => {
      const userListItem = buildUserListItem(user);
      list.appendChild(userListItem);
    });
  });
}

function buildUI() {
  fetchUserList();
}

window.onload = buildUI();
