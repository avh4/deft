(ns deft.examples.static-color
  (:require [deft [color :as color]])
  (:use [deft core widgets])
  (:gen-class))

(def app
  (application "Static Color"
               nil
               (display
                 (Color color/red))))

(defn -main []
  (run app))
