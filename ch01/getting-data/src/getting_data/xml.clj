(ns getting-data.xml
  (:use incanter.core
        clojure.xml
        [clojure.zip :exclude [next replace remove] :as z]))


;; <?xml version="1.0" encoding="utf-8"?>
;; <data>
;;   <person>
;;     <given-name>Gomez</given-name>
;;     <surname>Addams</surname>
;;     <relation>father</relation>
;;   </person>
;;   ...
;; </data>


(defn load-xml-data [xml-file first-data next-data]
  (let [data-map (fn [node]
                   [(:tag node) (first (:content node))])]
    (->> (parse xml-file)
         z/xml-zip

         ;; ??
         first-data
         (iterate next-data)
         (take-while #(not (nil? %)))
         (map z/children)

         (map #(mapcat data-map %))
         (map #(apply array-map %))

         to-dataset)))

(load-xml-data "data/small-sample.xml" z/down z/right)
;=> 
;=> | :given-name | :surname |   :relation |
;=> |-------------+----------+-------------|
;=> |       Gomez |   Addams |      father |
;=> |    Morticia |   Addams |      mother |
;=> |     Pugsley |   Addams |     brother |
;=> |   Wednesday |   Addams |      sister |
;=> |      Pubert |   Addams |     brother |
;=> |      Fester |   Addams |       uncle |
;=> |   Grandmama |          | grandmother |
;=> |       Thing |          |        hand |
;=> |       Lurch |          |      butler |
;=> |         Itt |          |      cousin |
;=> |      Cackle |          |      cousin |
