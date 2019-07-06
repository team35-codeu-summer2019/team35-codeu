const url = window.location.href;
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
function buildCommentDiv(comment, index) {
  const li = document.createElement('li');
  li.classList.add('list-group-item');
  li.innerHTML = `${index}<span>${
    comment.text
  }</span><br/><small class='comment-user'>Commented By: ${
    comment.user
  }</small>`;

  return li;
}
function fetchComments() {
  fetch(`comments?postId=${url.split('=')[1]}`)
    .then(response => response.json())
    .then((comments) => {
      const container = document.getElementById('comments-container-ul');
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
// eslint-disable-next-line no-unused-vars
function createNewComment() {
  const textarea = document.getElementById('comment-create-input');
  console.log(textarea);
  fetch(
    `comment?postId=${url.split('=')[1]}&text=${textarea.value}`,
    httpOptions('POST')
  )
    .then(response => response.json())
    .then((comments) => {
      const container = document.getElementById('comments-container-ul');
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

function postLike() {
  fetch(`like?id=${url.split('=')[1]}`, httpOptions('POST'))
    .then(response => response.json())
    .then((res) => {
      console.log(res);
      document.getElementById('like-id').innerText = res.id;
    });
}

function deleteLike() {
  const id = document.getElementById('like-id').innerText;
  fetch(`like?id=${id}`, httpOptions('DELETE'))
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

function fetchMessage() {
  fetch(`messageDetail?postId=${url.split('=')[1]}`)
    .then(response => response.json())
    .then((messages) => {
      console.log(messages);
      document.getElementById(
        'post-user'
      ).innerHTML = `<span class="message-detail-title">Created By: ${
        messages.user
      }</span>`;
      document.getElementById('post-text').innerHTML = `${messages.text}`;

      // document.getElementById('post-timestamp').innerText = messages.timestamp;
    });
}

function fetchContents() {
  fetchMessage();
  fetchLike();
  fetchComments();
}

window.onload = fetchContents();
