(ns home
  (:require [coast]
            [helpers]))

(defn index [_]
  (let [snippets (coast/q '[:select * :from code :order id desc :where ["published_at is not null"]])]
    [:div.vh-100-ns.dt-ns.dt--fixed-ns.w-100
     [:div.dtc-ns.v-mid.tc
      [:h1.dib.code.f1.f-headline-ns.lift
       "CODEZ"] ]

     [:div.overflow-y-auto-ns
      (for [{:code/keys [body language title slug published-at] :as record} snippets]
        [:div.mt4
         [:h2.f4.dib
          [:a.link.dim.black.code {:href (coast/url-for :code/get-item record)}
           (str slug " \"" title "\"")]]
         [:time.f6.dib.ml3.code
          {:data-seconds published-at
           :data-date    true}
          (helpers/time-ago published-at)]
         [:pre
          [:code {:class language}
           body]]])]]))
