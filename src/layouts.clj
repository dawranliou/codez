(ns layouts
  (:require [coast]))

(defn layout [request body]
  (let [title        (:title request)
        params       (:params request)
        og-image-url (if-let [slug (:code-slug params)]
                       (str "https://codez.xyz" (coast/url-for :code/get-image {:code/slug slug}))
                       "https://codez.xyz/assets/img/logo-full.png")]
    [:html
     [:head
      [:title title]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:meta {:name "description" :content "Copy, paste, and share code. Nothing more."}]
      [:meta {:name "twitter:card" :content "summary"}]
      [:meta {:name "twitter:creator" :content "@dawranliou"}]
      [:meta {:property "og:url" :content "https://codez.xyz"}]
      [:meta {:property "og:title" :content "Codez"}]
      [:meta {:property "og:description" :content "Copy, paste, and share code. Nothing more."}]
      [:meta {:property "og:image" :content "https://codez.xyz/assets/img/logo-full.png" #_og-image-url}]
      [:meta {:property "og:image:type" :content "image/png"}]
      [:meta {:name "theme-color" :content "#FF80CC"}]
      [:link {:rel "icon" :href "/favicon.png" :type "image/png"}]
      (coast/css "bundle.css")
      (coast/js "bundle.js")]
     [:body.bg-light-pink.sans-serif
      body
      [:script {:src "/js/highlight.pack.js"}]
      [:script "hljs.initHighlightingOnLoad();"]]]))
