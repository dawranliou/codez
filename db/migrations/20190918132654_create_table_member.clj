(ns migrations.20190918132654-create-table-member
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :member
    (text :email)
    (text :display-name)
    (text :password)
    (timestamps)))