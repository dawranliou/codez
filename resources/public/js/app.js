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

    // auto save
    var timer = null;
    var saveCount = 0;
    document.body.addEventListener('keyup', function(e) {
        if (e.target.matches('[name="post/title"],[name="post/body"]')) {
            if (!!timer) {
                // reset timer
                clearTimeout(timer);
            }

            document.getElementById("status").textContent = "Unsaved";
            var form = e.target.form;

            // auto save if nothing happens in the next 5 sec
            timer = setTimeout(function () {
                document.getElementById("status").textContent = "Saving...";

                var url = form.attributes.action.value;
                var method = form.attributes.method.value;
                var data = new FormData(form);
                var request = new XMLHttpRequest();
                request.open(method, url);
                request.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
                request.onload = function(event) {
                    timer = null;
                    if (request.status == 200) {
                        document.getElementById("status").textContent = "Saved";
                        var response = JSON.parse(request.response);
                        var url = response.url;
                        if (saveCount === 0 && !!url) {
                            history.pushState({}, '', url);
                        }
                        saveCount++;
                        setTimeout(function() {
                            status = '&nbsp;';
                        }, 2000);

                        if (!!response['form-params']) {
                            form.action = response['form-params']['action'];
                            form.method = response['form-params']['method'];
                            var node = document.createElement('input');
                            node.setAttribute('type', 'hidden');
                            node.setAttribute('name', '_method');
                            node.setAttribute('value', response['form-params']['_method']);
                            form.appendChild(node);
                        }
                    }
                    else {
                        status = "Something went wrong";
                    }
                };
                request.send(data);
            }, 3000);
        }
    });
});
