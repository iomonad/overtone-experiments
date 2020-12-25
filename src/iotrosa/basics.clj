(ns iotrosa.basics
  (:use [overtone.live]))


;; basic demos
(map #(demo 0.4 (sin-osc %))
     (range 200 400 4)) ; Range

;; envelopes

(doseq [x (range 0 1 0.1)]
  (let [env (envelope [0 x 1] [1 1] :step)]
    (demo (sin-osc :freq (+ 300 (* 200 (env-gen env :action FREE)))))))

(demo (* (env-gen (lin 0.1 1 1 0.25) :action FREE) (sin-osc)))

(demo (let [dur 0.4
            env (sin-osc:kr (/ 1 (* 2 dur)))]
        (line:kr 0 1 dur :action FREE)
        (* env (saw 220))))

(stop-all)
