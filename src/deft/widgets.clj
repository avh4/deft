(ns deft.widgets
  (:require [deft [rect :as rect]])
  (:use [deft core drawing])
  (:import
    [javax.swing JButton JComponent JFrame WindowConstants]
    [java.awt Dimension GridLayout]
    [java.awt.event ActionListener]))

(def ^:private S (atom nil))

(defn- connect-watcher [ref key update-fn]
  (add-watch ref key update-fn)
  (update-fn key ref nil @ref))

(defn Button
  "label-fn [state] -> String
   on-click-transformation [state] -> new-state"
  ([label] (Button (constantly label) identity))
  ([label-fn on-click-transformation]
    (let [button (proxy [JButton] [])
          listener (proxy [ActionListener] []
                     (actionPerformed [e]
                                      (swap! S on-click-transformation)) ) ]
      (.addActionListener button listener)
      (connect-watcher S button
        (fn [key ref old-state new-state]
          (.setText button (label-fn new-state))))
      button)))

(use 'deft-swing.core)

(defn Color [color]
  (new-widget [100 100] (fn [gc w h]
    (prepare gc)
    (fill-rect gc [0 0 w h] (load-color gc color)))))

(defn CustomComponent [[preferred-width preferred-height] rendering-fn]
  "rendering-fn [state [w h]] -> seq of rendering commands"
  (new-widget [preferred-width preferred-height] (fn [gc w h]
    (prepare gc)
    (doseq [command (rendering-fn @S (rect/of-size w h))]
      (draw command gc)) )))

(defn Grid [cell-fn]
  "cell-fn [state] -> seq of widgets"
  (let [self (proxy [JComponent] [])]
    (.setLayout self (GridLayout. 0 2))
    (connect-watcher S self
      (fn [key ref old-state new-state]
        (.removeAll self)
        (doseq [cell (cell-fn new-state)]
          (.add self cell))
        (.validate self)
        (.repaint self)
        ))
    self ))

(defn application [name initial-state & displays]
  {:title name
   :initial-state initial-state
   :components (map first displays)})

(defn display [& widgets]
  widgets)

(defn run [app]
  (let [window (JFrame. (:title app))
        component (first (:components app))]
    (swap! S (constantly (:initial-state app)))
    (doto window
      (.add component)
      (.pack)
      (.setLocationRelativeTo nil)
      (.setDefaultCloseOperation WindowConstants/EXIT_ON_CLOSE)
      (.setVisible true)
      )
    ))
