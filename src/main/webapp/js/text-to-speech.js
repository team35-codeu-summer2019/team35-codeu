async function play(audioId, bodyMessageId) {
  const messageBody = document.getElementById(bodyMessageId);
  const content = messageBody.innerHTML;
  const params = new URLSearchParams();
  params.append('text', content);
  if (content === '') {
    // Do nothing; consider showing a simple error to the user.
  }
  try {
    const resp = await fetch('/a11y/tts', {
      method: 'POST',
      body: params,
    });
    const audio = await resp.blob();
    const audioURL = URL.createObjectURL(audio);
    const elem = document.getElementById(audioId);
    elem.src = audioURL;
    elem.play();
  } catch (err) {
    throw new Error('Unable to call the Text to Speech API: {e}');
  }
}
