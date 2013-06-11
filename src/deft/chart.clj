(ns deft.chart
  (:require [deft color-solarized])
  (:use [deft drawing]))

(def axis-color deft.color-solarized/base01)

(defn convert-tick-range [[min max step] [x w]]
  (let [factor (/ w (- max min))]
    [x (- (+ (* max factor) x) min) (* step factor)]))

(defn tick-marks [ticks target]
  (let [[min max step] (convert-tick-range ticks target)]
    (range (+ min step) (inc max) step)))

(defn chart-x-axis [label x-min x-max maj-step [x y w h]]
  (flatten [
    (solid-rect [x (+ y (dec h)) w 1] axis-color)
    (map #(solid-rect [(dec %) (- h 5) 1 5] axis-color) 
      (tick-marks [x-min x-max maj-step] [x w]))]))

(defn chart-y-axis [label y-min y-max maj-step [x y w h]]
  (flatten [
    (solid-rect [x y 1 h] axis-color)
    (map #(solid-rect [0 (dec %) 5 1] axis-color)
      (tick-marks [y-min y-max maj-step] [y h]))]))

(defn chart-line-plot [[x-min x-max] [y-min y-max] data bounds])
