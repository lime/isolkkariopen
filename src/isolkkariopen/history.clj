(ns isolkkariopen.history
  (:require [isolkkariopen.olocam :as olocam]
            [isolkkariopen.settings :refer [settings]]
            [clj-time.core :as cljt]
            [clj-time.format :as fmt]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(defn timestamp [dateTime]
  (fmt/unparse (fmt/formatters :basic-date-time-no-ms) dateTime))

; Previously fetched picture as BufferedImage for comparison
(def prev-pic (atom (olocam/fetch-pic!)))

(defn entry [currPic prevPic]
  {:buzz (olocam/buzz currPic prevPic)
   :open (olocam/olkkari-open? currPic)
   :time (timestamp (cljt/now))})

(defn entries [] (map #(dissoc % :_id) (mc/find-maps "history")))

(defn insert-entry [entry]
  (mc/insert "history" (merge entry {:_id (ObjectId.)})))

(defn now [] (first (entries)))

(defn fetch-as-entry! []
  (let [currPic (olocam/fetch-pic!) prevPic @prev-pic]
    (reset! prev-pic currPic)
    (entry currPic prevPic)))

(defn update! [] (insert-entry (fetch-as-entry!)))