(ns helpers
  (:require [clojure.string :as string]
            coast))

(defn ellipsis [s n]
  (when (and (string? s)
             (number? n))
    (let [end (if (> (count s) n)
                n
                (count s))]
      (str (subs s 0 end)
           "..."))))

(defn slug
  ([]
   (last (string/split (str (coast/uuid)) #"-")))
  ([s]
   (str (-> (.toLowerCase s)
            (string/replace #"\s+" "-")
            (string/replace #"[^\w\-]+" "")
            (string/replace #"\-\-+" "-")
            (string/replace #"^-+" "")
            (string/replace #"-+$" ""))
        "-" (last (string/split (str (coast/uuid)) #"-")))))

(comment
  (slug))
