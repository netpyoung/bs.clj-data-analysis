(ns getting-data.rdf
  (:use incanter.core
        edu.ucdenver.ccp.kr.kb
        edu.ucdenver.ccp.kr.rdf
        edu.ucdenver.ccp.kr.sparql
        edu.ucdenver.ccp.kr.sesame.kb
        clojure.set)
  (:import [java.io File]))

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

(def tstore (init-kb (kb-memstore)))

(def q '((?/c rdf/type money/Currency)
         (?/c money/name ?/full_name)
         (?/c money/shortName ?/name)
         (?/c money/symbol ?/symbol)
         (?/c money/minorName ?/minor_name)
         (?/c money/minorExponent ?/minor_exp)
         (?/c money/isoAlpha ?/iso)
         (?/c money/currencyOf ?/country)))

(defn header-keyword [header-symbol]
  (keyword (.replace (name header-symbol) \_ \-)))

(defn fix-headers [coll]
  (into {} (map (fn [[k v]] [(header-keyword k) v]) coll)))

(defn load-data [k rdf-file q]
  (load-rdf-file k rdf-file)
  (to-dataset (map fix-headers (query k q))))

(view (load-data tstore (File. "data/currencies.ttl") q))
;=> #<JFrame javax.swing.JFrame[frame0,0,0,400x600,invalid,layout=java.awt.BorderLayout,title=Incanter Dataset,resizable,normal,defaultCloseOperation=HIDE_ON_CLOSE,rootPane=javax.swing.JRootPane[,8,31,384x561,invalid,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]>
