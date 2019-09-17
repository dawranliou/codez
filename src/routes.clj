(ns routes
  (:require [coast]
            [layouts]))

(def routes
  (coast/routes
   (coast/site
    (coast/with-layout layouts/layout
      [:get "/" :home/index]
      [:get "/z" :code/index]
      [:get "/z/new" :code/get-form]
      [:post "/z/new" :code/post-form]
      [:get "/z/:code-slug" :code/get-item]))
   [:get "/z/:code-slug/image.png" :code/get-image]))
