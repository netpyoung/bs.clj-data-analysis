(ns getting-data.core
  (:use incanter.core
        incanter.io))

(slurp "data/small-sample.csv")

(read-dataset "data/small-sample.csv")
