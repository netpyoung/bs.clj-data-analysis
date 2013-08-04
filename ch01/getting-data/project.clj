(defproject getting-data "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]

                 ;; for incanter itself
                 [incanter/incanter-core "1.5.1"]

                 ;; for csv
                 [incanter/incanter-io "1.5.1"]
                 [org.clojure/data.csv "0.1.2"]

                 ;; for json
                 [org.clojure/data.json "0.2.2"]

                 ;; for excel
                 [incanter/incanter-excel "1.5.1"]

                 ;; for sqlite
                 [org.clojure/java.jdbc "0.3.0-alpha4"]
                 [org.xerial/sqlite-jdbc "3.7.2"]

                 ;; for html - table, ul
                 [enlive "1.1.1"]

                 ;; for rdf
                 [edu.ucdenver.ccp/kr-sesame-core "1.4.8"]

                 [org.clojure/tools.logging "0.2.6"]
                 [org.slf4j/slf4j-simple "1.7.5"]
                 ])
