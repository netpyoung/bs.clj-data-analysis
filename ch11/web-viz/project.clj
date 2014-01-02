(defproject web-viz "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]

                 ;; for Web Service.
                 [ring/ring-devel "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [compojure "1.1.6"]

                 ;; for HTML.
                 [hiccup "1.0.4"]

                 ;; for ClojureScript.
                 [org.clojure/clojurescript "0.0-2030"]
                 ]

  :plugins [[lein-ring "0.8.8"]
            ;; for ClojureScript.
            [lein-cljsbuild "1.0.0-alpha2"]]

  :ring {:handler web-viz.web/app8}

  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}

  ;; for ClojureScript.
  :cljsbuild {;;:crossovers [web-viz.x-over],
              :builds
              [{:source-paths ["src-cljs"],
                ;;:crossover-path "xover-cljs",
                :compiler
                {:pretty-print true,
                 :output-to "resources/js/script.js",
                 :optimizations :whitespace}}]}
  )
