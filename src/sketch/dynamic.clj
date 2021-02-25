(ns sketch.dynamic
  (:require [clojure.pprint :as pretty]
            [quil.core :as quil]))

(def ^:private thetas [0
                       (/ (Math/PI) 4)
                       (/ (Math/PI) 2)
                       (* 3 (/ (Math/PI) 4))
                       (Math/PI)
                       (* 5 (/ (Math/PI) 4))
                       (* 3 (/ (Math/PI) 2))
                       (* 7 (/ (Math/PI) 4))])

(defn next-state
  [state]
  (let [x (:x2 state)
        y (:y2 state)
        a (:a state)
        r (* (quil/random-gaussian) 10) ; Step size is based on a Gaussian distribution with a mean
                                        ; of 0 and a standard deviation of 10.
        theta (rand-nth thetas)]
    {:x1 x
     :y1 y
     :x2 (+ x (* r (quil/cos theta)))
     :y2 (+ y (* r (quil/sin theta)))
     :a (quil/noise (+ a 0.03))}))

(defn draw
  [state]
  (quil/stroke 237 45 77 (:a state)) ; Solarized violet.
  (quil/line (:x1 state) (:y1 state) (:x2 state) (:y2 state)))

(defn initialise
  []
  (quil/smooth)
  (quil/color-mode :hsb 360 100 100 1.0)
  (quil/background 193 100 21 1.0) ; Solarized base03.
  {:x1 (/ (quil/width) 2)
   :y1 (/ (quil/height) 2)
   :x2 (/ (quil/width) 2)
   :y2 (/ (quil/height) 2)
   :a (quil/noise 0)})

(defn save-frame-to-disk
  [state _]
  (quil/save-frame (pretty/cl-format nil
                                     "frames/~d-~2,'0d-~2,'0d-~2,'0d-~2,'0d-~2,'0d-####.jpeg"
                                     (quil/year)
                                     (quil/month)
                                     (quil/day)
                                     (quil/hour)
                                     (quil/minute)
                                     (quil/seconds)))
  state)
