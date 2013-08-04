(ns getting-data.html-table
  (:use incanter.core)
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]))

(import [java.net URL])

;; data/small-sample-table.html
;; <table id="data" border="0">
;;   <tr><th>Given Name</th> <th>Surname</th> <th>Relation</th></tr>
;;   <tr><td>Gomez</td> <td>Addams</td> <td>father</td></tr>
;;   ...
;; </table>

(defn to-keyword [input]
  (-> input
      string/lower-case
      (string/replace \space \-)
      keyword))

(to-keyword "Hello World")
;=> :hello-world


(defn load-data [url]
  (let [html (html/html-resource (URL. url))

        ;; <table id="data" border="0">
        table (html/select html [:table#data])

        ;;   <tr><th>Given Name</th> <th>Surname</th> <th>Relation</th></tr>
        headers (->> (html/select table [:tr :th])
                     (map html/text)
                     (map to-keyword)
                     vec)

        ;;   <tr><td>Gomez</td> <td>Addams</td> <td>father</td></tr>
        rows (->> (html/select table [:tr])
                  (map #(html/select % [:td]))
                  (map #(map html/text %))
                  (filter seq))]
    (dataset headers rows)))

(html/html-resource (URL. "http://www.ericrochester.com/clj-data-analysis/data/small-sample-table.html"))
;=> ({:type :dtd, :data ["HTML" nil nil]} ...

(html/html-resource (java.io.StringReader. (slurp "data/small-sample-table.html")))
;=> ({:type :dtd, :data ["HTML" nil nil]} ...

(load-data "http://www.ericrochester.com/clj-data-analysis/data/small-sample-table.html");=> 
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
