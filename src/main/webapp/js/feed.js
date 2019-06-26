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

function buildMessageDiv(message, messageIndex) {
  const usernameDiv = document.createElement('div');
  usernameDiv.classList.add('left-align');
  usernameDiv.appendChild(document.createTextNode(message.user));

  const timeDiv = document.createElement('div');
  timeDiv.classList.add('right-align');
  timeDiv.appendChild(document.createTextNode(new Date(message.timestamp)));

  const langList = buildLanguageSelectList();
  const langId = 'lang-' + messageIndex.toString();
  langList.setAttribute('id', langId);

  const bodyDiv = document.createElement('div');
  const bodyMessageId = 'message-body-' + messageIndex.toString();
  bodyDiv.setAttribute("id", bodyMessageId);
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  const translateButton = document.createElement('button');
  translateButton.setAttribute('onclick', 'requestTranslator(\'' + langId + '\',\''+ bodyMessageId + '\');');
  translateButton.innerText = 'Translate';

  const audio = document.createElement('audio');
  const audioId = 'audio-' + messageIndex.toString();
  audio.setAttribute('id', audioId);

  const audioButton = document.createElement('button');
  audioButton.setAttribute('onclick', 'play(\'' + audioId + '\',\'' + bodyMessageId + '\');');
  audioButton.innerText = 'Play';

  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(usernameDiv);
  headerDiv.appendChild(timeDiv);
  headerDiv.appendChild(langList);
  headerDiv.appendChild(translateButton);
  headerDiv.appendChild(audio);
  headerDiv.appendChild(audioButton);

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

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
      var messageIndex = 0;
      messages.forEach((message) => {
        const messageDiv = buildMessageDiv(message, messageIndex);
        messageContainer.appendChild(messageDiv);
        messageIndex += 1;
      });
    });
}

// Fetch data and populate the UI of the page.
window.onload = fetchMessages();
