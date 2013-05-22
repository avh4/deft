(ns deft.core
  (:import [javax.swing JButton JComponent JFrame WindowConstants]))

(defn application [name & displays]
  {:title name
    :components (map first displays)})

(defn display [& widgets]
  widgets)

(defn Button [label]
  (proxy [JButton] [label]))

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
    (doto window
      (.add component)
      (.pack)
      (.setLocationRelativeTo nil)
      (.setDefaultCloseOperation WindowConstants/EXIT_ON_CLOSE)
      (.setVisible true)
      )
  ))

