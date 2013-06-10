(ns deft.color)

(defn rgb [r g b] [r g b 255])

(defprotocol Color
  (red-value   [this])
  (green-value [this])
  (blue-value  [this])
  (alpha-value [this]))

(extend-type clojure.lang.IPersistentVector
  Color
  (red-value   [this] (first this))
  (green-value [this] (second this))
  (blue-value  [this] (nth this 2))
  (alpha-value [this] (nth this 3)))