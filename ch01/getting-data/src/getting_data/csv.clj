(ns getting-data.csv
  (:use incanter.core
        incanter.io))
;; =======================================
;; slurp

(slurp "data/small-sample.csv")
;=> "Gomez,Addams,father\nMorticia,Addams,mother\nPugsley,Addams,brother\nWednesday,Addams,sister\nPubert,Addams,brother\nFester,Addams,uncle\nGrandmama,,grandmother\nThing,,hand\nLurch,,butler\nItt,,cousin\nCackle,,cousin\n"

;; =======================================
;; incanter dataset

(read-dataset "data/small-sample.csv")
;=> 
;=> |     :col0 |  :col1 |       :col2 |
;=> |-----------+--------+-------------|
;=> |     Gomez | Addams |      father |
;=> |  Morticia | Addams |      mother |
;=> |   Pugsley | Addams |     brother |
;=> | Wednesday | Addams |      sister |
;=> |    Pubert | Addams |     brother |
;=> |    Fester | Addams |       uncle |
;=> | Grandmama |        | grandmother |
;=> |     Thing |        |        hand |
;=> |     Lurch |        |      butler |
;=> |       Itt |        |      cousin |
;=> |    Cackle |        |      cousin |

(read-dataset "data/small-sample.csv" :header true)
;=> 
;=> |    :Gomez | :Addams |     :father |
;=> |-----------+---------+-------------|
;=> |  Morticia |  Addams |      mother |
;=> |   Pugsley |  Addams |     brother |
;=> | Wednesday |  Addams |      sister |
;=> |    Pubert |  Addams |     brother |
;=> |    Fester |  Addams |       uncle |
;=> | Grandmama |         | grandmother |
;=> |     Thing |         |        hand |
;=> |     Lurch |         |      butler |
;=> |       Itt |         |      cousin |
;=> |    Cackle |         |      cousin |


;; =======================================
;; data.csv
(require '[clojure.data.csv :as csv])

(csv/read-csv (slurp "data/small-sample.csv"))
;=> (["Gomez" "Addams" "father"] ["Morticia" "Addams" "mother"] ["Pugsley" "Addams" "brother"] ["Wednesday" "Addams" "sister"] ["Pubert" "Addams" "brother"] ["Fester" "Addams" "uncle"] ["Grandmama" "" "grandmother"] ["Thing" "" "hand"] ["Lurch" "" "butler"] ["Itt" "" "cousin"] ["Cackle" "" "cousin"])
