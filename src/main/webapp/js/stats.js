// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

function buildStatElement(statString) {
  const statElement = document.createElement('p');
  statElement.appendChild(document.createTextNode(statString));
  return statElement;
}

// Fetch stats and display them in the page.
function fetchStats() {
  const url = '/message-count';
  fetch(url).then(response => response.json()).then((stats) => {
    const statsContainer = document.getElementById('stats-container');
    statsContainer.innerHTML = '';

    const messageCountElement = buildStatElement(`Message count: ${stats.messageCount}`);
    statsContainer.appendChild(messageCountElement);
  });
}

function fetchUserStats() {
  const userStatsContainer = document.getElementById('user-stats-container');
  userStatsContainer.innerHTML = '';
  fetch('/login-status')
    .then(response => response.json())
    .then((loginStatus) => {
      if (loginStatus.isLoggedIn) {
        const url = `/user-message-count?user=${loginStatus.username}`;
        fetch(url).then(response => response.json()).then((userStats) => {
          const userMessageCountElement = buildStatElement(`You have sent: ${userStats.userMessageCount}`);
          userStatsContainer.appendChild(userMessageCountElement);
        });
      } else {
        const userMessageCountElement = buildStatElement('You have sent: Log in first to see the result');
        userStatsContainer.appendChild(userMessageCountElement);
      }
    });

}

function fetchAvgStats() {
  const url = '/avg-msg-length';
  fetch(url).then(response => response.json()).then((avgStats) => {
    const avgStatsContainer = document.getElementById('avg-stats-container');
    avgStatsContainer.innerHTML = '';

    const avgMessageCountElement = buildStatElement(`The average length of message is: ${avgStats.avgMessageLength}`);
    avgStatsContainer.appendChild(avgMessageCountElement);
  });
}

// Fetch data and populate the UI of the page.
function buildUI() {
  fetchStats();
  fetchUserStats();
  fetchAvgStats();
}
window.onload=buildUI();
