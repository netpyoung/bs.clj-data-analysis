# 11. Creating Charts for the Web



## Serving data with Ring and Compojure

Ring과 Compojure를 이용하여 데이터(민족별 인구조사 데이터(census-race.json))를 서비스하는 방법을 보임.

* [Ring](https://github.com/ring-clojure/)
 - 웹 서버와 웹 인터페이스를 연결시켜주는 인터페이스.
* [Compojure](http://compojure.org)
 - route를 다루고 정의하는데 있어, 간편한 방법을 제공해주는 자그마한 라이브러리.


> ~/.lein/.profiles.clj

```clojure
{:user {:plugins [[lein-ring "0.8.8"]]}}
```


```cmd
> lein new compojure web-viz
> cd web-viz
> lein deps
> mkdir data && cd data
data> wget http://www.ericrochester.com/clj-data-analysis/data/census-race.json
```


> web-viz/project.clj

```clojure
(defproject web-viz "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-devel "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [compojure "1.1.6"]]
  :plugins [[lein-ring "0.8.8"]]
  :ring {:handler web-viz.web/app
         :nrepl {:start? true :port 7000}}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]}})
```


> web-viz/src/web_viz/web.clj

```clojure
(ns web-viz.web
  (:require [compojure.route :as route]
            [compojure.handler :as handler])
  (:use compojure.core
        ring.adapter.jetty
        [ring.middleware.content-type :only
         (wrap-content-type)]
        [ring.middleware.file :only (wrap-file)]
        [ring.middleware.file-info :only
         (wrap-file-info)]
        [ring.middleware.stacktrace :only
         (wrap-stacktrace)]
        [ring.util.response :only (redirect)]))

;; Ring Wiki.
;; https://github.com/ring-clojure/ring/wiki

;; Compojure Wiki.
;; https://github.com/weavejester/compojure/wiki

(defroutes
  site-routes
  (GET "/" [] (redirect "/data/census-race.json"))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site site-routes)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))
```


http://clojure.or.kr/wiki/doku.php?id=lecture:ring:docs#개념

* Handlers: 핸들러는 HTTP 요청을 나타내는 클로져 맵을 인자로 받고, HTTP 응답을 나타내는 클로져 맵을 리턴한다. 
* Middleware: request, response를 변화시킴.
* Adapters: Ring을 web server와 연결시켜줌.



## Creating HTML with Hiccup

Hiccup을 이용하여 HTML페이지를 생성해, 링크를 클릭하여 민족별 인구조사 데이터를 것을 보여줌.


* [Hiccup](https://github.com/weavejester/hiccup)
 - Clojure의 자료구조를 HTML로 표현해주는 라이브러리.



> web-viz/src/web_viz/web.clj

```
  (:use
        ;; for HTML.
        [hiccup core element page]
        [hiccup.middleware :only (wrap-base-url)]
        )


;; Hiccup Wiki.
;; https://github.com/weavejester/hiccup/wiki
(defn index-page []
  (html5
   [:head
    [:title "Web Charts"]]
   [:body
    [:h1 {:id "web-charts"} "Web Charts"]
    [:ol
     [:li [:a {:href "/data/census-race.json"}
           "2010 Census Race Data"]]]]))

(defroutes
  site-routes2
  (GET "/" [] (index-page))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app2
  (-> (handler/site site-routes2)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))
```



## Setting up to use ClojureScript

ClojureScript를 이용, Javascript 생성 및 경고창 "Hello, from ClojureScript!"를 띄움.


* [ClojureScript](https://github.com/clojure/clojurescript)
 - Javascript를 대상으로한 컴파일러.


> web-viz/project.clj

```clojure
  :dependencies [
                 ;; for ClojureScript.
                 [org.clojure/clojurescript "0.0-2030"]
                 ]
  :plugins [
            ;; for clojureScript
            [lein-cljsbuild "1.0.0-alpha2"]
            ]

;; piggieback
;;  :profiles {:dev {
;;                   :plugins [[com.cemerick/austin "0.1.3"]]}}

  ;; for ClojureScript.
  :cljsbuild {:builds
              [{:source-paths ["src-cljs"],
                :compiler
                {:pretty-print true,
                 :output-to "resources/js/script.js",
                 :optimizations :whitespace}}]}
```

```cmd
> mkdir -p src-cljs/webviz
> mkdir -p resources/js
> lein cljsbuild auto
```

> src-cljs/webviz/core.cljs

```clojure
(ns webviz.core)

(defn ^:export hello [world]
  (js/alert (str "Hello, " world)))
```


> web-viz/src/web_viz/web.clj

```clojure
;; ClojureScript Wiki.
;; https://github.com/clojure/clojurescript/wiki
(defn page-cljs []
  (html5
   [:head
    [:title "Web Charts"]]
   [:body
    [:h1 {:id "web-charts"} "Web Charts"]
    [:ol
     [:li [:a {:href "/data/census-race.json"}
           "2010 Census Race Data"]]]
    (include-js "js/script.js")
    (javascript-tag
     "webviz.core.hello('from ClojureScript!');")]))
```

``cmd
> lein cljsbuild auto
``



## Creating scatter plots with NVD3

NVD3를 이용하여 scatter polt을 만듬.

* [D3](http://d3js.org/)
 - Data-Driven Documents
 - 데이터를 기반으로 문서(documents)를 조작하는 JavaScript 라이브러리.
* [NVD3](https://github.com/novus/nvd3)
 - 재활용가능한(reuseable) d3.js의 차트 라이브러리
 - http://nvd3.org/statement.html
* [C2](https://github.com/lynaghk/c2)
 - D3에서 영감을 받은, Clojure와 ClojureScript를 위한 데이터 시각화 라이브러리.


```cmd
> mkdir -p resources/data
resources/data> wget http://www.ericrochester.com/clj-data-analysis/data/census-race.json

> mkdir -p resources/css
resources/css> wget https://raw.github.com/novus/nvd3/master/src/nv.d3.css
```


## Creating bar charts with NVD3
NVD3를 이용하여 막대 그래프를 만듬.

```cmd
resources/data> wget http://www.ericrochester.com/clj-data-analysis/data/chick-weight.json
```


## Creating histograms with NVD3
NVD3를 이용하여 히스토그램을 만듬.


```cmd
resources/data> wget http://www.ericrochester.com/clj-data-analysis/data/abalone.json
```


## Visualizing graphs with force-directed layouts
데이터를 그래프로 표현하기 위해, 사용되는 유명한 방법 중 하나는, force-directed layout.


```cmd
resources/data> wget http://www.ericrochester.com/clj-data-analysis/data/clusters.json
```


## Creating interactive visualizations with D3

D3를 이용하여 반응형 시각자료를 만듬.
