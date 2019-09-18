(ns code
  (:require [coast]
            [helpers]
            [components :as c]
            [clojure.java.shell :as shell]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn error [m]
  [:div {:class "bg-red white pa2 mb4 br1"}
   [:h2 {:class "f4 f-subheadline"} "Errors Detected"]
   [:dl
    (for [[k v] m]
      [:div {:class "mb3"}
       (c/dt (str k))
       (c/dd v)])]])

(defn index [request]
  (let [language (-> request :params :language)
        query    (-> [:select :* :from :code :order :id :desc]
                     (concat (when language [:where [:language language]])))
        records  (coast/q query)]
    [:div.vh-100-ns.dt-ns.w-100
     [:div.dtc-ns.v-mid.tc.w-20-ns
      (c/nav request)]

     [:div.overflow-y-scroll-ns.vh-100-ns
      (c/container
       {:mw 8}

       [:h1.code
        (str "#" (or language "all"))]

       [:a.db.mt3.bg-white.gray.pv3.ph2.link.code.cursor-text;;.bg-animate.hover-bg-black.hover-white
        {:href (coast/url-for :code/get-form {:language language})} "// Add new codez here:"]

       (for [{:code/keys [body language title slug published-at] :as record} records]
         [:div.mt3
          [:h2.f4.dib.mb0
           [:a.link.dim.black.code {:href (coast/url-for :code/get-item record)}
            (str slug " \"" title "\"")]]
          [:time.f6.dib.ml3.code
           {:data-seconds published-at
            :data-date    true}
           (helpers/time-ago published-at)]
          [:pre
           [:code {:class language}
            body]]]))]]))

(defn get-form [request]
  (c/container {:mw 8}
               [:h1 "New Codez"]
               (when (some? (:errors request))
                 (error (:errors request)))

               (coast/form-for ::post-form
                               (c/label {:for "code/title"} "Title")
                               (c/input {:type "text" :name "code/title" :value (-> request :params :code/title)})

                               (c/label {:for "code/language"} "Language (default: plaintext)")
                               (c/input {:type "text" :name "code/language" :value (-> request :params :language)})

                               (c/label {:for "code/body"} "Body")
                               [:textarea.db.w-100.vh-50.pa2.mb2.code {:name "code/body" :value (-> request :params :code/body)}]

                               (c/link-to (coast/url-for :home/index) "Cancel")
                               (c/submit "Submit"))))

(defn post-form [request]
  (let [[record errors] (-> (coast/validate (:params request) [[:required [:code/body]]])
                            (select-keys [:code/body :code/title :code/language])
                            (update :code/language #(or % "plaintext"))
                            (update :code/body string/trim)
                            (assoc :code/published-at (coast.time2/now))
                            (assoc :code/slug (helpers/slug))
                            (coast/insert)
                            (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::get-item record)
      (get-form (merge request errors)))))

(defn get-item [{:keys [params errors]}]
  (c/container
   {:mw 8}

   (when (some? errors) (error errors))

   (let [slug   (:code-slug params)
         record (coast/pluck '[:select * :from code :where [slug ?slug]] {:slug slug})]
     (if (some? record)
       (let [{:code/keys [title published-at body language]} record]
         [:article
          [:header
           [:a.f3.pv1.ph2.mr3.link.black.bg-animate.hover-white.hover-bg-black.code {:href (coast/url-for :code/index {:language language})} "<"]
           [:h2.f4.dib.mb0
            [:a.link.dim.black.code {:href (coast/url-for :code/get-item record)}
             (str slug " \"" title "\"")]]
           [:time.f6.dib.ml3.code
            {:data-seconds published-at
             :data-date    true}
            (helpers/time-ago published-at)]]
          [:pre
           [:code {:class language}
            body]]])

       (coast/raise {:not-found true})))))

(defn get-image [{:keys [params]}]
  (let [slug   (:code-slug params)
        record (coast/pluck '[:select * :from code :where [slug ?slug]] {:slug slug})]
    (if (nil? record)
      (coast/raise {:not-found true})

      (let [file-path (-> (shell/sh "bin/screenshot" slug)
                          :out
                          string/trim)]
        {:status  200
         :headers {"Content-Type" "image/png"}
         :body    (io/file file-path)}))))

(comment
  (def slug "0e380c374a0d")
  (shell/sh "node" "bin/screenshot.js" slug))
