document.addEventListener('DOMContentLoaded', function() {
    // data confirm
    document.body.addEventListener("click", function(e) {
        var el = e.target;
        var confirmMessage = el.getAttribute("data-confirm");

        if(!!confirmMessage) {
            e.preventDefault();

            if(confirmMessage && confirm(confirmMessage)) {
                el.closest('form').submit();
            }
        }
    });

    // show editor
    document.body.addEventListener("click", function(e) {
        if (e.target.id === "edit") {
            document.getElementById("form-container").classList.remove("dn");
            document.getElementById("preview-container").classList.add("dn");
            e.target.classList.add("blue");
            e.target.classList.remove("gray");
            document.getElementById("preview").classList.remove("blue");
            document.getElementById("preview").classList.add("gray");
        }
    });

    // show preview
    document.body.addEventListener("click", function(e) {
        if (e.target.id === "preview") {
            e.preventDefault();
            e.target.classList.add("blue");
            e.target.classList.remove("gray");
            document.getElementById("form-container").classList.add("dn");
            document.getElementById("edit").classList.remove("blue");
            document.getElementById("edit").classList.add("gray");

            data = new FormData();
            data.append("title",document.getElementsByName("post/title")[0].value);
            data.append("body", document.getElementsByName("post/body")[0].value);
            data.append("__anti-forgery-token", document.getElementsByName("__anti-forgery-token")[0].value);

            fetch("/posts/preview", {
                method: "POST",
                body: data,
                credentials: 'same-origin'
            })
                .then(function(response) { return response.text();})
                .then(function(text) {
                    var preview = document.getElementById("preview-container");
                    preview.innerHTML = text;
                    preview.classList.remove("dn");
                    document.querySelectorAll('pre code').forEach(function(block) {
                        hljs.highlightBlock(block);
                    });
                });
        }
    });
});
