(ns deft.examples.static-button
  (:use [deft core])
  (:gen-class))

(def app
  (application "Static Button"
    (display
      (Button "Unclickable Button"))))

(defn -main []
  (run app))
