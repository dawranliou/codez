(ns helpers
  (:require [clojure.string :as string]
            [coast]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

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

;; https://stackoverflow.com/questions/32511405/how-would-time-ago-function-implementation-look-like-in-clojure
(defn time-ago [epoch-second]
  (let [units [{:name "second" :limit 60 :in-second 1}
               {:name "minute" :limit 3600 :in-second 60}
               {:name "hour" :limit 86400 :in-second 3600}
               {:name "day" :limit 604800 :in-second 86400}
               {:name "week" :limit 2629743 :in-second 604800}
               {:name "month" :limit 31556926 :in-second 2629743}
               {:name "year" :limit Long/MAX_VALUE :in-second 31556926}]
        diff  (t/in-seconds (t/interval (c/from-epoch epoch-second) (t/now)))]
    (if (< diff 5)
      "just now"
      (let [unit (first (drop-while #(or (>= diff (:limit %))
                                         (not (:limit %)))
                                    units))]
        (-> (/ diff (:in-second unit))
            Math/floor
            int
            (#(str % " " (:name unit) (when (> % 1) "s") " ago")))))))

(comment
  (time-ago (coast/now))
  (t/now)
  (c/from-epoch (coast/now))

  (slug))
