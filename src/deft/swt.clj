(ns deft.swt
  (:use [deft core]))

(def new-widget (fn
  ([[w h] render-fn] (new-widget (org.eclipse.swt.widgets.Shell.) [w h] render-fn))
  ([parent [w h] render-fn]
    (doto
      (proxy [org.eclipse.swt.widgets.Canvas] [parent 0]
        (computeSize 
          ([_ _] (org.eclipse.swt.graphics.Point. 800 600))
          ([_ _ _] (org.eclipse.swt.graphics.Point. 800 600))))
      (.addPaintListener (proxy [org.eclipse.swt.events.PaintListener] []
        (paintControl [event]
          (let [gc (.gc event)]
            (render-fn gc (-> this .getSize .x) (-> this .getSize .y))))))
      ; (.addDisposeListener (proxy [org.eclipse.swt.events.DisposeListener] []
      ;   (widgetDisposed [event]
      ;     (.dispose <stuff>))))
      ))))

(extend org.eclipse.swt.graphics.GC
  FontService
  { :load-font (memoize (fn [gc font-name pt]
      (let [device (org.eclipse.swt.widgets.Display/getDefault)]
        ; (.loadFont device filename)
        (org.eclipse.swt.graphics.Font. device font-name pt 0))))
    :text-width (fn [gc font string]
      (.setFont gc font)
      (.x (.stringExtent gc string)))
    }
  ColorService
  { :load-color (memoize (fn [gc [r g b]]
      (org.eclipse.swt.graphics.Color. nil r g b)))
    }
  GraphicsContext
  { :prepare (fn [gc]
      (.setTextAntialias gc org.eclipse.swt.SWT/ON))
    :fill-rect (fn [gc [x y w h] color]
      (.setBackground gc color)
      (.fillRectangle gc x y w h))
    :fill-oval (fn [gc [x y w h] color]
      (.setBackground gc color)
      (.fillOval gc x y w h))
    :draw-string (fn [gc string left-x baseline-y font color]
      (.setForeground gc color)
      (.setFont gc font)
      (.drawString gc string left-x (int (- baseline-y 17)) true))
    })