(ns deft.core
  (:use [deft drawing])
  (:import
    [javax.swing JButton JComponent JFrame WindowConstants]
    [java.awt Dimension GridLayout]
    [java.awt.event ActionListener]))

(defn application [name initial-state & displays]
  {:title name
   :initial-state initial-state
   :components (map first displays)})

(defn display [& widgets]
  widgets)

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

(defn Color [color]
  (proxy [JComponent] []
    (paintComponent [g]
                    (doto g
                      (.setColor color)
                      (.fillRect 0 0 (.getWidth this) (.getHeight this))
                      ))
    ))

(defn CustomComponent [[w h] rendering-fn]
  (proxy [JComponent] []
    (paintComponent [g]
                    (let [w (.getWidth this)
                          h (.getHeight this)]
                      (doseq [command (rendering-fn [w h] @S)]
                        (draw command g)) ))
    (getPreferredSize [] (Dimension. w h))
    ))

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

