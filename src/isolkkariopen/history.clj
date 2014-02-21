(ns isolkkariopen.history
  (:require [clojure.math.numeric-tower :refer [expt round]]
            [isolkkariopen.olocam :as olocam]
            [isolkkariopen.settings :refer [settings]]
            [clj-time.core :as cljt]
            [clj-time.format :as fmt]
            [monger.collection :as mc]
            [monger.query :as mq])
  (:import [org.bson.types ObjectId]))

(def collection "history")

(def prev-pic
  "Previously fetched picture as BufferedImage"
  (atom (olocam/fetch-pic!)))

(defn entriesMapQuery []
  (mq/with-collection collection
    (mq/find {})
    (mq/sort (array-map :_id -1)) ; sorted from newest to oldest by ObjectId
    (mq/limit (settings :max-history-results))))

(defn lastOpenQuery []
  (first (mq/with-collection collection
    (mq/find {:open true})
    (mq/sort (array-map :_id -1))
    (mq/limit 1))))

(defn nowQuery []
  (first (mq/with-collection collection
    (mq/find {})
    (mq/sort (array-map :_id -1))
    (mq/limit 1))))

(defn round-places [number decimals]
  (let [factor (expt 10 decimals)]
    (bigdec (/ (round (* factor number)) factor))))

(defn entry [currPic prevPic]
  {:buzz (olocam/buzz currPic prevPic)
   :open (olocam/olkkari-open? currPic)})

(defn remove-objId [objMap]
  (dissoc objMap :_id))

(defn entries []
  "Fetch entries from DB, customizing their display form"
  (defn add-timestamp [objMap]
    (assoc objMap :time (.getTime (:_id objMap))))
  (defn add-pretty-buzz [objMap]
    (assoc objMap
      :buzzPretty
      (str (format "%.1f" (:buzz objMap)) " %")))
  
  (map
    #(add-pretty-buzz (remove-objId (add-timestamp %)))
    (entriesMapQuery)))

(defn last-open []
  (remove-objId (:time lastOpenQuery)))

(defn insert-entry [entry]
  (mc/insert collection (merge entry {:_id (ObjectId.)})))

(defn now []
  (let [lopen (:time lastOpenQuery)]
    (if (nil? lopen)
      (remove-objId (nowQuery))
      (assoc (nowQuery) :last-open (last-open)))))

(defn fetch-as-entry! []
  (let [currPic (olocam/fetch-pic!) prevPic @prev-pic]
    (reset! prev-pic currPic)
    (entry currPic prevPic)))

(defn update! [] (insert-entry (fetch-as-entry!)))