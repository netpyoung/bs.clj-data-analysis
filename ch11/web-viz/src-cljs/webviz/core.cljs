(ns webviz.core)

(defn ^:export hello [world]
  (js/alert (str "Hello, " world)))



(deftype Group [key values])
(deftype Point [x y size])

(defn add-label
  [chart axis label]
  (if-not (nil? label)
    (.axisLabel (aget chart axis) label)))

(defn add-axes-labels [chart x-label y-label]
  (doto chart
    (add-label "xAxis" x-label)
    (add-label "yAxis" y-label)))

(defn populate-node
  [selector chart groups transition continuation]
  (-> (.select js/d3 selector)
      (.datum groups)
      (.transition)
      (.duration (if transition 500 0))
      (.call chart)
      (.call continuation)))

(defn create-chart
  [data-url selector make-chart json->groups &
   {:keys [transition continuation x-label y-label]
    :or {transition false, continuation (fn [_]),
         x-label nil, y-label nil}}]
  (.json
   js/d3 data-url
   (fn [error data]
     (when data (.addGraph
                 js/nv
                 (fn []
                   (let [chart (make-chart)]
                     (add-axes-labels chart x-label y-label)
                     (populate-node selector chart
                                    (json->groups data)
                                    transition continuation)
                     (.windowResize js/nv.utils
                                    #(.update chart)))))))))
