/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */
// Fetch messages and add them to the page.
function fetchMessages() {
  const url = '/feed';
  fetch(url)
    .then(response => response.json())
    .then((messages) => {
      const messageContainer = document.getElementById('message-container');
      if (messages.length === 0) {
        messageContainer.innerHTML = '<p>There are no posts yet.</p>';
      } else {
        messageContainer.innerHTML = '';
      }
      const profilePromises = new Map();
      messages.forEach((message) => {
        if (profilePromises.get(message.user) === undefined) {
          const profileUrl = `/profile?user=${message.user}`;
          profilePromises.set(message.user, fetch(profileUrl)
            .then(res => res.json()));
        }
      });

      Promise.all(profilePromises).then((values) => {
        let messageIndex = 0;
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message, messageIndex, profilePromises.get(message.user));
          messageContainer.appendChild(messageDiv);
          messageIndex += 1;
        });
      });
    });
}
window.onload = fetchMessages();

// eslint-disable-next-line no-unused-vars
function createNewFeed() {
  const textarea = document.getElementById('feed-create-input');
  console.log(textarea);
}
