(ns deft.examples.static-button
  (:use [deft core])
  (:gen-class))

(def app
  (application "Static Button"
               nil
               (display
                 (Button "Unclickable Button"))))

(defn -main []
  (run app))
