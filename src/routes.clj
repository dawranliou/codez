(ns routes
  (:require [coast]
            [layouts]))

(def routes
  (coast/routes
   (coast/site
    (coast/with-layout layouts/layout
      [:get "/" :home/index]
      [:get "/z" :code/get-form]
      [:post "/z" :code/post-form]
      [:get "/z/:code-slug" :code/get-item]))))
