(ns iotrosa.learning.basics
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

(tb-303 200
        :gate 4
        :cutoff 4
        :note 40
        :amp 50
        :action FREE
        :out-bus 1)

(defsynth foo [freq 200 dur 0.5]
  (let [src (saw [freq (* freq 1.01) (* 0.99 freq)])
        low (sin-osc (/ freq 2))
        filt (lpf src (line:kr (* 10 freq) freq 10))
        env (env-gen (perc 0.1 dur) :action FREE)]
    (out 0 (pan2 (* 0.8 low env filt)))))

(defn foo-timed
  []
  (let [n (now)]
    (dotimes [i 10]
      (at (+ n (* i 500))
          (foo (* i 220) 1)))))

(definst overpad [note 60 amp 0.7 attack 0.001 release 2]
  (let [freq  (midicps note)
        env   (env-gen (perc attack release) :action FREE)
        f-env (+ freq (* 3 freq (env-gen (perc 0.012 (- release 0.1)))))
        bfreq (/ freq 2)
        sig   (apply +
                     (concat (* 0.7 (sin-osc [bfreq (* 0.99 bfreq)]))
                             (lpf (saw [freq (* freq 1.01)]) f-env)))]
    (* amp env sig)))


(defn overpad-timed
  []
  (let [n (now)]
    (dotimes [i 10]
      (at (+ n (* i 500))
          (overpad (* i 220))))))


(overpad-timed 60)

(stop-all)
