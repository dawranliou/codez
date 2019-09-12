(ns routes
  (:require [coast]
            [layouts]))

(def routes
  (coast/routes
   (coast/site
    (coast/with-layout layouts/layout
      [:get "/" :home/index]))))
