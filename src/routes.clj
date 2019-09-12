(ns routes
  (:require [coast]
            [components]))

(def routes
  (coast/routes

    (coast/site
      (coast/with-layout components/layout
        [:get "/" :site.home/index]))

    (coast/api
      (coast/with-prefix "/api"
        [:get "/" :api.home/index]))))
