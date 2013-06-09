(ns deft.drawing
  (:require [deft [color :as color]])
  (:use [deft core])
  (:import [java.awt Dimension Graphics Color Font RenderingHints]))

(defmacro solid-rect [bounds color]
  {:shape :rect, :bounds bounds, :color color})

(defmacro solid-oval [bounds color]
  {:shape :oval, :bounds bounds, :color color})

(defmacro drawn-text
  ([text left-x baseline-y] (drawn-text text left-x baseline-y (color/rgb 0 0 0) "Lucida Grande" 13))
  ([text left-x baseline-y color font-name font-size]
   {:shape :text, :text text 
    :font-name font-name :font-size font-size
    :left-x left-x :baseline-y baseline-y
    :color color}) )

(defmulti draw :shape)
(defmethod draw :rect [{color :color [x y w h] :bounds} gc]
  (fill-rect gc [x y w h] (load-color gc color)))
(defmethod draw :oval [{color :color [x y w h] :bounds} gc]
  (fill-oval gc [x y w h] (load-color gc color)))
(defmethod draw :text [{:keys [text font font-size color left-x baseline-y]} gc]
  (draw-string gc text left-x baseline-y (load-font gc font font-size) (load-color gc color)))
