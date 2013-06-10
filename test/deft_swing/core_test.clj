(ns deft-swing.core-test
  (:use [deft color])
  (:use [deft-swing core])
  (:use [midje sweet]))

(def a-swing-color (java.awt.Color. 1 2 3))

(facts "about java.awt.Color"
  (facts "about implementing the Color protocol"
    (fact "they have a red value"   (red-value   a-swing-color) => 1)
    (fact "they have a green value" (green-value a-swing-color) => 2)
    (fact "they have a blue value"  (blue-value  a-swing-color) => 3)
    (fact "they have a alpha value" (alpha-value a-swing-color) => 255)))
