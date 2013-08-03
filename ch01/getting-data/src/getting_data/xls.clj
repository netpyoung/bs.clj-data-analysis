(ns getting-data.xls
  (:use incanter.core
        incanter.excel))

(read-xls "data/small-sample-header.xls")
;=> 
;=> | given-name | surname |    relation |
;=> |------------+---------+-------------|
;=> |      Gomez |  Addams |      father |
;=> |   Morticia |  Addams |      mother |
;=> |    Pugsley |  Addams |     brother |
;=> |  Wednesday |  Addams |      sister |
;=> |     Pubert |  Addams |     brother |
;=> |     Fester |  Addams |       uncle |
;=> |  Grandmama |         | grandmother |
;=> |      Thing |         |        hand |
;=> |      Lurch |         |      butler |
;=> |        Itt |         |      cousin |
;=> |     Cackle |         |      cousin |
