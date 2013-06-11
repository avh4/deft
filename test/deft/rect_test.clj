(ns deft.rect-test
  (:use [deft rect])
  (:use [midje sweet]))

(facts "about rect operations"
  (fact "from-center makes rects of a given size centered at a point"
    (from-center 0 0 2) => [-1 -1 2 2]
    (from-center 10 10 2) => [9 9 2 2]
    (from-center 0 0 2 4) => [-1 -2 2 4]
    (from-center 10 10 2 4) => [9 8 2 4])
  (fact "of-size makes rects at the origin of the given size"
    (of-size 10 20) => [0 0 10 20]))

(facts "about inset"
  (fact "creates an inset rectangle"
    (inset 10 [0 0 100 100]) => [10 10 80 80]))
