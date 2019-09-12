(ns code
  (:require [coast]
            [helpers]
            [components :as c]))

(defn error [m]
  [:div {:class "bg-red white pa2 mb4 br1"}
   [:h2 {:class "f4 f-subheadline"} "Errors Detected"]
   [:dl
    (for [[k v] m]
      [:div {:class "mb3"}
       (c/dt (str k))
       (c/dd v)])]])

(defn get-form [request]
  (c/container {:mw 6}
               (when (some? (:errors request))
                 (error (:errors request)))

               (coast/form-for ::post-form
                               (c/label {:for "code/title"} "Title")
                               (c/input {:type "text" :name "code/title" :value (-> request :params :code/title)})

                               (c/label {:for "code/language"} "Language (default: plaintext)")
                               (c/input {:type "text" :name "code/language" :value (-> request :params :code/body)})

                               (c/label {:for "code/body"} "Body")
                               [:textarea.db.w-100.vh-50.pa2.mb2.code {:name "code/body" :value (-> request :params :code/body)}]

                               (c/link-to (coast/url-for :home/index) "Cancel")
                               (c/submit "Submit"))))

(defn post-form [request]
  (let [[record errors] (-> (coast/validate (:params request) [[:required [:code/body :code/title]]])
                            (select-keys [:code/body :code/title :code/language])
                            (update :code/language #(or % "plaintext"))
                            (assoc :code/published-at (coast.time2/now))
                            (assoc :code/slug (helpers/slug))
                            (coast/insert)
                            (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::get-item record)
      (get-form (merge request errors)))))

(defn get-item [{:keys [params errors]}]
  (c/container
   {:mw 6}
   (when (some? errors) (error errors))
   (let [slug   (:code-slug params)
         record (coast/pluck '[:select * :from code :where [slug ?slug]] {:slug slug})]
     (if (some? record)
       [:article
        [:h1 (:code/title record)]
        [:div.code
         {:class (:code/language record)}
         [:pre.code (:code/body record)]]]
       (coast/raise {:not-found true})))))
