(ns deft-swing.core
  (:use [deft core color])
  (:require [deft-swing color]))
(import 'java.awt.RenderingHints)
(import 'java.awt.font.TextLayout)
(import 'java.awt.Font)

(def new-widget (fn [[w h] render-fn]
  (proxy [javax.swing.JComponent] []
    (getPreferredSize [] (java.awt.Dimension. 800 600))
    (paintComponent [gc]
      (render-fn gc (.getWidth this) (.getHeight this))))))

(extend java.awt.Graphics2D
  FontService
  { :load-font (memoize (fn [gc font-name pt]
      (let [base-font (Font/createFont Font/TRUETYPE_FONT (java.io.FileInputStream. (str font-name ".ttf")))]
        (.deriveFont base-font (float pt)))))
    :text-width (fn [gc font string]
      (.getAdvance (TextLayout. string font (.getFontRenderContext gc))))
    }
  ColorService
  { :load-color (memoize (fn [gc deft-color]
      (let [r (red-value   deft-color)
            g (green-value deft-color)
            b (blue-value  deft-color)]
        (java.awt.Color. r g b))))
    }
  GraphicsContext
  { :prepare (fn [gc]
    (.setRenderingHints gc
      {RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON
       RenderingHints/KEY_TEXT_ANTIALIASING RenderingHints/VALUE_TEXT_ANTIALIAS_LCD_HRGB
       RenderingHints/KEY_FRACTIONALMETRICS RenderingHints/VALUE_FRACTIONALMETRICS_ON
       }))
    :fill-rect (fn [gc [x y w h] color]
      (.setColor gc color)
      (.fillRect gc x y w h))
    :fill-oval (fn [gc [x y w h] color]
      (.setColor gc color)
      (.fillOval gc x y w h))
    :draw-string (fn [gc string left-x baseline-y font color]
      (.setFont gc font)
      (.setColor gc color)
      (.drawString gc string (float left-x) (float baseline-y)))
    })
