function buildLanguageOption(value, name) {
  const langOption = document.createElement('option');
  langOption.setAttribute('value', value);
  langOption.innerHTML = name;

  return langOption;
}

function buildLanguageSelectList() {
  const englishOption = buildLanguageOption('en', 'English');
  const chineseOption = buildLanguageOption('zh', 'Chinese');
  const spanishOption = buildLanguageOption('es', 'Spanish');
  const hindiOption = buildLanguageOption('hi', 'Hindi');
  const arabicOption = buildLanguageOption('ar', 'Arabic');

  const langSelect = document.createElement('select');
  langSelect.appendChild(englishOption);
  langSelect.appendChild(chineseOption);
  langSelect.appendChild(spanishOption);
  langSelect.appendChild(hindiOption);
  langSelect.appendChild(arabicOption);

  return langSelect;
}

// eslint-disable-next-line no-unused-vars
function requestTranslator(langId, bodyMessageId) {
  const messageBody = document.getElementById(bodyMessageId);
  const content = messageBody.innerHTML;
  const languageCode = document.getElementById(langId).value;

  messageBody.innerHTML = 'Loading...';

  const params = new URLSearchParams();
  params.append('text', content);
  params.append('languageCode', languageCode);

  fetch('/translate', {
    method: 'POST',
    body: params
  })
    .then(response => response.text())
    .then((translatedMessage) => {
      messageBody.innerHTML = translatedMessage;
    });
}

function buildProfileDiv(message, profilePromise) {
  const image = document.createElement('img');
  image.setAttribute('height', '50px');
  image.setAttribute('width', '50px');
  image.style.borderRadius = '50%';
  image.style.backgroundPosition = 'center center';

  const imageDiv = document.createElement('div');
  imageDiv.classList.add('col-xs-4');
  imageDiv.appendChild(image);

  const usernameDiv = document.createElement('div');

  const timeDiv = document.createElement('div');
  timeDiv.appendChild(document.createTextNode(new Date(message.timestamp)));

  const nameAndTimeDiv = document.createElement('div');
  nameAndTimeDiv.classList.add('col-xs-8');
  nameAndTimeDiv.appendChild(usernameDiv);
  nameAndTimeDiv.appendChild(timeDiv);

  const profileDiv = document.createElement('div');
  profileDiv.classList.add('row');
  profileDiv.style.cursor = 'pointer';

  profilePromise.then((profile) => {
    image.setAttribute('src', profile.imageUrl);
    usernameDiv.appendChild(document.createTextNode(profile.name));
    profileDiv.setAttribute('onclick', `location.href='/user-page.html?user=${profile.email}'`);
  });

  profileDiv.appendChild(imageDiv);
  profileDiv.appendChild(nameAndTimeDiv);

  return profileDiv;
}

// TODO
function httpOptions(method) {
  return {
    method, // *GET, POST, PUT, DELETE, etc.
    mode: 'cors', // no-cors, cors, *same-origin
    cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
    credentials: 'same-origin', // include, *same-origin, omit
    headers: {
      'Content-Type': 'application/json'
      // 'Content-Type': 'application/x-www-form-urlencoded',
    },
    redirect: 'follow', // manual, *follow, error
    referrer: 'no-referrer' // no-referrer, *client
    // body: JSON.stringify(data), // body data type must match "Content-Type" header
  };
}

function follow(user, currentUser) {
  console.log(user);
  console.log(currentUser);
  fetch(`follow?followeremail=${currentUser}&followingemail=${user}`, httpOptions('POST'))
    .then(response => response.json())
    .then((res) => {
      console.log(res);
    });
}

function unFollow(user, currentUser) {
  console.log(user);
  console.log(currentUser);
  fetch(`follow?followeremail=${currentUser}&followingemail=${user}`, httpOptions('DELETE')) // 
    .then(response => response.json())
    .then((res) => {
      console.log(res);
    });
}

function toggleFollow(user, currentUser, messageIndex) {
  console.log("Debugging point 3 " + currentUser);
  const elementID = "follow-button-" + messageIndex.toString();
  const element = document.getElementById(elementID);
  if (element.className === 'btn btn-primary') {
    follow(user, currentUser);
  } else {
    unFollow(user, currentUser);
  }
}

function createFollowButton(user, currentUser, messageIndex) {
  const elementID = "follow-button-" + messageIndex.toString();
  const element = document.getElementById(elementID);
  // check follower list
  const url = "/followers?user=" + user;
  fetch(url)
    .then((res) => res.json())
    .then((response) => {
      console.log(response);
      if (response.indexOf(currentUser) > -1) {
        console.log("Here is executed (1)");
        element.setAttribute('class', 'btn btn-secondary');
        element.innerText = "Unfollow";
      } else {
        console.log("Here is executed (2)");
        element.setAttribute('class', 'btn btn-primary');
        element.innerText = "Follow";
      }
    })
}
// eslint-disable-next-line no-unused-vars
function buildMessageDiv(message, messageIndex, profilePromise) {
  var feedDetailUrl = "/messageDetail.html?postId=" + message.id;

  const profileDiv = buildProfileDiv(message, profilePromise);

  const bodyDiv = document.createElement('div');
  const bodyMessageId = `message-body-${messageIndex.toString()}`;
  bodyDiv.setAttribute('id', bodyMessageId);
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = '<a href=' + feedDetailUrl + ' style="text-decoration:none">' + message.text + '</a>';

  const langList = buildLanguageSelectList();
  const langId = `lang-${messageIndex.toString()}`;
  langList.setAttribute('id', langId);

  const translateButton = document.createElement('button');
  translateButton.setAttribute('onclick', 'requestTranslator(\'' + langId + '\',\'' + bodyMessageId + '\');');
  translateButton.setAttribute('class', 'btn btn-light');
  translateButton.style.setProperty("margin-left", "20px");
  translateButton.style.setProperty("border-radius", "8px;");
  translateButton.innerText = 'Translate';

  const audio = document.createElement('audio');
  const audioId = `audio-${messageIndex.toString()}`;
  audio.setAttribute('id', audioId);

  const audioButton = document.createElement('button');
  audioButton.setAttribute('onclick', 'play(\'' + audioId + '\',\'' + bodyMessageId + '\');');
  audioButton.setAttribute('class', 'btn btn-light');
  audioButton.style.setProperty("margin-left", "20px");
  audioButton.style.setProperty("border-radius", "8px;");
  audioButton.innerText = 'Play';

  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(profileDiv);
  headerDiv.appendChild(langList);
  headerDiv.appendChild(translateButton);
  headerDiv.appendChild(audio);
  headerDiv.appendChild(audioButton);

  const currentUserPromise = fetch("/login-status").then(res => { return res.json() });
  currentUserPromise.then((res) => {
    console.log(res.username);
    if (res.username != message.user) {
      const followButton = document.createElement('button');
      const followButtonId = `follow-button-${messageIndex.toString()}`;
      followButton.setAttribute('id', followButtonId);

      const url = "/followers?user=" + message.user;
      const followButtonStylePromise = fetch(url).then(res2 => { return res2.json() });
      followButtonStylePromise.then((res2) => {
        if (res2 === null) {
          console.log("Here is executed (1)");
          followButton.setAttribute('class', 'btn btn-primary');
          followButton.innerText = "Follow";
          followButton.setAttribute('onclick', 'toggleFollow(\'' + message.user + '\',\'' + res.username + '\',\'' + messageIndex + '\');');
          followButton.style.setProperty("margin-left", "20px");
          followButton.style.setProperty("corner-radius", "2px");
          headerDiv.appendChild(followButton);
        } else {
          if (res2.indexOf(res.username) > -1) {
            console.log("Here is executed (2)");
            followButton.setAttribute('class', 'btn btn-secondary');
            followButton.innerText = "Unfollow";
            followButton.setAttribute('onclick', 'toggleFollow(\'' + message.user + '\',\'' + res.username + '\',\'' + messageIndex + '\');');
            followButton.style.setProperty("margin-left", "20px");
            followButton.style.setProperty("corner-radius", "2px");
            headerDiv.appendChild(followButton);
          } else {
            console.log("Here is executed (3)");
            followButton.setAttribute('class', 'btn btn-primary');
            followButton.innerText = "Follow";
            followButton.setAttribute('onclick', 'toggleFollow(\'' + message.user + '\',\'' + res.username + '\',\'' + messageIndex + '\');');
            followButton.style.setProperty("margin-left", "20px");
            followButton.style.setProperty("corner-radius", "2px");
            headerDiv.appendChild(followButton);
          }
        }
      });
    }
  })

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}
