const quill = new Quill('#editor-container', {
  modules: {
    toolbar: [
      ['bold', 'italic', 'underline', 'link'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
    ]
  },
  bounds: document.querySelector('#editor-container'),
  placeholder: 'Compose a message...',
  theme: 'snow'
});
function copyInput() {
  // Populate hidden form on submit
  const message = document.querySelector('textarea[name=text]');
  message.value = quill.root.innerHTML;
  return true;
}
