(ns migrations.20190912125700-create-table-code
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :code
                (text :title)
                (text :slug)
                (text :body)
                (text :language)
                (integer :published-at)
                (timestamps)))
