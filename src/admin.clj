(ns admin
  (:require [coast]
            [buddy.hashers :as hashers]
            [components :refer [container link-to button-to text-muted dl dd dt submit input label]]))

(defn table [& body]
  [:table {:class "f6 w-100" :cellspacing 0}
   body])

(defn thead [& body]
  [:thead body])

(defn tbody [& body]
  [:tbody {:class "lh-copy"} body])

(defn tr [& body]
  [:tr {:class "stripe-dark"}
   body])

(defn th
  ([s]
   [:th {:class "fw6 tl pa3 bg-white"} s])
  ([]
   (th "")))

(defn td [& body]
  [:td {:class "pa3"} body])

(defn mr2 [body]
  [:span.mr2 body])

(defn index [request]
  (let [code (coast/q '[:select * :from code :order id desc])]
    (container
     {:mw 9}
     [:h1 "Dashboard"]

     [:div.mv3
      (coast/form-for ::delete-session
                      (submit "Sign out"))]

     [:div.mv5
      [:h2 "code"]

      (table
       (thead
        (tr
         (th "id")
         (th "created-at")
         (th "updated-at")
         (th "published-at")
         (th "slug")
         (th "title")
         (th "")
         (th "")))
       (tbody
        (for [row code]
          (tr
           (td (:code/id row))
           (td (:code/created-at row))
           (td (:code/updated-at row))
           (td (:code/published-at row))
           (td (:code/slug row))
           (td (:code/title row))
           (td
            (link-to (coast/url-for ::edit-code-form row) "Edit"))
           (td
            (button-to (coast/action-for ::delete-code row) {:data-confirm "Are you sure?"} "Delete"))))))])))

(defn errors [m]
  [:div {:class "bg-red white pa2 mb4 br1"}
   [:h2 {:class "f4 f-subheadline"} "Errors Detected"]
   [:dl
    (for [[k v] m]
      [:div {:class "mb3"}
       (dt (str k))
       (dd v)])]])

(defn edit-code-form [request]
  (let [code (coast/fetch :code (-> request :params :code-id))]
    (container {:mw 8}
               (when (some? (:errors request))
                 (errors (:errors request)))

               (coast/form-for ::change-code code
                               (label {:for "code/body"} "body")
                               [:textarea.db.w-100.vh-50.pa2.mb2.code {:name "code/body"} (:code/body code)]

                               (label {:for "code/published-at"} "published-at")
                               (input {:type "text" :name "code/published-at" :value (:code/published-at code)})

                               (label {:for "code/slug"} "slug")
                               (input {:type "text" :name "code/slug" :value (:code/slug code)})

                               (label {:for "code/title"} "title")
                               (input {:type "text" :name "code/title" :value (:code/title code)})

                               (label {:for "code/language"} "language")
                               (input {:type "text" :name "code/language" :value (:code/language code)})

                               (link-to (coast/url-for ::index) "Cancel")
                               (submit "Update code")))))

(defn change-code [request]
  (let [code       (coast/fetch :code (-> request :params :code-id))
        [_ errors] (-> (select-keys code [:code/id])
                       (merge (:params request))
                       (coast/validate [[:required [:code/id :code/body :code/published-at :code/slug :code/title :code/language]]])
                       (select-keys [:code/id :code/body :code/published-at :code/slug :code/title :code/language])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (edit-code-form (merge request errors)))))

(defn delete-code [request]
  (let [[_ errors] (-> (coast/fetch :code (-> request :params :code-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (-> (coast/redirect-to ::index)
          (coast/flash "Something went wrong!")))))

(defn sign-in
  [request]
  (container
   {:mw 6}
   [:h1 "Admin"]
   (when-let [error (:error/message request)]
     [:div error])
   (coast/form-for
    ::create-session
    (input {:type "email" :name "member/email"})
    (input {:type "password" :name "member/password"})
    (submit "Submit"))))

(defn create-session
  [request]
  (let [email           (get-in request [:params :member/email])
        member          (coast/find-by :member {:email email})
        [valid? errors] (-> (:params request)
                            (select-keys [:member/email :member/password])
                            (coast/validate [[:email [:member/email]]
                                             [:required [:member/email :member/password]]])
                            (get :member/password)
                            (hashers/check (:member/password member))
                            (coast/rescue))]
    (if (or (some? errors) (false? valid?))
      (sign-in (merge request {:error/message "Invalid email or password"}))
      (-> (coast/redirect-to ::index)
          (assoc :session {:member/email email})))))

(defn delete-session
  [_]
  (-> (coast/redirect-to ::sign-in)
      (assoc :session nil)))

(comment
  ;; sign up a admin user
  (-> #:member{:email    "admin@email.com"
               :password "password"}
      (update :member/password hashers/derive)
      (coast/insert)))
