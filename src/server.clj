(ns server
  (:require [coast]
            [routes]
            [nrepl.server :refer [start-server stop-server]])
  (:gen-class))

(defonce nrepl-server
  (do
    (println "nrepl starting at port 7888")
    (start-server :port 7888)))

(def app (coast/app {:routes routes/routes}))

(defn -main [& [port]]
  (coast/server app {:port port}))

(comment
  (-main))
