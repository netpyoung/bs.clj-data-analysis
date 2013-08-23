(ns playground.diff_fuzzy)

(require '[clj-diff.core :as diff])


(def FUZZY_MAX_DIFF 2)
(def FUZZY_PERCENT_DIFF 0.1)
(def fuzzy-dist diff/edit-distance)

(defn fuzzy=
  "This returns a fuzzy match."
  [a b]
  (let [dist (fuzzy-dist a b)]
    (or (<= dist FUZZY_MAX_DIFF)
        (<= (/ dist (min (count a) (count b))) FUZZY_PERCENT_DIFF))))

(defn records-match
  [key-fns a b]
  (letfn [(rfn [prev next-fn]
            (and prev (fuzzy= (next-fn a) (next-fn b))))]
    (reduce rfn true key-fns)))

(def data
  {:mulder {:given-name "Fox" :surname "Mulder"}
   :molder {:given-name "Fox" :surname "Molder"}
   :mulder2 {:given-name "fox" :surname "mulder"}
   :scully {:given-name "Dana" :surname "Scully"}
   :scully2 {:given-name "Dan" :surname "Scully"}})


(records-match [:given-name :surname] (data :mulder) (data :molder))
;=> true

(records-match [:given-name :surname] (data :mulder) (data :mulder2))
;=> true

(records-match [:given-name :surname] (data :scully) (data :scully2))
;=> true

(records-match [:given-name :surname] (data :mulder) (data :scully))
;=> false
