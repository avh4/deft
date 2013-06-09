(ns deft.widgets
  (:use [deft core drawing])
  (:import
    [javax.swing JButton JComponent JFrame WindowConstants]
    [java.awt Dimension GridLayout]
    [java.awt.event ActionListener]))

(def S (atom nil))

(defn- connect-watcher [ref key update-fn]
  (add-watch ref key update-fn)
  (update-fn key ref nil @ref))

(defn Button
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
      button )))

(use 'deft.swing)

(defn Color [color]
  (new-widget [100 100] (fn [gc w h]
    (prepare gc)
    (fill-rect gc [0 0 w h] (load-color gc color)))))

(defn CustomComponent [[w h] rendering-fn]
  (new-widget [w h] (fn [gc w h]
    (doseq [command (rendering-fn [w h] @S)]
      (draw command gc)) )))

(defn Grid [cell-fn]
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
