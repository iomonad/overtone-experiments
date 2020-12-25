(ns iotrosa.sequences
  (:use [overtone.live])
  (:require [leipzig.live :as live]
            [leipzig.melody :refer :all]
            [leipzig.temperament :as temperament]
            [leipzig.scale :as scale]))

(def da-funk
  (->> (phrase [2 1/2 1/2 1/2 2.5 1/2 1/2 1/2 2.5 1/2 1/2 1/2 2.5 1 1]
               [0 -1 0 2 -3 -4 -3 -1 -5 -6 -5 -3 -7 -6 -5])
       (where :pitch (comp scale/G scale/minor))
       (all :part :da-funk)
       (all :amp 1)))

(definst da-funk [freq 440 dur 1.0 amp 1.0]
  (let [env (env-gen (adsr 0.3 0.7 0.5 0.3)
	             (line:kr 1.0 0.0 dur) :action FREE)
        osc (saw freq)]
    (-> osc (* env amp) pan2)))

(defmethod live/play-note :da-funk [{hertz :pitch seconds :duration amp :amp}]
  (when hertz (da-funk :freq hertz :dur seconds :amp (or amp 1))))

(->> da-funk
     (wherevqer :pitch, :pitch temperament/equal)
     (tempo (bpm 110))
     live/play)
