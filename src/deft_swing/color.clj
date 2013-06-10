(ns deft-swing.color
  (:require [deft color]))

(extend-type java.awt.Color
  deft.color/Color
  (red-value   [this] (.getRed   this))
  (green-value [this] (.getGreen this))
  (blue-value  [this] (.getBlue  this))
  (alpha-value [this] (.getAlpha this)))