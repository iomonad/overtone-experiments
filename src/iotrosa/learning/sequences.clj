(ns iotrosa.learning.sequences
  (:use [overtone.live]
        [overtone.synth.retro]))

(def metro (metronome 160))

(definst kick []
  (let [src (sin-osc 80)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* 0.7 src env)))

(kick)

(defn player [beat notes]
  (let [notes (if (empty? notes)
                [50 55 23]
                notes)]
    (at (metro beat)
        (kick))
    (at (metro beat)
        (if (zero? (mod beat 5))
          (overpad (+ 24 (choose notes)) 0.2 0.75 0.005)))
    (at (metro (+ 0.5 beat))
        (if (zero? (mod beat 6))
          (overpad (+ 12 (choose notes)) 0.5 0.15 0.1)
          (overpad (choose notes) 0.5 0.15 0.1)))
  (apply-by (metro (inc beat)) #'player (inc beat) (next notes) [])))

(player (metro) [])

;;;

(defn play-notes [t beat-dur notes attacks]
  (when notes
    (let [note      (+ 12 (first notes))
          attack    (first attacks)
          amp       0.5
          release   0.1
          next-beat (+ t beat-dur)]
      (at t (tb-303 200 :note note :cutoff 4 :note note :amp 50 :action FREE :out-bus 1))
      (apply-by next-beat #'play-notes next-beat beat-dur (next notes) (next attacks) []))))

(play-notes (now) 425 (cycle [20]) (repeat 0.4))

(def kick-d (freesound 41155))

(stop-all)

(definst trancy-waves []
  (* 0.2
     (+ (sin-osc 200) (saw 200) (saw 203) (sin-osc 400))))

(demo 10 (bpf (* [0.5 0.5] (pink-noise))
              (mouse-y 10 10000)
              (mouse-x 0.0001 0.9999)))
