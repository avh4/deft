(ns deft.core
  (:import
    [javax.swing JButton JComponent JFrame WindowConstants]
    [java.awt.event ActionListener]))

(defn application [name initial-state & displays]
  {:title name
    :initial-state initial-state
    :components (map first displays)})

(defn display [& widgets]
  widgets)

(def S (atom nil))

(defn Button
  ([label] (Button (constantly label) identity))
  ([label-fn on-click-transformation]
    (let [button (proxy [JButton] [])
          listener (proxy [ActionListener] []
                          (actionPerformed [e]
                            (swap! S on-click-transformation)) ) ]
          (.addActionListener button listener)
          (add-watch S :foo (fn [key ref old-state new-state]
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

