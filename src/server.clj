(ns server
  (:require [coast]
            [routes]
            [nrepl.server :refer [start-server stop-server]])
  (:gen-class))

(defonce nrepl-server
  (do
    (println "nrepl starting at port 7889")
    (start-server :port 7889)))

(def app (coast/app {:routes routes/routes}))

(defn -main [& [port]]
  (coast/server app {:port port}))

(comment
  (-main))
