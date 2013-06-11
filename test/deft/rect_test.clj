(ns deft.rect-test
  (:use [deft rect])
  (:use [midje sweet]))

(facts "about rect operations"
  (fact "about from-center"
    (from-center 0 0 2) => [-1 -1 2 2]
    (from-center 10 10 2) => [9 9 2 2]
    (from-center 0 0 2 4) => [-1 -2 2 4]
    (from-center 10 10 2 4) => [9 8 2 4]))
