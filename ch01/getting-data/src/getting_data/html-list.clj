(ns getting-data.html-list
  (:use incanter.core)
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]))

(import [java.net URL])

;; data/small-sample-list.html
;; <article>
;;   <header>
;;     <h2 id='addams'>Addam's Family</h2>
;;   </header>
;;   <p>Here's some information about the Addam's Family.</p>
;;   <ul>
;;     <li><em>Gomez Addams</em> &mdash; father</li>
;;     <li><em>Morticia Addams</em> &mdash; mother</li>
;;     ...
;;   </ul>
;; </article>
;; 
;; <article>
;;   <header>
;;     <h2 id='munsters'>The Munsters</h2>
;;   </header>
;;   <p>A different take.</p>
;;   <ul>
;;     <li><em>Herman Munster</em> &mdash; father</li>
;;     ...
;;   </ul>
;; </article>

(defn get-family
  ([article]
     ;; <article>
     ;;   <header>
     ;;     <h2 id='addams'>Addam's Family</h2>
     ;;   </header>

     (string/join (map html/text (html/select article [:header :h2])))))

(defn get-person
  ([li]
     ;;     <li><em>Gomez Addams</em> &mdash; father</li>
     ;; pnames: Gomez Addams
     ;; rel: - father
     (let [[{pnames :content} rel] (:content li)]
       (println pnames)
       {:name (apply str pnames)
        :relationship (string/trim rel)})))

(defn get-rows
  ([article]
     (let [family (get-family article)]
       (map #(assoc % :family family)

            ;;   <ul>
            ;;     <li><em>Gomez Addams</em> &mdash; father</li>
            (map get-person (html/select article [:ul :li]))))))

(defn load-data [html-url]
  (let [html (html/html-resource (URL. html-url))
        articles (html/select html [:article])]
    (to-dataset (mapcat get-rows articles))))

(load-data "http://www.ericrochester.com/clj-data-analysis/data/small-sample-list.html")
;=> 
;=> |        :family |            :name | :relationship |
;=> |----------------+------------------+---------------|
;=> | Addam's Family |     Gomez Addams |      — father |
;=> | Addam's Family |  Morticia Addams |      — mother |
;=> | Addam's Family |   Pugsley Addams |     — brother |
;=> | Addam's Family | Wednesday Addams |      — sister |
;=> | Addam's Family |    Pubert Addams |     — brother |
;=> | Addam's Family |    Fester Addams |       — uncle |
;=> | Addam's Family |        Grandmama | — grandmother |
;=> | Addam's Family |            Thing |        — hand |
;=> | Addam's Family |            Lurch |      — butler |
;=> | Addam's Family |       Cousin Itt |      — cousin |
;=> | Addam's Family |    Cousin Cackle |      — cousin |
;=> |   The Munsters |   Herman Munster |      — father |
;=> |   The Munsters |     Lily Munster |      — mother |
;=> |   The Munsters |  Grandpa Dracula | — grandfather |
;=> |   The Munsters |    Eddie Munster |     — brother |
;=> |   The Munsters |  Marilyn Munster |      — sister |
;=> |   The Munsters |        The Raven |         — pet |
