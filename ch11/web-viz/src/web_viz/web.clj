(ns web-viz.web
  (:require [compojure.route :as route]
            [compojure.handler :as handler])
  (:use compojure.core

        ;; for Webserver.
        ring.adapter.jetty
        [ring.middleware.content-type :only
         (wrap-content-type)]
        [ring.middleware.file :only (wrap-file)]
        [ring.middleware.file-info :only
         (wrap-file-info)]
        [ring.middleware.stacktrace :only
         (wrap-stacktrace)]
        [ring.util.response :only (redirect)]

        ;; for HTML.
        [hiccup core element page]
        [hiccup.middleware :only (wrap-base-url)]
        ))


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

(html5 [:head "this is head"]
       [:body "this is body"])
;=> "<!DOCTYPE html>\n<html><head>this is head</head><body>this is body</body></html>"


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


(defroutes
  routes-cljs
  (GET "/" [] (page-cljs))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app3
  (-> (handler/site routes-cljs)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))


;; D3

(defn d3-page
  [title js body & {:keys [extra-js] :or {extra-js []}}]
  (html5
   [:head
    [:title title]
    (include-css "/css/nv.d3.css")
    (include-css "/css/style.css")]
   [:body
    (concat
     [body]
     [(include-js "http://d3js.org/d3.v3.min.js")
      (include-js "https://raw.github.com/novus/nvd3/master/nv.d3.min.js")]
      (map include-js extra-js)
      [(include-js "/js/script.js")
       (javascript-tag js)])]))

;; scatter-chart
(defn scatter-charts []
  (d3-page "Scatter Chart"
           "webviz.scatter.scatter_plot();"
           [:div#scatter.chart [:svg]]))


(defroutes
  routes-scatter
  (GET "/scatter" [] (scatter-charts))
  (GET "/scatter/data.json" []
       (redirect "/data/census-race.json"))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app4
  (-> (handler/site routes-scatter)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))

;; bar chart
(defn bar-chart []
  (d3-page "Bar Chart"
           "webviz.barchart.bar_chart();"
           [:div#barchart.chart [:svg]]))

(defroutes
  routes-bar
  (GET "/barchart" [] (bar-chart))
  (GET "/barchart/data.json" []
       (redirect "/data/chick-weight.json"))
  (route/resources "/")
  (route/not-found "Page not found"))


(def app5
  (-> (handler/site routes-bar)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))

;; histogram
(defn hist-plot []
  (d3-page "Histogram"
           "webviz.histogram.histogram();"
           [:div#histogram.chart [:svg]]))

(defroutes
  routes-hist
  (GET "/histogram" [] (hist-plot))
  (GET "/histogram/data.json" []
       (redirect "/data/abalone.json"))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app6
  (-> (handler/site routes-hist)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))

;; force-directed layout.
(defn force-layout-plot []
  (d3-page "Force-Directed Layout"
           "webviz.force.force_layout();"
           [:div#force.chart [:svg]]))

(defroutes
  routes-force
  (GET "/force" [] (force-layout-plot))
  (GET "/force/data.json" []
       (redirect "/data/clusters.json"))
  (route/resources "/")
  (route/not-found "Page not found"))


(def app7
  (-> (handler/site routes-force)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))

;; interactive
(defn interactive-force-plot []
  (d3-page "Interactive Force-Directive Layout"
           "webviz.int_force.interactive_force_layout();"
           [:div
            [:div#force.chart [:svg]]
            [:div#datapane]]))


(defroutes
  routes-int-force
  (GET "/int-force" [] (interactive-force-plot))
  (GET "/force/data.json" []
       (redirect "/data/clusters.json"))
  (route/resources "/")
  (route/not-found "Page not found"))


(def app8
  (-> (handler/site routes-int-force)
      (wrap-file "resources")
      (wrap-file-info)
      (wrap-content-type)))

;; http://docs.closure-library.googlecode.com/git/namespace_goog_events.html
;; http://docs.closure-library.googlecode.com/git/namespace_goog_dom.html
