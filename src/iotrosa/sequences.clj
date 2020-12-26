(ns iotrosa.sequences
  (:use [overtone.live]
        [overtone.synth.retro]))

(def metro (metronome 160))

(definst kick []
  (let [src (sin-osc 80)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* 0.7 src env)))

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
      (at t (tb-303 note :amp amp))
      (apply-by next-beat #'play-notes next-beat beat-dur (next notes) (next attacks) []))))

(play-notes (now) 425 (cycle [40 42 44 45 47 49 51 52]) (repeat 0.4))
(stop)
