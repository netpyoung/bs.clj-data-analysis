Importing Data for Analysis
===========================


# Creating a new project

![!leiningen-banner.png](http://leiningen.org/img/leiningen-banner.png)

* leiningen: https://github.com/technomancy/leiningen
 - Java 프로젝트 매니저인, ant와 maven은 XML을 가지고 프로젝트를 관리한다.
 - 하지만, XML은 보고 수정하기 힘들다!
 - Leiningen은 프로젝트관리 파일 자체가 .clj 파일이다.
 - 참고: http://clojure.or.kr/wiki/doku.php?id=lecture:clojure:%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8&s[]=lein


# Incanter
Incanter는 R과 같이, Clojure기반의 통계계산 및 그래프를 위한 플랫폼이다.

![!incanter-i-logo-holo.png](http://incanter.org/incanter-i-logo-holo.png) 
* http://incanter.org/
* https://github.com/liebke/incanter


# CSV
* wiki:csv: http://en.wikipedia.org/wiki/Comma-separated_values
* 표준: RFC 4180 - http://tools.ietf.org/html/rfc4180

* 1967년 OS/360상의 IBM Fortran(level G) 컴파일러가 CSV를 지원함.

```
1997,Ford,E350
"1997","Ford","E350"
```

* incanter-io
 - `[incanter/incanter-io "1.5.1"]`
 - // Retrieving net/sf/opencsv/opencsv/2.3/opencsv-2.3.pom from central 로 봐서, opencsv를 쓰는듯.
 - // https://code.google.com/p/opencsv/


* https://github.com/clojure/data.csv
 - `[org.clojure/data.csv "0.1.2"]`

* small-sample.csv

```
$ mkdir data & cd data
$ wget http://www.ericrochester.com/clj-data-analysis/data/small-sample.csv
```

* csv.clj

```clj
(ns getting-data.csv
  (:use incanter.core
        incanter.io))

(slurp "data/small-sample.csv")
;=> "Gomez,Addams,father\nMorticia,Addams,mother\nPugsley,Addams,brother\nWednesday,Addams,sister\nPubert,Addams,brother\nFester,Addams,uncle\nGrandmama,,grandmother\nThing,,hand\nLurch,,butler\nItt,,cousin\nCackle,,cousin\n"

(read-dataset "data/small-sample.csv")
;=> 
;=> |     :col0 |  :col1 |       :col2 |
;=> |-----------+--------+-------------|
;=> |     Gomez | Addams |      father |
;=> |  Morticia | Addams |      mother |
;=> |   Pugsley | Addams |     brother |
;=> | Wednesday | Addams |      sister |
;=> |    Pubert | Addams |     brother |
;=> |    Fester | Addams |       uncle |
;=> | Grandmama |        | grandmother |
;=> |     Thing |        |        hand |
;=> |     Lurch |        |      butler |
;=> |       Itt |        |      cousin |
;=> |    Cackle |        |      cousin |

(require '[clojure.data.csv :as csv])

(csv/read-csv (slurp "data/small-sample.csv"))
;=> (["Gomez" "Addams" "father"] ["Morticia" "Addams" "mother"] ["Pugsley" "Addams" "brother"] ["Wednesday" "Addams" "sister"] ["Pubert" "Addams" "brother"] ["Fester" "Addams" "uncle"] ["Grandmama" "" "grandmother"] ["Thing" "" "hand"] ["Lurch" "" "butler"] ["Itt" "" "cousin"] ["Cackle" "" "cousin"])

```



# JSON
* http://json.org/
* 표준: RFC 4627 - http://tools.ietf.org/html/rfc4627

- Douglas Crockford 가 처음 명시(specify), 대중화시킴.
- 2001년 4월, State Software란 회사에서 (crockford가 공동 창업자) 사용됨.

## form
* object : `{"key1": value1, "key2": value2}`
* array : `[val1, val2, val3]`
* value : __object__, __array__, "string", 123,  true, false, nil
* string : "blabla"
* number : -123.010E-10

```
{
    "k1": 1,
    "k2": "hellow",
    "k3": {"k4": [1, 2, 3] }
}
```


* https://github.com/clojure/data.json
 - `[org.clojure/data.json "0.2.2"]`


```
$ mkdir data & cd data
$ wget http://www.ericrochester.com/clj-data-analysis/data/small-sample.json
```


* json.clj

```clj
(ns getting-data.json
  (:use incanter.core)
  (:require [clojure.data.json :as json]))

;; "data/small-sample.json"
;; [
;;     {
;;         "given_name": "Gomez",
;;         "surname": "Addams",
;;         "relation": "father"
;;     },
;; .....
;; ]

(use 'clojure.pprint)
;=> nil

(pprint (json/read-json (slurp "data/small-sample.json")))
;>> [{:given_name "Gomez", :surname "Addams", :relation "father"}
;>>  {:given_name "Morticia", :surname "Addams", :relation "mother"}
;>>  {:given_name "Pugsley", :surname "Addams", :relation "brother"}
;>>  {:given_name "Wednesday", :surname "Addams", :relation "sister"}
;>>  {:given_name "Pubert", :surname "Addams", :relation "brother"}
;>>  {:given_name "Fester", :surname "Addams", :relation "uncle"}
;>>  {:given_name "Grandmama", :surname "", :relation "grandmother"}
;>>  {:given_name "Thing", :surname "", :relation "hand"}
;>>  {:given_name "Lurch", :surname "", :relation "butler"}
;>>  {:given_name "Itt", :surname "", :relation "cousin"}
;>>  {:given_name "Cackle", :surname "", :relation "cousin"}]

(to-dataset (json/read-json (slurp "data/small-sample.json")))
;=> 
;=> | :given_name | :surname |   :relation |
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


(view (to-dataset (json/read-json (slurp "data/small-sample.json"))))
;=> #<JFrame javax.swing.JFrame[frame1,0,0,400x600,invalid,layout=java.awt.BorderLayout,title=Incanter Dataset,resizable,normal,defaultCloseOperation=HIDE_ON_CLOSE,rootPane=javax.swing.JRootPane[,8,31,384x561,layout=javax.swing.JRootPane$RootLayout,alignmentX=0.0,alignmentY=0.0,border=,flags=16777673,maximumSize=,minimumSize=,preferredSize=],rootPaneCheckingEnabled=true]>
```

* incanter:dataset: https://github.com/liebke/incanter/wiki/datasets

# Excel
* msdn.microsoft.com/en-us/library/office/cc313154(v=office.14).aspx
* http://www.openoffice.org/sc/excelfileformat.odt

* for excel
 - `[incanter/incanter-excel "1.5.1"]`

```
wget http://www.ericrochester.com/clj-data-analysis/data/small-sample-header.xls
```

```clj
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

```

# JDBC
http://en.wikipedia.org/wiki/Java_Database_Connectivity
JDBC
database에 접근 가능한 java api제공.

1997년 2월 19일, Sun Microsystems가 JDK 1.1의 한 부분으로써 JDBC를 공개.

![jdbcRuntime.gif](http://www.dbvis.com/doc/4.3/doc/ug/getConnected/images/jdbcRuntime.gif)

# SQLLite
관계형 데이터베이스 매니지 시스템. 서버가 아니라 응용 프로그램에 넣어 사용하는 비교적 가벼운 데이터베이스.

2000년 여름, D. Richard Hipp가 SQLite를 설계함.
2000년 8월, SQLite 1.0 발표.
2011년, Hipp은 SQLite database에 대한 UnQL을 추가하기로 계획하고, UnQLite를 개발함.


* https://github.com/clojure/java.jdbc
 - `[org.clojure/java.jdbc "0.3.0-alpha4"]`
* https://bitbucket.org/xerial/sqlite-jdbc
 - `[org.xerial/sqlite-jdbc "3.7.2"]`

```
wget http://www.ericrochester.com/clj-data-analysis/data/small-sample.sqlite
```


* sqlite.clj

```clj
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
```

# XML

# RDF
# SPARQL