(ns server
  (:require [coast]
            [coast.env :as env]
            [routes]
            [nrepl.server :refer [start-server stop-server]])
  (:gen-class))

(def app (coast/app {:routes routes/routes}))

(defn -main [& [port]]
  (coast/server app {:port port}))

(defonce nrepl-server (if (= "dev" (env/env :coast-env))
                        nil
                        (do
                          (println "Starting nrepl server at port 7889")
                          (start-server :port 7889))))

(comment
  (-main))
