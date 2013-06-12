(ns deft.core)

(defprotocol FontService
  (load-font [gc font-name pt])
  (text-width [gc font string]))
(defprotocol ColorService
  (load-color [gc [r g b]]))
(defprotocol GraphicsContext
  (prepare [gc])
  (fill-rect [gc [x y w h] color])
  (fill-oval [gc [x y w h] color])
  (draw-string [gc string left-x baseline-y font color])
  (draw-line [gc points color]))
