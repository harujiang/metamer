function updateList(data) {
    author = '<span style="font-weight:bold" class="author">' + data.author + '</span>: ';
    message = '<span class="message">' + data.text + '</span>';
    jQuery("ul.push-list").append('<li>' + author + message + '</li>');
}
