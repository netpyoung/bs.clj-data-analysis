(ns playground.sampling)

;; sampling by percentage
(defn sample-percent
  [k coll] (filter (fn [_] (<= (rand) k)) coll))

(sample-percent 0.01 (range 1000))

;; sampling exactly by count
* Donald Knuth's algorithm S from The Art of Computer Programming, Volume 2

(defn rand-replace
  [m k v]
  (assoc (dissoc m (rand-nth (keys m))) k v))

(defn range-from
  [x]
  (map (partial + x) (range)))

(defn sample-amount
  [k coll]
  (->> coll
       (drop k)
       (map vector (range-from (inc k)))
       (filter #(<= (rand) (/ k (first %))))
       (reduce rand-replace
               (into {} (map vector (range k) (take k coll))))
       (sort-by first)
       (map second)))

(sample-amount 10 (range 1000))

