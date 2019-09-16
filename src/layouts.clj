(ns layouts
  (:require [coast]))

(defn layout [request body]
  (let [params       (:params request)
        og-image-url (if-let [slug (:code-slug params)]
                       (str "https://codez.xyz" (coast/url-for :code/get-image {:code/slug slug}))
                       "https://codez.xyz/assets/img/logo-full.png")]
    [:html
     [:head
      [:title "Codez"]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:meta {:name "description" :content "Copy, paste, and share code. Nothing more."}]
      [:meta {:name "twitter:card" :content "summary"}]
      [:meta {:name "twitter:creator" :content "@dawranliou"}]
      [:meta {:property "og:url" :content "https://codez.xyz"}]
      [:meta {:property "og:title" :content "Codez"}]
      [:meta {:property "og:description" :content "Copy, paste, and share code. Nothing more."}]
      [:meta {:property "og:image" :content og-image-url}]
      [:meta {:property "og:image:type" :content "image/png"}]
      [:link {:rel "apple-touch-icon" :sizes= "57x57" :href "/apple-icon-57x57.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "60x60" :href "/apple-icon-60x60.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "72x72" :href "/apple-icon-72x72.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "76x76" :href "/apple-icon-76x76.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "114x114" :href "/apple-icon-114x114.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "120x120" :href "/apple-icon-120x120.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "144x144" :href "/apple-icon-144x144.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "152x152" :href "/apple-icon-152x152.png"}]
      [:link {:rel "apple-touch-icon" :sizes= "180x180" :href "/apple-icon-180x180.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "192x192" :href "/android-icon-192x192.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/favicon-32x32.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "96x96" :href "/favicon-96x96.png"}]
      [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/favicon-16x16.png"}]
      [:link {:rel "manifest" :href "/manifest.json"}]
      [:meta {:name "msapplication-TileColor" :content "#FF80CC"}]
      [:meta {:name "msapplication-TileImage" :content "/ms-icon-144x144.png"}]
      [:meta {:name "theme-color" :content "#FF80CC"}]
      (coast/css "bundle.css")
      (coast/js "bundle.js")]
     [:body.bg-light-pink.sans-serif
      body
      [:script {:src "/js/highlight.pack.js"}]
      [:script "hljs.initHighlightingOnLoad();"]]]))
