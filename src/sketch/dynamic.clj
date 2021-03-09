(ns sketch.dynamic
  (:require [clojure.pprint :as pretty]
            [quil.core :as quil]))

(def ^:private walker-count 5)

(def ^:private frame-count 10000)

(def ^:private thetas [0
                       (/ (Math/PI) 4)
                       (/ (Math/PI) 2)
                       (* 3 (/ (Math/PI) 4))
                       (Math/PI)
                       (* 5 (/ (Math/PI) 4))
                       (* 3 (/ (Math/PI) 2))
                       (* 7 (/ (Math/PI) 4))])

(def ^:private colours [(partial quil/stroke 45 100 71) ; Solarized yellow.
                        (partial quil/stroke 8 89 80) ; Solarized orange.
                        (partial quil/stroke 68 100 60)]) ; Solarized green.

(defn save-frame-to-disk
  ([]
   (quil/save-frame (pretty/cl-format nil
                                      "frames/~d-~2,'0d-~2,'0d-~2,'0d-~2,'0d-~2,'0d-####.jpeg"
                                      (quil/year)
                                      (quil/month)
                                      (quil/day)
                                      (quil/hour)
                                      (quil/minute)
                                      (quil/seconds))))
  ([state _]
   (save-frame-to-disk)
   state))

(defn- update-walker
  [{:keys [x2 y2 a]
    :as   walker}]
  (let [r     (* 10 (quil/random-gaussian))
        theta (rand-nth thetas)]
    (merge walker {:x1 x2
                   :y1 y2
                   :x2 (+ x2 (* r (quil/cos theta)))
                   :y2 (+ y2 (* r (quil/sin theta)))
                   :a  (quil/noise (+ a 0.03))})))

(defn next-state
  [state]
  (map update-walker state))

(defn- draw-walker
  [{:keys [x1 y1 x2 y2 colour-fn a]}]
  (colour-fn a)
  (quil/line x1 y1 x2 y2))

(defn draw
  [walkers]
  (dorun (map draw-walker walkers))
  (when (= frame-count (quil/frame-count))
    (save-frame-to-disk)
    (quil/exit)))

(defn- init-walker
  [x y]
  {:x1        x
   :y1        y
   :x2        x
   :y2        y
   :colour-fn (rand-nth colours)
   :a         (quil/noise (rand))})

(defn initialise
  []
  (quil/smooth)
  (quil/color-mode :hsb 360 100 100 1.0)
  (quil/background 44 10 99 1.0) ; Solarized base3, a pale yellow.
  (let [mid-x (/ (quil/width) 2)
        mid-y (/ (quil/height) 2)]
    (take walker-count
          (repeatedly #(init-walker mid-x mid-y)))))
