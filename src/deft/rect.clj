(ns deft.rect)

(defn from-center 
  ([x y size] (from-center x y size size))
  ([x y w h] [(- x (/ w 2)) (- y (/ h 2)) w h]))

(defn of-size [w h] [0 0 w h])

(defn inset [inset [x y w h]]
  [(+ inset x) (+ inset y) (- w inset inset) (- h inset inset)])
