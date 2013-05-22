(ns deft.examples.increment-button
  (:use [deft core])
  (:gen-class))

;;;; In this example, the application state is a single integer.
;;;; The button text displays the current integer, and clicking the button
;;;; increments the count, causing the button text to be updated.

(defn increment-count [state]
  (inc state))

(defn button-title [state]
  (str "Increment " state))

(def app
  (application "Increment Button"
    1
    (display
      (Button button-title increment-count))))

(defn -main []
  (run app))
