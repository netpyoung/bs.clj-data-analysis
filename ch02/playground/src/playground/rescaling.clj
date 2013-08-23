(ns playground.rescaling)

(require '[clojure.pprint :as pp])

(defn rescale-by-total
  [src dest coll]
  (let [total (reduce + (map src coll))
        update (fn [m] (assoc m dest (/ (m src) total)))]
    (map update coll)))

(defn rescale-by-group
  "group내의 freq의 합(total)으로, scaled(= freq / total)를 구한다."
  [src group dest coll]
  (mapcat (partial rescale-by-total src dest)
          (vals (group-by group
                          (sort-by group coll)))))

(def word-counts
  [{:word 'the, :freq 92, :doc 'a}
   {:word 'a, :freq 76,:doc 'a}
   {:word 'jack, :freq 4,:doc 'a}
   {:word 'the, :freq 3,:doc 'b}
   {:word 'a, :freq 2,:doc 'b}
   {:word 'mary, :freq 1,:doc 'b}])



(pp/pprint (rescale-by-group :freq :doc :scaled word-counts))
;>> ({:freq 92, :word the, :scaled 23/43, :doc a}
;>>  {:freq 76, :word a, :scaled 19/43, :doc a}
;>>  {:freq 4, :word jack, :scaled 1/43, :doc a}
;>>  {:freq 3, :word the, :scaled 1/2, :doc b}
;>>  {:freq 2, :word a, :scaled 1/3, :doc b}
;>>  {:freq 1, :word mary, :scaled 1/6, :doc b})
;=> nil
