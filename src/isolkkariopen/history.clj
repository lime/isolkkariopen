(ns isolkkariopen.history
  (:require [isolkkariopen.olocam :as olocam]
            [isolkkariopen.settings :refer [settings]]
            [clj-time.core :as cljt]
            [clj-time.format :as fmt]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(def collection "history")

(def prev-pic
  "Previously fetched picture as BufferedImage"
  (atom (olocam/fetch-pic!)))

(defn entry [currPic prevPic]
  {:buzz (olocam/buzz currPic prevPic)
   :open (olocam/olkkari-open? currPic)})

(defn entries []
  (defn add-timestamp [objMap]
    (assoc objMap :time (.getTime (:_id objMap))))
  (defn remove-objId [objMap]
    (dissoc objMap :_id))

  (map #(remove-objId (add-timestamp %)) (mc/find-maps collection)))

(defn insert-entry [entry]
  (mc/insert collection (merge entry {:_id (ObjectId.)})))

(defn now [] (first (entries)))

(defn fetch-as-entry! []
  (let [currPic (olocam/fetch-pic!) prevPic @prev-pic]
    (reset! prev-pic currPic)
    (entry currPic prevPic)))

(defn update! [] (insert-entry (fetch-as-entry!)))