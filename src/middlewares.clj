(ns middlewares
  (:require [coast]))

(defn title [handler]
  (fn [request]
    (let [title (case (:coast.router/name request)
                  :code/index           (if-let [language (-> request :params :language)]
                                          (str "Codez | #" language)
                                          "Codez | #all")
                  :code/get-item        (str "Codez | " (-> request :params :code-slug))
                  :code/get-form        "Codez | New Codez"
                  :admin/index          "Codez | Admin"
                  :admin/edit-code-form "Codez | Edit"
                  :admin/sign-in "Codez | Admin Sign In"
                  "Codez")]
      (-> (assoc request :title title)
          (handler)))))

(defn auth
  [handler]
  (fn [request]
    (if (get-in request [:session :member/email])
      (handler request)
      (coast.responses/forbidden "Sorry pal, you are forbidden here."))))

(defn current-member
  [handler]
  (fn [request]
    (let [email  (get-in request [:session :member/email])
          member (coast/find-by :member {:member/email email})]
      (handler (assoc request :member member)))))
