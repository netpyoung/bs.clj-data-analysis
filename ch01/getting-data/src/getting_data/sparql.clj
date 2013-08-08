(ns getting-data.sparql
  (:require [clojure.java.io :as io]
            [clojure
             [xml :as xml]
             [pprint :as pp]
             [zip :as zip]])
  (:use incanter.core
        edu.ucdenver.ccp.kr.kb
        edu.ucdenver.ccp.kr.rdf
        edu.ucdenver.ccp.kr.sparql
        edu.ucdenver.ccp.kr.sesame.kb
        [clojure.set :only (rename-keys)])
  (:import [java.io File]
           [java.net URL URLEncoder]))

(defn kb-memstore []
  (kb :sesame-mem))

(defn init-kb [kb-store]
  (register-namespaces
   kb-store
   '(("geographis"   "http://telegraphis.net/ontology/geography/geography#")
     ("code"         "http://telegraphis.net/ontology/measurement/code#")
     ("money"        "http://telegraphis.net/ontology/money/money#")
     ("owl"          "http://www.w3.org/2002/07/owl#")
     ("rdf"          "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
     ("xsd"          "http://www.w3.org/2001/XMLSchema#")
     ("currency"     "http://telegraphis.net/data/currencies/")
     ("dbpedia"      "http://dbpedia.org/resource/")
     ("dbpedia-ont"  "http://dbpedia.org/ontology/")
     ("dbpedia-prop" "http://dbpedia.org/property/")
     ("err"          "http://ericrochester.com/"))))

(select-keys {:a 1 :b 2} [:a :c])
;=> {:a 1}

(rename-keys {:a 1, :b 2} {:a :new-a, :b :new-b})
;=> {:new-b 2, :new-a 1}

(rename-keys (select-keys {:a 1 :b 2} [:a :c]) {:a 10 :c 30})
;=> {10 1}

(defn rekey
  ([k-map map]
     (rename-keys (select-keys map (keys k-map)) k-map)))

;; ====

(defn make-query
  "This creates a query that returns all the triples related to a subject URI.
  It does filter out non-English strings."
  ([subject kb]
   (binding [*kb* kb
             *select-limit* 200]
     (sparql-select-query
       (list `(~subject ?/p ?/o)
             '(:or (:not (:isLiteral ?/o))
                   (!= (:datatype ?/o) rdf/langString)
                   (= (:lang ?/o) ["en"])))))))

(defn make-query-uri
  "This constructs a URI for the query."
  ([base-uri query]
   (URL. (str base-uri
              "?format=" (URLEncoder/encode "text/xml")
              "&query=" (URLEncoder/encode query)))))

(defn result-seq
  "This takes the first result and returns a sequence of this node, plus all
  the nodes to the right of it."
  ([first-result]
   (cons (zip/node first-result)
         (zip/rights first-result))))

(defn binding-str
  "This takes a binding, pulls out the first tag's content, and concatenates it
  into a string."
  ([b]
   (apply str (:content (first (:content b))))))

(defn result-to-kv
  "This takes a result node and creates a key-value vector pair from it."
  ([r]
   (let [[p o] (:content r)]
     [(binding-str p) (binding-str o)])))

(defn accum-hash
  "This takes a map and key-value vector pair and adds the pair to the map. If
  the key is already in the map, the current value is converted to a vector and
  the new value is added to it."
  ([m [k v]]
   (if-let [current (m k)]
     (assoc m k (conj current v))
     (assoc m k [v]))))

(defn pull-result [set]
  ;; (pull-result {:a ["123"] :b ["456"]})
  ;; ;=> {:a "123", :b "456"}
  (into {} (for [[x y] set] [x (first y)])))

(defn query-sparql-results
  "This queries a SPARQL endpoint and returns a sequence of result nodes."
  ([sparql-uri subject kb]
   (->>
     kb
     ;; Build the URI query string.
     (make-query subject)
     (make-query-uri sparql-uri)
     ;; Get the results, parse the XML, and return the zipper.
     io/input-stream
     xml/parse
     zip/xml-zip
     ;; Find the first child.
     zip/down
     zip/right
     zip/down
     ;; Convert all children into a sequence.
     result-seq)))

(defn load-data
  "This loads the data about a currency for the given IRI."
  [sparql-uri subject col-map]
  (->>
    ;; Initialize the triple store.
    (kb-memstore)
    init-kb
    ;; Get the reults
    (query-sparql-results sparql-uri subject)
    ;; Generate a mapping.
    (map result-to-kv)
    (reduce accum-hash (array-map))
    ;; Translate the keys in the map.
    (rekey col-map)

    pull-result

    to-dataset))


;; =============

(def rdfs "http://www.w3.org/2000/01/rdf-schema#")
(def dbpedia "http://dbpedia.org/resource/")
(def dbpedia-ont "http://dbpedia.org/ontology/")
(def dbpedia-prop "http://dbpedia.org/property/")

(def col-map {(str rdfs 'label) :name,
              (str dbpedia-prop 'usingCountries) :country
              (str dbpedia-prop 'peggedWith) :pegged-with
              (str dbpedia-prop 'symbol) :symbol
              (str dbpedia-prop 'usedBanknotes) :used-banknotes
              (str dbpedia-prop 'usedCoins) :used-coins
              (str dbpedia-prop 'inflationRate) :inflation})

(view (load-data "http://dbpedia.org/sparql"
                 (symbol (str dbpedia "/United_Arab_Emirates_dirham"))
                 col-map))
;=> #<JFrame javax.swing.JFrame[frame7,0,0,400x600,layout=java.awt.BorderLayout,title=Incanter Dataset,resizable,normal,defaultCloseOperation=HIDE_ON_CLOSE,rootPane=javax.swing.JRootPane[,8,31,384x561,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]>

(make-query
 (symbol (str dbpedia "/United_Arab_Emirates_dirham"))
 (init-kb (kb-memstore)))
;=> "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \nSELECT ?p ?o \nWHERE {  <http://dbpedia.org/resource/United_Arab_Emirates_dirham> ?p ?o .  \n FILTER (  ( ! isLiteral(?o)  \n ||  (  datatype(?o)  != <http://www.w3.org/1999/02/22-rdf-syntax-ns#langString> )  \n ||  (  lang(?o)  = \"en\" )  ) \n )  \n} LIMIT 200 "