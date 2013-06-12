(ns deft-swing.core
  (:use [deft core color])
  (:require [deft-swing color]))
(import 'java.awt.RenderingHints)
(import 'java.awt.font.TextLayout)
(import 'java.awt.Font)

(def new-widget (fn [[preferred-width preferred-height] render-fn]
  "render-fn [gc w h] -> side effects on gc"
  (proxy [javax.swing.JComponent] []
    (getPreferredSize [] (java.awt.Dimension. preferred-width preferred-height))
    (paintComponent [gc]
      (render-fn gc (.getWidth this) (.getHeight this))))))

(def ^:private get-color (memoize (fn [r g b]
  (java.awt.Color. r g b))))

(def ^:private get-font (memoize (fn
  ([font-name pt] (.deriveFont (get-font font-name) (float pt)))
  ([font-name]
    (try
      (Font/createFont Font/TRUETYPE_FONT (java.io.FileInputStream. (str font-name ".ttf")))
      (catch java.io.FileNotFoundException e
        (Font. font-name 0 0)))))))

(extend-type java.awt.Graphics2D
  FontService
  (load-font [gc font-name pt]
    (get-font font-name pt))
  (text-width [gc font string]
    (.getAdvance (TextLayout. string font (.getFontRenderContext gc))))

  ColorService
  (load-color [gc deft-color]
    (let [r (red-value   deft-color)
          g (green-value deft-color)
          b (blue-value  deft-color)]
      (get-color r g b)))

  GraphicsContext
  (prepare [gc]
    (.setRenderingHints gc
      {RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON
       RenderingHints/KEY_TEXT_ANTIALIASING RenderingHints/VALUE_TEXT_ANTIALIAS_DEFAULT
       RenderingHints/KEY_FRACTIONALMETRICS RenderingHints/VALUE_FRACTIONALMETRICS_ON
       }))
  (fill-rect [gc [x y w h] color]
    (.setColor gc color)
    (.fillRect gc x y w h))
  (fill-oval [gc [x y w h] color]
    (.setColor gc color)
    (.fillOval gc x y w h))
  (draw-string [gc string left-x baseline-y font color]
    (.setFont gc font)
    (.setColor gc color)
    (.drawString gc string (float left-x) (float baseline-y)))
  (draw-line [gc points color]
    (.setColor gc color)
    (loop [ps points]
      (let [a (first ps)
            b (second ps)]
        (if (nil? b) nil
          (do
            (.drawLine gc (first a) (second a) (first b) (second b))
            (recur (rest ps)))))))
  )
