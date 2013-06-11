(ns deft.chart-test
  (:use [deft chart drawing])
  (:use [midje sweet]))

(facts about "chart x-axes"
  (fact "it has a baseline"
    (chart-x-axis .label. 0 100 10 [0 0 100 100])
      => (contains (solid-rect [0 99 100 1] axis-color))
    (chart-x-axis .label. 0 100 10 [10 10 100 100])
      => (contains (solid-rect [10 109 100 1] axis-color)))
  (facts "about major tick marks"
    (fact "it has the last tick mark"
      (chart-x-axis .label. 0 100 10 [0 0 100 100])
        => (contains (solid-rect [99 95 1 5] axis-color)))
    (fact "it has a tick mark for each major step"
      (chart-x-axis .label. 0 100 10 [0 0 100 100])
        => (contains (map #(solid-rect [% 95 1 5] axis-color)
          [9 19 29 39 49 59 69 79 89 99])))
    (fact "it places the tick marks when the chart has an offset"
      (chart-x-axis .label. 0 100 50 [30 0 100 100])
        => (contains (map #(solid-rect [% 95 1 5] axis-color) [79 129])))
    ))

(facts "about tick mark placement"
  (fact ""
    (tick-marks [0 100 10] [0 100]) => [10 20 30 40 50 60 70 80 90 100]
    (tick-marks [0 100 50] [3 100]) => [53 103]
    (tick-marks [0 100 51] [0 100]) => [51]))

(facts "about mapping tick ranges"
  (fact ""
    (convert-tick-range [ 0 100 10] [0 100]) => [0 100 10]
    (convert-tick-range [ 0 100 10] [3 100]) => [3 103 10]
    (convert-tick-range [ 0 100 10] [0 200]) => [0 200 20]
    (convert-tick-range [ 0  10  1] [0 100]) => [0 100 10]
    (convert-tick-range [ 0  10  1] [3 100]) => [3 103 10]
    (convert-tick-range [10 110 50] [0 100]) => [0 100 50]))

(facts "about chart y-axes"
  (fact "it has a baseline"
    (chart-y-axis .label. 0 100 10 [0 0 100 100])
      => (contains (solid-rect [0 0 1 100] axis-color))
    (chart-y-axis .label. 0 100 10 [3 4 100 100])
      => (contains (solid-rect [3 4 1 100] axis-color)))
  (fact "it has tick marks for each major step"
    (chart-y-axis .label. ..min.. ..max.. ..step.. [.x. ..y.. .w. ..h..])
      => (contains (map #(solid-rect [0 % 5 1] axis-color) [49 99]))
    (provided 
      (tick-marks [..min.. ..max.. ..step..] [..y.. ..h..]) => [50 100])))
