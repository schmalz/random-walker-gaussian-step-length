(ns sketch.core
  (:require [quil.core :as quil]
            [quil.middleware :as middleware]
            [sketch.dynamic :as sketch])
  (:gen-class))

(quil/defsketch sketch
  :title "Random Walker with Gaussian Derived Step Length"
  :setup sketch/initialise
  :draw sketch/draw
  :update sketch/next-state
  :middleware [middleware/fun-mode]
  :features [:keep-on-top]
  :mouse-clicked sketch/save-frame-to-disk
  :size [2187 1352])

(defn refresh
  []
  (use :reload 'dynamic)
  (.loop sketch))
