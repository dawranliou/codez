(ns home
  (:require [coast]
            [helpers]
            [components :as c]))

(defn index [_]
  (let [snippets  (coast/q '[:select * :from code :order id desc :where ["published_at is not null"] :limit 50])
        languages (coast/q [:select :distinct :code/language :from :code])]
    [:div.vh-100-ns.dt-ns.dt--fixed-ns.w-100
     [:div.dtc-ns.v-mid.tc
      [:h1.code.f1.f-headline-ns.lift.ttu
       [:a.link.black
        {:href (coast/url-for :code/index)}
        "Codez"]]
      [:p.f4.f2-ns
       "Copy, paste, and "
       [:a.link.dim.black.underline
        {:href (coast/url-for :code/get-form)}
        "share code:"]]
      [:ul.list.pl0
       [:li.dib
        [:a.link.dim.black.code
         {:href (coast/url-for :code/index)}
         "#all"]]
       (for [{:code/keys [language]} languages]
         [:li.dib.ml3
          [:a.link.dim.black.code
           {:href (coast/url-for :code/index {:language language})}
           (str "#" language)]])]]

     [:div.overflow-y-scroll-ns.vh-100-ns
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
           body]]])

      (c/footer)
      ]]))
