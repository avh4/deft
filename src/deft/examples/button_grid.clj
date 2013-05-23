(ns deft.examples.button-grid
  (:use [deft core])
  (:gen-class))

;;;; In this example, the application state is an array of integers.
;;;; A button is shown for each integer, indicating the number of times the
;;;; button has been clicked.  When a button is clicked, it's count is
;;;; incremented, and a new button is added to the grid.

(defn increment-count [state i]
  (update-in state [i] inc))

(defn add-new-button [state]
  (assoc state (count state) 0))

(defn button-clicked [i state]
  (-> state
      (increment-count i)
      (add-new-button)
      ))

(defn button-title [i state]
  (str (state i) " clicks"))

(defn buttons [state]
  (map #(Button (partial button-title %) (partial button-clicked %))
       (range (count state))))

(def app
  (application "Button Grid"
               [0]
               (display
                 (Grid buttons) )
                 ))

(defn -main []
  (run app))
