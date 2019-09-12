(ns home
  (:require [coast]))

(defn index [request]
  [:div.vh-100.dt
   [:h1.dtc.v-mid.code.f1.f-headline-ns.tc.pl3
    "CODEZ"]])

#_(defn index [request]
    (let [rows (coast/q '[:select *
                          :from code
                          :order id
                          :limit 10])]
      (container {:mw 8}
                 (when (not (empty? rows))
                   (link-to (coast/url-for ::build) "New code"))

                 (when (empty? rows)
                   (tc
                    (link-to (coast/url-for ::build) "New code")))

                 (when (not (empty? rows))
                   (table
                    (thead
                     (tr
                      (th "member")
                      (th "body")
                      (th "id")
                      (th "published-at")
                      (th "updated-at")
                      (th "slug")
                      (th "created-at")
                      (th "title")))
                    (tbody
                     (for [row rows]
                       (tr
                        (td (:code/member row))
                        (td (:code/body row))
                        (td (:code/id row))
                        (td (:code/published-at row))
                        (td (:code/updated-at row))
                        (td (:code/slug row))
                        (td (:code/created-at row))
                        (td (:code/title row))
                        (td
                         (link-to (coast/url-for ::view row) "View"))
                        (td
                         (link-to (coast/url-for ::edit row) "Edit"))
                        (td
                         (button-to (coast/action-for ::delete row) {:data-confirm "Are you sure?"} "Delete"))))))))))
