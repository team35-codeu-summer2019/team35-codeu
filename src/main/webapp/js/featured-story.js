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

function fetchFeaturedStories() {
  const url = '/featured-posts';
  fetch(url)
    .then(response => response.json())
    .then((messages) => {
      const messageContainer = document.getElementById('featured-story-container');
      if (messages.length === 0) {
        messageContainer.innerHTML = '<p>There are no featured stories yet.</p>';
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
          const messageDiv = buildFeaturedStoryDiv(message, messageIndex, profilePromises.get(message.user));
          messageContainer.appendChild(messageDiv);
          messageIndex += 1;
        });
      });
    });
}

function save(user, post, elementID) {
  console.log(user);
  console.log(post);
  fetch(`saving?user=${user}&post=${post}`, httpOptions('POST'))
    .then(response => response.json())
    .then((res) => {
      console.log(res);
    });
  const button = document.getElementById(elementID);
  button.setAttribute('class', 'btn btn-secondary');
  button.innerText = 'Unsave';
}

function unSave(user, post, elementID) {
  console.log(user);
  console.log(post);
  fetch(`saving?user=${user}&post=${post}`, httpOptions('DELETE')) //
    .then(response => response.json())
    .then((res) => {
      console.log(res);
    });
    const button = document.getElementById(elementID);
    button.setAttribute('class', 'btn btn-primary');
    button.innerText = 'Save';
}

function toggleSave(user, post, messageIndex) {
  const elementID = "save-button-" + messageIndex.toString();
  const element = document.getElementById(elementID);
  if (element.className === 'btn btn-primary') {
    save(user, post, elementID);
  } else {
    unSave(user, post, elementID);
  }
}

function buildFeaturedStoryDiv(message, messageIndex, profilePromise) {
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
    // create the button follow
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

    // create the button save
    console.log(message.id);
    const savedStoryPrmise = fetch("/saving?user="+res.username+"&post="+message.id, httpOptions('GET')).then(res => { return res.json() });
    savedStoryPrmise.then((res) => {
      console.log(res);
      const saveButton = document.createElement('button');
      const saveButtonId = `save-button-${messageIndex.toString()}`;
      saveButton.setAttribute('id', saveButtonId);
      saveButton.style.setProperty("margin-left", "20px");
      saveButton.style.setProperty("corner-radius", "2px");

      if (res === 'Not Stored') {
        saveButton.setAttribute('class','btn btn-primary');
        saveButton.innerText = 'Save';
      } else if (res === 'Stored') {
        saveButton.setAttribute('class','btn btn-secondary');
        saveButton.innerText = 'Unsave';
      }
      fetch('/login-status')
        .then(response => response.json())
        .then((loginStatus) => {
          if (loginStatus.isLoggedIn) {
            saveButton.setAttribute('onclick', 'toggleSave(\'' + loginStatus.username + '\', \'' + message.id + '\', \'' + messageIndex + '\');');
            headerDiv.appendChild(saveButton);
          }
        });
    })
  })

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);
  console.log("BUILDING!");

  return messageDiv;
}

window.onload = fetchFeaturedStories();
