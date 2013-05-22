(ns deft.drawing
  (:import [java.awt Dimension Graphics Color Font RenderingHints]))

(def color-rgb (memoize (fn [r g b] (new Color r g b))))

(def load-font (memoize (fn [font font-size] (new Font font 0 font-size))))

(defmacro fill-rect [bounds color]
  {:shape :rect, :bounds bounds, :color color})

(defmacro fill-oval [bounds color]
  {:shape :oval, :bounds bounds, :color color})

(defmacro draw-text
  ([text left-x baseline-y] (draw-text text left-x baseline-y (color-rgb 0 0 0) "Lucida Grande" 13))
  ([text left-x baseline-y color font font-size] 
   {:shape :text, :text text 
    :font font :font-size font-size 
    :left-x left-x :baseline-y baseline-y
    :color color}) )

(defmulti draw :shape)
(defmethod draw :rect [{color :color [x y w h] :bounds} g]
  (.setColor g color)
  (.fillRect g x y w h))
(defmethod draw :oval [{color :color [x y w h] :bounds} g]
  (.setColor g color)
  (.fillOval g x y w h))
(defmethod draw :text [{:keys [text font font-size color left-x baseline-y]} g]
  (.setColor g color)
  (.setFont g (load-font font font-size))
  (.drawString g text left-x baseline-y))