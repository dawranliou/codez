(ns components
  (:require [coast]))

(def footer-items
  '([:a.f6.dib.ph2.link.dim.black.underline {:href "https://github.com/dawran6/codez/issues"} "Report an issue"]
    [:a.f6.dib.ph2.link.dim.black.underline {:href "https://github.com/dawran6/codez"} "Source"]
    [:a.f6.dib.ph2.link.dim.black.underline {:href "mailto://dawran6@gmail.com"} "Contact Me"]))

(defn nav [request]
  [:nav.w-100
   [:ul.list.pa3.mh3-ns.ba
    [:li.mb3 [:h1.code.f3.f-1-ns.lift.ttu [:a.link.dim.black.code {:href (coast/url-for :home/index)} "Codez"]]]
    [:li.mb3
     [:a.link.dim.black.code
      {:href (coast/url-for :code/index)}
      "#all"]]
    (for [{:code/keys [language]} (coast/q [:select :distinct :code/language :from :code])]
      [:li.mb3
       [:a.link.dim.black.code
        {:href (coast/url-for :code/index {:language language})}
        (str "#" language)]])
    (for [footer-item footer-items]
      [:li.mb3.f6 footer-item])]])

(defn footer []
  [:footer.pv4.ph3.ph5-m.ph6-l
   [:small.f6.db.tc
    "© 2019 " [:b.ttu "Daw-Ran Liou"] ", All Rights Reserved"]
   [:div.tc.mt3
    footer-items]])

(defn link-to [url & body]
  [:a {:href url :class "f6 link underline black"}
   body])

(defn button-to
  ([am m s]
   (let [data (select-keys m [:data-confirm])
         form (select-keys am [:action :_method :method :class])]
     (coast/form (merge {:class "dib ma0"} form)
                 [:input (merge data {:class "input-reset pointer link underline bn f6 br2 ma0 pa0 dib blue bg-transparent"
                                      :type  "submit"
                                      :value s})])))
  ([am s]
   (button-to am {} s)))

(defn container [m & body]
  (let [mw (or (:mw m) 8)]
    [:div {:class (str "pa4-ns w-100 center mw" mw)}
     [:div {:class "overflow-auto"}
      body]]))

(defn submit [value]
  [:input {:class "input-reset pointer dim ml3 db bn f6 br2 ph3 pv2 dib white bg-black"
           :type  "submit"
           :value value}])

(defn form-for
  ([k body]
   (form-for k {} body))
  ([k m body]
   (form-for k m {} body))
  ([k m params body]
   (coast/form-for k m (merge params {:class "pa4"})
                   [:div {:class "measure"}
                    body])))

(defn label [m s]
  [:label (merge {:for s :class "f6 b db mb2"} m) s])

(defn input [m]
  [:input (merge {:class "input-reset ba b--black-20 pa2 mb2 db w-100 outline-0"} m)])

(defn text-muted [s]
  [:div {:class "f6 tc gray"}
   s])

(defn dt [s]
  [:dt {:class "f6 b mt2"} s])

(defn dd [s]
  [:dd {:class "ml0"} s])

(defn dl [& body]
  [:dl body])
