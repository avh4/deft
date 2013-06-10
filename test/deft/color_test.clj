(ns deft.color-test
  (:use [deft color])
  (:use [midje sweet]))

(def a-deft-color (rgb 1 2 3))

(facts "about deft color objects"
  (facts "about implementing the Color protocol"
    (fact "they have a red value"   (red-value   a-deft-color) => 1)
    (fact "they have a green value" (green-value a-deft-color) => 2)
    (fact "they have a blue value"  (blue-value  a-deft-color) => 3)
    (fact "they have a alpha value" (alpha-value a-deft-color) => 255)))