(ns deft.examples.line-chart
  (:require [deft [color-solarized :as color]])
  (:use [deft core drawing widgets chart rect])
  (:gen-class))

(defn chart-values [state] state)

(defn chart-rendering
  ([data-fn state bounds] (chart-rendering (data-fn state) bounds))
  ([data bounds]
    (let [chart-bounds (inset 20 bounds)]
      [(solid-rect bounds color/base3)
       (chart-x-axis "X value" 0 100 10 chart-bounds)
       (chart-y-axis "Y value" 0 100 10 chart-bounds)
       (chart-line-plot [0 100] [0 100] data chart-bounds)]
    )))

(defn LineChart [data-fn]
  (CustomComponent [400 200] (partial chart-rendering data-fn)))

(def app
  (application "Line Chart"
    [[0 (rand-int 10)] [2 (rand-int 10)] [5 (rand-int 10)] [10 (rand-int 10)]]
    (display
      (LineChart chart-values))))

(defn -main []
  (run app))
