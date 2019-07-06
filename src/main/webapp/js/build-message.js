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

// eslint-disable-next-line no-unused-vars
function buildMessageDiv(message, messageIndex, profilePromise) {
  const profileDiv = buildProfileDiv(message, profilePromise);

  const bodyDiv = document.createElement('div');
  const bodyMessageId = `message-body-${messageIndex.toString()}`;
  bodyDiv.setAttribute('id', bodyMessageId);
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  const langList = buildLanguageSelectList();
  const langId = `lang-${messageIndex.toString()}`;
  langList.setAttribute('id', langId);

  const translateButton = document.createElement('button');
  translateButton.setAttribute('onclick', `requestTranslator('${langId}','${bodyMessageId}');`);
  translateButton.innerText = 'Translate';

  const audio = document.createElement('audio');
  const audioId = `audio-${messageIndex.toString()}`;
  audio.setAttribute('id', audioId);

  const audioButton = document.createElement('button');
  audioButton.setAttribute('onclick', `play('${audioId}','${bodyMessageId}');`);
  audioButton.innerText = 'Play';

  const viewDetail = document.createElement('button');
  viewDetail.setAttribute('onclick', `window.location.href="messageDetail.html?id=${message.id}"`);
  viewDetail.classList.add('btn');
  viewDetail.classList.add('btn-info');
  viewDetail.innerText = 'View Detail';

  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(profileDiv);
  headerDiv.appendChild(langList);
  headerDiv.appendChild(translateButton);
  headerDiv.appendChild(audio);
  headerDiv.appendChild(audioButton);
  headerDiv.appendChild(viewDetail);

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}
