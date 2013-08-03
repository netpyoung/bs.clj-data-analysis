(ns getting-data.sqlite
  (:use incanter.core
        [clojure.java.jdbc :exclude (resultset-seq)]))

;; doall
;; ref: http://clojuredocs.org/clojure_core/clojure.core/doall


(defn load-table-data
  [db table-name]
  (let [sql (str "SELECT * FROM " table-name ";")]
    (with-connection db
      (with-query-results rs [sql]
        (to-dataset (doall rs))))))

(def db {:subprotocol "sqlite"
         :subname "data/small-sample.sqlite"
         :classname "org.sqlite.JDBC"})

(load-table-data db 'people)
;=> 
;=> |   :relation | :surname | :given_name |
;=> |-------------+----------+-------------|
;=> |      father |   Addams |       Gomez |
;=> |      mother |   Addams |    Morticia |
;=> |     brother |   Addams |     Pugsley |
;=> |      sister |   Addams |   Wednesday |
;=> |     brother |   Addams |      Pubert |
;=> |       uncle |   Addams |      Fester |
;=> | grandmother |          |   Grandmama |
;=> |        hand |          |       Thing |
;=> |      butler |          |       Lurch |
;=> |      cousin |          |         Itt |
;=> |      cousin |          |      Cackle |

(view (load-table-data db 'people))
;=> #<JFrame javax.swing.JFrame[frame0,0,0,400x600,layout=java.awt.BorderLayout,title=Incanter Dataset,resizable,normal,defaultCloseOperation=HIDE_ON_CLOSE,rootPane=javax.swing.JRootPane[,8,31,384x561,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]>
