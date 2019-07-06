const url = window.location.href;
function httpOptions(method) {
  return {
    method, // *GET, POST, PUT, DELETE, etc.
    mode: 'cors', // no-cors, cors, *same-origin
    cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
    credentials: 'same-origin', // include, *same-origin, omit
    headers: {
      'Content-Type': 'application/json',
      // 'Content-Type': 'application/x-www-form-urlencoded',
    },
    redirect: 'follow', // manual, *follow, error
    referrer: 'no-referrer', // no-referrer, *client
    // body: JSON.stringify(data), // body data type must match "Content-Type" header
  };
}

// eslint-disable-next-line no-unused-vars
function createNewComment() {
  const textarea = document.getElementById('comment-create-input');
  console.log(textarea);
  fetch(`comment?postId=${url.split('=')[1]}&text=${textarea.innerHTML}`, httpOptions('POST'))
    .then(response => response.json())
    .then((res) => {
      console.log(res);
    });
}

function postLike() {
  fetch(`like?postId=${url.split('=')[1]}`, httpOptions('DELETE'))
    .then(response => response.json())
    .then((res) => {
      console.log(res);
    });
}

function deleteLike() {
  fetch(`like?postId=${url.split('=')[1]}`, httpOptions('DELETE'))
    .then(response => response.json())
    .then((res) => {
      console.log(res);
    });
}

// eslint-disable-next-line no-unused-vars
function toggleLike() {
  const temp = document.getElementById('like-num-span').innerText;
  const path = document.getElementById('img-icon-heart');
  if (path.style.fill === 'red') {
    deleteLike();
    path.style.fill = 'black';
    document.getElementById('like-num-span').innerText = parseInt(temp, 10) - 1;
  } else {
    postLike();
    path.style.fill = 'red';
    document.getElementById('like-num-span').innerText = parseInt(temp, 10) + 1;
  }
}

function buildLikeDiv(likes) {
  const likeDiv = document.createElement('div');
  const likeSpan = document.createElement('span');
  likeSpan.innerHTML = likes.length;
  likeSpan.id = 'like-num-span';

  likeDiv.appendChild(likeSpan);
  likeDiv.classList.add('like-count');
  return likeDiv;
}

function fetchLike() {
  fetch(`like?postId=${url.split('=')[1]}`)
    .then(response => response.json())
    .then((likes) => {
      const container = document.getElementById('likes-container');
      const likeDiv = buildLikeDiv(likes);
      container.appendChild(likeDiv);
    });
}

function buildCommentDiv(comment, index) {
  const bodyDiv = document.createElement('div');
  const bodyMessageId = `comment-body-${index.toString()}`;
  bodyDiv.setAttribute('id', bodyMessageId);
  bodyDiv.classList.add('comment-body');
  bodyDiv.innerHTML = comment.text;

  const headerDiv = document.createElement('div');
  headerDiv.innerHTML = `<span class='comment-user'>${
    comment.user
  }</span><span class='comment-timestamp>${comment.timestamp}</span>`;

  const commentDiv = document.createElement('div');
  commentDiv.classList.add('comment-div');
  commentDiv.appendChild(headerDiv);
  commentDiv.appendChild(bodyDiv);

  return commentDiv;
}

function fetchComments() {
  fetch(`comments?postId=${url.split('=')[1]}`)
    .then(response => response.json())
    .then((comments) => {
      const container = document.getElementById('comments-container');
      if (comments.length === 0) {
        container.innerHTML = '<p>There are no comments yet.</p>';
      } else {
        container.innerHTML = '';
      }
      let index = 0;
      comments.forEach((c) => {
        console.log(c);
        const commentDiv = buildCommentDiv(c, index);
        container.appendChild(commentDiv);
        index += 1;
      });
    });
}

function fetchMessage() {
  fetch(`messageDetail?postId=${url.split('=')[1]}`)
    .then(response => response.json())
    .then((messages) => {
      console.log(messages);
      document.getElementById('post-user').innerHTML = `<span class="message-detail-title">Created By:</span> ${messages.user}`;
      document.getElementById('post-text').innerHTML = `<span class="message-detail-title">Content:</span> ${messages.text}`;

      // document.getElementById('post-timestamp').innerText = messages.timestamp;
    });
}

function fetchContents() {
  fetchMessage();
  fetchLike();
  fetchComments();
}

window.onload = fetchContents();
