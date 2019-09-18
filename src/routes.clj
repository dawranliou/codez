(ns routes
  (:require [coast]
            [layouts]
            [middlewares]))

(def routes
  (coast/routes

   (coast/site

    (coast/with-layout layouts/layout
      (coast/with
       middlewares/title
       [:get "/" :home/index]
       [:get "/z" :code/index]
       [:get "/z/new" :code/get-form]
       [:post "/z/new" :code/post-form]
       [:get "/z/:code-slug" :code/get-item])

      ;; admin
      [:get "/admin/sign-in" :admin/sign-in]
      [:post "/admin/sign-in" :admin/create-session]
      (coast/with
       middlewares/auth middlewares/current-member
       [:get "/admin" :admin/index]
       [:post "/admin/sign-out" :admin/delete-session]
       [:get "/admin/code/:code-id/edit" :admin/edit-code-form]
       [:put "/admin/code/:code-id" :admin/change-code]
       [:delete "/admin/code/:code-id" :admin/delete-code])))

   [:get "/z/:code-slug/image.png" :code/get-image]))
