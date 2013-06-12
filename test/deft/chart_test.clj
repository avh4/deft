(ns deft.chart-test
  (:use [deft chart drawing])
  (:use [midje sweet]))

(facts about "chart x-axes"
  (fact "it has a baseline"
    (chart-x-axis .label. 0 100 10 [0 0 100 100])
      => (contains (solid-rect [0 99 100 1] axis-color))
    (chart-x-axis .label. 0 100 10 [10 10 100 100])
      => (contains (solid-rect [10 109 100 1] axis-color)))
  (fact "it has tick marks for each major step"
    (chart-x-axis .label. ..min.. ..max.. ..step.. [..x.. 20 ..w.. 200])
      => (contains (map #(solid-rect [% (- 220 5) 1 5] axis-color) [49 99]))
    (provided
      (tick-marks [..min.. ..max.. ..step..] [..x.. ..w..]) => [50 100])))

(facts "about tick mark placement"
  (fact ""
    (tick-marks [0 100 10] [  0  100]) => [10 20 30 40 50 60 70 80 90 100]
    (tick-marks [0 100 10] [100 -100]) => [90 80 70 60 50 40 30 20 10 0]
    (tick-marks [0 100 50] [  3  100]) => [53 103]
    (tick-marks [0 100 51] [  0  100]) => [51]))

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
    (chart-y-axis .label. ..min.. ..max.. ..step.. [..x.. 20 .w. 200])
      => (contains (map #(solid-rect [..x.. % 5 1] axis-color) [50 0]))
    (provided 
      (tick-marks [..min.. ..max.. ..step..] [220 -200]) => [50 0])))

(facts "about line plots"
  (fact "it draws a line through each data point"
    (chart-line-plot ..color.. [0 100] [0 100] [[0 0] [2 5] [80 90]] [0 0 100 100])
      => (contains (line [[0 100] [2 95] [80 10]] ..color..))
    (chart-line-plot ..color.. [0 100] [0 100] [[0 0] [2 5] [80 90]] [100 100 1000 1000])
      => (contains (line [[100 1100] [120 1050] [900 200]] ..color..))))

(facts "about normalize-data"
  (fact "it passes the data through when the to and from range are the same"
    (normalize-data [0 1 5 10] [ 0 10] [0  10]) => [0 1 5 10])
  (fact "it scales the data when the ranges have different sizes"
    (normalize-data [0 1 5 10] [ 0 10] [  0 100]) => [  0  10  50 100]
    (normalize-data [0 1 5 10] [ 0 10] [100 200]) => [100 110 150 200])
  (fact "it translates the data when the ranges have different origins"
    (normalize-data [0 1 5 10] [ 0 10] [10 20]) => [10 11 15 20]
    (normalize-data [0 1 5 10] [10 20] [20 30]) => [10 11 15 20]))
