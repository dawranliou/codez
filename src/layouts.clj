(ns layouts
  (:require [coast]))

(defn layout [request body]
  [:html
   [:head
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    (coast/css "bundle.css")
    (coast/js "bundle.js")]
   [:body.bg-light-pink
    body]])