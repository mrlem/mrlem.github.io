$("#title").on("input", onChange);
$("#content").on("input", onChange);
$("#key").on("input", onChange);

$("button").click(function() {
    console.log("clicked");
    post("/topics/alerts", $("#title").val(), $("#content").val(), $("#key").val());
});

function onChange() {
    var title = $("#title").val();
    var content = $("#content").val();
    var key = $("#key").val();

    // TODO - improve key check
    if (title.length > 0 && content.length > 0 && key.length > 0) {
        $("button").removeClass("disabled");
    } else {
        $("button").addClass("disabled");
    }
}

function post(topic, title, content, key) {
    $.ajax({
        type : 'POST',
        url : "https://fcm.googleapis.com/fcm/send",
        headers : {
            Authorization: "key=" + key
        },
        contentType: 'application/json',
        data: JSON.stringify({
          "to": topic,
          "data": {
            "title": title,
            "message": content,
            "sound": 0,
            "vibrate": 1
           }
        }),
        success: function(response) {
            console.log("response: " + response);
        },
        error: function(xhr, status, error) {
            console.log("error: " + xhr.error);
        }
    });
}
