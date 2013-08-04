(ns getting-data.aggregate
  (:require [clojure.java.io :as io]
            [clojure
             [xml :as xml]
             [string :as string]
             [zip :as zip]]
            [net.cgrand [enlive-html :as html]])
  (:use incanter.core
        clj-time.coerce
        [clj-time.format :only (formatter formatters parse unparse)]
        edu.ucdenver.ccp.kr.kb
        edu.ucdenver.ccp.kr.rdf
        edu.ucdenver.ccp.kr.sparql
        edu.ucdenver.ccp.kr.sesame.kb
                [clojure.set :only (rename-keys)])
  (:import [java.io File]
           [java.net URL URLEncoder]))


(defn replace-month-str [month-str]
  ;; (replace-month-str "Aug 04, 2013 19:08 UTC")
  ;; ;=> "08 04, 2013 19:08 UTC"

  (let [months {:Jan 1, :Feb 2, :Mar 3, :Apr 4, :May 5,
                :Jun 6, :Jul 7, :Aug 8, :Sep 9, :Oct 10,
                :Nov 11, :Dec 12}
        month-str "Aug 04, 2013 19:08 UTC"]
    (if-let [[k v] (some (fn [[k v]]
                           (if (re-find (re-pattern (name k)) month-str) [k v]))
                         months)]
      (string/replace month-str (re-pattern (name k)) (format "%02d" v)))))

;; ===========
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

(defn rekey
  ([k-map map]
     (rename-keys (select-keys map (keys k-map)) k-map)))

;; ===========
;; pull out the timestamp
(defn find-time-stamp
  ([module-content]
     (second
      (map html/text
           (html/select module-content
                        [:span.ratesTimestamp])))))

(def time-stamp-format
  ;; (formatter "MMM dd, yyyy HH:mm 'UTC'"))
  (formatter "MM dd, yyyy HH:mm 'UTC'"))

(defn normalize-date
  ([date-time]
     (unparse (formatters :date-time)
              ;;(parse time-stamp-format date-time))))
              (parse time-stamp-format (replace-month-str date-time)))))

;; get countries, exchange rates

(defn find-data
  ([module-content]
     (html/select module-content
                  [:table.tablesorter.ratesTable
                   :tbody :tr])))
(defn td->code
  ([td]
     (let [code (-> td
                    (html/select [:a])
                    first
                    :attrs
                    :href
                    (string/split #"=")
                    last)]
       (symbol "currency" (str code "#" code)))))

(defn get-td-a
  ([td]
     (->> td
          :content
          (mapcat :content)
          string/join
          read-string)))

(defn get-data
  ([row]
     (let [[td-header td-to td-from]
           (filter map? (:content row))]
       {:currency (td->code td-to)
        :exchange-to (get-td-a td-to)
        :exchange-from (get-td-a td-from)})))

;;take data from html page. generate RDF triple list.
(defn data->statements
  ([time-stamp data]
     (let [{:keys [currency exchange-to]} data]
       (list [currency 'err/exchangeRate exchange-to]
             [currency 'err/exchangeWith
              'currency/USD#USD]
             [currency 'err/exchangeRateDate
              [time-stamp 'xsd/dateTime]]))))

;; pulling data, convert triple, adding database
(defn load-exchange-data
  "This downloads the HTML page and pulls the data out
of it."
  [kb html-url]
  (let [html (html/html-resource html-url)
        div (html/select html [:div.moduleContent])
        time-stamp (normalize-date
                    (find-time-stamp div))]
    (add-statements
     kb
     (mapcat (partial data->statements time-stamp)
             (map get-data (find-data div))))))

;; gogoog
(defn aggregate-data
  "This controls the process and returns the aggregated data."
  [kb data-file data-url q col-map]
  (load-rdf-file kb (File. data-file))
  (load-exchange-data kb (URL. data-url))
  (to-dataset (map (partial rekey col-map) (query kb q))))

(def t-store (init-kb (kb-memstore)))

(def q
  '((?/c rdf/type money/Currency)
    (?/c money/name ?/name)
    (?/c money/shortName ?/shortName)
    (?/c money/isoAlpha ?/iso)
    (?/c money/minorName ?/minorName)
    (?/c money/minorExponent ?/minorExponent)
    (:optional
     ((?/c err/exchangeRate ?/exchangeRate)
      (?/c err/exchangeWith ?/exchangeWith)
      (?/c err/exchangeRateDate ?/exchangeRateDate)))))


(def col-map {'?/name :fullname
              '?/iso :iso
              '?/shortName :name
              '?/minorName :minor-name
              '?/minorExponent :minor-exp
              '?/exchangeRate :exchange-rate
              '?/exchangeWith :exchange-with
              '?/exchangeRateDate :exchange-date})


(view (aggregate-data t-store "data/currencies.ttl"
                "http://www.x-rates.com/table/?from=USD&amount=1.00"
                q col-map))
;=> #<JFrame javax.swing.JFrame[frame0,0,0,400x600,invalid,layout=java.awt.BorderLayout,title=Incanter Dataset,resizable,normal,defaultCloseOperation=HIDE_ON_CLOSE,rootPane=javax.swing.JRootPane[,8,31,384x561,invalid,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]>
