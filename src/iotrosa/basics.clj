(ns iotrosa.basics
  (:use [overtone.live]
        [overtone.synth.retro]))

;; basic demos
(map #(demo 0.4 (sin-osc %))
     (range 200 400 4)) ; Range

;; envelopes

(doseq [x (range 0 1 0.1)]
  (let [env (envelope [0 x 1] [1 1] :step)]
    (demo (sin-osc :freq (+ 300 (* 200 (env-gen env :action FREE)))))))

(demo (* (env-gen (lin 0.1 1 1 0.25) :action FREE) (sin-osc)))

(demo (let [dur 3
            env (sin-osc:kr (/ 1 (* 2 dur)))]
        (line:kr 0 1 dur :action FREE)
        (* env (saw 220))))

(defsynth okay [out-bus 10 amp 0.5]
  (out out-bus
       (* amp
          (+ (* (decay2 (* (impulse 10 0)
                           (+ (* (lf-saw:kr 0.3 0) -0.3) 0.3))
                        0.001)
                0.3)
             (apply + (pulse [80 81]))))))

(doseq [note (range 20 40 2)]
  (tb-303 200
          :gate 2
          :cutoff 4
          :note note
          :amp 50
          :action FREE
          :out-bus 1))

(stop-all)
