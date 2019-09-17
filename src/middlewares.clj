(ns middlewares
  (:require [coast]))

(defn title [handler]
  (fn [request]
    (let [title (case (:coast.router/name request)
                  :code/index    (if-let [language (-> request :params :language)]
                                   (str "Codez | #" language)
                                   "Codez | #all")
                  :code/get-item (str "Codez | " (-> request :params :code-slug))
                  :code/get-form "Codez | New Codez"
                  "Codez")]
      (-> (assoc request :title title)
          (handler)))))
