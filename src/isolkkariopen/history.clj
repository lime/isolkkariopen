(ns isolkkariopen.history
  (:require [clojure.math.numeric-tower :refer [expt round]]
            [isolkkariopen.olocam :as olocam]
            [isolkkariopen.settings :refer [settings]]
            [clj-time.core :as cljt]
            [clj-time.format :as fmt]
            [monger.collection :as mc]
            [monger.query :as mq])
  (:import [org.bson.types ObjectId]
           [org.joda.time DateTime Period PeriodType]
           [org.joda.time.format PeriodFormat]))

(def collection "history")

(defn round-places [number decimals]
  (let [factor (expt 10 decimals)]
    (bigdec (/ (round (* factor number)) factor))))

(defn unix-epoch-to-datetime [epochTime]
  (new DateTime epochTime))

(def prev-pic
  "Previously fetched picture as BufferedImage"
  (atom (olocam/fetch-pic!)))

(defn qry-entries []
  (mq/with-collection collection
    (mq/find {})
    (mq/sort (array-map :_id -1)) ; sorted from newest to oldest by ObjectId
    (mq/limit (settings :max-history-results))))

(defn qry-newest-entry []
  (first (mq/with-collection collection
    (mq/find {})
    (mq/sort (array-map :_id -1))
    (mq/limit 1))))

(defn qry-last-open-entry []
  (first (mq/with-collection collection
    (mq/find {:open true})
    (mq/sort (array-map :_id -1))
    (mq/limit 1))))

(defn qry-last-closed-entry []
  (first (mq/with-collection collection
    (mq/find {:open false})
    (mq/sort (array-map :_id -1))
    (mq/limit 1))))

; Mongo object tweaks
(defn objId-as-time [objMap]
  (.getTime (:_id objMap)))

(defn remove-objId [objMap]
  (dissoc objMap :_id))

(defn timestamp-datetime [objMap]
  (unix-epoch-to-datetime (objId-as-time objMap)))

(defn add-timestamp [objMap]
  (assoc objMap :time
    (.toString (timestamp-datetime objMap))))

(defn datetime-until-now [datetime]
  (def minute-precision
    (.withSecondsRemoved (.withMillisRemoved (PeriodType/standard))))
  (new Period datetime (cljt/now) minute-precision))

(defn ago-string [datetime]
  (str
    (.print (PeriodFormat/getDefault) (datetime-until-now datetime))
    " ago"))

(defn add-pretty-buzz [objMap]
  (assoc objMap :buzzPretty
    (str (format "%.1f" (* 100 (:buzz objMap))) " %")))

(defn format-entry [objMap]
  "Prepare entry for JSON output. Add timestamp and pretty buzz string, remove Mongo ObjectId."
  (add-pretty-buzz (remove-objId (add-timestamp objMap))))

(defn add-last-open [objMap]
  (let [last-open (qry-last-open-entry)]
    (assoc objMap
      :lastOpen
        (if (nil? last-open) "" (.toString (timestamp-datetime last-open)))
      :lastOpenAgo
        (if (nil? last-open) "" (ago-string (timestamp-datetime last-open))))))

(defn add-last-closed [objMap]
  (let [last-closed (qry-last-closed-entry)]
    (assoc objMap
      :lastClosed
        (if (nil? last-closed) "" (.toString (timestamp-datetime last-closed)))
      :lastClosedAgo
        (if (nil? last-closed) "" (ago-string (timestamp-datetime last-closed))))))

(defn insert-entry! [dbEntry]
  "Insert entry into history database."
  ; Monger recommends manual ObjectId creation as below
  (mc/insert collection (merge dbEntry {:_id (ObjectId.)})))

(defn db-entry [currPic prevPic]
  {:buzz (olocam/buzz currPic prevPic)
   :open (olocam/olkkari-open? currPic)})

(defn fetch-pic-as-entry! []
  (let [currPic (olocam/fetch-pic!) prevPic @prev-pic]
    (reset! prev-pic currPic)
    (db-entry currPic prevPic)))

(defn update! [] (insert-entry! (fetch-pic-as-entry!)))

(defn entries []
  "Fetch entries from DB, customizing their display form."
  (map #(format-entry %) (qry-entries)))

(defn newest-entry []
  "Newest entry from DB, enhanced with last open data."
  (format-entry (add-last-closed (add-last-open (qry-newest-entry)))))