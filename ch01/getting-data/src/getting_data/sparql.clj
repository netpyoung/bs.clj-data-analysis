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

(defn make-query
  ([subject kb]
     (binding [*kb* kb
               *select-limit* 200]
       (sparql-select-query
        (list '(~subject ?/p ?/o)
              
