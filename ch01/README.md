Importing Data for Analysis
===========================


# Creating a new project

![!leiningen-banner.png](http://leiningen.org/img/leiningen-banner.png)

* leiningen: https://github.com/technomancy/leiningen
 - Java ������Ʈ �Ŵ�����, ant�� maven�� XML�� ������ ������Ʈ�� �����Ѵ�.
 - ������, XML�� ���� �����ϱ� �����!
 - Leiningen�� ������Ʈ���� ���� ��ü�� .clj �����̴�.
 - ����: http://clojure.or.kr/wiki/doku.php?id=lecture:clojure:%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8&s[]=lein


# Incanter
Incanter�� R�� ����, Clojure����� ����� �� �׷����� ���� �÷����̴�.

![!incanter-i-logo-holo.png](http://incanter.org/incanter-i-logo-holo.png) 
* http://incanter.org/
* https://github.com/liebke/incanter


# CSV
* wiki:csv: http://en.wikipedia.org/wiki/Comma-separated_values
* ǥ��: RFC 4180 - http://tools.ietf.org/html/rfc4180

* 1967�� OS/360���� IBM Fortran(level G) �����Ϸ��� CSV�� ������.

```
1997,Ford,E350
"1997","Ford","E350"
```

* incanter-io
 - `[incanter/incanter-io "1.5.1"]`
 - // Retrieving net/sf/opencsv/opencsv/2.3/opencsv-2.3.pom from central �� ����, opencsv�� ���µ�.
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
* ǥ��: RFC 4627 - http://tools.ietf.org/html/rfc4627

- Douglas Crockford �� ó�� ���(specify), ����ȭ��Ŵ.
- 2001�� 4��, State Software�� ȸ�翡�� (crockford�� ���� â����) ����.

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
database�� ���� ������ java api����.

1997�� 2�� 19��, Sun Microsystems�� JDK 1.1�� �� �κ����ν� JDBC�� ����.

![jdbcRuntime.gif](http://www.dbvis.com/doc/4.3/doc/ug/getConnected/images/jdbcRuntime.gif)

# SQLLite
������ �����ͺ��̽� �Ŵ��� �ý���. ������ �ƴ϶� ���� ���α׷��� �־� ����ϴ� ���� ������ �����ͺ��̽�.

2000�� ����, D. Richard Hipp�� SQLite�� ������.
2000�� 8��, SQLite 1.0 ��ǥ.
2011��, Hipp�� SQLite database�� ���� UnQL�� �߰��ϱ�� ��ȹ�ϰ�, UnQLite�� ������.


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

# XML (Extensible Markup Language)

* 1960���, IBM���� Charles Goldfarb, Ed Mosher, Ray Lorie�� GML( __G__oldfarb, __M__osher and __L__orie)�� ����.
* Charles Goldfarb�� `mark-up language`�� �� �������, SGML(Standard Generalised Markup Language)�� ź����.
* 1986��, SGML�� ISO�� ä�õ�.
* 1996��, Sun Microsystems���� XML������ ���۵�. Jon Bosak�� ���� �������� SGML�� ���⸵��.
* 1998�� 2��, XML�� W3C Recommendation�� ��.


* tag
 - `<tag></tag>`
 - `<empty-tag />`
* element
 - `<tag>element</tag>`
* attribute
 - `<tag attribute="hello attribute">element</tag>`
* comment
 - `<!-- comment -->`

`wget http://www.ericrochester.com/clj-data-analysis/data/small-sample.xml`


## ����
* clojure-doc zipper
 - http://clojure-doc.org/articles/tutorials/parsing_xml_with_zippers.html
* Gerard Huet�� �� The Zipper
 - http://www.st.cs.uni-saarland.de/edu/seminare/2005/advanced-fp/docs/huet-zipper.pdf

* xml.clj

```clj
(ns getting-data.xml
  (:use incanter.core
        clojure.xml
        [clojure.zip :exclude [next replace remove] :as z]))


;; <?xml version="1.0" encoding="utf-8"?>
;; <data>
;;   <person>
;;     <given-name>Gomez</given-name>
;;     <surname>Addams</surname>
;;     <relation>father</relation>
;;   </person>
;;   ...
;; </data>


(defn load-xml-data [xml-file first-data next-data]
  (let [data-map (fn [node]
                   [(:tag node) (first (:content node))])]
    (->> (parse xml-file)
         z/xml-zip

         ;; ??
         first-data
         (iterate next-data)
         (take-while #(not (nil? %)))
         (map z/children)

         (map #(mapcat data-map %))
         (map #(apply array-map %))

         to-dataset)))

(load-xml-data "data/small-sample.xml" z/down z/right)
;=> 
;=> | :given-name | :surname |   :relation |
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

```

# html
* enlive: https://github.com/cgrand/enlive
 - `[enlive "1.1.1"]`
 - tutorial: https://github.com/swannodette/enlive-tutorial

## table

`wget http://www.ericrochester.com/clj-data-analysis/data/small-sample-table.html`

* html-table.clj

```clj
(ns getting-data.html-table
  (:use incanter.core)
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html]))

(import [java.net URL])

;; data/small-sample-table.html
;; <table id="data" border="0">
;;   <tr><th>Given Name</th> <th>Surname</th> <th>Relation</th></tr>
;;   <tr><td>Gomez</td> <td>Addams</td> <td>father</td></tr>
;;   ...
;; </table>

(defn to-keyword [input]
  (-> input
      string/lower-case
      (string/replace \space \-)
      keyword))

(to-keyword "Hello World")
;=> :hello-world


(defn load-data [url]
  (let [html (html/html-resource (URL. url))

        ;; <table id="data" border="0">
        table (html/select html [:table#data])

        ;;   <tr><th>Given Name</th> <th>Surname</th> <th>Relation</th></tr>
        headers (->> (html/select table [:tr :th])
                     (map html/text)
                     (map to-keyword)
                     vec)

        ;;   <tr><td>Gomez</td> <td>Addams</td> <td>father</td></tr>
        rows (->> (html/select table [:tr])
                  (map #(html/select % [:td]))
                  (map #(map html/text %))
                  (filter seq))]
    (dataset headers rows)))

(html/html-resource (URL. "http://www.ericrochester.com/clj-data-analysis/data/small-sample-table.html"))
;=> ({:type :dtd, :data ["HTML" nil nil]} ...

(html/html-resource (java.io.StringReader. (slurp "data/small-sample-table.html")))
;=> ({:type :dtd, :data ["HTML" nil nil]} ...

(load-data "http://www.ericrochester.com/clj-data-analysis/data/small-sample-table.html");=> 
;=> | :given-name | :surname |   :relation |
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

```

## ul(unordered list)

* html-list.clj

```clj
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
;=> | Addam's Family |     Gomez Addams |      ? father |
;=> | Addam's Family |  Morticia Addams |      ? mother |
;=> | Addam's Family |   Pugsley Addams |     ? brother |
;=> | Addam's Family | Wednesday Addams |      ? sister |
;=> | Addam's Family |    Pubert Addams |     ? brother |
;=> | Addam's Family |    Fester Addams |       ? uncle |
;=> | Addam's Family |        Grandmama | ? grandmother |
;=> | Addam's Family |            Thing |        ? hand |
;=> | Addam's Family |            Lurch |      ? butler |
;=> | Addam's Family |       Cousin Itt |      ? cousin |
;=> | Addam's Family |    Cousin Cackle |      ? cousin |
;=> |   The Munsters |   Herman Munster |      ? father |
;=> |   The Munsters |     Lily Munster |      ? mother |
;=> |   The Munsters |  Grandpa Dracula | ? grandfather |
;=> |   The Munsters |    Eddie Munster |     ? brother |
;=> |   The Munsters |  Marilyn Munster |      ? sister |
;=> |   The Munsters |        The Raven |         ? pet |

```

# Symantic web
* �ø�ƽ ��(Semantic Web)�� ������ ���ͳݰ� ���� �л�ȯ�濡�� ���ҽ�(�� ����, ���� ȭ��, ���� ��)�� ���� ������ �ڿ� ������ ����-�ǹ� ����(Semanteme)�� ���(��ǻ��)�� ó���� �� �ִ� ����������·� ǥ���ϰ�, �̸� �ڵ�ȭ�� ���(��ǻ��)�� ó���ϵ��� �ϴ� �����ӿ�ũ���� ����̴�. ���� â������ �� ���ʽ� ���� 1998�� �����߰� ���� W3C�� ���� ǥ��ȭ �۾��� �������̴�. - http://ko.wikipedia.org/wiki/%EC%8B%9C%EB%A7%A8%ED%8B%B1_%EC%9B%B9

![semantic-web-stack.png](http://upload.wikimedia.org/wikipedia/en/3/37/Semantic-web-stack.png)

# RDF(Resource Description Framework)

Facebook���� ����ϰ� �ִ� Open Graph Protocol ����, RDF�� ������� ��.

## Turtle (Terse RDF Triple Language)
* filename extension: `.ttl`


* https://github.com/drlivingston/kr


`wget http://telegraphis.net/data/currencies/currencies.ttl`




Sesame�� RDF�����͸� �ٷ�� Java ����� ��ǻ�(de-facto) ǥ�� �����ӿ�ũ.
Sesame�� �ΰ��� query �� ������: SPARQL, SeRQL.

RDF�� ��ü ���踦 statement�� �����̶� ��.
�� statement�� ��� 3���� �κ����� ������.

subject
predicate
object

subject�� predicate�� URI�� ���� �� ����.
object�� ���ڿ��̳� URI�� �� �� ����.



# SPARQL(Simple Protocol And RDF Query Language)

# Agrregate
* http://www.x-rates.com/
 - ���� ȯ�� ��������Ʈ, ������ ��ȭ, ��ȭ����, �ֽŴ��� �� ����.