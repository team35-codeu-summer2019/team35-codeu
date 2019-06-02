// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// Fetch stats and display them in the page.
function fetchStats() {
    const url = '/stats';
    fetch(url).then((response) => {
        return response.json();
    }).then((stats) => {
        const statsContainer = document.getElementById('stats-container');
        statsContainer.innerHTML = '';

        const messageCountElement = buildStatElement('Message count: ' + stats.messageCount);
        statsContainer.appendChild(messageCountElement);
    });
}

function fetchUserStats(){
    const url = '/stats?user=' + parameterUsername;
    fetch(url).then((response) => {
        return response.json();
    }).then((userStats) => {
        const userStatsContainer = document.getElementById('user-stats-container');
        userStatsContainer.innerHTML = '';

        const userMessageCountElement = buildStatElement('You have sent: ' + userStats.messageCount);
        userStatsContainer.appendChild(userMessageCountElement);
    })
}

function buildStatElement(statString) {
    const statElement = document.createElement('p');
    statElement.appendChild(document.createTextNode(statString));
    return statElement;
}

// Fetch data and populate the UI of the page.
function buildUI() {
    fetchStats();
    addLoginOrLogoutLinkToNavigation();
}