(ns isolkkariopen.history
  (:require [isolkkariopen.olocam :as olocam]
            [isolkkariopen.settings :refer [settings]]
            [clj-time.core :as cljt]
            [clj-time.format :as fmt]))

(defn timestamp [dateTime]
  (fmt/unparse (fmt/formatters :basic-date-time-no-ms) dateTime))

; Previously fetched picture as BufferedImage for comparison
(def prev-pic (atom (olocam/fetch-pic!)))

(def entries (atom (list {:buzz 0.0 :open false :time (timestamp (cljt/now))})))
(defn now [] (first @entries))

(defn entry [currPic prevPic]
  {:buzz (olocam/buzz currPic prevPic)
   :open (olocam/olkkari-open? currPic)
   :time (timestamp (cljt/now))})

(defn fetch-entry! []
  (let [currPic (olocam/fetch-pic!) prevPic @prev-pic]
    (reset! prev-pic currPic)
    (entry currPic prevPic)))

(defn update! []
  (reset! entries
    (take (:history-size settings)
      (cons (fetch-entry!) @entries))))