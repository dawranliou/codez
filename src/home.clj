(ns home
  (:require [coast]
            [helpers]))

(defn index [_]
  (let [snippets (coast/q '[:select * :from code :order id desc :where ["published_at is not null"] :limit 50])]
    [:div.vh-100-ns.dt-ns.dt--fixed-ns.w-100
     [:div.dtc-ns.v-mid.tc
      [:h1.dib.code.f1.f-headline-ns.lift.ttu
       "Codez"] ]

     [:div.overflow-y-scroll-ns.vh-100-ns
      [:a.db.mt3.bg-white.gray.pv3.ph2.link.code.cursor-text;;.bg-animate.hover-bg-black.hover-white
       {:href (coast/url-for :code/get-form)} "// Add new codez here:"]
      (for [{:code/keys [body language title slug published-at] :as record} snippets]
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
           body]]])]]))
