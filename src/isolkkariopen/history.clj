(ns isolkkariopen.history
  (:require [isolkkariopen.olocam :as olocam]
            [isolkkariopen.settings :refer [settings]]))

; Previously fetched picture as BufferedImage for comparison
(def prev-pic (atom (olocam/fetch-pic!)))

(def entries  (atom (list {:open false :buzz 0.0})))
(defn now [] (first @entries))

(defn entry [currPic prevPic]
  {:open (olocam/olkkari-open? currPic)
   :buzz (olocam/buzz currPic  prevPic)})

(defn fetch-entry! []
  (let [currPic (olocam/fetch-pic!) prevPic @prev-pic]
    (reset! prev-pic currPic)
    (entry currPic prevPic)))

(defn update! []
  (reset! entries
    (take (:history-size settings)
      (cons (fetch-entry!) @entries))))